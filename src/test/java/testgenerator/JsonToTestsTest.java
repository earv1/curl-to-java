package testgenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonToTestsTest {

    final static ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    @Test
    void getConversionString() throws IOException {
        JsonNode jsonNodeString = mapper.readValue("{\"test\":\"amazing\"}", JsonNode.class);
        String stringConversionString = JsonToTests.getConversionString(jsonNodeString.get("test"));
        assertEquals(".asText()", stringConversionString);

        JsonNode jsonNodeInt = mapper.readValue("{\"test\":42}", JsonNode.class);
        String intConversionString = JsonToTests.getConversionString(jsonNodeInt.get("test"));
        assertEquals(".asInt()", intConversionString);

        JsonNode jsonNodeDouble = mapper.readValue("{\"test\":42.00}", JsonNode.class);
        String doubleConversionString = JsonToTests.getConversionString(jsonNodeDouble.get("test"));
        assertEquals(".asDouble()", doubleConversionString);

        JsonNode jsonNodeboolean = mapper.readValue("{\"test\":true}", JsonNode.class);
        String booleanConversionString = JsonToTests.getConversionString(jsonNodeboolean.get("test"));
        assertEquals(".asBoolean()", booleanConversionString);

    }
}