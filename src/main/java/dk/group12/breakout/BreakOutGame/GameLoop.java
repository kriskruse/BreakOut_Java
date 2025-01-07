package dk.group12.breakout.BreakOutGame;

import java.util.Scanner;

public class GameLoop {
    public static GameState gameState;
    public static Scanner scannerInput;

    public GameLoop(int n, int m) {
        scannerInput = new Scanner(System.in);
        gameState = new GameState(n, m);
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
