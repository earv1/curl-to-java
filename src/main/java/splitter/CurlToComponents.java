package splitter;

import component.Component;
import org.apache.commons.collections4.map.DefaultedMap;

import java.util.*;

//curl -d "param1=value1&param2=value2&param3=http://test.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:3000/data
public class CurlToComponents {
    public static Map<Component.ComponentType, List<Component>> extractComponents(String curl) {
        Map<Component.ComponentType, List <Component>> componentMap = new DefaultedMap<>(new LinkedList<>());
        SeparatedStringComponentList currentSeparatedStringComponent = getRequestType(curl);
        componentMap.get(currentSeparatedStringComponent.getComponentType())
        .addAll(currentSeparatedStringComponent.getExtractedComponent());

//        currentSeparatedStringComponent
//        componentList.addAll(currentSeparatedStringComponent.getExtractedComponent());

        return componentMap;
    }

    private void getUrl(String curl){
//        String regex
//
//        curl
//        return new SeparatedStringComponent()

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
            dataStart = curl.indexOf(enclosingQuote, dataStart);
            dataEnd = curl.indexOf(enclosingQuote);
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
