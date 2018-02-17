package model;

import java.util.Random;

public class Die {
    private int value;

    public Die(Die d1) {
        this.value = d1.getValue();
    }

    public Die() {
    }

    public Die(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void roll() {
        value = new Random().nextInt(6) + 1;
    }

    @Override
    public boolean equals(Object obj) {
        final Die other = (Die) obj;
        if (this.value == other.value) {
            return true;
        }
        return false;
    }
}