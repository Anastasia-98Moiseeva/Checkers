package ru.moiseeva;

import ru.moiseeva.converters.DataConverter;
import ru.moiseeva.exceptions.CheckerGameException;
import ru.moiseeva.game.CheckersGame;
import ru.moiseeva.game.board.BoardCell;
import ru.moiseeva.game.checkers.Color;
import ru.moiseeva.game.moves.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class App {
    private App() {
    }

    private static String playCheckers(String whiteCoordinates, String blackCoordinates, List<String> moves) throws CheckerGameException {
        DataConverter converter = new DataConverter();
        List<BoardCell> whiteCheckerCells = converter.getCheckerCells(whiteCoordinates, Color.WHITE);
        List<BoardCell> blackCheckerCells = converter.getCheckerCells(blackCoordinates, Color.BLACK);
        List<List<Move>> allWhiteMoves = new ArrayList<>();
        List<List<Move>> allBlackMoves = new ArrayList<>();
        for (String str : moves) {
            String[] moveStr = str.split(" ");
            allWhiteMoves.add(converter.getMoves(moveStr[0]));
            allBlackMoves.add(converter.getMoves(moveStr[1]));
        }
        CheckersGame game = new CheckersGame(whiteCheckerCells, blackCheckerCells);
        return converter.getResultStrings(game.play(allWhiteMoves, allBlackMoves));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String whiteCoordinates = scanner.nextLine();
        String blackCoordinates = scanner.nextLine();
        List<String> moves = new ArrayList<>();
        while (scanner.hasNext()) {
            moves.add(scanner.nextLine());
        }
        try {
            String result = playCheckers(whiteCoordinates, blackCoordinates, moves);
            System.out.println(result);
        } catch (CheckerGameException e) {
            e.printStackTrace();
        }
    }
}
