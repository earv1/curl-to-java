import component.Component;
import splitter.CurlToComponents;

import java.util.List;

public class Main {
    public static void main (String [] args) {
        List<Component> componentList = CurlToComponents.extractComponents("curl -d \"param1=value1&param2=value2&param3=http://test.com\" -H \"Content-Type: application/x-www-form-urlencoded\" -X POST http://localhost:3000/data");
        System.out.println(componentList);
    }
}
