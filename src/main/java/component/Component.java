package component;

public class Component {
    public enum ComponentType {
        HEADER,
        URL,
        PAYLOAD,
        REQUEST_TYPE
    }

    final String value;
    public Component(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
