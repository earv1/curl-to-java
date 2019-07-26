package component;

public class Component {
    public enum ComponentType {
        HEADER,
        URL,
        PAYLOAD,
        REQUEST_TYPE
    }

    final ComponentType componentType;
    final String value;
    public Component(ComponentType componentType, String value) {
        this.componentType = componentType;
        this.value = value;
    }
}
