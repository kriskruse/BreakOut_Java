package dk.group12.breakout.BreakOutGame;

public class GameState {
    Platform platform;
    Ball ball;
    Blockcluster blockcluster;
    int platformX , platformY ,platformWidth, platformHeight, clusterWidth, clusterHeight;

    public GameState(int n, int m, int gameWidth, int gameHeight) {
        platformWidth = gameWidth / 7;
        platformX = gameWidth / 2 - platformWidth / 2;
        platformHeight = 10;
        platformY = (int) (gameHeight - platformHeight - gameHeight*0.05);
        platform = new Platform(platformX, platformY,platformWidth, platformHeight);


        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x, platform.height + 0.1, 5);
        blockcluster = new Blockcluster(n, m, clusterWidth, clusterHeight);

    }

    public void update(char input) {
        platform.move(input);
        ball.move();
        blockcluster.update(getCollision(ball));
        // update blockcluster

    }
    public void update() {
        ball.move();
        blockcluster.update(getCollision(ball));
        // update blockcluster

    }

    public Collision getCollision(Ball ball) {
        // TODO Auto-generated method stub
        return null;

    }

    public class Block {
        public double x, y, width, height;
        public int hp = 1;

        public Block(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

    }

    public class Blockcluster {
        public Block[][] cluster;
        double x,y,width,height;

        public Blockcluster(int n, int m, double clusterWidth, double clusterHeight) {
            cluster = new Block[n][m];
            width = clusterWidth / m - m * 0.1;
            height = clusterHeight / n - n * 0.1;
            for (int i = 0; i < n; i++) {
                y = i * height;
                for (int j = 0; j < m; j++) {
                    x = j * width;
                    cluster[i][j] = new Block(x,y,width,height);
                }
            }
        }

        public void update(Collision collision) {
            // TODO Auto-generated method stub

        }
    }

    public class Ball {
        public double x, y;
        public vec2 direction;
        public int radius;

        public Ball(double x, double y, int radius) {
            this.x = x;
            this.y = y;
            this.radius=radius;
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
        public double x,y,width,height;

        public Platform(int x, int y, int width, int height) {
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
        }

        public void move(double direction) {
            this.x += direction;
        }

    }

}
