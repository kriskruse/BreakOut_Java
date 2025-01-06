package BreakOutGame;
public class Setup {
    public static GameState gameState;

    public static void Setup(int n, int m) {
        gameState = new GameState(n, m);
    }
}