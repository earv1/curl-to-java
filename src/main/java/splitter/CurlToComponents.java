package splitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import datastructures.CommandConversionException;
import datastructures.CommandSection;
import datastructures.CommandType;

import java.io.File;
import java.io.IOException;
import java.util.*;

//TODO
//Make map immutable to force deep cloning
//curl -d "param1=value1&param2=value2&param3=http://test.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:3000/data
public class CurlToComponents {


    public static Map<CommandType, List<String>> extractComponents(String curl) throws CommandConversionException {
        Map<CommandType, List <String>> componentMap = new HashMap<>();

        componentMap.computeIfAbsent(CommandType.HEADER, k -> new LinkedList<>());
        componentMap.computeIfAbsent(CommandType.REQUEST_TYPE, k -> new LinkedList<>());
        componentMap.computeIfAbsent(CommandType.DATA, k -> new LinkedList<>());
        componentMap.computeIfAbsent(CommandType.NONE, k -> new LinkedList<>());


        componentMap = extractComponents(validateAndRemoveCurlCommand(curl), componentMap);

        componentMap = addRequestTypeIfNonExistent(componentMap);

        try {
            new ObjectMapper().writeValue(new File("tmp/exctractedCurl.json"), componentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return componentMap;
    }

    private static Map<CommandType, List <String>>  addRequestTypeIfNonExistent(Map<CommandType, List <String>> componentMap ){
        if(componentMap.get(CommandType.REQUEST_TYPE).size() == 0) {
            String requestType = "GET";
            if(componentMap.get(CommandType.DATA).size() > 0) {
                requestType = "POST";
            }
            componentMap.get(CommandType.REQUEST_TYPE).add(requestType);
        }
        return componentMap;

    }


    private static Map<CommandType, List <String>> extractComponents(String curl, Map<CommandType, List <String>> componentMap) throws CommandConversionException {
        CommandSection nextCommandSection = extractNextCommandSection(curl);
        String remainingCurl = curl.substring(nextCommandSection.getNextStartingPoint()).trim();
        componentMap.get(nextCommandSection.getCommandType()).add(nextCommandSection.getCurrentString());

        if(remainingCurl.trim().length() <=0) {
            return componentMap;
        } else {
            return extractComponents(remainingCurl, componentMap);
        }
    }

    private static CommandSection extractNextCommandSection(String curl) throws CommandConversionException {
        curl = curl.trim();
        char nextChar = curl.charAt(0);
        if (isQuote(nextChar)) {
            return new CommandSection(getPayloadInQuote(curl, nextChar), CommandType.NONE);
        } else {

            String [] commandAndRemainingCurl = curl.split("\\s", 2);
            String command = commandAndRemainingCurl[0];
            String remainingCurl = commandAndRemainingCurl[1].trim();
            nextChar = remainingCurl.charAt(0);
            CommandType commandType = extractCommandType(command.trim());
            CommandSection commandSectionWithRemainingLengthNotIncludingCommand = getPayloadInQuote(remainingCurl, nextChar);
            return new CommandSection(commandSectionWithRemainingLengthNotIncludingCommand.getCurrentString(),
                    commandSectionWithRemainingLengthNotIncludingCommand.getNextStartingPoint() + command.length() + 1,
                    commandType);

        }
    }

    private static CommandType extractCommandType(String commandSection) {
        List dataOptions = ImmutableList.of("-d", "--data-raw", "--data");
        List headerOptions = ImmutableList.of("-H", "--header");
        List requestTypeOptions = ImmutableList.of("-X", "--request");

        if(dataOptions.contains(commandSection)) {
            return CommandType.DATA;
        } else if (headerOptions.contains(commandSection)) {
            return CommandType.HEADER;
        } else if (requestTypeOptions.contains(commandSection)) {
            return CommandType.REQUEST_TYPE;
        } else {
            return CommandType.NONE;
        }

    }

    private static CommandSection getPayloadInQuote(String curl, char enclosingQuote) throws CommandConversionException {
        final int dataStart = 1;
        int dataEnd = curl.indexOf(enclosingQuote, dataStart);

        do {
            if(curl.charAt(dataEnd - 1) != '/') {
                break;
            } else if (dataEnd > curl.length()) {
                throw new CommandConversionException();
            }  else {
                dataEnd = curl.indexOf(enclosingQuote, dataEnd + 1);
            }

        } while (true);

        return new CommandSection(curl.substring(dataStart, dataEnd), dataEnd + 1);
    }


    private static String validateAndRemoveCurlCommand(String curl) {
        String curlRegex = "^curl ";

        //Don't want to have to deal with spaces that don't mean anything
        curl = curl.trim();

        if(curl.matches(curlRegex + ".*")){
            return curl.replaceAll(curlRegex, "");
        } else {
            throw new RuntimeException("Not a valid curl command, must start with 'curl '");
        }
    }



    private static boolean isQuote(char charToCheck) {
        return charToCheck == '"' || charToCheck == '\'';
    }



}
