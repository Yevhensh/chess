package chess.main.sample.figures.movements;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Movement;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.utils.ChessUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KingMove extends Movement {
    @Override
    public List<Integer> determineAvailableMovements(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        List<Integer> allMoves = getBasicMoves(positions, deckCell, figure);
        return filterAllMovesWithCheckPossible(positions, allMoves, figure);
    }

    public List<Integer> getBasicMoves(Map<Integer, Figure> positions, int deckCell, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        int row = ChessUtils.getRow(deckCell);
        int col = ChessUtils.getCol(deckCell);

        return IntStream.rangeClosed(-1, 1)
                .boxed()
                .flatMap(dr -> IntStream.rangeClosed(-1, 1)
                        .filter(dc -> dr != 0 || dc != 0)
                        .mapToObj(dc -> new int[]{row + dr, col + dc}))
                .filter(coords -> ChessUtils.isValid(coords[0], coords[1]))
                .map(coords -> ChessUtils.getIndex(coords[0], coords[1]))
                .filter(nextIndex -> !deckManager.isAllyFigureOnDeckCell(positions, nextIndex, figure.getPosition()))
                .collect(Collectors.toList());
    }

    private List<Integer> filterAllMovesWithCheckPossible(Map<Integer, Figure> positions, List<Integer> allMoves, Figure figure) {
        DeckManager deckManager = DeckManager.getInstance();
        chess.main.sample.figures.Position oppositePosition =
            (figure.getPosition() == chess.main.sample.figures.Position.WHITE) ?
            chess.main.sample.figures.Position.BLACK : chess.main.sample.figures.Position.WHITE;

        List<Integer> oppositeSiteAttacks = deckManager.getAllOppositeSiteAttacks(positions, oppositePosition);

        return allMoves.stream()
                .filter(item -> !oppositeSiteAttacks.contains(item))
                .collect(Collectors.toList());
    }
}
