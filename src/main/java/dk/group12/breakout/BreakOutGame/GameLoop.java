package dk.group12.breakout.BreakOutGame;

import java.util.Scanner;

public class GameLoop {
    public static GameState gameState;
    public static Scanner scannerInput;

    public GameLoop(int n, int m, int gameWidth, int gameHeight) {
        scannerInput = new Scanner(System.in);
        gameState = new GameState(n, m, gameWidth, gameHeight);
    }

    public GameState update() {
        if (scannerInput.hasNext()){
            char input = scannerInput.next().charAt(0);
            gameState.update(input);
        }
        gameState.update();
        return gameState;
    }

}
