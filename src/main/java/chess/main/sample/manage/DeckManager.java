package chess.main.sample.manage;


import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.King;
import chess.main.sample.guimanage.DeckLayoutManager;
import chess.main.sample.storage.ChessPositionsStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeckManager {

    private static DeckManager instance;

    private static ChessPositionsStorage chessPositionsStorage = ChessPositionsStorage.getGlobalStorage();

    public static DeckManager getInstance() {
        if (instance == null) {
            instance = new DeckManager();
        }
        return instance;
    }

    private DeckManager() {
    }

    public boolean isAllyFigureOnDeckCell(int deckCell, Position position) {
        return (!isEmptyDeckCell(deckCell) && !isOppositeFigureOnDeckCell(deckCell, position));
    }

    public boolean isEmptyDeckCell(int deckCell) {
        Map<Integer, Figure> positions = chessPositionsStorage.getPositionsContainer();
        return (positions.get(deckCell) == null);
    }

    public boolean isOppositeFigureOnDeckCell(int deckCell, Position position) {
        Map<Integer, Figure> positions = chessPositionsStorage.getPositionsContainer();
        Figure figure = positions.get(deckCell);
        return (figure != null) && (figure.getPosition() != position);
    }

    public List<Integer> getAllAvailableSiteMovements(Position position) {
        Map<Integer, Figure> positions = chessPositionsStorage.getPositionsContainer();
        return positions.entrySet()
                .stream()
                .filter(item -> !(item.getValue() instanceof King))
                .filter(item -> item.getValue().getPosition().equals(position))
                .flatMap(item -> item.getValue().getAllAvailableMovements(item.getKey()).stream())
                .collect(Collectors.toList());
    }

    public boolean isCheck(ChessPositionsStorage positionsStorage, Position position) {
        Position pos;
        if (position == Position.BLACK) {
            pos = Position.WHITE;
        } else {
            pos = Position.BLACK;
        }
        List<Integer> positionsOppositeSite = positionsStorage.getPositionsContainer()
                .entrySet()
                .stream()
                .filter(item -> item.getValue().getPosition().equals(pos))
                .flatMap(item -> item.getValue().getAllAvailableMovements(item.getKey()).stream())
                .peek(System.out::println)
                .collect(Collectors.toList());
        int kingIndex;
        if (position == Position.BLACK) {
            kingIndex = positionsStorage.getBlackKingIndex();
        } else {
            kingIndex = positionsStorage.getWhiteKingIndex();
        }
        return positionsOppositeSite.contains(kingIndex);
    }

    public void makeIndependentTurn(Map<Integer, Figure> positionsContainer, int fromInd, int toInd) {
        Figure figure = positionsContainer.get(fromInd);
        positionsContainer.remove(toInd);
        positionsContainer.put(toInd, figure);
    }

    public void makeTurn(int fromInd, int toInd) {
        Map<Integer, Figure> positionsContainer = chessPositionsStorage.getPositionsContainer();
        Figure figure = positionsContainer.get(fromInd);
        DeckLayoutManager.getInstance().makeTurn(fromInd, toInd, figure);

        positionsContainer.remove(toInd);
        positionsContainer.put(toInd, figure);
    }
}
