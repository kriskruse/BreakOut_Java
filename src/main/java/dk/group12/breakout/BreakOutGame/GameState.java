package dk.group12.breakout.BreakOutGame;

public class GameState {
    public Platform platform;
    public Ball ball;
    public BlockCluster blockCluster;
    public StaticElements topWall, leftWall, rightWall;
    public boolean gameRunning = false;
    public boolean gameEnded = false;
    private final int gameHeight;

    public GameState(int n, int m, int gameWidth, int gameHeight) {
        this.gameHeight = gameHeight;
        int platformWidth = gameWidth / 10;
        int platformX = (gameWidth - platformWidth) / 2;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);

        topWall = new StaticElements(0, 20, gameWidth, 10);
        leftWall = new StaticElements(0, 0, 10, gameHeight);
        rightWall = new StaticElements(gameWidth - 10, 0, 10, gameHeight);

        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x + platform.width / 2, platform.y - 10, 5);

        int clusterWidth = (int) (gameWidth - leftWall.width - rightWall.width);
        int clusterHeight = (int) ((gameHeight - (topWall.x + topWall.height)) * 0.25);
        blockCluster = new BlockCluster(n, m, clusterWidth, clusterHeight);
    }
    public void startGame() {
        gameRunning = true;
        ball.direction = new Ball.Vec2((Math.random() - 0.5) * 2, -1, 3);
    }
    public void endGame() {
        ball.direction = new Ball.Vec2(0, 0, 0);
        System.out.println("Game Ended");
        gameRunning = false;
        gameEnded = true;
    }

    public void update() {
        ball.move();
        blockCluster.update(getCollision(ball));
    }

    public Collision getCollision(Ball ball) {
        // TODO Auto-generated method stub
        return null;
    }

    public static class Block {
        public double x, y, width, height;
        public int hp = 1;

        public Block(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public class BlockCluster {
        public Block[][] cluster;
        double width,height;
        double spacing;
        final double spacingOffset = 0.01;

        public BlockCluster(int n, int m, double clusterWidth, double clusterHeight) {
            cluster = new Block[n][m];

            // Calculate block dimensions
            spacing = clusterWidth * spacingOffset;
            width = (clusterWidth - (m + 1) * spacing) / m;
            height = (clusterHeight - (n + 1) * spacing) / n;

            // Populate the cluster with blocks
            for (int i = 0; i < n; i++) {
                double top = topWall.y + topWall.height;
                double verticalOffset = (gameHeight - top) * 0.15;
                double y = top + verticalOffset + (i * (height + spacing)) + spacing; // Add vertical spacing
                for (int j = 0; j < m; j++) {
                    double x = (leftWall.x + leftWall.width) + spacing + (j * (width + spacing)) ; // Add horizontal spacing
                    cluster[i][j] = new Block(x, y, width, height);
                }
            }
        }

        public void update(Collision collision) {
            // TODO Auto-generated method stub
        }
    }

    public static class Ball {
        public double x, y;
        public Vec2 direction;
        public int radius;

        public Ball(double x, double y, int radius) {
            this.x = x;
            this.y = y;
            this.radius=radius;
            direction = new Vec2(0, 0, 0);
        }

        public void move() {
            this.x += direction.x * direction.scalar;
            this.y += direction.y * direction.scalar;
        }

        public static class Vec2 {
            public double x, y;
            public double scalar;

            public Vec2(double x, double y, double scalar) {
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

            if (this.x < leftWall.x + leftWall.width) this.x = leftWall.x + leftWall.width;
            if (this.x + this.width > rightWall.x) this.x = rightWall.x - this.width;
        }
    }

    public static class StaticElements {
        public double x, y, width, height;
        public StaticElements(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
