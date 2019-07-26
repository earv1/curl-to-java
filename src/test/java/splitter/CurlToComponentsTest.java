package splitter;

import component.Component;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurlToComponentsTest {
    @Test
    public void extractComponentsTest() {
        Map<Component.ComponentType, List<Component>> componentList = CurlToComponents.extractComponents(
                "curl -d \"param1=value1&param2=value2&param3=http://test.com\" " +
                        "-H 'Accept-Language: en-US,en;q=0.5' " +
                        "-H \"Content-Type: application/x-www-form-urlencoded\" -X POST http://localhost:3000/data");

        List<Component> requestTypes = componentList.get(Component.ComponentType.REQUEST_TYPE);
        assertEquals(1, requestTypes.size());
        assertEquals("POST", requestTypes.get(0).getValue());
    }
}