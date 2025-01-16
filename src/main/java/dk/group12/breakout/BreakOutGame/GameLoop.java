package dk.group12.breakout.BreakOutGame;

import javafx.scene.control.Menu;

import java.util.Set;

public class GameLoop {
    public GameState gameState;

    public boolean gameEnded = false;
    private final int n;
    private final int m;
    private final int gameWidth;
    private final int gameHeight;
    private final int lives;

    public GameLoop(int n, int m, int gameWidth, int gameHeight, int lives) {
        this.n = n;
        this.m = m;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.lives = lives;

        gameState = new GameState(n, m, gameWidth, gameHeight, lives, new ScoreTracker());
    }

    public void handleInput(Set<String> activeKeys) {
        if (!gameState.gameRunning && !activeKeys.isEmpty()) {
            gameState.startGame();
        }
        if (gameState.gameEnded) {
            gameEnded = true;
        }

        if (activeKeys.contains("A") || activeKeys.contains("LEFT")) {
            gameState.platform.move(-7);
        }
        if (activeKeys.contains("D") || activeKeys.contains("RIGHT")) {
            gameState.platform.move(7);
        }
    }

    public void update() {
        gameState.update();

        if (gameState.gameWon) {
            GameState oldGameState = gameState;
            gameState = new GameState(n,m,gameWidth,gameHeight,lives, oldGameState.scoreTracker);

        }

    }

    public void restartGame() {
        gameState = new GameState(n, m, gameWidth, gameHeight, lives, new ScoreTracker());
    }
}
