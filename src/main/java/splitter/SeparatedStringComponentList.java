package splitter;

import component.Component;

import java.util.List;

public class SeparatedStringComponentList {
    private String remainingCurl;
    private Component.ComponentType componentType;
    private List<Component> extractedComponent;

    public SeparatedStringComponentList(final String remainingCurl, final Component.ComponentType componentType, final List<Component> extractedComponent) {
        this.remainingCurl = remainingCurl;
        this.componentType = componentType;
        this.extractedComponent = extractedComponent;
    }

    public String getRemainingCurl() {
        return remainingCurl;
    }

    public Component.ComponentType getComponentType() {
        return componentType;
    }

    public List<Component> getExtractedComponent() {
        return extractedComponent;
    }
}
