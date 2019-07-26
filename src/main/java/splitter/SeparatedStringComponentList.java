package splitter;

import component.Component;
import component.ComponentType;

import java.util.List;

public class SeparatedStringComponentList {
    private String remainingCurl;
    private ComponentType componentType;
    private List<Component> extractedComponent;

    public SeparatedStringComponentList(final String remainingCurl, final ComponentType componentType, final List<Component> extractedComponent) {
        this.remainingCurl = remainingCurl;
        this.componentType = componentType;
        this.extractedComponent = extractedComponent;
    }

    public String getRemainingCurl() {
        return remainingCurl;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public List<Component> getExtractedComponent() {
        return extractedComponent;
    }
}
