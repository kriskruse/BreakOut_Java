package dk.group12.breakout.BreakOutGame;

import java.util.Set;

public class GameLoop {
    public GameState gameState;

    public GameLoop(int n, int m, int gameWidth, int gameHeight, int lives) {
        gameState = new GameState(n, m, gameWidth, gameHeight, lives);
    }

    public void handleInput(Set<String> activeKeys) {
        if (!gameState.gameRunning && !activeKeys.isEmpty()) {
            gameState.startGame();
        }
        if (gameState.gameEnded && !activeKeys.isEmpty()) {
            System.exit(0);
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
    }
}
