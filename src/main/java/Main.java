import testgenerator.CurlToClassWithDependencies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String [] args) throws Exception {
        Path curlsPath = Paths.get(ClassLoader.getSystemResource("curls.txt").toURI());
        String [] curlArray = Files.readString(curlsPath).split("[\n|\r]");

        CurlToClassWithDependencies.generateClassWithDependencies(curlArray);
    }
}
