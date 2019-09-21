package testgenerator;

import codeexecutor.CodeExecutor;
import codeexecutor.UrlToClassName;
import codegenerator.CommandSectionsToInitialization;
import codegenerator.CommandSectionsToTests;
import datastructures.CommandType;
import org.openjdk.tools.javac.jvm.Code;
import splitter.CurlToComponents;

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

//        try {
//            new ObjectMapper().writeValue(new File("tmp/exctractedCurl.json"), componentMap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        for (String curl: curlArray) {
            Map<CommandType, List<String>> componentMap = CurlToComponents.extractComponents(curl);


            String restTemplateBlock = CommandSectionsToInitialization.commandSectionsToRestTemplate(componentMap);
            String restResponse = codeExecutor.runCode("GeneratedCodeForInitialHttpRequest", defaultCodeTemplate, restTemplateBlock, "String", "responseEntity.getBody()");
            String tests = CommandSectionsToTests.jsonToTests(restResponse);

            String fullCodeBlockWithTests =
                    restTemplateBlock +
                            tests;

            String url = componentMap.get(CommandType.NONE).get(0);
            String output = codeExecutor.runCode(UrlToClassName.urlToClassName(url) +"Default", defaultCodeTemplate,  fullCodeBlockWithTests, "boolean", "true");

            if(!output.equals("true")) {
                throw new RuntimeException("The following curl failed: " + curl);
            }

            codeExecutor.generateCode(UrlToClassName.urlToClassName(url), customTemplate,  fullCodeBlockWithTests, "boolean", "true");
        }
    }
}
