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
    private final static String FILE_NAME = "Test";
    private final String imports;


    public CodeExecutor(String imports) {
        this.imports = imports;
    }

    public String runCode(String codeToExecute, String returnType, String variableToGrabOutputFrom) {
        try {
            JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager standardFileManager = jc.getStandardFileManager(null, null, null);
            File javaFile = new File(FILE_NAME + ".java"); //create file in current working directory

            String source = imports + "\n" +
                    "public class " + FILE_NAME + "\n"  +
                    "{\n " +
                    "   public static " + returnType + " execute() throws Exception\n" +
                    "   {\n" +
                    codeToExecute +
                    "return " + variableToGrabOutputFrom + ";"+
                    "   }\n" +
                    "}";
            String formattedSource = new Formatter().formatSource(source);
            PrintWriter printWriter= new PrintWriter(javaFile);
            printWriter.println(formattedSource);
            printWriter.close();

            Iterable standardFileManagerJavaFileObjects = standardFileManager.getJavaFileObjects(javaFile);
            if (!jc.getTask(null, standardFileManager, null, null, null, standardFileManagerJavaFileObjects).call()) { //compile the code
                throw new RuntimeException("compilation failed");
            }
            URL[] urls = new URL[]{new File("").toURI().toURL()}; //use current working directory
            URLClassLoader ucl = new URLClassLoader(urls);
            Object object = ucl.loadClass(FILE_NAME).newInstance();
            String output = object.getClass().getDeclaredMethod("execute").invoke(object).toString();
            new File(FILE_NAME + ".class").delete();
            javaFile.delete();
            return output;

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | FileNotFoundException | MalformedURLException | FormatterException e) {
            throw new RuntimeException(e);
        }
    }
}
