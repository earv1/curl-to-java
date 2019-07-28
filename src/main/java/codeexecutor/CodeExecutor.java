package codeexecutor;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CodeExecutor {
    private static String fileName = "Test";
    public static String runCode(String codeToExecute, String variableToGrabOutputFrom) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            // IMPORTANT: Save the old System.out!
            PrintStream old = System.out;
            // Tell Java to use your special stream
            System.setOut(ps);


            JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager standardFileManager = jc.getStandardFileManager(null, null, null);
            File javaFile = new File(fileName + ".java"); //create file in current working directory
            PrintWriter printWriter= new PrintWriter(javaFile);
            printWriter.println(
                    "import org.springframework.http.HttpEntity;\n" +
                            "import org.springframework.http.ResponseEntity;\n" +
                            "import org.springframework.web.client.RestTemplate;\n" +
                            "import org.springframework.http.HttpMethod;\n" +
                            "public class " + fileName + "\n"  +
                            "{\n " +
                            "   public static void execute(StringBuffer output)\n" +
                            "   {\n" +
                            codeToExecute +
                            "output.append(" + variableToGrabOutputFrom +");"+
                            "   }\n" +
                            "}");
            printWriter.close();
            Iterable standardFileManagerJavaFileObjects = standardFileManager.getJavaFileObjects(javaFile);
            if (!jc.getTask(null, standardFileManager, null, null, null, standardFileManagerJavaFileObjects).call()) { //compile the code
                throw new RuntimeException("compilation failed");
            }
            URL[] urls = new URL[]{new File("").toURI().toURL()}; //use current working directory
            URLClassLoader ucl = new URLClassLoader(urls);
            Object object = ucl.loadClass(fileName).newInstance();
            StringBuffer output = new StringBuffer("");
            object.getClass().getDeclaredMethod("execute", output.getClass()).invoke(object, output);
            new File(fileName + ".class").delete();
            javaFile.delete();
            return output.toString();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | FileNotFoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
