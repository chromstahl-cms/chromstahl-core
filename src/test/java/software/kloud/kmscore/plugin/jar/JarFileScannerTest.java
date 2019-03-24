package software.kloud.kmscore.plugin.jar;

import org.junit.Before;
import org.junit.Test;
import software.kloud.kmscore.util.FileHasher;
import software.kloud.kmscore.util.ProcUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class JarFileScannerTest {
    private static final String testPluginPath = "./SamplePluginForUnitTests/";

    @Before
    public void init() throws NoSuchAlgorithmException {
        FileHasher.init();
    }

    @Test
    public void it_can_scan_a_directory_correctly()
            throws InterruptedException, IOException, SubProcessException, JarUnpackingException {
        var testPluginJar = buildTestPluginJar();

        var scanner = new JarFileScanner();
        File foo = Files.createTempDirectory("foo").toFile();
        try {
            scanner.setUnpackingBaseDir(foo);
            scanner.addDirectory(testPluginJar.getParentFile());
            scanner.init();
            scanner.scan(JarFileScanner.ScanMode.SKIP_ALREADY_SCANNED);

            var found = scanner.getAll();
            assertEquals(1, found.size());
        } finally {
            if (foo.isDirectory()) {
                //noinspection ResultOfMethodCallIgnored
                foo.delete();
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void it_throw_an_exception_if_scanning_before_being_initialized() throws IOException, JarUnpackingException {
        var scanner = new JarFileScanner();
        scanner.scan(JarFileScanner.ScanMode.SKIP_ALREADY_SCANNED);
    }

    @Test
    public void it_skips_jar_files_when_they_did_not_change()
            throws IOException, SubProcessException, InterruptedException, JarUnpackingException {
        File foo = Files.createTempDirectory("foo").toFile();
        try {
            var testPluginJar = buildTestPluginJar();

            // intentionally used this block to motivate GC to collect the Scanner used in this block
            // Also to show, that the two scanners are independent of each other
            {

                var scanner = new JarFileScanner();
                scanner.setUnpackingBaseDir(foo);
                scanner.addDirectory(testPluginJar.getParentFile());
                scanner.init();
                scanner.scan(JarFileScanner.ScanMode.SKIP_ALREADY_SCANNED);

                var found = scanner.getAll();
                assertEquals(1, found.size());
            }

            var debugScanner = new DebugJarFileScanner();
            debugScanner.setUnpackingBaseDir(foo);
            debugScanner.addDirectory(testPluginJar.getParentFile());
            debugScanner.init();
            debugScanner.scan(JarFileScanner.ScanMode.SKIP_ALREADY_SCANNED);

            var debugInfo = debugScanner.getDebugInfo();
            assertEquals(0, debugInfo.countOfUnpackedJarEntries);
            assertEquals(0, debugInfo.countOfSkippedJarEntries);
            assertEquals(0, debugInfo.countOfUnpackedJarFiles);
            assertEquals(1, debugInfo.countOfSkippedJarFiles);
        } finally {
            if (foo.isDirectory()) {
                //noinspection ResultOfMethodCallIgnored
                foo.delete();
            }
        }
    }

    private File buildTestPluginJar() throws IOException, InterruptedException, SubProcessException {
        Process proc = ProcUtil.runProcOutputToStdIo(
                new String[]{"./gradlew", "clean", "jar"},
                null,
                new File(testPluginPath)
        );

        if (proc.exitValue() != 0) {
            throw new SubProcessException("Gradle process did not exit normally");
        }

        return new File(testPluginPath + "build/libs/SamplePlugin-1.0-SNAPSHOT.jar");
    }

    private static class DebugJarFileScanner extends JarFileScanner {
        public DebugJarFileScanner(int threadCount) throws IOException {
            super(threadCount);
            this.setDebug();
        }

        public DebugJarFileScanner() throws IOException {
            super();
            this.setDebug();
        }
    }

    private static class SubProcessException extends Exception {
        SubProcessException(String message) {
            super(message);
        }
    }
}

