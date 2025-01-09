package dk.group12.breakout.BreakOutGame;

import java.util.Set;

public class GameLoop {
    public GameState gameState;

    public GameLoop(int n, int m, int gameWidth, int gameHeight) {
        gameState = new GameState(n, m, gameWidth, gameHeight);
    }

    public void handleInput(Set<String> activeKeys) {
        if (!gameState.gameRunning && !activeKeys.isEmpty()) {
            gameState.startGame();
        }

        if (activeKeys.contains("A") || activeKeys.contains("LEFT")) {
            gameState.platform.move(-10);
        }
        if (activeKeys.contains("D") || activeKeys.contains("RIGHT")) {
            gameState.platform.move(10);
        }
    }

    public void update() {
        gameState.update();
    }
}
