package software.kloud.kmscore.util;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class FileHasher {
    private static final int DEFAULT_BUF_SIZE = 2048;
    private static MessageDigest MD5_MD;
    private File fd;
    private String lastDigest;

    public FileHasher(File fd) {
        this.fd = fd;
    }

    public static void init() throws NoSuchAlgorithmException {
        MD5_MD = MessageDigest.getInstance("MD5");
    }

    public String hashMD5() throws IOException {
        try (var fin = new FileInputStream(this.fd)) {
            return this.getDigest(fin, MD5_MD);
        }
    }

    public Optional<String> getLastDigest() {
        return Optional.ofNullable(this.lastDigest);
    }

    private String getDigest(InputStream is, MessageDigest md) throws IOException {
        assert md != null;
        md.reset();
        byte[] bytes = new byte[DEFAULT_BUF_SIZE];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            md.update(bytes, 0, numBytes);
        }
        byte[] digest = md.digest();
        var res = new String(Hex.encodeHex(digest));
        lastDigest = res;
        return res;
    }
}