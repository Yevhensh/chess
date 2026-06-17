package chess.main.sample.guimanage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.game.GameState;
import chess.main.sample.game.Selected;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Map;

public class MovementHandler implements EventHandler<MouseEvent> {

    private static final int IMAGE_VIEW_OFFSET = 10;

    private final DeckLayoutManager layoutManager;
    private final DeckManager deckManager;
    private final ChessPositionsStorage storage;
    private final GameState gameState;

    public MovementHandler(DeckLayoutManager layoutManager, DeckManager deckManager, ChessPositionsStorage storage, GameState gameState) {
        this.layoutManager = layoutManager;
        this.deckManager = deckManager;
        this.storage = storage;
        this.gameState = gameState;
    }

    @Override
    public void handle(MouseEvent event) {
        Selected clicked = resolveClickedSelection((Node) event.getTarget());
        if (clicked == null) {
            return;
        }

        if (gameState.hasSelection()) {
            handleExistingSelection(clicked);
        } else {
            handleInitialSelection(clicked);
        }
    }

    public void handleUndo() {
        clearSelectionHighlights();
        gameState.clearSelection();
        deckManager.undoMove();
    }

    public void handleRedo() {
        clearSelectionHighlights();
        gameState.clearSelection();
        deckManager.redoMove();
    }

    private Selected resolveClickedSelection(Node node) {
        if (node instanceof Rectangle rectangle) {
            return layoutManager.getSelectedByDeckPosition((int) rectangle.getX(), (int) rectangle.getY());
        }
        if (node instanceof ImageView imageView) {
            return layoutManager.getSelectedByDeckPosition(
                    (int) imageView.getX() - IMAGE_VIEW_OFFSET,
                    (int) imageView.getY() - IMAGE_VIEW_OFFSET
            );
        }
        return null;
    }

    private void handleExistingSelection(Selected clicked) {
        Figure selectedFigure = gameState.getSelectedPiece();
        int fromIndex = gameState.getSelectedIndex();

        if (isAllyPiece(clicked, selectedFigure)) {
            selectPiece(clicked);
            return;
        }

        if (isValidMoveTarget(clicked, selectedFigure, fromIndex)) {
            executeMove(clicked, fromIndex, selectedFigure);
        }
    }

    private void handleInitialSelection(Selected clicked) {
        Figure clickedFigure = clicked.selected();
        if (clickedFigure != null && isCurrentTurn(clickedFigure)) {
            selectPiece(clicked);
        }
    }

    private boolean isAllyPiece(Selected clicked, Figure selectedFigure) {
        Figure clickedFigure = clicked.selected();
        return clickedFigure != null && clickedFigure.getPosition().equals(selectedFigure.getPosition());
    }

    private boolean isCurrentTurn(Figure figure) {
        return gameState.getCurrentTurn().equals(figure.getPosition());
    }

    private boolean isValidMoveTarget(Selected clicked, Figure selectedFigure, int fromIndex) {
        Map<Integer, Figure> positions = storage.getPositionsContainer();
        List<Integer> availableMoves = selectedFigure.getAllAvailableMovements(positions, fromIndex);
        return availableMoves.contains(clicked.index());
    }

    private void selectPiece(Selected selected) {
        if (gameState.hasSelection()) {
            clearSelectionHighlights();
        }
        gameState.setSelected(selected.selected(), selected.index());
        layoutManager.highlightCell(selected.index());

        Map<Integer, Figure> positions = storage.getPositionsContainer();
        List<Integer> availableMoves = selected.selected().getAllAvailableMovements(positions, selected.index());
        for (int moveIndex : availableMoves) {
            if (deckManager.isMoveLegal(selected.index(), moveIndex, selected.selected().getPosition())) {
                boolean isCapture = deckManager.isOppositeFigureOnDeckCell(
                        positions,
                        moveIndex,
                        selected.selected().getPosition()
                );
                layoutManager.showMoveIndicator(moveIndex, isCapture);
            }
        }
    }

    private void clearSelectionHighlights() {
        if (gameState.hasSelection()) {
            int fromIndex = gameState.getSelectedIndex();
            layoutManager.unhighlightCell(fromIndex);
            Map<Integer, Figure> positions = storage.getPositionsContainer();
            List<Integer> availableMoves = gameState.getSelectedPiece().getAllAvailableMovements(positions, fromIndex);
            for (int moveIndex : availableMoves) {
                layoutManager.clearIndicators(moveIndex);
            }
        }
    }

    private void executeMove(Selected clicked, int fromIndex, Figure selectedFigure) {
        if (!deckManager.isMoveLegal(fromIndex, clicked.index(), selectedFigure.getPosition())) {
            return;
        }

        clearSelectionHighlights();
        deckManager.makeTurn(fromIndex, clicked.index());

        // Highlight last move
        layoutManager.highlightCell(fromIndex, javafx.scene.paint.Color.web("#f6f669", 0.3));
        layoutManager.highlightCell(clicked.index(), javafx.scene.paint.Color.web("#f6f669", 0.3));

        gameState.switchTurn();
        gameState.clearSelection();
        updateStatus();
    }

    public void updateStatus() {
        Label statusLabel = layoutManager.getStatusLabel();
        if (statusLabel == null) {
            return;
        }
        statusLabel.setText(buildStatusMessage(gameState.getCurrentTurn()));
    }

    private String buildStatusMessage(Position currentSide) {
        int kingIndex = currentSide == Position.WHITE ? storage.getWhiteKingIndex() : storage.getBlackKingIndex();

        if (deckManager.isCheckmate(currentSide)) {
            layoutManager.highlightCell(kingIndex, javafx.scene.paint.Color.web("#f04444"));
            return "Checkmate! " + sideName(oppositeSide(currentSide)) + " wins!";
        }
        if (deckManager.isStalemate(currentSide)) {
            return "Stalemate! Draw.";
        }

        String status = sideName(currentSide) + "'s turn";
        if (deckManager.isCheck(storage, currentSide)) {
            layoutManager.highlightCell(kingIndex, javafx.scene.paint.Color.web("#f04444", 0.6));
            status += " - Check!";
        } else {
            layoutManager.unhighlightCell(kingIndex);
        }
        return status;
    }

    private Position oppositeSide(Position side) {
        return side == Position.WHITE ? Position.BLACK : Position.WHITE;
    }

    private String sideName(Position side) {
        return side == Position.WHITE ? "White" : "Black";
    }
}
