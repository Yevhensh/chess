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

    @Override
    public void handle(MouseEvent event) {
        Node node = (Node) event.getTarget();
        DeckLayoutManager layoutManager = DeckLayoutManager.getInstance();
        DeckManager deckManager = DeckManager.getInstance();
        Selected selected = null;
        if (node instanceof Rectangle rectangle) {
            // clicked on rectangle
            selected = layoutManager.getSelectedByDeckPosition((int) rectangle.getX(), (int) rectangle.getY());
        } else if (node instanceof ImageView imageView) {
            // clicked on image
            selected = layoutManager.getSelectedByDeckPosition((int) imageView.getX() - 9, (int) imageView.getY() - 9);
        }

        if (selected == null) return;

        System.out.println(selected);

        // check whether selected is prev selected
        if (Selected.isGlobalSelected()) {
            Figure globalFigure = Selected.getGlobalSelected();
            int globalIndex = Selected.getGlobalIndex();

            // If clicking on another ally piece, change selection
            if (selected.selected() != null && selected.selected().getPosition().equals(globalFigure.getPosition())) {
                layoutManager.unhighlightCell(globalIndex);
                Selected.setGlobalSelected(selected);
                layoutManager.highlightCell(selected.index());
                return;
            }

            List<Integer> globalSelectedMovements = globalFigure.getAllAvailableMovements(globalIndex);

            if (globalSelectedMovements.contains(selected.index())) {
                if (deckManager.isMoveLegal(globalIndex, selected.index(), globalFigure.getPosition())) {
                    layoutManager.unhighlightCell(globalIndex);
                    deckManager.makeTurn(globalIndex, selected.index());
                    TurnSwitcher.switchPosition();
                    Selected.emptyGlobalSelected();
                    updateStatus(deckManager);
                } else {
                    System.out.println("Move is illegal (King in check)");
                }
            }
            // Optional: don't empty selection if clicked on invalid square (not ally, not valid move)
            // Selected.emptyGlobalSelected();
        } else {
            // selected figure correspond to actual site turn
            if (selected.selected() != null && TurnSwitcher.getPosition().equals(selected.selected().getPosition())) {
                Selected.setGlobalSelected(selected);
                layoutManager.highlightCell(selected.index());
            }
        }
    }

    private void updateStatus(DeckManager deckManager) {
        Position currentSide = TurnSwitcher.getPosition();
        Label statusLabel = LayoutContainer.getStatusLabel();
        if (statusLabel == null) return;

        String status = (currentSide == Position.WHITE ? "White" : "Black") + "'s turn";

        if (deckManager.isCheckmate(currentSide)) {
            status = "Checkmate! " + (currentSide == Position.WHITE ? "Black" : "White") + " wins!";
        } else if (deckManager.isStalemate(currentSide)) {
            status = "Stalemate! Draw.";
        } else if (deckManager.isCheck(ChessPositionsStorage.getGlobalStorage(), currentSide)) {
            status += " - Check!";
        }

        statusLabel.setText(status);
    }
}
