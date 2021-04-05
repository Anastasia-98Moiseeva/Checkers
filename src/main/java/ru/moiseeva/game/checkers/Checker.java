package ru.moiseeva.game.checkers;

/**
 * Класс шашки.
 * @author Moiseeva Anastasia
 */
public class Checker {
    private Color color;
    private boolean isKing;

    /**
     * @param color - цвет шашки
     * @param isKing - идентификатор того, является ли шашка дамкой
     */
    public Checker(Color color, boolean isKing) {
        this.color = color;
        this.isKing = isKing;
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }
}
