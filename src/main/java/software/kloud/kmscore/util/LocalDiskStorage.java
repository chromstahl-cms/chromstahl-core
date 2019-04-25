package software.kloud.kmscore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.kloud.ChromPluginSDK.ChromStorage;

import java.io.File;

@Component
public final class LocalDiskStorage implements ChromStorage {

    private static final Logger logger = LoggerFactory.getLogger(LocalDiskStorage.class);
    private File root;

    public LocalDiskStorage() {
        var root = new File(System.getProperty("user.home") + File.separator + ".chromstahl"+ File.separator+ "data");
        if (!root.isDirectory()) {
            if (!root.mkdir()) {
                logger.error("Failed to create " + root.getPath() + " check permissions or disk corruptions");
                System.exit(-1);
            }
        }

        this.root = root;
    }
    
    @Override
    public File getRoot() {
        return root;
    }

    private static class INSTANCE_HOLDER {
        static LocalDiskStorage INSTANCE = new LocalDiskStorage();
    }

}
