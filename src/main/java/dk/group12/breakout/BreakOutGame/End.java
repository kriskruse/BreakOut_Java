package dk.group12.breakout.BreakOutGame;
public class End {
    public End() {
        GameLoop.scannerInput.close();
        System.out.println("Game Over!");
    }
    
}
