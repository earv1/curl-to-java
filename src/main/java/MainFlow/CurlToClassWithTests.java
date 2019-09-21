package MainFlow;

import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import codegenerator.CommandSectionsToInitialization;
import codegenerator.CommandSectionsToTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastructures.CommandType;
import splitter.CurlToComponents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurlToClassWithTests {
    private static CodeExecutor codeExecutor = new CodeExecutor();

    public static void generateClassWithDependencies (String [] curlArray) throws Exception {
        Path customTemplatePath = Paths.get(ClassLoader.getSystemResource("template/Custom.java").toURI());
        String customTemplate = Files.readString(customTemplatePath);

        Path defaultTemplatePath = Paths.get(ClassLoader.getSystemResource("template/Default.java").toURI());
        String defaultCodeTemplate = Files.readString(defaultTemplatePath);

        List<Map<CommandType, List<String>>> componentMapList = Arrays.stream(curlArray).map(CurlToComponents::extractComponents).collect(Collectors.toList());

        for (Map<CommandType, List<String>> componentMap: componentMapList) {
            String url = componentMap.get(CommandType.NONE).get(0);
            String classNameFromUrl = UrlToClassName.urlToClassName(url);

            componentMapToJsonFile(componentMap, classNameFromUrl);

            String restTemplateBlock = CommandSectionsToInitialization.commandSectionsToRestTemplate(componentMap);
            String restResponse = codeExecutor.runCode("GeneratedCodeForInitialHttpRequest", defaultCodeTemplate, restTemplateBlock, "String", "responseEntity.getBody()");
            String tests = CommandSectionsToTests.jsonToTests(restResponse);

            String fullCodeBlockWithTests =
                    restTemplateBlock +
                            tests;

            String output = codeExecutor.runCode(classNameFromUrl + "Default", defaultCodeTemplate,  fullCodeBlockWithTests, "boolean", "true");

            if(!output.equals("true")) {
                throw new RuntimeException("The following curl failed: " + componentMap.get(CommandType.ORIGINAL_COMMAND).get(0));
            }

            codeExecutor.generateCode(UrlToClassName.urlToClassName(url), customTemplate,  fullCodeBlockWithTests, "boolean", "true");
        }
    }

    private static void componentMapToJsonFile(Map<CommandType, List<String>> componentMap, String fileName){
         try {
            new ObjectMapper().writeValue(new File("tmp/" +fileName + ".json"), componentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
