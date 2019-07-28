import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import component.ComponentType;
import splitter.CurlToComponents;
import testgenerator.JsonToTests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static CodeExecutor codeExecutor = new CodeExecutor();
    public static void main (String [] args) throws Exception {
        String inputFilename = args[0];
        String [] curlArray = Files.readString(Paths.get(inputFilename)).split("[\n|\r]");
        String codeTemplate = Files.readString(Paths.get("template/Default/java"));

        for (String curl: curlArray) {
            Map<ComponentType, List<String>> componentList = CurlToComponents.extractComponents(curl);

            String requestType = componentList.get(ComponentType.REQUEST_TYPE).get(0);
            String url = componentList.get(ComponentType.URL).get(0);

            String restTemplateBlock = String.format(
                    "HttpEntity<String> requestEntity = new HttpEntity<String>(\"\");\n" +
                            "ResponseEntity<String> responseEntity = restTemplate.exchange(\"%s\", HttpMethod.%s, requestEntity, String.class);\n",
                    url, requestType);


            String restResponse = codeExecutor.runCode("Testing12345", codeTemplate, restTemplateBlock, "String", "responseEntity.getBody()");
            String tests = JsonToTests.jsonToTests(restResponse);

            String jsonNodeConversionBlock = "final JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);\n";
            String fullCodeBlockWithTests =
                    restTemplateBlock +
                            jsonNodeConversionBlock +
                            tests;




            String output = codeExecutor.runCode(UrlToClassName.urlToClassName(url), codeTemplate,  fullCodeBlockWithTests, "boolean", "true");


            if(!output.equals("success")) {
                throw new RuntimeException("The following curl failed: " + curl);
            }
        }

        Test2.execute();
    }


}
