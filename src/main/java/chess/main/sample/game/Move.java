package chess.main.sample.game;

import chess.main.sample.figures.Figure;

public record Move(
    int fromIndex,
    int toIndex,
    Figure movedFigure,
    Figure capturedFigure,
    int capturedIndex,
    boolean wasPromoted,
    boolean isCastling,
    boolean isEnPassant
) {
    public Move(int fromIndex, int toIndex, Figure movedFigure, Figure capturedFigure, boolean wasPromoted) {
        this(fromIndex, toIndex, movedFigure, capturedFigure, toIndex, wasPromoted, false, false);
    }
}
