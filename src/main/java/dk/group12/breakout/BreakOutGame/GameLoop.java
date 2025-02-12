package dk.group12.breakout.BreakOutGame;

import java.util.Set;

public class GameLoop {
    public GameState gameState;
    public boolean gameEnded = false;
    public final int n;
    public final int m;
    public final int gameWidth;
    public final int gameHeight;
    public final int lives;
    public static int playerMovementSpeed = 8;

    public GameLoop(int n, int m, int gameWidth, int gameHeight, int lives) {
        this.n = n;
        this.m = m;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.lives = lives;

        gameState = new GameState(n, m, gameWidth, gameHeight, lives, new ScoreTracker());

        // Load scores from file
        gameState.scoreHistory.loadFromFile("scores.txt");
    }

    public void handleInput(Set<String> activeKeys) {
        if (!gameState.gameRunning && !activeKeys.isEmpty()) {
            gameState.startGame();
        }
        if (gameState.gameEnded) {
            gameEnded = true;
        }

        if (activeKeys.contains("A") || activeKeys.contains("LEFT")) {
            gameState.platform.move(-playerMovementSpeed);
        }
        if (activeKeys.contains("D") || activeKeys.contains("RIGHT")) {
            gameState.platform.move(playerMovementSpeed);
        }
    }

    public void update() {
        gameState.update();

        if (gameState.gameWon) {
            GameState oldGameState = gameState;
            gameState = new GameState(n, m, gameWidth, gameHeight, lives, oldGameState.scoreTracker);

        }

    }

    public void restartGame() {
        gameEnded = false;

        gameState.scoreHistory.saveToFile("scores.txt");

        gameState = new GameState(n, m, gameWidth, gameHeight, lives, new ScoreTracker());

        gameState.scoreHistory.loadFromFile("scores.txt");

        gameState.scoreHistory.printScores();

    }
}
