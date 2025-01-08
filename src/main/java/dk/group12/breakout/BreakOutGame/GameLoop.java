package dk.group12.breakout.BreakOutGame;

import java.util.Set;

public class GameLoop {
    private GameState gameState;

    public GameLoop(int n, int m, int gameWidth, int gameHeight) {
        gameState = new GameState(n, m, gameWidth, gameHeight);
    }

    public void handleInput(Set<String> activeKeys) {
        if (activeKeys.contains("A") || activeKeys.contains("LEFT")) {
            gameState.platform.move(-10);
        }
        if (activeKeys.contains("D") || activeKeys.contains("RIGHT")) {
            gameState.platform.move(10);
        }
    }

    public GameState update() {
        gameState.update();
        return gameState;
    }
}
