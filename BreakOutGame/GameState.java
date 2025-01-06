package BreakOutGame;

public class GameState {

	public GameState(int n, int m) {
        Platform platform = new Platform();
        Ball ball = new Ball(platform.x);
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
        public double x;
        public double y;
        public vec2 direction;
    
        public Ball(double x) {
            this.x = x;
            this.y = 0.1;
        }
    }
    
    public class vec2 {
        public double x;
        public double y;
        public double scalar;
    }
    
    public class Platform {
        public double height;
        public double width;
        public double x;

        public void move(double direction) {
            this.x += direction;
        }

    }
    
}


