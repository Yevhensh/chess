package chess.main.sample.guimanage;


import chess.main.sample.constants.SceneConstants;
import chess.main.sample.figures.Figure;
import chess.main.sample.game.Selected;
import chess.main.sample.game.TurnSwitcher;
import chess.main.sample.manage.DeckManager;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
            selected = layoutManager.getSelectedByDeckPosition(
                (int) imageView.getX() - SceneConstants.IMAGE_OFFSET,
                (int) imageView.getY() - SceneConstants.IMAGE_OFFSET
            );
        }

        if (selected == null) return;

        System.out.println(selected);

        // check whether selected is prev selected
        if (Selected.isGlobalSelected()) {
            Figure globalFigure = Selected.getGlobalSelected();
            int globalIndex = Selected.getGlobalIndex();
            List<Integer> globalSelectedMovements = globalFigure.getAllAvailableMovements(globalIndex);

            if (globalSelectedMovements.contains(selected.getIndex())) {
                if (deckManager.isMoveLegal(globalIndex, selected.getIndex(), globalFigure.getPosition())) {
                    deckManager.makeTurn(globalIndex, selected.getIndex());
                    TurnSwitcher.switchPosition();
                } else {
                    System.out.println("Move is illegal (King in check)");
                }
            }
            Selected.emptyGlobalSelected();
        } else {
            // selected figure correspond to actual site turn
            if (selected.getSelected() != null && TurnSwitcher.getPosition().equals(selected.getSelected().getPosition())) {
                Selected.setGlobalSelected(selected);
            }
        }
    }
}
