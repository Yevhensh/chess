# Chess Application

A JavaFX-based Chess game implementation featuring a robust move validation engine and a clean GUI.

## Features

- **Full Piece Set:** Includes Pawns, Knights, Bishops, Rooks, Queens, and Kings with accurate movement rules.
- **Move Validation:** Robust engine prevents illegal moves, including moves that leave the King in check.
- **Check Detection:** Automatic detection of check situations.
- **Dynamic UI:** Responsive JavaFX board that updates reliably after each move.
- **Turn-based Play:** Enforces alternating turns between White and Black.

## Architecture

The project follows a modular structure:

- **`chess.main.sample.figures`**: Abstract base class and specific piece implementations.
- **`chess.main.sample.figures.movements`**: Logic for calculating 'pseudo-legal' moves for each piece type.
- **`chess.main.sample.manage`**: Core game logic (`DeckManager`) for check detection and move legality.
- **`chess.main.sample.storage`**: State management for board positions and King tracking.
- **`chess.main.sample.guimanage`**: UI components and event handlers.
- **`chess.main.sample.utils`**: Utility methods for board coordinate transformations.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Building the Project

To build the project and install dependencies, run:

```bash
mvn clean install
```

### Running the Application

To start the Chess game, use the JavaFX Maven plugin:

```bash
mvn javafx:run
```

### Running Tests

To execute the unit test suite:

```bash
mvn test
```

## Implementation Details

- **Board Representation:** The board is internally represented as a 1D map (0-63).
- **Coordinate System:** `ChessUtils` provides helper methods to convert between 1D indices and 2D (row, column) coordinates to prevent wrap-around bugs.
- **Move Simulation:** Legality is checked by simulating a move and verifying the King's safety before finalizing the turn.
