package ru.moiseeva.converters;

import ru.moiseeva.game.board.BoardCell;
import ru.moiseeva.game.checkers.Checker;
import ru.moiseeva.game.checkers.Color;
import ru.moiseeva.game.moves.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Конвертер данных.
 * @author Moiseeva Anastasia
 */
public class DataConverter {

    /**
     * Создание последовательности клеток доски, заполненных шашками
     * @param coordinates - координаты клетки
     * @param color - цвет шашки
     * @return возвращает последовательность клеток, заполненных шашками
     */
    public List<BoardCell> getCheckerCells(String coordinates, Color color) {
        List<BoardCell> cells = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            if (i % 3 == 0) {
                int[] pair = getPairOfCoordinates(coordinates, i);
                if (Character.isUpperCase(coordinates.charAt(i))) {
                    BoardCell cell = new BoardCell(pair[0], pair[1], new Checker(color,true));
                    cells.add(cell);
                } else {
                    BoardCell cell = new BoardCell(pair[0], pair[1], new Checker(color,false));
                    cells.add(cell);
                }
            }
        }
        return cells;
    }

    /**
     * Метод преобразовывает строку в пару последовательностей из ходов шашке
     * @param str - строка с информацией о ходах шашек
     * @return возвращает пару последовательностей ходов, первая последовательность - ходы белых шашек, вторая -ходы черных шашек
     */
    public List<Move> getMoves(String str) {
        str += " ";
        List<Move> moves = new ArrayList<>();
        int i = 3;
        int[] from = getPairOfCoordinates(str, i - 3);
        boolean isStrike = str.charAt(i - 1) == ':';
        while (i < str.length() - 1) {
            if (i % 3 == 0) {
                int[] to = getPairOfCoordinates(str, i);
                moves.add(new Move(from, to, isStrike));
                from = new int[]{to[0], to[1]};
                isStrike = str.charAt(i + 2) == ':';
            }
            i++;
        }
        return moves;
    }

    /**
     * Получение пары строк с координатами шашек, находящихся на доске
     * @param cells - последовательность клеток, заполненных шашками
     * @return возвращает строку состоящую из координат белых и черных шашек
     */
    public String getResultStrings(List<BoardCell> cells){
        ArrayList<String> whiteCoordinates = new ArrayList<>();
        ArrayList<String> blackCoordinates = new ArrayList<>();
        for (BoardCell cell : cells) {
            Checker checker = cell.getChecker();
            if (checker.getColor() == Color.WHITE) {
                whiteCoordinates.add(getStringFromCoordinate(cell));
            } else {
                blackCoordinates.add(getStringFromCoordinate(cell));
            }
        }
        Collections.sort(whiteCoordinates);
        Collections.sort(blackCoordinates);
        String result = "";
        for (String str : whiteCoordinates) {
            result += str;
        }
        if (whiteCoordinates.size() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        result += System.lineSeparator();
        for (String str : blackCoordinates) {
            result += str;
        }
        if (blackCoordinates.size() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    private int[] getPairOfCoordinates(String str, int i) {
        return new int[]{str.charAt(i + 1) - '0' - 1, getNumByLetter(str.toLowerCase().charAt(i))};
    }

    private String getStringFromCoordinate(BoardCell cell){
        String column = getLetterByNum(cell.getColumn());
        String row = String.valueOf(cell.getRow() + 1);
        if (cell.getChecker().isKing()){
            column = column.toUpperCase();
        }
        return column + row + " ";
    }

    private int getNumByLetter(char letter) {
        switch (letter) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            default:
                return 8;
        }
    }

    private String getLetterByNum(int num) {
        switch (num) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
            default:
                return "";
        }
    }
}

