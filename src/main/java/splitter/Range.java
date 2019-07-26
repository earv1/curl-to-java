package splitter;

public class Range {
    private final int flagStart;
    private final int valueStart;
    private final int valueEnd;

    public Range(int flagStart, int valueStart, int valueEnd) {
        this.flagStart = flagStart;
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    public int getFlagStart() {
        return flagStart;
    }

    public int getValueStart() {
        return valueStart;
    }

    public int getValueEnd() {
        return valueEnd;
    }
}
