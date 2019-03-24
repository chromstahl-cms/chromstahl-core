package software.kloud.kmscore.util;

import java.io.IOException;
import java.io.InputStream;

public class FileHashingUtil {
    public static boolean compareHashes(InputStream first, InputStream second) throws IOException {
        var firstHash = new FileHasher(first).hashMD5();
        var secondHash = new FileHasher(second).hashMD5();

        return firstHash.equals(secondHash);
    }
}
