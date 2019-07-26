package splitter;

import component.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//curl -d "param1=value1&param2=value2&param3=http://test.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:3000/data
public class CurlToComponents {
    public static List<Component> extractComponents(String curl) {
        List <Component> componentList = new LinkedList<>();
        SeparatedStringComponentList currentSeparatedStringComponent = getRequestType(curl);
        componentList.addAll(currentSeparatedStringComponent.getExtractedComponent());

//        currentSeparatedStringComponent
//        componentList.addAll(currentSeparatedStringComponent.getExtractedComponent());

        return componentList;


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
            dataStart = curl.indexOf("\"", dataStart);
            dataEnd = curl.indexOf("\"");
            if(dataEnd == -1) {
                dataStart = -1;
            }
        }
        return new Range(flagStart, dataStart, dataEnd);
    }
    private static SeparatedStringComponentList getRequestType(String curl){
        Range dataRange = getDataRange(curl);
        if (dataRange.getValueStart() >0 || curl.contains("-X ") || curl.contains("--request ")) {
            return new SeparatedStringComponentList(curlWithRangeRemoved(curl, dataRange), Collections.singletonList(new Component(Component.ComponentType.REQUEST_TYPE, "POST")));
        } else {
            return new SeparatedStringComponentList(curl, Collections.singletonList(new Component(Component.ComponentType.REQUEST_TYPE, "GET")));
        }
    }

    private static String curlWithRangeRemoved(final String curl, final Range range) {
        return curl.replace(curl, curl.substring(range.getFlagStart(), range.getValueEnd()));
    }

}
