package splitter;

import component.Component;

import java.util.List;

public class SeparatedStringComponentList {
    private String remainingCurl;
    private List<Component> extractedComponent;

    public SeparatedStringComponentList(String remainingCurl, List<Component> extractedComponent) {
        this.remainingCurl = remainingCurl;
        this.extractedComponent = extractedComponent;
    }

    public String getRemainingCurl() {
        return remainingCurl;
    }

    public List<Component> getExtractedComponent() {
        return extractedComponent;
    }
}
