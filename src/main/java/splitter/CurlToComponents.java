package splitter;

import com.google.common.collect.ImmutableList;
import component.ComponentType;

import java.util.*;

//curl -d "param1=value1&param2=value2&param3=http://test.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:3000/data
public class CurlToComponents {
    public static Map<ComponentType, List<String>> extractComponents(String curl) {
        Map<ComponentType, List <String>> componentMap = new HashMap<>();

        SeparatedStringComponentList currentSeparatedStringComponent = getRequestType(curl);

        componentMap.computeIfAbsent(ComponentType.HEADER, k -> new LinkedList<>());
        componentMap.get(ComponentType.HEADER)
                .addAll(getHeadersFromCurl(curl));

        componentMap.computeIfAbsent(ComponentType.REQUEST_TYPE, k -> new LinkedList<>());
        componentMap.get(ComponentType.REQUEST_TYPE)
        .addAll(currentSeparatedStringComponent.getExtractedValues());

        componentMap.computeIfAbsent(ComponentType.URL, k -> new LinkedList<>());
        componentMap.get(ComponentType.URL)
                .addAll(getUrlFromCurl(curl).getExtractedValues());


        return componentMap;
    }


    private static List<String> getHeadersFromCurl(String curl) {
        List<String> headers = new ArrayList<>();

        do {
            Range dataRange = getArgumentDataRange(curl, ImmutableList.of("-H", "--header"));

            if(dataRange.getFlagStart() > -1 && dataRange.getValueStart() > -1 && dataRange.getValueEnd() > -1) {
                headers.add(curl.substring(dataRange.getValueStart(), dataRange.getValueEnd()));
                curl = curlWithRangeRemoved(curl, dataRange);
            } else {
                break;
            }
        } while (true);
        return headers;
    }

    private static Range getArgumentDataRange(String curl, List<String> argumentSynonyms) {
        int flagStart = -1;
        int dataStart = -1;
        int dataEnd = -1;

        for(String argumentSynonym: argumentSynonyms) {
            flagStart = flagStart> -1 ? flagStart : curl.indexOf(argumentSynonym);
        }

        if(flagStart > -1 ) {
            char enclosingQuote = getEnclosingQuote(curl, flagStart);
            dataStart = curl.indexOf(enclosingQuote, flagStart + 1);
            dataEnd = curl.indexOf(enclosingQuote, dataStart + 1);
            if(dataEnd == -1) {
                dataStart = -1;
            }
        }
        return new Range(flagStart, dataStart, dataEnd);
    }

    private static Range getDataRange(String curl) {
        return getArgumentDataRange(curl, ImmutableList.of("-d", "--data-", "--data"));
    }

    private static SeparatedStringComponentList getUrlFromCurl(String curl) {
        int httpStart = 0;
        boolean found = false;
        do {
             httpStart = curl.indexOf("http", httpStart + 1);
             found = isUrlDestination(curl, httpStart);
        } while (!found);

        if(!found) {
            throw new RuntimeException("No destination address found");
        } else {
            int valueStart = httpStart;
            int valueEnd = curl.indexOf(' ', valueStart + 1);
            if(valueEnd == -1) {
                valueEnd = curl.length();
            }

            Range range = new Range(valueStart, valueStart, valueEnd);

            return new SeparatedStringComponentList(
                    curlWithRangeRemoved(curl, range),
                    ComponentType.URL,
                    Collections.singletonList(curl.substring(range.getValueStart(), range.getValueEnd())));
        }

    }

    //Check if the url is what we send our data to.
    private static boolean isUrlDestination(String curl, int httpStart){
            if (curl.charAt(httpStart -1) == ' ' &&  curl.charAt(httpStart -2) != ':') {
                return true;
            } else {
                return false;
            }
    }

    public static char getEnclosingQuote(String curl, int start) {
        int doubleQuoteStart = curl.indexOf("\"", start);
        int singleQuoteStart = curl.indexOf("'", start);

        if(doubleQuoteStart == -1) {
            doubleQuoteStart = Integer.MAX_VALUE;
        }

        if(singleQuoteStart == -1) {
            singleQuoteStart = Integer.MAX_VALUE;
        }

        return doubleQuoteStart < singleQuoteStart ? '\"'  : '\'';

    }

    private static SeparatedStringComponentList getRequestType(String curl){
        Range dataRange = getDataRange(curl);
        if ((dataRange.getValueStart() > 0 || curl.contains("-X POST") || curl.contains("--request POST")) &&
                !curl.contains("-X GET") && !curl.contains("--request GET")) {
            return new SeparatedStringComponentList(curlWithRangeRemoved(curl, dataRange), ComponentType.REQUEST_TYPE, Collections.singletonList("POST"));
        } else {
            return new SeparatedStringComponentList(curl, ComponentType.REQUEST_TYPE, Collections.singletonList("GET"));
        }
    }

    private static String curlWithRangeRemoved(final String curl, final Range range) {
        int valueEnd = range.getValueEnd() + 1;
        if(valueEnd > curl.length()) {
            valueEnd--;
        }
        return curl.replace(curl.substring(range.getFlagStart(), valueEnd), "");
    }

}
