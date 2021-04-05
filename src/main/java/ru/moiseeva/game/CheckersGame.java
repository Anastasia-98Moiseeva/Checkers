package ru.moiseeva.game;

import ru.moiseeva.exceptions.CheckerGameException;
import ru.moiseeva.game.board.BoardCell;
import ru.moiseeva.game.checkers.Checker;
import ru.moiseeva.game.checkers.Color;
import ru.moiseeva.game.moves.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс игры в шашки.
 * @author Moiseeva Anastasia
 */
public class CheckersGame {
    private final int boardSize = 8;
    private BoardCell[][] board = new BoardCell[boardSize][boardSize];

    /**
     * @param whiteCheckerCells - клетки звполненные белыми шашками
     * @param blackCheckerCells - клетки звполненные черными шашками
     */
    public CheckersGame(List<BoardCell> whiteCheckerCells, List<BoardCell> blackCheckerCells) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new BoardCell(i, j);
            }
        }
        setCheckerCells(whiteCheckerCells);
        setCheckerCells(blackCheckerCells);
    }

    /**
     * Функция, запускающая игру в шашки
     * @param whiteCheckersMoves - последовательность ходов белых шашек
     * @param blackCheckersMoves - последовательность ходов черных шашек
     * @return возвращает результат игры в шашки (результатом является расположение фигур на доске, после выполнения всех ходов)
     */
    public List<BoardCell> play(List<List<Move>> whiteCheckersMoves, List<List<Move>> blackCheckersMoves) throws CheckerGameException {
        for (int i = 0; i < whiteCheckersMoves.size(); i++) {
            playRound(whiteCheckersMoves.get(i), blackCheckersMoves.get(i));
        }
        return getPlayResult();
    }

    private void setCheckerCells(List<BoardCell> checkerCells) {
        for (int i = 0; i < checkerCells.size(); i++) {
            BoardCell cell = checkerCells.get(i);
            board[cell.getRow()][cell.getColumn()] = cell;
        }
    }

    private List<BoardCell> getPlayResult(){
        List<BoardCell> filledCells = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!board[j][i].isEmpty()) {
                    filledCells.add(board[j][i]);
                }
            }
        }
        return filledCells;
    }

    private void playRound(List<Move> whiteCheckerMove, List<Move> blackCheckerMove) throws CheckerGameException {
        playMoves(whiteCheckerMove);
        playMoves(blackCheckerMove);
    }

    private void playMoves(List<Move> moves) throws CheckerGameException {
        BoardCell fromCell = board[moves.get(0).getFrom()[0]][moves.get(0).getFrom()[1]];
        Checker fromChecker = new Checker(fromCell.getChecker().getColor(), fromCell.getChecker().isKing());
        BoardCell toCell = null;
        Checker toChecker = null;
        for (Move move : moves) {
            playOneMove(move);
            toCell = board[move.getTo()[0]][move.getTo()[1]];
            toChecker = toCell.getChecker();
        }
        toCell.setChecker(fromChecker);
        if (strikeMoveExist(toCell) && moves.get(moves.size() - 1).isStrikeMove()) {
            throw new CheckerGameException("invalid move");
        }
        toCell.setChecker(toChecker);
    }

    private void playOneMove(Move move) throws CheckerGameException {
        int fromRow = move.getFrom()[0];
        int fromColumn = move.getFrom()[1];
        int toRow = move.getTo()[0];
        int toColumn = move.getTo()[1];
        BoardCell fromCell = board[fromRow][fromColumn];
        BoardCell toCell = board[toRow][toColumn];
        if (!toCell.isEmpty()) {
            throw new CheckerGameException("busy cell");
        }
        if (toCell.isWhiteCell()) {
            throw new CheckerGameException("white cell");
        }
        if (!move.isStrikeMove() && isStrikeExistOnBoard(fromCell.getChecker().getColor())) {
            throw new CheckerGameException("invalid move");
        }
        if (!move.isStrikeMove() && !fromCell.getChecker().isKing() &&
                (Math.abs(fromRow - toRow) > 1 || Math.abs(fromColumn - toColumn) > 1)) {
            throw new CheckerGameException("error");
        }
        toCell.setChecker(fromCell.getChecker());
        fromCell.deleteChecker();
        if (move.isStrikeMove()) {
            makeStrike(fromRow, fromColumn, toRow, toColumn);
        }
        if (toCell.isBottomEdge() && toCell.getChecker().getColor() == Color.BLACK ||
                toCell.isTopEdge() && toCell.getChecker().getColor() == Color.WHITE ) {
            toCell.getChecker().setKing(true);
        }
    }

    private boolean isStrikeExistOnBoard(Color color) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!board[i][j].isEmpty() && board[i][j].getChecker().getColor() == color && strikeMoveExist(board[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean strikeMoveExist(BoardCell cell) {
        if (cell.getChecker().isKing()) {
            return tryToFindStrikeWithKing(cell);
        }
        return tryToFindStrike(cell);
    }

    private boolean tryToFindStrike(BoardCell cell) {
        int row = cell.getRow();
        int column = cell.getColumn();
        return (row + 1 < boardSize - 1 && column + 1 < boardSize - 1 &&
                isStrike(board[row + 1][column + 1], board[row + 2][column + 2], cell)) ||

                (row + 1 < boardSize - 1 && column - 1 > 0 &&
                        isStrike(board[row + 1][column - 1], board[row + 2][column - 2], cell)) ||

                (row - 1 > 0 && column + 1 < boardSize - 1 &&
                        isStrike(board[row - 1][column + 1], board[row - 2][column + 2], cell)) ||

                (row - 1 > 0 && column - 1 > 0 &&
                        isStrike(board[row - 1][column - 1], board[row - 2][column - 2], cell));
    }

    private boolean isStrike(BoardCell strikeCell, BoardCell finishCell, BoardCell startCell) {
        return !strikeCell.isEmpty() && finishCell.isEmpty() &&
                strikeCell.getChecker().getColor() != startCell.getChecker().getColor();
    }

    private boolean tryToFindStrikeWithKing(BoardCell cell) {
        return tryToFindLeftDownStrike(cell) || tryToFindLeftUpStrike(cell) ||
                tryToFindRightDownStrike(cell) || tryToFindRightUpStrike(cell);
    }

    private boolean tryToFindLeftDownStrike(BoardCell cell){
        int row = cell.getRow();
        int column = cell.getColumn();
        while (row > 1 && column > 1) {
            row--;
            column--;
            if (isStrike(board[row][column], board[row - 1][column - 1], cell)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryToFindLeftUpStrike(BoardCell cell){
        int row = cell.getRow();
        int column = cell.getColumn();
        while (row < boardSize - 2 && column > 1) {
            row++;
            column--;
            if (isStrike(board[row][column], board[row + 1][column - 1], cell)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryToFindRightDownStrike(BoardCell cell){
        int row = cell.getRow();
        int column = cell.getColumn();
        while (row > 1 && column < boardSize - 2) {
            row--;
            column++;
            if (isStrike(board[row][column], board[row - 1][column + 1], cell)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryToFindRightUpStrike(BoardCell cell){
        int row = cell.getRow();
        int column = cell.getColumn();
        while (row < boardSize - 2 && column < boardSize - 2) {
            row++;
            column++;
            if (isStrike(board[row][column], board[row + 1][column + 1], cell)) {
                return true;
            }
        }
        return false;
    }

    private void makeUpRightStrike(int fromRow, int fromColumn, int toRow, int toColumn) {
        int i = fromRow + 1;
        int j = fromColumn + 1;
        while (i < toRow && j < toColumn && board[i][j].isEmpty()) {
            i++;
            j++;
        }
        board[i][j].deleteChecker();
    }

    private void makeDownLeftStrike(int fromRow, int fromColumn, int toRow, int toColumn) {
        int i = fromRow - 1;
        int j = fromColumn - 1;
        while (i > toRow && j > toColumn && board[i][j].isEmpty()) {
            i--;
            j--;
        }
        board[i][j].deleteChecker();
    }

    private void makeUpLeftStrike(int fromRow, int fromColumn, int toRow, int toColumn) {
        int i = fromRow + 1;
        int j = fromColumn - 1;
        while (i < toRow && j > toColumn && board[i][j].isEmpty()) {
            i++;
            j--;
        }
        board[i][j].deleteChecker();
    }

    private void makeDownRightStrike(int fromRow, int fromColumn, int toRow, int toColumn) {
        int i = fromRow - 1;
        int j = fromColumn + 1;
        while (i > toRow && j < toColumn && board[i][j].isEmpty()) {
            i--;
            j++;
        }
        board[i][j].deleteChecker();
    }

    private void makeStrike(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (toRow > fromRow) {
            if (toColumn > fromColumn) {
                makeUpRightStrike(fromRow, fromColumn, toRow, toColumn);
            } else {
                makeUpLeftStrike(fromRow, fromColumn, toRow, toColumn);
            }
        } else {
            if (toColumn < fromColumn) {
                makeDownLeftStrike(fromRow, fromColumn, toRow, toColumn);
            } else {
                makeDownRightStrike(fromRow, fromColumn, toRow, toColumn);
            }
        }
    }
}

