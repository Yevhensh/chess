package chess.main.sample.guimanage;

import chess.main.sample.constants.SceneConstants;
import chess.main.sample.figures.Figure;
import chess.main.sample.figures.instances.Empty;
import chess.main.sample.game.Selected;
import chess.main.sample.manage.DeckManager;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.storage.LayoutChessPositionsStorage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static chess.main.sample.constants.SceneConstants.*;

public class DeckLayoutManager {

    private static DeckLayoutManager instance;

    public static DeckLayoutManager getInstance() {
        if (instance == null) {
            instance = new DeckLayoutManager();
        }
        return instance;
    }

    private DeckLayoutManager() {
    }

    private static Map<Integer, List<Node>> cellNodes = new HashMap<>();

    private static ChessPositionsStorage chessPositionsStorage = new ChessPositionsStorage();

    public void initializeBasicGameStart() {
        Map<Integer, Figure> positionsContainer = chessPositionsStorage.gameStartPositionRemind();
        AnchorPane pane = new AnchorPane();
        LayoutContainer.setLayout(pane);
        LayoutContainer.getLayout().getChildren().add(createDeckBackgroundRectangle());

        for (int iteratorDeckRectangle = 0; iteratorDeckRectangle < CELLS_COUNT; iteratorDeckRectangle++) {
            Figure figure;
            if (positionsContainer.containsKey(iteratorDeckRectangle)) {
                figure = positionsContainer.get(iteratorDeckRectangle);
            } else {
                figure = new Empty();
            }

            int row = iteratorDeckRectangle / COLUMNS_COUNT;
            int col = iteratorDeckRectangle % COLUMNS_COUNT;
            int xPosRec = START_FROM_BORDER_WIDTH + col * RECTANGLE_DIMENSION;
            int yPosRec = START_FROM_BORDER_WIDTH + row * RECTANGLE_DIMENSION;

            createDeckCellRectangle(iteratorDeckRectangle, xPosRec, yPosRec, figure);
        }
    }

    private Rectangle createDeckBackgroundRectangle() {
        int dimension = RECTANGLE_DIMENSION * SceneConstants.ROWS_COUNT + SceneConstants.BORDER_WIDTH * 2;
        return new Rectangle(0, 0, dimension, dimension);
    }

    private void createDeckCellRectangle(int iterator, int x, int y, Figure figure) {
        int row = iterator / COLUMNS_COUNT;
        int col = iterator % COLUMNS_COUNT;
        Color cellColor;
        if ((row + col) % 2 != 0) {
            cellColor = Color.DARKGREEN;
        } else {
            cellColor = Color.GREY;
        }
        LayoutContainer.addLayoutColor(cellColor);
        renderChessRow(iterator, cellColor, x, y, figure);
    }

    private void renderChessRow(int index, Color color, int positionX, int positionY, Figure figure) {
        List<Node> nodes = new ArrayList<>();

        Rectangle rectangle = new Rectangle(positionX, positionY, RECTANGLE_DIMENSION, RECTANGLE_DIMENSION);
        rectangle.setFill(color);
        rectangle.setX(positionX);
        rectangle.setY(positionY);
        rectangle.setOnMouseClicked(new MovementHandler());
        LayoutContainer.getLayout().getChildren().add(rectangle);
        nodes.add(rectangle);

        String picturePath = figure.getFilenamePath();
        if (!(figure instanceof Empty)) {
            Image image = null;
            try {
                image = new Image(new FileInputStream(picturePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setX(positionX + SceneConstants.IMAGE_OFFSET);
            imageView.setY(positionY + SceneConstants.IMAGE_OFFSET);
            imageView.setFitWidth(IMAGE_DIMENSION);
            imageView.setFitHeight(IMAGE_DIMENSION);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(new MovementHandler());
            LayoutContainer.getLayout().getChildren().add(imageView);
            nodes.add(imageView);
        }
        cellNodes.put(index, nodes);
    }

    public Selected getSelectedByDeckPosition(int x, int y) {
        int col = (x - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int row = (y - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int deckCell = row * COLUMNS_COUNT + col;
        return new Selected(ChessPositionsStorage.getGlobalStorage().getFigureByDeckCell(deckCell), deckCell);
    }

    public void makeTurn(int fromIndex, int toIndex, Figure figure) {
        // Clear nodes at fromIndex
        List<Node> fromNodes = cellNodes.get(fromIndex);
        if (fromNodes != null) {
            LayoutContainer.getLayout().getChildren().removeAll(fromNodes);
            cellNodes.remove(fromIndex);
        }

        // Clear nodes at toIndex
        List<Node> toNodes = cellNodes.get(toIndex);
        if (toNodes != null) {
            LayoutContainer.getLayout().getChildren().removeAll(toNodes);
            cellNodes.remove(toIndex);
        }

        // Render Empty at fromIndex
        int rowFrom = fromIndex / COLUMNS_COUNT;
        int colFrom = fromIndex % COLUMNS_COUNT;
        int xFrom = START_FROM_BORDER_WIDTH + colFrom * RECTANGLE_DIMENSION;
        int yFrom = START_FROM_BORDER_WIDTH + rowFrom * RECTANGLE_DIMENSION;
        Color colorFrom = LayoutContainer.getLayoutColors().get(fromIndex);
        renderChessRow(fromIndex, colorFrom, xFrom, yFrom, new Empty());

        // Render Figure at toIndex
        int rowTo = toIndex / COLUMNS_COUNT;
        int colTo = toIndex % COLUMNS_COUNT;
        int xTo = START_FROM_BORDER_WIDTH + colTo * RECTANGLE_DIMENSION;
        int yTo = START_FROM_BORDER_WIDTH + rowTo * RECTANGLE_DIMENSION;
        Color colorTo = LayoutContainer.getLayoutColors().get(toIndex);
        renderChessRow(toIndex, colorTo, xTo, yTo, figure);
    }
}
