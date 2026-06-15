# Agent Rules and Skills for Java Chess Project

This document provides guidance for AI agents working on this project.

## Architectural Rules

1. **Coordinate Logic**: Always use `chess.main.sample.utils.ChessUtils` for converting between 1D indices (0-63) and 2D coordinates (row, col), and for checking if a move is within board boundaries.
2. **Move Validation**:
    - **Pseudo-legal moves**: Defined in `Figure` subclasses and `Movement` implementations. These only check if the move is physically possible for the piece on a given board state (e.g., sliding pieces blocked by others).
    - **Legal moves**: Validated by `chess.main.sample.manage.DeckManager`. A move is only legal if it is pseudo-legal AND does not leave the player's king in check.
3. **Avoid Nulls**: Methods returning lists of movements (e.g., `getAllAvailableMovements`) must return `Collections.emptyList()` instead of `null` if no moves are available.
4. **Global State**: The project uses a singleton-like global storage in `ChessPositionsStorage`. When writing tests, ensure you properly set up and tear down this global state to avoid side effects between tests.

## Coding Conventions

- **Piece Instances**: Pieces are represented by classes in `chess.main.sample.figures.instances`.
- **Movement Strategies**: Complex movement logic (Diagonal, Line, etc.) is encapsulated in classes in `chess.main.sample.figures.movements`.
- **UI Management**: JavaFX UI components are managed by `DeckLayoutManager`. Do not modify the UI directly from logic classes.

## Testing Skills

- **JUnit 5**: Use JUnit 5 for all unit tests.
- **Simulating Board States**: Use `ChessPositionsStorage` with a custom map to simulate specific board configurations for testing piece movements and game rules.
- **Reflection in Tests**: Since `setGlobalStorage` in `ChessPositionsStorage` might be private or protected, you may need to use reflection to set it during test setup, as seen in existing tests.

## Knowledge Base

- Board indices: 0 is a8, 7 is h8, 56 is a1, 63 is h1.
- White pawn starting row: 6 (indices 48-55).
- Black pawn starting row: 1 (indices 8-15).
