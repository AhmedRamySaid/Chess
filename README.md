# High-Performance Java 23 Chess Engine & Validation Suite

A modular Java 23 chess engine featuring dynamic transposition tables, depth extensions, and parallelized alpha-beta search. Includes an integrated Stockfish evaluation pipeline benchmarked across 3+ billion chess positions.

---

## Key Features

- **Minimax search with Alpha-Beta pruning**


- **Uses a complex algorithm inspired by ray-tracing algorithms to draw imaginary lines from pieces to evaluate legal moves, reducing algorithm complexity to O(n) from O(n²) for standard chess algorithms**


- **Tested and validated against stockfish across over 3 billion chess positions**
   
---

## Tech Stack

- **Language**: Java 23
- **Build System**: Apache Maven
- **UI Framework**: JavaFX

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):** Version 23 or higher
- **Build Tool:** Apache Maven 3.8.5 (bundled via Maven Wrapper, no standalone Maven install required)

### Quickstart

- **Simply run the JAR file included in the repo**