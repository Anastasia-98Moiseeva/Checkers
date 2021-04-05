package ru.moiseeva.game.board;

import ru.moiseeva.game.checkers.Checker;

/**
 * Класс клетки шахматной доски.
 * @author Moiseeva Anastasia
 */
public class BoardCell {
    private int row;
    private int column;
    private boolean isEmpty;
    private Checker checker;

    /**
     * @param row - строка, в которой расположена клетка на доске
     * @param column - столбец, в котором расположена клетка на доске
     */
    public BoardCell(int row, int column) {
        this.row = row;
        this.column = column;
        this.checker = null;
        isEmpty = true;
    }

    /**
     * @param row - строка, в которой расположена клетка на доске
     * @param column - столбец, в котором расположена клетка на доске
     * @param checker - шашка, которую необходимо установить в клетку
     */
    public BoardCell(int row, int column, Checker checker) {
        this.row = row;
        this.column = column;
        setChecker(checker);
    }

    /**
     * Метод, определяющий является ли текущая клетка белой
     * @return возвращает true - если клетка белая, false - если клетка черная
     */
    public boolean isWhiteCell() {
        return (row + column) % 2 != 0;
    }

    public boolean isTopEdge() {
        return row == 7;
    }

    public boolean isBottomEdge() {
        return row == 0;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
        this.isEmpty = false;
    }

    public void deleteChecker() {
        this.checker = null;
        this.isEmpty = true;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Checker getChecker() {
        return checker;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
