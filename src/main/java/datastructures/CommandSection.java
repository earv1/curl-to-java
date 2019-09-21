package datastructures;

public class CommandSection {
    private final String currentString;
    private final int nextStartingPoint;
    private final CommandType commandType;

    public CommandSection(String currentString, int nextStartingPoint) {
        this.currentString = currentString;
        this.nextStartingPoint = nextStartingPoint;
        this.commandType = CommandType.NONE;
    }

    public CommandSection(String currentString, int nextStartingPoint, CommandType commandType) {
        this.currentString = currentString;
        this.nextStartingPoint = nextStartingPoint;
        this.commandType = commandType;
    }

    public CommandSection(CommandSection commandSection, CommandType commandType) {
        this.currentString = commandSection.currentString;
        this.nextStartingPoint = commandSection.getNextStartingPoint();
        this.commandType = commandType;
    }

    public String getCurrentString() {
        return currentString;
    }

    public int getNextStartingPoint() {
        return nextStartingPoint;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
