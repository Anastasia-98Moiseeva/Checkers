package ru.moiseeva;

import org.junit.jupiter.api.Test;
import ru.moiseeva.converters.DataConverter;
import ru.moiseeva.exceptions.CheckerGameException;
import ru.moiseeva.game.CheckersGame;
import ru.moiseeva.game.board.BoardCell;
import ru.moiseeva.game.checkers.Color;
import ru.moiseeva.game.moves.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {

    @Test
    public void testFromTask() throws CheckerGameException {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";
        List<String> moves = Arrays.asList("g3-f4 f6-e5", "c3-d4 e5:c3", "b2:d4 d6-c5", "d2-c3 g7-f6",
                "h2-g3 h8-g7", "c1-b2 f6-g5", "g3-h4 g7-f6", "f4-e5 f8-g7");
        String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        String whiteExpected = "a1 a3 b2 c3 d4 e1 e3 e5 f2 g1 h4";
        String blackExpected = "a7 b6 b8 c5 c7 d8 e7 f6 g5 g7 h6";
        assertEquals(whiteExpected+ System.lineSeparator() + blackExpected, result);
    }

    @Test
    public void testWithEmptyData() throws CheckerGameException {
        String whiteCoordinates = "";
        String blackCoordinates = "";
        List<String> moves = Arrays.asList();
        String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        String whiteExpected = "";
        String blackExpected = "";
        assertEquals(whiteExpected+ System.lineSeparator() + blackExpected, result);
    }

    @Test
    public void testKingChecker() throws CheckerGameException {
        String whiteCoordinates = "d4 e1";
        String blackCoordinates = "e7 H8";
        List<String> moves = Arrays.asList("e1-d2 H8:A1");
        String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        String whiteExpected = "d2";
        String blackExpected = "A1 e7";
        assertEquals(whiteExpected+ System.lineSeparator() + blackExpected, result);
    }

    @Test
    public void testBusyCell() {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";
        List<String> moves = Arrays.asList("e3-d4 d6-c5", "d2-e3 c5-e3");
        Throwable thrown = assertThrows(CheckerGameException.class, () -> {
            String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        });
        assertEquals("busy cell", thrown.getMessage());
    }

    @Test
    public void testWhiteCell() {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";
        List<String> moves = Arrays.asList("e3-e4 d6-c5");
        Throwable thrown = assertThrows(CheckerGameException.class, () -> {
            String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        });
        assertEquals("white cell", thrown.getMessage());
    }


    @Test
    public void testInvalidMove() {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";
        List<String> moves = Arrays.asList("e3-d4 b6-c5", "d4-e5 d6-f4");
        Throwable thrown = assertThrows(CheckerGameException.class, () -> {
            String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        });
        assertEquals("invalid move", thrown.getMessage());
    }

    @Test
    public void testError() {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";
        List<String> moves = Arrays.asList("e3-c5 b6-c5");
        Throwable thrown = assertThrows(CheckerGameException.class, () -> {
            String result = testPlay(whiteCoordinates, blackCoordinates, moves);
        });
        assertEquals("error", thrown.getMessage());
    }

    private String testPlay(String whiteCoordinates, String blackCoordinates, List<String> moves) throws CheckerGameException {
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
}
