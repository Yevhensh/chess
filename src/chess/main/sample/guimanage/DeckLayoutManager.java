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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

    private int shifter = 0;

    private static ChessPositionsStorage chessPositionsStorage = new ChessPositionsStorage();
    private static LayoutChessPositionsStorage layoutChessPositionsStorage = LayoutChessPositionsStorage.getInstance();

    public void initializeBasicGameStart() {
        Map<Integer, Figure> positionsContainer = chessPositionsStorage.gameStartPositionRemind();
        chessPositionsStorage = ChessPositionsStorage.getGlobalStorage();
        AnchorPane pane = new AnchorPane();
        LayoutContainer.setLayout(pane);
        LayoutContainer.getLayout().getChildren().add(createDeckBackgroundRectangle());
        int xPosRec = START_FROM_BORDER_WIDTH;
        int yPosRec = START_FROM_BORDER_WIDTH;
        for (int iteratorDeckRectangle = 0; iteratorDeckRectangle < CELLS_COUNT; iteratorDeckRectangle++) {
            Figure figure;
            if (positionsContainer.containsKey(iteratorDeckRectangle)) {
                figure = positionsContainer.get(iteratorDeckRectangle);
            } else {
                figure = new Empty();
            }
            createDeckCellRectangle(iteratorDeckRectangle, xPosRec, yPosRec, figure);
            if (isShiftTop(iteratorDeckRectangle)) {
                yPosRec += RECTANGLE_DIMENSION;
                xPosRec = 20;
            } else {
                // shift right
                xPosRec += RECTANGLE_DIMENSION;
            }
        }
        for (int i = 0; i < LayoutContainer.getLayout().getChildren().size(); i++) {
            System.out.println(LayoutContainer.getLayout().getChildren().get(i) + "   i:" + i);
        }

        LayoutChessPositionsStorage.getInstance()
                .getLayoutPositionsContainer()
                .entrySet()
                .forEach(item -> System.out.println(item.getKey() + ": " + item.getValue()));
    }

    private Rectangle createDeckBackgroundRectangle() {
        int dimension = RECTANGLE_DIMENSION * SceneConstants.ROWS_COUNT + SceneConstants.BORDER_WIDTH * 2;
        return new Rectangle(0, 0, dimension, dimension);
    }

    private void createDeckCellRectangle(int iterator, int x, int y, Figure figure) {
        if (iterator % 8 == 0) {
            if (shifter == 0) {
                shifter = 1;
            } else {
                shifter = 0;
            }
        }
        int shiftedIterator = iterator + shifter;
        Color cellColor;
        if (isOdd(shiftedIterator)) {
            cellColor = Color.DARKGREEN;
        } else {
            cellColor = Color.GREY;
        }
        LayoutContainer.addLayoutColor(cellColor);
        renderChessRow(cellColor, x, y, figure);
    }

    private boolean isOdd(int value) {
        return (value % 2 != 0);
    }

    private boolean isShiftTop(int iterator) {
        return ((iterator + 1) % 8 == 0);
    }

    private void renderChessRow(Color color, int positionX, int positionY, Figure figure) {
        Rectangle rectangle = new Rectangle(positionX, positionY, RECTANGLE_DIMENSION, RECTANGLE_DIMENSION);
        rectangle.setFill(color);
        rectangle.setOnMouseClicked(new MovementHandler());
        LayoutContainer.getLayout().getChildren().add(rectangle);

        String picturePath = figure.getFilenamePath();
        if (picturePath != "empty") {
            Image image = null;
            try {
                image = new Image(new FileInputStream(picturePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setX(positionX + 9);
            imageView.setY(positionY + 9);
            imageView.setFitWidth(IMAGE_DIMENSION);
            imageView.setFitHeight(IMAGE_DIMENSION);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(new MovementHandler());
            Group root = new Group(imageView);
            LayoutContainer.getLayout().getChildren().add(root);
        }
    }

    public Selected getSelectedByDeckPosition(int x, int y) {
        int row = (x - 20) / 70;
        int column = (y - 20) / 70 * 8;
        int deckCell = row + column;
        return new Selected(ChessPositionsStorage.getGlobalStorage().getFigureByDeckCell(deckCell), deckCell);
    }

    public void makeTurn(int fromIndex, int toIndex, Figure figure) {
        ChessPositionsStorage chessPositionsStorage = ChessPositionsStorage.getGlobalStorage();
        Map<Integer, Integer> layoutPositionsContainer = layoutChessPositionsStorage.getLayoutPositionsContainer();
        int layoutIndexFrom = layoutPositionsContainer.get(fromIndex);
        int layoutIndexTo = layoutPositionsContainer.get(toIndex);
        layoutPositionsContainer.remove(fromIndex);
        layoutPositionsContainer.remove(toIndex);

        List<Node> layoutList = LayoutContainer.getLayout().getChildren();
        layoutList.remove(layoutIndexFrom);
        layoutList.remove(layoutIndexFrom + 1);
        if (DeckManager.getInstance().isEmptyDeckCell(toIndex)) {
            layoutList.remove(layoutIndexTo);
        } else {
            layoutList.remove(layoutIndexTo);
            layoutList.remove(layoutIndexTo + 1);
        }

        shifting(layoutIndexFrom, toIndex, layoutPositionsContainer, layoutIndexTo);

        int xFrom = (int) Math.floor(fromIndex / 8.0);
        int yFrom = fromIndex % 8;
        xFrom = 20 + xFrom * 70;
        yFrom = 20 + yFrom * 70;
        Color colorFrom = LayoutContainer.getLayoutColors().get(fromIndex);
        renderChessRow(colorFrom, xFrom, yFrom, new Empty());
        layoutPositionsContainer.put(fromIndex, layoutList.size() - 2);

        int xTo = (int) Math.floor(toIndex / 8.0);
        int yTo = toIndex % 8;
        xTo = 20 + xTo * 70;
        yTo = 20 + yTo * 70;
        Color colorTo = LayoutContainer.getLayoutColors().get(toIndex);
        renderChessRow(colorTo, xTo, yTo, figure);
        layoutPositionsContainer.put(toIndex, layoutList.size() - 2);
    }

    /*
    * shifting all FORWARD layout indexes back coz we dealing with Layout pane list
    * */
    private void shifting(int layoutIndexFrom, int toIndex, Map<Integer, Integer> layoutPositionsContainer,
                          int layoutIndexTo) {
        if (DeckManager.getInstance().isEmptyDeckCell(toIndex)) {
            layoutPositionsContainer.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > layoutIndexTo)
                    .forEach(entry -> {
                        layoutPositionsContainer.put(entry.getKey(), entry.getValue() - 1);
                    });
        } else {
            layoutPositionsContainer.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > layoutIndexTo)
                    .forEach(entry -> {
                        layoutPositionsContainer.put(entry.getKey(), entry.getValue() - 2);
                    });
        }

        layoutPositionsContainer.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > layoutIndexFrom)
                .forEach(entry -> {
                    layoutPositionsContainer.put(entry.getKey(), entry.getValue() - 2);
                });
    }
}
