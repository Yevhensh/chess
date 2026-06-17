package chess.main.sample.guimanage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.game.Selected;
import chess.main.sample.game.TurnSwitcher;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class MovementHandler implements EventHandler<MouseEvent> {

    private static final int IMAGE_VIEW_OFFSET = 10;

    public static final MovementHandler INSTANCE = new MovementHandler();

    private final DeckLayoutManager layoutManager = DeckLayoutManager.getInstance();
    private final DeckManager deckManager = DeckManager.getInstance();

    private MovementHandler() {
    }

    @Override
    public void handle(MouseEvent event) {
        Selected clicked = resolveClickedSelection((Node) event.getTarget());
        if (clicked == null) {
            return;
        }

        if (Selected.isGlobalSelected()) {
            handleExistingSelection(clicked);
        } else {
            handleInitialSelection(clicked);
        }
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
        Figure selectedFigure = Selected.getGlobalSelected();
        int fromIndex = Selected.getGlobalIndex();

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
        return TurnSwitcher.getPosition().equals(figure.getPosition());
    }

    private boolean isValidMoveTarget(Selected clicked, Figure selectedFigure, int fromIndex) {
        List<Integer> availableMoves = selectedFigure.getAllAvailableMovements(fromIndex);
        return availableMoves.contains(clicked.index());
    }

    private void selectPiece(Selected selected) {
        if (Selected.isGlobalSelected()) {
            clearSelectionHighlights();
        }
        Selected.setGlobalSelected(selected);
        layoutManager.highlightCell(selected.index());

        List<Integer> availableMoves = selected.selected().getAllAvailableMovements(selected.index());
        for (int moveIndex : availableMoves) {
            if (deckManager.isMoveLegal(selected.index(), moveIndex, selected.selected().getPosition())) {
                boolean isCapture = deckManager.isOppositeFigureOnDeckCell(
                        chess.main.sample.storage.ChessPositionsStorage.getGlobalStorage().getPositionsContainer(),
                        moveIndex,
                        selected.selected().getPosition()
                );
                layoutManager.showMoveIndicator(moveIndex, isCapture);
            }
        }
    }

    private void clearSelectionHighlights() {
        if (Selected.isGlobalSelected()) {
            int fromIndex = Selected.getGlobalIndex();
            layoutManager.unhighlightCell(fromIndex);
            List<Integer> availableMoves = Selected.getGlobalSelected().getAllAvailableMovements(fromIndex);
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

        TurnSwitcher.switchPosition();
        Selected.emptyGlobalSelected();
        updateStatus();
    }

    private void updateStatus() {
        Label statusLabel = LayoutContainer.getStatusLabel();
        if (statusLabel == null) {
            return;
        }
        statusLabel.setText(buildStatusMessage(TurnSwitcher.getPosition()));
    }

    private String buildStatusMessage(Position currentSide) {
        ChessPositionsStorage storage = ChessPositionsStorage.getGlobalStorage();
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
