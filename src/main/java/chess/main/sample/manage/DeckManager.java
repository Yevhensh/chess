package chess.main.sample.manage;

import chess.main.sample.figures.Figure;
import chess.main.sample.figures.Position;
import chess.main.sample.figures.instances.Bishop;
import chess.main.sample.figures.instances.King;
import chess.main.sample.figures.instances.Knight;
import chess.main.sample.figures.instances.Pawn;
import chess.main.sample.figures.instances.Queen;
import chess.main.sample.figures.instances.Rok;
import chess.main.sample.figures.movements.KingMove;
import chess.main.sample.game.GameState;
import chess.main.sample.game.Move;
import chess.main.sample.storage.ChessPositionsStorage;
import chess.main.sample.utils.ChessUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeckManager {

  private final ChessPositionsStorage storage;
  private final GameState gameState;

  public DeckManager(ChessPositionsStorage storage, GameState gameState) {
    this.storage = storage;
    this.gameState = gameState;
  }

  public boolean isOppositeFigureOnDeckCell(
      Map<Integer, Figure> positions, int deckCell, Position position) {
    return ChessUtils.isOpposite(positions, deckCell, position);
  }

  public List<Integer> getAllOppositeSiteAttacks(
      Map<Integer, Figure> positions, Position oppositePosition) {
    return positions.entrySet().stream()
        .filter(entry -> entry.getValue().getPosition().equals(oppositePosition))
        .flatMap(entry -> getAttacksForFigure(positions, entry).stream())
        .collect(Collectors.toList());
  }

  private List<Integer> getAttacksForFigure(
      Map<Integer, Figure> positions, Map.Entry<Integer, Figure> entry) {
    Figure figure = entry.getValue();
    return switch (figure) {
      case King king -> new KingMove().getBasicMoves(positions, entry.getKey(), king);
      case Pawn pawn -> getPawnAttacks(entry.getKey(), pawn);
      default ->
          figure.getAllAvailableMovements(
              positions, entry.getKey(), gameState.getHistoryManager().getLastMove());
    };
  }

  private List<Integer> getPawnAttacks(int deckCell, Pawn pawn) {
    int row = ChessUtils.getRow(deckCell);
    int col = ChessUtils.getCol(deckCell);
    int forwardDir = pawn.getPosition() == Position.WHITE ? -1 : 1;

    return IntStream.of(col - 1, col + 1)
        .filter(nextCol -> ChessUtils.isValid(row + forwardDir, nextCol))
        .map(nextCol -> ChessUtils.getIndex(row + forwardDir, nextCol))
        .boxed()
        .collect(Collectors.toList());
  }

  public boolean isCheck(ChessPositionsStorage positionsStorage, Position position) {
    Position oppositePosition =
        switch (position) {
          case BLACK -> Position.WHITE;
          case WHITE -> Position.BLACK;
        };
    List<Integer> oppositeAttacks =
        getAllOppositeSiteAttacks(positionsStorage.getPositionsContainer(), oppositePosition);

    int kingIndex =
        (position == Position.BLACK)
            ? positionsStorage.getBlackKingIndex()
            : positionsStorage.getWhiteKingIndex();

    return oppositeAttacks.contains(kingIndex);
  }

  public boolean hasPieceMoved(int index, Figure figure) {
    return gameState.getHistoryManager().getMoves().stream()
        .anyMatch(
            m -> m.fromIndex() == index || (m.movedFigure() == figure && m.fromIndex() != index));
  }

  public boolean isCastlingLegal(int fromInd, int toInd, Position side) {
    Figure king = storage.getPositionsContainer().get(fromInd);
    if (!(king instanceof King)) return false;

    // King must not have moved
    // Note: This is simplified, we should check if THIS specific king moved.
    // HistoryManager has the moves.
    boolean kingMoved =
        gameState.getHistoryManager().getMoves().stream()
            .anyMatch(
                m -> m.movedFigure() instanceof King && m.movedFigure().getPosition() == side);
    if (kingMoved) return false;

    int row = ChessUtils.getRow(fromInd);
    int col = ChessUtils.getCol(toInd);
    int rookInd = (col == 6) ? ChessUtils.getIndex(row, 7) : ChessUtils.getIndex(row, 0);
    Figure rook = storage.getPositionsContainer().get(rookInd);

    if (!(rook instanceof chess.main.sample.figures.instances.Rok) || rook.getPosition() != side)
      return false;

    // Rook must not have moved
    boolean rookMoved =
        gameState.getHistoryManager().getMoves().stream()
            .anyMatch(
                m ->
                    m.fromIndex() == rookInd
                        || (m.movedFigure() == rook && m.fromIndex() != rookInd));
    if (rookMoved) return false;

    // Path must be clear
    int start = Math.min(ChessUtils.getCol(fromInd), ChessUtils.getCol(rookInd));
    int end = Math.max(ChessUtils.getCol(fromInd), ChessUtils.getCol(rookInd));
    for (int c = start + 1; c < end; c++) {
      if (!ChessUtils.isEmpty(storage.getPositionsContainer(), ChessUtils.getIndex(row, c)))
        return false;
    }

    // King must not be in check, and must not pass through squares under attack
    if (isCheck(storage, side)) return false;

    int direction = (col == 6) ? 1 : -1;
    for (int i = 1; i <= 2; i++) {
      int intermediateIndex = ChessUtils.getIndex(row, 4 + i * direction);
      if (isSquareAttacked(intermediateIndex, side)) return false;
    }

    return true;
  }

  private boolean isSquareAttacked(int index, Position side) {
    Position oppositePosition = (side == Position.WHITE) ? Position.BLACK : Position.WHITE;
    return getAllOppositeSiteAttacks(storage.getPositionsContainer(), oppositePosition)
        .contains(index);
  }

  public boolean isMoveLegal(int fromInd, int toInd, Position side) {
    Map<Integer, Figure> currentPositions = storage.getPositionsContainer();

    Figure movingFigure = currentPositions.get(fromInd);
    Figure capturedFigure = currentPositions.get(toInd);

    int oldWhiteKingIndex = storage.getWhiteKingIndex();
    int oldBlackKingIndex = storage.getBlackKingIndex();

    // Simulate move
    currentPositions.remove(fromInd);
    currentPositions.put(toInd, movingFigure);
    if (movingFigure instanceof King) {
      if (side == Position.WHITE) storage.setWhiteKingIndex(toInd);
      else storage.setBlackKingIndex(toInd);
    }

    boolean isCheck = isCheck(storage, side);

    // Revert move
    currentPositions.put(fromInd, movingFigure);
    if (capturedFigure != null) {
      currentPositions.put(toInd, capturedFigure);
    } else {
      currentPositions.remove(toInd);
    }
    storage.setWhiteKingIndex(oldWhiteKingIndex);
    storage.setBlackKingIndex(oldBlackKingIndex);

    return !isCheck;
  }

  public String getBoardSnapshot() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 64; i++) {
      Figure f = storage.getPositionsContainer().get(i);
      if (f == null) sb.append(".");
      else {
        char c =
            switch (f) {
              case Pawn p -> 'p';
              case Rok r -> 'r';
              case Knight n -> 'n';
              case Bishop b -> 'b';
              case Queen q -> 'q';
              case King k -> 'k';
              default -> '?';
            };
        if (f.getPosition() == Position.WHITE) c = Character.toUpperCase(c);
        sb.append(c);
      }
    }
    sb.append(gameState.getCurrentTurn() == Position.WHITE ? "w" : "b");
    return sb.toString();
  }

  public void makeTurn(int fromInd, int toInd) {
    Map<Integer, Figure> positionsContainer = storage.getPositionsContainer();
    Figure figure = positionsContainer.get(fromInd);
    Figure captured = positionsContainer.get(toInd);
    int capturedIndex = toInd;
    boolean wasPromoted = false;
    boolean isEnPassant = false;

    // Check for En Passant
    if (figure instanceof Pawn
        && captured == null
        && ChessUtils.getCol(fromInd) != ChessUtils.getCol(toInd)) {
      isEnPassant = true;
      capturedIndex = ChessUtils.getIndex(ChessUtils.getRow(fromInd), ChessUtils.getCol(toInd));
      captured = positionsContainer.get(capturedIndex);
    }

    // Check for Pawn Promotion
    if (figure instanceof Pawn) {
      int row = ChessUtils.getRow(toInd);
      if ((figure.getPosition() == Position.WHITE && row == 0)
          || (figure.getPosition() == Position.BLACK && row == 7)) {
        figure = new Queen(figure.getPosition());
        wasPromoted = true;
      }
    }

    boolean isCastling =
        (figure instanceof King
            && Math.abs(ChessUtils.getCol(fromInd) - ChessUtils.getCol(toInd)) == 2);

    if (figure instanceof Pawn || captured != null) {
      gameState.resetHalfMoveClock();
    } else {
      gameState.incrementHalfMoveClock();
    }

    gameState
        .getHistoryManager()
        .addMove(
            new Move(
                fromInd,
                toInd,
                positionsContainer.get(fromInd),
                captured,
                capturedIndex,
                wasPromoted,
                isCastling,
                isEnPassant),
            getBoardSnapshot(),
            gameState.getHalfMoveClock());

    positionsContainer.remove(fromInd);
    positionsContainer.put(toInd, figure);

    if (isCastling) {
      int row = ChessUtils.getRow(toInd);
      int rookFrom, rookTo;
      if (ChessUtils.getCol(toInd) == 6) { // Kingside
        rookFrom = ChessUtils.getIndex(row, 7);
        rookTo = ChessUtils.getIndex(row, 5);
      } else { // Queenside
        rookFrom = ChessUtils.getIndex(row, 0);
        rookTo = ChessUtils.getIndex(row, 3);
      }
      Figure rook = positionsContainer.remove(rookFrom);
      positionsContainer.put(rookTo, rook);
    }
    if (isEnPassant) {
      positionsContainer.remove(capturedIndex);
    }

    if (figure instanceof King) {
      if (figure.getPosition() == Position.WHITE) storage.setWhiteKingIndex(toInd);
      else storage.setBlackKingIndex(toInd);
    }
  }

  public void undoMove() {
    Move move = gameState.getHistoryManager().undo();
    if (move == null) return;

    gameState.setHalfMoveClock(gameState.getHistoryManager().getPreviousHalfMoveClock());

    Map<Integer, Figure> positions = storage.getPositionsContainer();
    positions.put(move.fromIndex(), move.movedFigure());

    // Handle captured figure reversal
    if (move.capturedFigure() != null) {
      positions.put(move.capturedIndex(), move.capturedFigure());
      if (move.isEnPassant()) {
        positions.remove(move.toIndex());
      }
    } else {
      positions.remove(move.toIndex());
    }

    // Update king index if needed
    if (move.movedFigure() instanceof King) {
      if (move.movedFigure().getPosition() == Position.WHITE)
        storage.setWhiteKingIndex(move.fromIndex());
      else storage.setBlackKingIndex(move.fromIndex());
    }

    // Handle castling reversal
    if (move.isCastling()) {
      int row = ChessUtils.getRow(move.toIndex());
      int rookFrom, rookTo;
      if (ChessUtils.getCol(move.toIndex()) == 6) { // Kingside
        rookFrom = ChessUtils.getIndex(row, 7);
        rookTo = ChessUtils.getIndex(row, 5);
      } else { // Queenside
        rookFrom = ChessUtils.getIndex(row, 0);
        rookTo = ChessUtils.getIndex(row, 3);
      }
      Figure rook = positions.remove(rookTo);
      positions.put(rookFrom, rook);
    }

    gameState.switchTurn();
  }

  public void redoMove() {
    Move move = gameState.getHistoryManager().redo();
    if (move == null) return;

    if (move.movedFigure() instanceof Pawn || move.capturedFigure() != null) {
      gameState.resetHalfMoveClock();
    } else {
      gameState.incrementHalfMoveClock();
    }

    Figure figure = move.movedFigure();
    if (move.wasPromoted()) {
      figure = new Queen(figure.getPosition());
    }

    Map<Integer, Figure> positions = storage.getPositionsContainer();
    positions.remove(move.fromIndex());
    positions.put(move.toIndex(), figure);

    if (move.capturedFigure() != null && move.isEnPassant()) {
      positions.remove(move.capturedIndex());
    }

    if (figure instanceof King) {
      if (figure.getPosition() == Position.WHITE) storage.setWhiteKingIndex(move.toIndex());
      else storage.setBlackKingIndex(move.toIndex());
    }

    // Handle castling redo
    if (move.isCastling()) {
      int row = ChessUtils.getRow(move.toIndex());
      int rookFrom, rookTo;
      if (ChessUtils.getCol(move.toIndex()) == 6) { // Kingside
        rookFrom = ChessUtils.getIndex(row, 7);
        rookTo = ChessUtils.getIndex(row, 5);
      } else { // Queenside
        rookFrom = ChessUtils.getIndex(row, 0);
        rookTo = ChessUtils.getIndex(row, 3);
      }
      Figure rook = positions.remove(rookFrom);
      positions.put(rookTo, rook);
    }

    gameState.switchTurn();
  }

  public boolean hasAnyLegalMoves(Position side) {
    Map<Integer, Figure> currentPositions = new HashMap<>(storage.getPositionsContainer());

    return currentPositions.entrySet().stream()
        .filter(entry -> entry.getValue().getPosition() == side)
        .anyMatch(
            entry -> {
              int fromInd = entry.getKey();
              return entry
                  .getValue()
                  .getAllAvailableMovements(
                      currentPositions, fromInd, gameState.getHistoryManager().getLastMove())
                  .stream()
                  .anyMatch(toInd -> isMoveLegal(fromInd, toInd, side));
            });
  }

  public boolean isCheckmate(Position side) {
    return isCheck(storage, side) && !hasAnyLegalMoves(side);
  }

  public boolean isStalemate(Position side) {
    return !isCheck(storage, side) && !hasAnyLegalMoves(side);
  }

  public boolean isThreefoldRepetition() {
    return gameState.getHistoryManager().getRepetitionCount(getBoardSnapshot()) >= 3;
  }

  public boolean isFiftyMoveRule() {
    return gameState.getHalfMoveClock() >= 100;
  }

  public boolean isInsufficientMaterial() {
    Map<Integer, Figure> positions = storage.getPositionsContainer();
    if (positions.size() > 4) return false;

    List<Figure> figures = positions.values().stream().toList();

    // King vs King
    if (figures.size() == 2) return true;

    // King and Bishop vs King or King and Knight vs King
    if (figures.size() == 3) {
      return figures.stream().anyMatch(f -> f instanceof Bishop || f instanceof Knight);
    }

    // King and Bishop vs King and Bishop (same color)
    if (figures.size() == 4) {
      List<Figure> bishops = figures.stream().filter(f -> f instanceof Bishop).toList();
      if (bishops.size() == 2 && bishops.get(0).getPosition() != bishops.get(1).getPosition()) {
        // Find indices of bishops
        List<Integer> bishopIndices =
            positions.entrySet().stream()
                .filter(e -> e.getValue() instanceof Bishop)
                .map(Map.Entry::getKey)
                .toList();

        int b1 = bishopIndices.get(0);
        int b2 = bishopIndices.get(1);
        return (ChessUtils.getRow(b1) + ChessUtils.getCol(b1)) % 2
            == (ChessUtils.getRow(b2) + ChessUtils.getCol(b2)) % 2;
      }
    }

    return false;
  }
}
