package ru.moiseeva.game.moves;

/**
 * Класс хода шашки.
 * @author Moiseeva Anastasia
 */
public class Move {
    private int[] from;
    private int[] to;
    private boolean isStrikeMove;

    /**
     * @param from - начальная точка
     * @param to - конечная точка
     * @param isStrikeMove - идентификатор того, бьет ли шашка во время хода другую шашку
     */
    public Move(int[] from, int[] to, boolean isStrikeMove) {
        this.from = from;
        this.to = to;
        this.isStrikeMove = isStrikeMove;
    }

    public int[] getTo() {
        return to;
    }

    public int[] getFrom() {
        return from;
    }

    public boolean isStrikeMove() {
        return isStrikeMove;
    }
}

