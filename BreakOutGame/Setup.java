package BreakOutGame;
public class Setup {
    public static GameState gameState;

    public Setup(int n, int m) {
        gameState = new GameState(n, m);
    }
}