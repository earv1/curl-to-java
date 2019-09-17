import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import component.ComponentType;
import splitter.CurlToComponents;
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

        Path templatePath = Paths.get(ClassLoader.getSystemResource("template/Default.java").toURI());
        String codeTemplate = Files.readString(templatePath);

        for (String curl: curlArray) {
            Map<ComponentType, List<String>> componentList = CurlToComponents.extractComponents(curl);

            String requestType = componentList.get(ComponentType.REQUEST_TYPE).get(0);
            String url = componentList.get(ComponentType.URL).get(0);

            String restTemplateBlock = String.format(
                    "HttpEntity<String> requestEntity = new HttpEntity<String>(\"\");\n" +
                            "ResponseEntity<String> responseEntity = restTemplate.exchange(\"%s\", HttpMethod.%s, requestEntity, String.class);\n",
                    url, requestType);

            String restResponse = codeExecutor.runCode("GeneratedCodeForInitialHttpRequest", codeTemplate, restTemplateBlock, "String", "responseEntity.getBody()");
            String tests = JsonToTests.jsonToTests(restResponse);

            String jsonNodeConversionBlock = "final JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);\n";
            String fullCodeBlockWithTests =
                    restTemplateBlock +
                            jsonNodeConversionBlock +
                            tests;

            String output = codeExecutor.runCode(UrlToClassName.urlToClassName(url), codeTemplate,  fullCodeBlockWithTests, "boolean", "true");

            if(!output.equals("true")) {
                throw new RuntimeException("The following curl failed: " + curl);
            }
        }
    }


}
