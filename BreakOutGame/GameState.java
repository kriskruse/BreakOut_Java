package BreakOutGame;

public class GameState {

	public GameState(int n, int m) {
        Ball ball = new Ball();
        Platform platform = new Platform();
        Blockcluster blockcluster = new Blockcluster(n, m);
        
    }

    public void update() {
        // look input 
        // update platform
        // update ball
        // check collision
        // update blockcluster
        
    }


    public class Block {
        public int hp = 1;
    
        public void block(){
            
        }
        
    }

    public class Blockcluster {
        public Block[][] cluster;
    
        public Blockcluster(int n, int m) {
            cluster = new Block[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    cluster[i][j] = new Block();
                }
            }
        }  
    }


    public class Ball {
    
    
    }
    
    public class Platform {
    
    }
    
}


