package splitter;

import component.ComponentType;

import java.util.List;

public class SeparatedStringComponentList {
    private String remainingCurl;
    private ComponentType componentType;
    private List<String> extractedValues;

    public SeparatedStringComponentList(final String remainingCurl, final ComponentType componentType, final List<String> extractedValues) {
        this.remainingCurl = remainingCurl;
        this.componentType = componentType;
        this.extractedValues = extractedValues;
    }

    public String getRemainingCurl() {
        return remainingCurl;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public List<String> getExtractedValues() {
        return extractedValues;
    }
}
