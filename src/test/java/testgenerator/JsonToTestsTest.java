package testgenerator;

import codegenerator.CommandSectionsToTests;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class JsonToTestsTest {

    final static ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    @Test
    void getConversionString() throws IOException {
        JsonNode jsonNodeString = mapper.readValue("{\"test\":\"amazing\"}", JsonNode.class);
        String stringConversionString = CommandSectionsToTests.getConversionString(jsonNodeString.get("test"));
        assertEquals(".asText()", stringConversionString);

        JsonNode jsonNodeInt = mapper.readValue("{\"test\":42}", JsonNode.class);
        String intConversionString = CommandSectionsToTests.getConversionString(jsonNodeInt.get("test"));
        assertEquals(".asInt()", intConversionString);

        JsonNode jsonNodeDouble = mapper.readValue("{\"test\":42.00}", JsonNode.class);
        String doubleConversionString = CommandSectionsToTests.getConversionString(jsonNodeDouble.get("test"));
        assertEquals(".asDouble()", doubleConversionString);

        JsonNode jsonNodeboolean = mapper.readValue("{\"test\":true}", JsonNode.class);
        String booleanConversionString = CommandSectionsToTests.getConversionString(jsonNodeboolean.get("test"));
        assertEquals(".asBoolean()", booleanConversionString);

    }
}