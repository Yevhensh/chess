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
        if (node instanceof Rectangle) {
            // clicked on rectangle
            Rectangle rectangle = (Rectangle) node;
            selected = layoutManager.getSelectedByDeckPosition((int) rectangle.getX(), (int) rectangle.getY());
        } else if (node instanceof ImageView) {
            // clicked on image
            ImageView imageView = (ImageView) node;
            selected = layoutManager.getSelectedByDeckPosition((int) imageView.getX() - 9, (int) imageView.getY() - 9);
        }

        if (selected == null) return;

        System.out.println(selected);

        // check whether selected is prev selected
        if (Selected.isGlobalSelected()) {
            Figure globalFigure = Selected.getGlobalSelected();
            int globalIndex = Selected.getGlobalIndex();

            // If clicking on another ally piece, change selection
            if (selected.getSelected() != null && selected.getSelected().getPosition().equals(globalFigure.getPosition())) {
                layoutManager.unhighlightCell(globalIndex);
                Selected.setGlobalSelected(selected);
                layoutManager.highlightCell(selected.getIndex());
                return;
            }

            List<Integer> globalSelectedMovements = globalFigure.getAllAvailableMovements(globalIndex);

            if (globalSelectedMovements.contains(selected.getIndex())) {
                if (deckManager.isMoveLegal(globalIndex, selected.getIndex(), globalFigure.getPosition())) {
                    layoutManager.unhighlightCell(globalIndex);
                    deckManager.makeTurn(globalIndex, selected.getIndex());
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
            if (selected.getSelected() != null && TurnSwitcher.getPosition().equals(selected.getSelected().getPosition())) {
                Selected.setGlobalSelected(selected);
                layoutManager.highlightCell(selected.getIndex());
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
