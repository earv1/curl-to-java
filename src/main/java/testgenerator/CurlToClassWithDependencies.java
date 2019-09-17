package testgenerator;

import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import component.ComponentType;
import splitter.CurlToComponents;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CurlToClassWithDependencies {
    private static CodeExecutor codeExecutor = new CodeExecutor();

    public static void generateClassWithDependencies (String [] curlArray) throws Exception {
        Path customTemplatePath = Paths.get(ClassLoader.getSystemResource("template/Custom.java").toURI());
        String customTemplate = Files.readString(customTemplatePath);

        Path defaultTemplatePath = Paths.get(ClassLoader.getSystemResource("template/Default.java").toURI());
        String defaultCodeTemplate = Files.readString(defaultTemplatePath);

        for (String curl: curlArray) {
            Map<ComponentType, List<String>> componentList = CurlToComponents.extractComponents(curl);

            String requestType = componentList.get(ComponentType.REQUEST_TYPE).get(0);
            String url = componentList.get(ComponentType.URL).get(0);

            String addHeaderCode = "";
            for (String header: componentList.get(ComponentType.HEADER)) {
                String [] headerComponents = header.split(":", 2);
                addHeaderCode += "headers.add(\"" + headerComponents[0] + "\", \"" + headerComponents[1] + "\"); \n";
            }
            List<String> dataList = componentList.get(ComponentType.DATA);
            String data = "";
            if(dataList.size() > 0) {
                data = dataList.get(0).replace("\"", "\\\"");
            }
            String restTemplateBlock = String.format(
                    "HttpHeaders headers = new HttpHeaders(); \n " +
                            addHeaderCode +
                            "HttpEntity<String> requestEntity = new HttpEntity<String>(\"" + data + "\", " +
                            "headers);\n" +
                            "ResponseEntity<String> responseEntity = restTemplate.exchange(\"%s\", HttpMethod.%s, requestEntity, String.class);\n",
                    url, requestType);

            String restResponse = codeExecutor.runCode("GeneratedCodeForInitialHttpRequest", defaultCodeTemplate, restTemplateBlock, "String", "responseEntity.getBody()");
            String tests = JsonToTests.jsonToTests(restResponse);

            String jsonNodeConversionBlock = "final JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);\n";
            String fullCodeBlockWithTests =
                    restTemplateBlock +
                            jsonNodeConversionBlock +
                            tests;

            String output = codeExecutor.runCode(UrlToClassName.urlToClassName(url) +"Default", defaultCodeTemplate,  fullCodeBlockWithTests, "boolean", "true");

            if(!output.equals("true")) {
                throw new RuntimeException("The following curl failed: " + curl);
            }

            codeExecutor.generateCode(UrlToClassName.urlToClassName(url), customTemplate,  fullCodeBlockWithTests, "boolean", "true");
        }
    }
}
