package splitter;

import component.Component;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurlToComponentsTest {
    @Test
    void extractComponents() {
        List<Component> componentList = CurlToComponents.extractComponents(
                "curl -d \"param1=value1&param2=value2&param3=http://test.com\" -H \"Content-Type: application/x-www-form-urlencoded\" -X POST http://localhost:3000/data");
        assertEquals(componentList.size(), 1);
    }
}