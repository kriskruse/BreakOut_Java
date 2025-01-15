package dk.group12.breakout.BreakOutGame;
import dk.group12.breakout.BreakoutGraphical;
import javafx.scene.control.Menu;

import java.util.Set;

public class GameLoop {
    public GameState gameState;
    public MenuController menuController;
    public BreakoutGraphical breakoutGraphical;

    public GameLoop(int n, int m, int gameWidth, int gameHeight, int lives, BreakoutGraphical breakoutGraphical) {

        this.breakoutGraphical = breakoutGraphical;
        gameState = new GameState(n, m, gameWidth, gameHeight, lives);
    }

    public void handleInput(Set<String> activeKeys) {
        if (!gameState.gameRunning && !activeKeys.isEmpty()) {
            gameState.startGame();
        }
        if (gameState.gameEnded) {
            breakoutGraphical.setGameEnded(true);
        }

        if (activeKeys.contains("A") || activeKeys.contains("LEFT")) {
            GameState.platform.move(-7);
        }
        if (activeKeys.contains("D") || activeKeys.contains("RIGHT")) {
            GameState.platform.move(7);
        }
    }

    public void update() {
        gameState.update();
    }
}
