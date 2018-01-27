package chess.main.sample.storage;


import chess.main.sample.figures.*;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ChessPositionsStorage {

    private static ChessPositionsStorage globalStorage;

    public static ChessPositionsStorage getGlobalStorage() {
        return globalStorage;
    }

    private static void setGlobalStorage(ChessPositionsStorage chessPositionsStorage) {
        globalStorage = chessPositionsStorage;
    }

    private Map<Integer, Figure> positionsContainer;

    private int whiteKingIndex;
    private int blackKingIndex;

    public Map<Integer, Figure> gameStartPositionRemind() {
        positionsContainer = new HashMap<>();
        int index = 0;
        positionsContainer.put(index, new Rok(Position.BLACK));
        positionsContainer.put(++index, new Knight(Position.BLACK));
        positionsContainer.put(++index, new Bishop(Position.BLACK));
        positionsContainer.put(++index, new King(Position.BLACK));
        setBlackKingIndex(index);
        positionsContainer.put(++index, new Queen(Position.BLACK));
        positionsContainer.put(++index, new Bishop(Position.BLACK));
        positionsContainer.put(++index, new Knight(Position.BLACK));
        positionsContainer.put(++index, new Rok(Position.BLACK));
        IntStream.range(++index, index + 8)
                .forEach(item -> positionsContainer.put(item, new Pawn(Position.BLACK)));
        index = 48;
        IntStream.range(index, index += 8)
                .forEach(item -> positionsContainer.put(item, new Pawn(Position.WHITE)));
        positionsContainer.put(index, new Rok(Position.WHITE));
        positionsContainer.put(++index, new Knight(Position.WHITE));
        positionsContainer.put(++index, new Bishop(Position.WHITE));
        positionsContainer.put(++index, new King(Position.WHITE));
        setWhiteKingIndex(index);
        positionsContainer.put(++index, new Queen(Position.WHITE));
        positionsContainer.put(++index, new Bishop(Position.WHITE));
        positionsContainer.put(++index, new Knight(Position.WHITE));
        positionsContainer.put(++index, new Rok(Position.WHITE));
        LayoutChessPositionsStorage.getInstance().gameStartPositionsRemind(positionsContainer);
        ChessPositionsStorage.setGlobalStorage(this);
        return positionsContainer;
    }

    public Map<Integer, Figure> getPositionsContainer() {
        return positionsContainer;
    }

    public void setPositionsContainer(Map<Integer, Figure> positionsContainer) {
        this.positionsContainer = positionsContainer;
    }

    public int getWhiteKingIndex() {
        return whiteKingIndex;
    }

    public void setWhiteKingIndex(int whiteKingIndex) {
        this.whiteKingIndex = whiteKingIndex;
    }

    public int getBlackKingIndex() {
        return blackKingIndex;
    }

    public void setBlackKingIndex(int blackKingIndex) {
        this.blackKingIndex = blackKingIndex;
    }

    public Figure getFigureByDeckCell(int deckCell) {
        return positionsContainer.get(deckCell);
    }
}
