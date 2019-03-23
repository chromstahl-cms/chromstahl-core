package software.kloud.kmscore.plugin.jar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.kloud.kmscore.util.FileHasher;
import software.kloud.kmscore.util.LocalDiskStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JarFileScanner {
    private static final Logger logger = LoggerFactory.getLogger(JarFileScanner.class);
    private static final File CACHE_FILE = new File(LocalDiskStorage.getStaticRoot(), "plugins.cache");
    private static final CacheTypeReference CACHE_TYPE = new CacheTypeReference();
    private static final ObjectMapper objm = new ObjectMapper();
    private static int DEFAULT_THREAD_COUNT = 4;
    private final List<File> pluginDirectories;
    private final CompletionService<JarStateHolder> completionService;
    private Map<File, Set<JarStateHolder>> jarFileTmpMap;
    private Map<File, FileHasher> jarFileHasherMap;
    private boolean hasScanned = false;


    public JarFileScanner(int threadCount) throws IOException {
        this.jarFileTmpMap = new HashMap<>();
        this.jarFileHasherMap = new HashMap<>();
        this.pluginDirectories = new ArrayList<>();
        this.completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(threadCount));
        this.init();
    }

    public JarFileScanner() throws IOException {
        this(DEFAULT_THREAD_COUNT);
    }

    private void init() throws IOException {
        if (!CACHE_FILE.isFile()) {
            if (!CACHE_FILE.createNewFile()) {
                logger.error("Could not create fresh cache file. Filesystem corrupt or not enough permissions?");
                throw new IOException("Could not create fresh cache file. Filesystem corrupt or not enough permissions?");
            }
            return;
        }
        if (CACHE_FILE.length() == 0) return;
        this.jarFileTmpMap = objm.readValue(CACHE_FILE, CACHE_TYPE);
    }

    private void writeCacheToDisk() throws IOException {
        if (!CACHE_FILE.isFile()) {
            if (!CACHE_FILE.createNewFile()) {
                logger.error("Could not create fresh cache file. Filesystem corrupt or not enough permissions?");
                throw new IOException("Could not create fresh cache file. Filesystem corrupt or not enough permissions?");
            }
        }
        objm.writeValue(CACHE_FILE, this.jarFileTmpMap);
    }

    public void addDirectory(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("File is not a directory");
        }

        pluginDirectories.add(directory);
    }


    public void scan(ScanMode scanMode) throws JarUnpackingException, IOException {
        try {
            if (scanMode == ScanMode.FORCE) {
                jarFileTmpMap.clear();
            }

            for (File pluginDirectory : pluginDirectories) {
                Set<JarStateHolder> scanResult = scanDirectory(pluginDirectory);
                jarFileTmpMap.put(pluginDirectory, scanResult);
            }
        } finally {
            this.writeCacheToDisk();
        }

        hasScanned = true;
    }

    private Set<JarStateHolder> scanDirectory(File directory) throws JarUnpackingException, IOException {
        var jarFiles = directory.listFiles((dir, name) -> name.endsWith(".jar") | name.endsWith(".war"));
        if (null == jarFiles) return Collections.emptySet();

        var res = new HashSet<JarStateHolder>();
        int futuresSpawned = 0;
        for (File zippedJarFile : jarFiles) {
            if (!zippedJarFile.isFile()) continue;

            var hasher = new FileHasher(zippedJarFile);
            var hash = hasher.hashMD5();

            var hasAlreadyScanned = this.getAllScannedJars()
                    .filter(j -> j.getJarFileHash() != null)
                    .anyMatch(j -> j.getJarFileHash().equals(hash));

            if (hasAlreadyScanned) {
                logger.info(String.format(
                        "Skipping file %s. Hasn't changed since last scan. Use ScanMode.FORCE to force"
                        , zippedJarFile.getAbsolutePath())
                );
                continue;
            }

            Callable<JarStateHolder> unpackFuture = () -> {
                File innerZipperJarFile = new File(zippedJarFile.getAbsolutePath());
                var holder = new JarStateHolder(innerZipperJarFile);
                res.add(holder);
                try (JarFile jarFile = new JarFile(innerZipperJarFile)) {
                    holder.setUnzippedDirectory(unpackJarFileToTmp(jarFile, innerZipperJarFile.getName().replace(".jar", "")));
                    holder.setJarFileHash(hash);
                } catch (IOException e) {
                    throw new JarUnpackingException("Failed to unpack Jar", e);
                }
                return holder;
            };
            completionService.submit(unpackFuture);
            futuresSpawned++;
        }

        int deltaReceived = futuresSpawned;
        try {
            while (deltaReceived > 0) {
                Future<JarStateHolder> maybeRanFuture = completionService.take();
                res.add(maybeRanFuture.get());
                deltaReceived--;
            }
        } catch (InterruptedException e) {
            throw new JarUnpackingException("Future was interrupted", e);
        } catch (ExecutionException e) {
            throw new JarUnpackingException("Future did not complete successfully", e);
        }

        return res;
    }

    /**
     * Unpacks a jar into a temporary directory. Returns temporary directory for further processing
     * Runs in its own future
     *
     * @param jarfile JarFile to unpack
     * @return Temporary directory in which the jarFile was unpacked
     * @throws IOException If not able to unpack jar
     */
    private File unpackJarFileToTmp(JarFile jarfile, String name) throws IOException {
        var tmpDir = Files.createDirectory(new File(LocalDiskStorage.getStaticRoot(), String.format("KMS-Plugin-%s", name)).toPath());

        Iterator<JarEntry> entryIterator = jarfile.entries().asIterator();

        while (entryIterator.hasNext()) {
            var entry = entryIterator.next();
            var destFile = new File(tmpDir.toFile(), entry.getName());
            if (entry.getName().endsWith("/") && !destFile.isDirectory()) {
                if (!destFile.mkdir()) {
                    throw new IOException("Failed to create tmp directory: " + destFile.getAbsolutePath());
                }
                logger.info("Creating directory " + destFile.getAbsolutePath());
                continue;
            }
            logger.info("Unzipping file " + destFile.getAbsolutePath());
            try (var is = jarfile.getInputStream(entry)) {
                try (var fout = new FileOutputStream(destFile)) {
                    while (is.available() > 0) {
                        fout.write(is.read());
                    }
                }
            }
        }

        logger.info("Finished unzipping " + tmpDir.toString());
        return new File(tmpDir.toString());
    }

    public Set<JarStateHolder> getAll() {
        return this.streamAll()
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("WeakerAccess")
    public Stream<JarStateHolder> streamAll() {
        guardAccess();
        return this.jarFileTmpMap
                .values()
                .stream()
                .flatMap(Set::stream);
    }

    private void guardAccess() {
        if (!hasScanned)
            throw new IllegalStateException("Hasn't scanned yet");
    }

    private Set<JarStateHolder> getAllScannedJarForDirectory(File directory) {
        var found = this.jarFileTmpMap.get(directory);

        if (found == null) return Collections.emptySet();

        return found;
    }

    private Stream<JarStateHolder> getAllScannedJars() {
        return this.jarFileTmpMap
                .values()
                .stream()
                .flatMap(Set::stream);
    }

    public enum ScanMode {
        SKIP_ALREADY_SCANNED,
        FORCE;
    }

    /**
     * Sole reason for this class it use it as a type token when reading / writing to cache.
     * Using {@link TypeReference} inline leads to weird autoformatting with Intellij :(
     */
    private static final class CacheTypeReference extends TypeReference<Map<File, Set<JarStateHolder>>> {

    }
}
