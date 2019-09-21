package splitter;

public class Range {
    private final int valueStart;
    private final int valueEnd;

    public Range(int flagStart, int valueStart, int valueEnd) {
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    public int getValueStart() {
        return valueStart;
    }

    public int getValueEnd() {
        return valueEnd;
    }
}
