package testgenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonToTests {

    final static ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    public static String jsonToTests(String json) {

        try {
            JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
            return jsonToTests(jsonNode, "jsonNode", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String jsonToTests(JsonNode jsonNode, String parentNodeSelector, String fieldNameSelector) {
            String generatedCodeString = "";
            if(jsonNode.isArray()) {
                int jsonElementIndex = 0;
                for(JsonNode jsonNodeElement: jsonNode) {
                   generatedCodeString += jsonToTests(jsonNodeElement, parentNodeSelector + ".get(" + jsonElementIndex + ")", "");
                   jsonElementIndex ++;
                }
            } else if (!jsonNode.isValueNode()) {
                Iterator<Map.Entry<String, JsonNode>> elements = jsonNode.fields();
                while(elements.hasNext()) {
                    Map.Entry<String, JsonNode> entry = elements.next();
                    JsonNode element = entry.getValue();
                    generatedCodeString += jsonToTests(element, parentNodeSelector + fieldNameSelector, ".get(\"" + entry.getKey() + "\")") + "\n";
                }
            } else {
                generatedCodeString +=
                        "if (!(" +
                                parentNodeSelector + fieldNameSelector + getConversionString(jsonNode) + generateEquals(jsonNode) + ")) {\n" +
                                "   return false;\n" +
                                "}";
            }
            return generatedCodeString;
    }

    public static String generateEquals(JsonNode jsonNode) {
        if(jsonNode.isTextual()){
            return ".equals(\"" + jsonNode.textValue() + "\")";
        } else if(jsonNode.isBoolean()) {
            return " == " + jsonNode.booleanValue();
        } else if (jsonNode.isDouble()) {
            return " == " + jsonNode.doubleValue();
        } else if (jsonNode.isInt()) {
            return " == " + jsonNode.intValue();
        } else {
            return "UNKNOWN CONVERSION";
        }
    }

    public static String getConversionString(JsonNode jsonNode) {
        if(jsonNode.isTextual()){
            return ".asText()";
        } else if(jsonNode.isBoolean()) {
            return ".asBoolean()";
        } else if (jsonNode.isDouble()) {
            return ".asDouble()";
        } else if (jsonNode.isInt()) {
            return ".asInt()";
        } else {
            return "UNKNOWN CONVERSION";
        }
    }
}
