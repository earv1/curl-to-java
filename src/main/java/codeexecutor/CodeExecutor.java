package codeexecutor;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CodeExecutor {

    public void deleteCodeFilesIfExists(String fileName) {
        File javaFile = new File(fileName + ".java");
        if(javaFile.exists()){
            javaFile.delete();
        }

        File classFile = new File(fileName + ".class");
        if(classFile.exists()){
            classFile.delete();
        }
    }

    public String getContainingClass() {
        return "import org.springframework.http.HttpEntity;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.client.RestTemplate;\n" +
                "import org.springframework.http.HttpMethod;\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "public class {class-name} {\n" +
                "\n" +
                "    final static RestTemplate restTemplate = new RestTemplate();\n" +
                "    final static Logger logger = LoggerFactory.getLogger({class-name}.class);\n" +
                "    final static ObjectMapper mapper = new ObjectMapper();\n" +
                "\n" +
                "\n" +
                "    public static {return-type} execute() throws Exception {\n" +
                "        {main-logic}\n" +
                "\n" +
                "        return {return-variable};\n" +
                "    }\n" +
                "}\n" +
                "\n";
    }

    public String getClassWithPlaceHolders(String fileName, String source, String mainLogic, String returnType, String variableToGrabOutputFrom) {
        return source.replace("{class-name}", fileName)
                .replace("{return-type}", returnType)
                .replace("{main-logic}", mainLogic)
                .replace("{return-variable}", variableToGrabOutputFrom);
    }

    public String runCode(String fileName, String source, String mainLogic, String returnType, String variableToGrabOutputFrom) {
        try {
            deleteCodeFilesIfExists(fileName);

            JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager standardFileManager = jc.getStandardFileManager(null, null, null);
            File javaFile = new File("tmp/" + fileName + ".java"); //create file in tmp directory, located in current working directory

            File javaFileParentFile = javaFile.getParentFile();
            javaFileParentFile.mkdirs();

            source = getClassWithPlaceHolders(fileName, source, mainLogic, returnType, variableToGrabOutputFrom);

            String formattedSource = null;
            try {
                formattedSource = new Formatter().formatSource(source);
            } catch (FormatterException e) {
                formattedSource = source;
            }
            PrintWriter printWriter= new PrintWriter(javaFile);
            printWriter.println(formattedSource);
            printWriter.close();

            Iterable standardFileManagerJavaFileObjects = standardFileManager.getJavaFileObjects(javaFile);
            if (!jc.getTask(null, standardFileManager, null, null, null, standardFileManagerJavaFileObjects).call()) { //compile the code
                throw new RuntimeException("compilation failed");
            }
            URL[] urls = new URL[]{javaFileParentFile.toURI().toURL()}; //Use the folder that the source file is located in
            URLClassLoader ucl = new URLClassLoader(urls);
            Object object = ucl.loadClass(fileName).newInstance();
            return object.getClass().getDeclaredMethod("execute").invoke(object).toString();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | FileNotFoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
