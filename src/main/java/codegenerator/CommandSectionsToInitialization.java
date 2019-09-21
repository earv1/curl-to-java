package codegenerator;

import datastructures.CommandType;

import java.util.List;
import java.util.Map;

public class CommandSectionsToInitialization {
    public static String commandSectionsToRestTemplate(Map<CommandType, List<String>> componentMap){

        String requestType = componentMap.get(CommandType.REQUEST_TYPE).get(0);
        String url = componentMap.get(CommandType.NONE).get(0);

        String addHeaderCode = "\n";
        for (String header: componentMap.get(CommandType.HEADER)) {
            String [] headerComponents = header.split(":", 2);
            addHeaderCode += "headers.add(\"" + headerComponents[0] + "\", \"" + headerComponents[1] + "\"); \n";
        }
        List<String> dataList = componentMap.get(CommandType.DATA);
        String data = "";
        if(dataList.size() > 0) {
            data = dataList.get(0).replace("\"", "\\\"");
        }

        return  "HttpHeaders headers = new HttpHeaders(); \n " +
                        addHeaderCode +
                        "HttpEntity<String> requestEntity = new HttpEntity<String>(\"" + data + "\", " +
                        "headers);\n" +
                        "ResponseEntity<String> responseEntity = restTemplate.exchange(\"" + url +"\", HttpMethod." + requestType + ", requestEntity, String.class);\n " +
                        "final JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);";
    }
}
