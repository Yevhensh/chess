package chess.main.sample.guimanage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.instances.Empty;
import chess.main.sample.game.Selected;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static chess.main.sample.constants.SceneConstants.*;

public class DeckLayoutManager {

    private static final int IMAGE_VIEW_OFFSET = 9;
    private static final int STATUS_LABEL_OFFSET = 10;

    private static DeckLayoutManager instance;

    private final Map<Integer, List<Node>> cellNodes = new HashMap<>();
    private final ChessPositionsStorage chessPositionsStorage = new ChessPositionsStorage();

    public static DeckLayoutManager getInstance() {
        if (instance == null) {
            instance = new DeckLayoutManager();
        }
        return instance;
    }

    private DeckLayoutManager() {
    }

    public void initializeBasicGameStart() {
        Map<Integer, Figure> positions = chessPositionsStorage.gameStartPositionRemind();
        AnchorPane pane = new AnchorPane();
        LayoutContainer.setLayout(pane);
        pane.getChildren().add(createDeckBackgroundRectangle());
        pane.getChildren().add(createStatusLabel());

        IntStream.range(0, CELLS_COUNT).forEach(cellIndex -> {
            Figure figure = positions.getOrDefault(cellIndex, new Empty());
            initializeCell(cellIndex, figure);
        });
    }

    public void highlightCell(int index) {
        Rectangle rectangle = findCellRectangle(index);
        if (rectangle != null) {
            rectangle.setFill(Color.YELLOW);
        }
    }

    public void unhighlightCell(int index) {
        Rectangle rectangle = findCellRectangle(index);
        if (rectangle != null) {
            rectangle.setFill(LayoutContainer.getLayoutColors().get(index));
        }
    }

    public Selected getSelectedByDeckPosition(int x, int y) {
        int col = (x - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int row = (y - START_FROM_BORDER_WIDTH) / RECTANGLE_DIMENSION;
        int deckCell = ChessUtils.getIndex(row, col);
        return new Selected(ChessPositionsStorage.getGlobalStorage().getFigureByDeckCell(deckCell), deckCell);
    }

    public void makeTurn(int fromIndex, int toIndex, Figure figure) {
        clearCell(fromIndex);
        clearCell(toIndex);
        renderCellAtIndex(fromIndex, new Empty());
        renderCellAtIndex(toIndex, figure);
    }

    private Rectangle createDeckBackgroundRectangle() {
        int dimension = RECTANGLE_DIMENSION * ROWS_COUNT + BORDER_WIDTH * 2;
        return new Rectangle(0, 0, dimension, dimension);
    }

    private Label createStatusLabel() {
        Label statusLabel = new Label("White's turn");
        statusLabel.setLayoutX(START_FROM_BORDER_WIDTH);
        statusLabel.setLayoutY(START_FROM_BORDER_WIDTH + RECTANGLE_DIMENSION * ROWS_COUNT + STATUS_LABEL_OFFSET);
        statusLabel.setFont(new Font("Arial", 20));
        LayoutContainer.setStatusLabel(statusLabel);
        return statusLabel;
    }

    private void initializeCell(int cellIndex, Figure figure) {
        int row = ChessUtils.getRow(cellIndex);
        int col = ChessUtils.getCol(cellIndex);
        Color cellColor = getCellColor(row, col);
        LayoutContainer.addLayoutColor(cellColor);
        renderCell(cellIndex, cellColor, getScreenX(col), getScreenY(row), figure);
    }

    private void renderCellAtIndex(int cellIndex, Figure figure) {
        int row = ChessUtils.getRow(cellIndex);
        int col = ChessUtils.getCol(cellIndex);
        Color color = LayoutContainer.getLayoutColors().get(cellIndex);
        renderCell(cellIndex, color, getScreenX(col), getScreenY(row), figure);
    }

    private void renderCell(int cellIndex, Color color, int x, int y, Figure figure) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(createCellRectangle(x, y, color));

        if (!(figure instanceof Empty)) {
            nodes.add(createPieceImageView(x, y, figure));
        }

        cellNodes.put(cellIndex, nodes);
    }

    private Rectangle createCellRectangle(int x, int y, Color color) {
        Rectangle rectangle = new Rectangle(x, y, RECTANGLE_DIMENSION, RECTANGLE_DIMENSION);
        rectangle.setFill(color);
        rectangle.setOnMouseClicked(MovementHandler.INSTANCE);
        LayoutContainer.getLayout().getChildren().add(rectangle);
        return rectangle;
    }

    private ImageView createPieceImageView(int cellX, int cellY, Figure figure) {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(figure.getFilenamePath()));
        ImageView imageView = new ImageView(image);
        imageView.setX(cellX + IMAGE_VIEW_OFFSET);
        imageView.setY(cellY + IMAGE_VIEW_OFFSET);
        imageView.setFitWidth(IMAGE_DIMENSION);
        imageView.setFitHeight(IMAGE_DIMENSION);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(MovementHandler.INSTANCE);
        LayoutContainer.getLayout().getChildren().add(imageView);
        return imageView;
    }

    private void clearCell(int cellIndex) {
        List<Node> nodes = cellNodes.get(cellIndex);
        if (nodes == null) {
            return;
        }
        LayoutContainer.getLayout().getChildren().removeAll(nodes);
        cellNodes.remove(cellIndex);
    }

    private Rectangle findCellRectangle(int index) {
        List<Node> nodes = cellNodes.get(index);
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        Node firstNode = nodes.get(0);
        return firstNode instanceof Rectangle rectangle ? rectangle : null;
    }

    private Color getCellColor(int row, int col) {
        return (row + col) % 2 != 0 ? Color.DARKGREEN : Color.GREY;
    }

    private int getScreenX(int col) {
        return START_FROM_BORDER_WIDTH + col * RECTANGLE_DIMENSION;
    }

    private int getScreenY(int row) {
        return START_FROM_BORDER_WIDTH + row * RECTANGLE_DIMENSION;
    }
}
