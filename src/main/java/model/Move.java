package model;

public class Move {
    private int fromPosition;
    private int toPosition;

    public Move(int fromPosition, int toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }
}
