package dk.group12.breakout.BreakOutGame;

public class GameState {
    public Platform platform;
    public Ball ball;
    public BlockCluster blockcluster;
    public StaticElements topWall, leftWall, rightWall;
    public boolean gameRunning = false;

    public GameState(int n, int m, int gameWidth, int gameHeight) {
        int platformWidth = gameWidth / 10;
        int platformX = gameWidth / 2 - platformWidth / 2;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);
        topWall = new StaticElements(0, 0, gameWidth, 10);
        leftWall = new StaticElements(0, 0, 10, gameHeight);
        rightWall = new StaticElements(gameWidth - 10, 0, 10, gameHeight);

        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x + platform.width / 2, platform.y - 10, 5);


        int clusterWidth = (int) (gameWidth*0.95 - leftWall.width - rightWall.width);
        int clusterHeight = (int) (gameHeight * 0.20 - topWall.y + topWall.height);

        blockcluster = new BlockCluster(n, m, clusterWidth, clusterHeight);
    }
    public void startGame() {
        gameRunning = true;
        ball.direction = new Ball.vec2((Math.random() - 0.5) * 2, -1, 1);
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

    public static class BlockCluster {
        public Block[][] cluster;
        double width,height;
        double spacing;
        final int offsetHeight = 10;
        final int offsetWidth = 20;
        final double spacingOffset = 0.02;

        public BlockCluster(int n, int m, double clusterWidth, double clusterHeight) {
            cluster = new Block[n][m];

            // Calculate block dimensions
            spacing = Math.min(clusterWidth, clusterHeight) * spacingOffset;
            width = (clusterWidth - (m - 1) * spacing) / m;
            height = (clusterHeight - (n - 1) * spacing) / n;

            // Populate the cluster with blocks
            for (int i = 0; i < n; i++) {
                double y = i * (height + spacing) + clusterHeight / offsetHeight; // Add vertical spacing
                for (int j = 0; j < m; j++) {
                    double x = j * (width + spacing) + clusterWidth / offsetWidth; // Add horizontal spacing
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
        public vec2 direction;
        public int radius;

        public Ball(double x, double y, int radius) {
            this.x = x;
            this.y = y;
            this.radius=radius;
            direction = new vec2(0, 0, 0);
        }

        public void move() {
            this.x += direction.x * direction.scalar;
            this.y += direction.y * direction.scalar;
        }

        public static class vec2 {
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
