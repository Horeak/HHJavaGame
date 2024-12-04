# HHJavaGame

HHJavaGame is a simple Java-based game developed as a learning exercise to understand Java programming concepts and game development fundamentals.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Installation](#installation)
  - [Prerequisites](#prerequisites)
  - [Setup](#setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## About

This project is a practical exploration of Java programming, focusing on object-oriented principles and basic game mechanics. The game uses Java's built-in libraries for its graphical interface and player interactions.

## Features

- **Interactive Gameplay**: Implements fundamental mechanics for a simple, engaging game.
- **Graphics**: Utilizes Java's AWT and Swing libraries for 2D graphics rendering.
- **Keyboard Controls**: Supports input from the keyboard for gameplay interactions.

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed on your system.
- **Git**: Required to clone the repository.
### Setup

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/Horeak/HHJavaGame.git
   cd HHJavaGame
   ```

2. **Compile All Java Files**:

   Navigate to the root directory and compile all Java source files in the `TestGame/src` directory:

   ```bash
   javac TestGame/src/**/*.java
   ```

   This command recursively compiles all `.java` files in all subdirectories of `TestGame/src`.

3. **Create a JAR File**:

   Package the compiled files into a JAR file:

   ```bash
   jar cvf Game.jar -C TestGame/src .
   ```

   This command creates a `Game.jar` file in the current directory.

4. **Run the JAR File**:

   Execute the game using the following command:

   ```bash
   java -jar Game.jar
   ```

## Usage

Once the game is running, interact using the keyboard controls provided in the game. The specific controls and instructions will be visible or explained during gameplay.

## Contributing

As this repository has been archived and is now read-only, contributions are no longer accepted. However, feel free to fork the project for personal learning or experimentation.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
