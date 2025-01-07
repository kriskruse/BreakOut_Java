package dk.group12.breakout.BreakOutGame;


public class Main {
    public static void main(String[] args) {
        int arg1 = 8;
        int arg2 = 8;
        try {
            arg1 = Integer.parseInt(args[0]);
            arg2 = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Default to 8x8, use 2 arguments to change the size of the game board.");
        }
        

        
        
        //Setup(arg1, arg2);
        //GameLoop.GameLoop();
        //End.End();

    }
}