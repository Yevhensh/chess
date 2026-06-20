# Java Chess Application

This is a Chess application built with Java and JavaFX, following an object-oriented design to handle chess rules, piece movements, and the graphical user interface.

## Project Structure

The project is organized into several packages:

- `chess.main.sample.figures`: Contains the base `Figure` and `Movement` classes, as well as specific figure implementations (King, Queen, Bishop, Knight, Rok, Pawn).
- `chess.main.sample.game`: Handles game-level logic like turn switching and selection management.
- `chess.main.sample.guimanage`: Manages the JavaFX UI, including layout and event handling.
- `chess.main.sample.manage`: Contains the `DeckManager` which enforces chess rules like check and move legality.
- `chess.main.sample.storage`: Manages the state of the chessboard and piece positions.
- `chess.main.sample.utils`: Provides utility methods for coordinate conversions and validation.

## Building and Running

The project uses Maven for dependency management and build automation.

### Build the project:
```bash
mvn clean compile
```

### Run the application:
```bash
mvn javafx:run
```

### Run from IntelliJ IDEA:
`mvn javafx:run` works because Maven adds the JavaFX module path. IntelliJ's default **Run** on `Main` does not.

Either use the Maven tool window and run **`javafx:run`**, or add this once to your **Main** run configuration VM options (after `mvn compile`):

```
--module-path $PROJECT_DIR$/target/javafx-lib --add-modules javafx.controls
```

A shared run configuration is in `.run/Main.run.xml`.

### Run tests:
```bash
mvn test
```

## Features

- Full implementation of standard chess piece movements.
- Move validation including check detection and pinning.
- JavaFX-based graphical interface.
- Support for both white and black pieces.
