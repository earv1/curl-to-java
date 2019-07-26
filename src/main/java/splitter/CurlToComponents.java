package splitter;

import component.Component;

import java.util.*;

//curl -d "param1=value1&param2=value2&param3=http://test.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:3000/data
public class CurlToComponents {
    public static Map<Component.ComponentType, List<Component>> extractComponents(String curl) {
        Map<Component.ComponentType, List <Component>> componentMap = new HashMap<>();

        SeparatedStringComponentList currentSeparatedStringComponent = getRequestType(curl);

        componentMap.computeIfAbsent(Component.ComponentType.REQUEST_TYPE, k -> new LinkedList<>());
        componentMap.get(Component.ComponentType.REQUEST_TYPE)
        .addAll(currentSeparatedStringComponent.getExtractedComponent());

        componentMap.computeIfAbsent(Component.ComponentType.URL, k -> new LinkedList<>());
        componentMap.get(Component.ComponentType.URL)
                .addAll(getUrlFromCurl(curl).getExtractedComponent());

        return componentMap;
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
                    Component.ComponentType.URL,
                    Collections.singletonList(new Component(curl.substring(range.getValueStart(), range.getValueEnd()))));
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

    private static Range getDataRange(String curl) {
        int flagStart = -1;
        int dataStart = -1;
        int dataEnd = -1;
        flagStart = flagStart> -1 ? flagStart : curl.indexOf("-d");
        flagStart = flagStart> -1 ? flagStart : curl.indexOf("--data-");
        flagStart = flagStart> -1 ? flagStart : curl.indexOf("--data ");

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

    private static char getEnclosingQuote(String curl, int start) {
        int doubleQuoteStart = curl.indexOf("\"", start);
        int singleQuoteStart = curl.indexOf("'", start);

        return doubleQuoteStart < singleQuoteStart ? '\"'  : '\'';

    }
    private static SeparatedStringComponentList getRequestType(String curl){
        Range dataRange = getDataRange(curl);
        if (dataRange.getValueStart() >0 || curl.contains("-X ") || curl.contains("--request ")) {
            return new SeparatedStringComponentList(curlWithRangeRemoved(curl, dataRange), Component.ComponentType.REQUEST_TYPE, Collections.singletonList(new Component("POST")));
        } else {
            return new SeparatedStringComponentList(curl, Component.ComponentType.REQUEST_TYPE, Collections.singletonList(new Component("GET")));
        }
    }

    private static String curlWithRangeRemoved(final String curl, final Range range) {
        return curl.replace(curl, curl.substring(range.getFlagStart(), range.getValueEnd()));
    }

}
