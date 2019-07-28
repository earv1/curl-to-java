package testgenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonToTests {

    final static ObjectMapper mapper = new ObjectMapper(); // create once, reuse
    public static void jsonToTest(String json, String parent) {
        try {

            JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
            if(jsonNode.get(0).get("id").asInt() != 1) {
                return;
            }

            if(!jsonNode.get(0).get("name").asText().equals("Leanne Graham")) {
                return;
            }

            if(!jsonNode.get(0).get("address").get("street").asText().equals("Kulas Light")) {
                return;
            }
            JsonNode s = jsonNode.get(0).get("id");

            System.out.println(jsonNode);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConversionString(JsonNode jsonNode){
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
