package chess.main.sample.game;

import chess.main.sample.figures.Figure;

public record Move(int fromIndex, int toIndex, Figure movedFigure, Figure capturedFigure, boolean wasPromoted) {
}
