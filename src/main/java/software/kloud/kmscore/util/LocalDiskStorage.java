package software.kloud.kmscore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class LocalDiskStorage {

    private static final Logger logger = LoggerFactory.getLogger(LocalDiskStorage.class);
    private boolean initialized = false;
    private File root;

    private LocalDiskStorage() {
    }

    public static LocalDiskStorage getInstance() {
        return INSTANCE_HOLDER.INSTANCE;
    }

    public void init() {
        if (initialized) return;

        var root = new File(System.getProperty("user.home") + File.separator + ".kmscache");
        if (!root.isDirectory()) {
            if (!root.mkdir()) {
                logger.error("Failed to create " + root.getPath() + " check permissions or disk corruptions");
                System.exit(-1);
            }
        }

        this.root = root;

        initialized = true;
    }

    public File getRoot() {
        return root;
    }

    private static class INSTANCE_HOLDER {
        static LocalDiskStorage INSTANCE = new LocalDiskStorage();
    }

}
