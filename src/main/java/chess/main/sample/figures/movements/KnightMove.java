package chess.main.sample.figures.movements;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.utils.ChessUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KnightMove extends Movement {

    private static final int[][] MOVES = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
    };

    @Override
    public List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        return Arrays.stream(MOVES)
                .filter(move -> ChessUtils.isValid(row + move[0], col + move[1]))
                .map(move -> ChessUtils.getIndex(row + move[0], col + move[1]))
                .filter(nextIndex -> ChessUtils.isEmpty(positions, nextIndex)
                        || ChessUtils.isOpposite(positions, nextIndex, figure.getPosition()))
                .collect(Collectors.toList());
    }
}
