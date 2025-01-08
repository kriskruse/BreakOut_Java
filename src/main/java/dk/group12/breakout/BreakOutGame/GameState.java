package dk.group12.breakout.BreakOutGame;

import dk.group12.breakout.BreakoutGraphical;

public class GameState {
    public Platform platform;
    public Ball ball;
    public Blockcluster blockcluster;
    private int clusterWidth, clusterHeight;

    public GameState(int n, int m, int gameWidth, int gameHeight) {
        int platformWidth = gameWidth / 10;
        int platformX = gameWidth / 2 - platformWidth / 2;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);

        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x + platform.width / 2, platform.y - 10, 5);

        // Init block cluster
        blockcluster = new Blockcluster(n, m, clusterWidth, clusterHeight);
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
        double width,height;

        public Blockcluster(int n, int m, double clusterWidth, double clusterHeight) {
            cluster = new Block[n][m];
            width = clusterWidth / m - m * 0.1;
            height = clusterHeight / n - n * 0.1;
            for (int i = 0; i < n; i++) {
                double y = i * height;
                for (int j = 0; j < m; j++) {
                    double x = j * width;
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

            // Initialize direction for testing
            direction = new vec2(0, -1, 5);
        }

        public void move() {
            this.x += direction.x * direction.scalar;
            this.y += direction.y * direction.scalar;
        }

        public class vec2 {
            public double x, y;
            public double scalar;

            public vec2(double x, double y, double scalar) {
                this.x = x;
                this.y = y;
                this.scalar = scalar;
            }
        }
    }

    public class Platform {
        public double x, y, width, height;

        public Platform(int x, int y, int width, int height) {
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
        }

        public void move(double direction) {
            this.x += direction;

            if (this.x < 0) this.x = 0;
            if (this.x + this.width > clusterWidth) this.x = clusterWidth - this.width;
        }
    }
}
