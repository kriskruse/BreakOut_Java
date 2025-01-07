package dk.group12.breakout.BreakOutGame;

public class GameState {
    Platform platform;
    Ball ball;
    Blockcluster blockcluster;

    public GameState(int n, int m) {
        platform = new Platform();
        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x, platform.height + 0.1);
        blockcluster = new Blockcluster(n, m);

    }

    public void update(char input) {
        platform.move(input);
        ball.move();
        blockcluster.update(getCollision(ball));
        // update blockcluster

    }

    public Collision getCollision(Ball ball) {
        // TODO Auto-generated method stub
        return null;

    }

    public class Block {
        public int hp = 1;

        public void block() {

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

        public void update(Collision collision) {
            // TODO Auto-generated method stub

        }
    }

    public class Ball {
        public double x;
        public double y;
        public vec2 direction;

        public Ball(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            this.x += direction.x * direction.scalar;
            this.y += direction.y * direction.scalar;
        }

        public class vec2 {
            public double x;
            public double y;
            public double scalar;
        }
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
