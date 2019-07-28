package testgenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

public class JsonToTests {

    final static ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    public static void jsonToTest(String json) {

        try {
            JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
            jsonToTest(jsonNode, "jsonNode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String jsonToTest(JsonNode jsonNode, String parentNodeSelector) {
//            if(jsonNode.get(0).get("id").asInt() != 1) {
//                return "";
//            }
//
//            if(!jsonNode.get(0).get("name").asText().equals("Leanne Graham")) {
//                return "";
//            }
//
//            if(!jsonNode.get(0).get("address").get("street").asText().equals("Kulas Light")) {
//                return "";
//            }

            String generatedCodeString = "";
            if(jsonNode.isArray()) {
                int jsonElementIndex = 0;
                for(JsonNode jsonNodeElement: jsonNode) {
                   generatedCodeString += jsonToTest(jsonNode, parentNodeSelector + ".get(" + jsonElementIndex + ")");
                   jsonElementIndex ++;
                }
            } else if (jsonNode.isValueNode()) {
                Iterator<JsonNode> elements = jsonNode.elements();
                while(elements.hasNext()) {
                    JsonNode element = elements.next();
                    generatedCodeString += jsonToTest(element, parentNodeSelector) + "\n";
                }
            } else {
                generatedCodeString +=
                        "if (!(" +
                                "" +getConversionString(jsonNode) + getConversionString(jsonNode) +
                                "" +generateEquals(jsonNode) + ")) {\n" +
                                "   return false;\n" +
                                "}";
            }
            return generatedCodeString;
    }

    public static String generateEquals(JsonNode jsonNode) {
        if(jsonNode.isTextual()){
            return ".equals(\"" + jsonNode.textValue() + "\")";
        } else if(jsonNode.isBoolean()) {
            return " == " + jsonNode.textValue();
        } else if (jsonNode.isDouble()) {
            return " == " + jsonNode.textValue();
        } else if (jsonNode.isInt()) {
            return " == " + jsonNode.textValue();
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
