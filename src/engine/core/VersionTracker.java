package engine.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class VersionTracker {

    public static String version;

    //Reads the version data file and updates it every build
    public static void UpdateVersionData() {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader("./versionHistory/version.txt"));
            version = reader.readLine();
            float v = Float.parseFloat(version);
            reader.close();

            PrintWriter writer = new PrintWriter("./versionHistory/version.txt");
            writer.println(String.valueOf(v += 0.1f));
            writer.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
