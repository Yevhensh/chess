# Agent Rules and Skills for Chess Project

Welcome! This file provides instructions and tips for working with this chess repository.

## Project Context
This is a JavaFX-based chess application. It uses a 1D mapping (0-63) for board representation but performs logic in 2D (row/col) to prevent bugs.

## Core Directives

- **Board Math:** Always use `ChessUtils` for coordinate conversions. Avoid manually calculating `index / 8` or `index % 8` outside of the utility class.
- **Move Validation:** New movement logic must be implemented in the respective `Movement` subclass. Always consider board boundaries.
- **Legality vs. Attacks:** Distinguish between 'pseudo-legal' moves (where a piece *could* go) and 'legal' moves (where it is safe to go without leaving the King in check).
- **UI Consistency:** When updating the UI, use `DeckLayoutManager.getInstance().makeTurn()`. Do not manipulate the `LayoutContainer`'s child list directly.

## Coding Conventions

- **Constants:** Any new UI-related dimension or magic number should be added to `SceneConstants`.
- **Testing:** New features or bug fixes must be accompanied by JUnit 5 tests.
- **Immutability:** When calculating moves, return new lists to avoid side effects on shared state.

## Tips for Agents

- **Check Detection:** If you need to check for a checkmate, simulate all possible legal moves for the current player. If no legal moves exist and they are in check, it's checkmate.
- **Logging:** Use `System.out.println` sparingly for debugging moves, but ensure it's removed or kept minimal in final submissions.
- **Maven:** Use `mvn clean install` after adding resources or changing the `pom.xml`.
