package dk.group12.breakout.BreakOutGame;

public class GameState {
    public Platform platform;
    public Ball ball;
    public BlockCluster blockcluster;
    public StaticElements topWall, leftWall, rightWall;
    private int clusterWidth, clusterHeight;

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


        this.clusterWidth = gameWidth - gameWidth / 10; // 90% of screen width
        this.clusterHeight = (int) (gameHeight * 0.25); // top 25% of screen

        // Init block cluster
        blockcluster = new BlockCluster(n, m, clusterWidth, clusterHeight);
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
        double spacing; // Adjust this value for desired spacing

        public BlockCluster(int n, int m, double clusterWidth, double clusterHeight) {
            cluster = new Block[n][m];

            // Define the spacing between blocks
            double horizontalSpacing = 5; // Space between blocks horizontally
            double verticalSpacing = 5;   // Space between blocks vertically

            // Calculate block dimensions
            width = (clusterWidth - (m - 1) * horizontalSpacing) / m;
            height = (clusterHeight - (n - 1) * verticalSpacing) / n;

            // Populate the cluster with blocks
            for (int i = 0; i < n; i++) {
                double y = i * (height + verticalSpacing); // Add vertical spacing
                for (int j = 0; j < m; j++) {
                    double x = j * (width + horizontalSpacing); // Add horizontal spacing
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

            // Initialize direction for testing
            direction = new vec2(0, -1, 5);
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

            if (this.x < 0) this.x = 0;
            if (this.x + this.width > clusterWidth) this.x = clusterWidth - this.width;
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
