package chess.main.sample.storage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Bishop;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Knight;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.figures.instances.Queen;
import chess.main.sample.figures.instances.Rok;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ChessPositionsStorage {

    private static final int BLACK_BACK_RANK_START = 0;
    private static final int BLACK_PAWNS_START = 8;
    private static final int WHITE_PAWNS_START = 48;
    private static final int WHITE_BACK_RANK_START = 56;
    private static final int KING_BACK_RANK_OFFSET = 4;
    private static final int PAWNS_PER_SIDE = 8;
    private static final int BACK_RANK_SIZE = 8;

    private Map<Integer, Figure> positionsContainer;
    private int whiteKingIndex;
    private int blackKingIndex;

    public Map<Integer, Figure> gameStartPositionRemind() {
        positionsContainer = new HashMap<>();

        placeBackRank(Position.BLACK, BLACK_BACK_RANK_START);
        placePawnRow(Position.BLACK, BLACK_PAWNS_START);
        placePawnRow(Position.WHITE, WHITE_PAWNS_START);
        placeBackRank(Position.WHITE, WHITE_BACK_RANK_START);

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

    private void placeBackRank(Position side, int startIndex) {
        IntStream.range(0, BACK_RANK_SIZE).forEach(offset -> {
            int index = startIndex + offset;
            positionsContainer.put(index, createBackRankPiece(offset, side));
            if (offset == KING_BACK_RANK_OFFSET) {
                setKingIndex(side, index);
            }
        });
    }

    private void placePawnRow(Position side, int startIndex) {
        IntStream.range(startIndex, startIndex + PAWNS_PER_SIDE)
                .forEach(index -> positionsContainer.put(index, new Pawn(side)));
    }

    private Figure createBackRankPiece(int offset, Position side) {
        return switch (offset) {
            case 0, 7 -> new Rok(side);
            case 1, 6 -> new Knight(side);
            case 2, 5 -> new Bishop(side);
            case 3 -> new Queen(side);
            case 4 -> new King(side);
            default -> throw new IllegalArgumentException("Invalid back rank offset: " + offset);
        };
    }

    private void setKingIndex(Position side, int index) {
        if (side == Position.WHITE) {
            setWhiteKingIndex(index);
        } else {
            setBlackKingIndex(index);
        }
    }
}
