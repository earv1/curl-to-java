import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import component.ComponentType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import splitter.CurlToComponents;
import testgenerator.CurlToClassWithDependencies;
import testgenerator.JsonToTests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {
    private static CodeExecutor codeExecutor = new CodeExecutor();
    public static void main (String [] args) throws Exception {
        Path curlsPath = Paths.get(ClassLoader.getSystemResource("curls.txt").toURI());
        String [] curlArray = Files.readString(curlsPath).split("[\n|\r]");

        CurlToClassWithDependencies.generateClassWithDependencies(curlArray);
    }
}
