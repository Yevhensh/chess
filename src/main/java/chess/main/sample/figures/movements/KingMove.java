package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.figures.Position;
import chess.main.sample.utils.ChessUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KingMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> moves = getBasicMoves(positions, deckCell, figure);

        // Pseudo-legal castling moves
        int row = ChessUtils.getRow(deckCell);
        if (ChessUtils.getCol(deckCell) == 4) {
            // Kingside
            moves.add(ChessUtils.getIndex(row, 6));
            // Queenside
            moves.add(ChessUtils.getIndex(row, 2));
        }

        return moves;
    }

    public List<Integer> getBasicMoves(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        return IntStream.rangeClosed(-1, 1)
                .boxed()
                .flatMap(dr -> IntStream.rangeClosed(-1, 1)
                        .filter(dc -> dr != 0 || dc != 0)
                        .mapToObj(dc -> new int[]{row + dr, col + dc}))
                .filter(coords -> ChessUtils.isValid(coords[0], coords[1]))
                .map(coords -> ChessUtils.getIndex(coords[0], coords[1]))
                .filter(nextIndex -> !ChessUtils.isAlly(positions, nextIndex, figure.getPosition()))
                .collect(Collectors.toList());
    }
}
