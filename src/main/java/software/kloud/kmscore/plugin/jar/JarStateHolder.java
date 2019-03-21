package software.kloud.kmscore.plugin.jar;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class JarStateHolder {
    private File zippedJarFile;
    private File unzippedDirectory;

    public JarStateHolder() {
    }

    public JarStateHolder(File zippedJarFile) {
        this.zippedJarFile = zippedJarFile;
        this.unzippedDirectory = null;
    }

    public File getZippedJarFile() {
        return zippedJarFile;
    }

    public File getUnzippedDirectory() {
        return unzippedDirectory;
    }

    public void setUnzippedDirectory(File directory) {
        this.unzippedDirectory = directory;
    }

    public void setZippedJarFile(File zippedJarFile) {
        this.zippedJarFile = zippedJarFile;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JarStateHolder)) return false;
        JarStateHolder other = (JarStateHolder) obj;

        if (this.unzippedDirectory == null) {
            return this.zippedJarFile.equals(other.zippedJarFile) && null == other.unzippedDirectory;
        }

        return this.zippedJarFile.equals(other.zippedJarFile)
                && this.unzippedDirectory.equals(other.unzippedDirectory);
    }
}
