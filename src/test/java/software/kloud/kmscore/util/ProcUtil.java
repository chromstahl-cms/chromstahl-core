package software.kloud.kmscore.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcUtil {
    public static Process runProcOutputToStdIo(
            String[] cmdArray,
            String[] envp,
            File file
    ) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(cmdArray, envp, file);
        proc.waitFor();
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = bri.readLine()) != null) System.out.println(line);
        }

        try (BufferedReader bri = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            String line;
            while ((line = bri.readLine()) != null) System.err.println(line);
        }

        return proc;
    }
}
