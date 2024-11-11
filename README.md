# Minesweeper with Leaderboard

A classic Minesweeper game reimagined as a full-stack application with a leaderboard to track players' fastest completion times. Challenge yourself to complete the game as quickly as possible and see how you rank!

## Table of Contents
- [Features](#features)
- [Gameplay](#gameplay)
- [Installation](#installation)
- [How to Play](#how-to-play)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Classic Minesweeper Gameplay**: Enjoy the traditional Minesweeper mechanics with hidden mines, number hints, and strategic clicks.
- **Leaderboard for Fastest Times**: Compete with others by finishing the game in the shortest time. Your best time is recorded and displayed on the leaderboard.
- **User Accounts**: Optionally, create an account to save and track your best times on the leaderboard.

## Gameplay

This version of Minesweeper focuses on the classic mechanics with the addition of a leaderboard to make the game more competitive. Players aim to complete the game as quickly as possible to secure a spot on the leaderboard.

## Installation

### Prerequisites
- **Docker**: Make sure you have Docker installed on your system. You can download it from [here](https://www.docker.com/get-started).

### Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Finlay123456/Minesweeper
2. **Navigate to the Project Directory**:
   ```bash
   cd Minesweeper
   ```
3. **Start the Application with Docker**:
   - Run the following command to build and start the application:
     ```bash
     docker-compose up --build
     ```
   - This command will build the Docker images and start containers for the frontend, backend, and database.
4. **Access the Game**:
   - Once the containers are running, open your browser and go to `http://localhost:3000` (or the port configured in your `docker-compose.yml`) to start playing Minesweeper with the leaderboard feature.
  
### Stopping the Application

To stop the application and remove the containers, use:
   ```bash
     docker-compose up --build
   ```

## How to Play

1. **Objective**: Uncover all safe squares without hitting a mine. Each number revealed represents the count of adjacent mines.
2. **Win Condition**: Clear the entire board without detonating a mine.
3. **Submit Your Time**: When you win, you can submit your time to the leaderboard.

## Contributing

Contributions are welcome! If you have ideas for additional twists or improvements, feel free to submit a pull request.
1. **Fork the repository**.
2. **Create your feature branch**:
    ```bash
    git checkout -b feature/YourFeature
3. **Commit your changes**:
   ```bash
   git commit -m "Add YourFeature"
4. **Push to the branch**:
   ```bash
   git push origin feature/YourFeature
5. **Open a pull request**.

## License
This project is licensed under the MIT License.

