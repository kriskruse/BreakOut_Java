package dk.group12.breakout.BreakOutGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState {
    public Platform platform;
    public static Ball ball;
    public BlockCluster blockCluster;
    public CollisionElement topWall, leftWall, rightWall;
    public boolean gameRunning = false;
    public boolean gameEnded = false;
    private int lives;
    private final int gameWidth;
    private final int gameHeight;
    public List<CollisionElement> collisionElements;

    public GameState(int n, int m, int gameWidth, int gameHeight, int lives) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.lives = lives;
        int platformWidth = gameWidth / 6;
        int platformX = (gameWidth - platformWidth) / 2;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);

        topWall = new CollisionElement(0, 20, gameWidth, 10);
        leftWall = new CollisionElement(0, 0, 10, gameHeight);
        rightWall = new CollisionElement(gameWidth - 10, 0, 10, gameHeight);

        // we want to add the ball right on top of the platform
        ball = new Ball(platform.x + platform.width / 2 - 5, platform.y - 20, 5);

        int clusterWidth = (int) (gameWidth - leftWall.width - rightWall.width);
        int clusterHeight = (int) ((gameHeight - (topWall.x + topWall.height)) * 0.25);
        blockCluster = new BlockCluster(n, m, clusterWidth, clusterHeight);

        collisionElements = new ArrayList<>();
        collisionElements.add(platform);
        collisionElements.add(topWall);
        collisionElements.add(leftWall);
        collisionElements.add(rightWall);
        collisionElements.addAll(flattenBlockCluster(blockCluster));
    }

    private List<Block> flattenBlockCluster(BlockCluster blockCluster) {
        List<Block> blocks = new ArrayList<>();
        for (Block[] row : blockCluster.cluster) {
            blocks.addAll(Arrays.asList(row));
        }
        return blocks;
    }


    public void startGame() {
        gameRunning = true;
        ball.direction = new Vec2((Math.random() - 0.5) * 2, -1, 3);
    }
    public void endGame() {
        ball.direction = new Vec2(0, -1, 0);
        System.out.println("Game Ended");
        gameRunning = false;
        gameEnded = true;
    }

    // Reset the ball and platform after losing a life
    public void resetBallAndPlatform() {
        platform.x = (gameWidth - platform.width) / 2;  // Reset platform to center
        ball.x = platform.x + platform.width / 2;  // Reset ball to above the platform
        ball.y = platform.y - ball.radius;  // Position the ball just above the platform
        ball.direction = new Vec2((Math.random() - 0.5) * 2, -1, 3);  // Random initial ball direction
    }

    public void update() {
        ball.move();
        Collision.collisionCheck(this);
        removeDestroyedBlocks();

        // Check if the ball has crossed the bottom
        if (ball.y - ball.radius > gameHeight) {
            lives--;  // Decrease a life
            if (lives <= 0) {
                endGame();  // End the game if no lives are left
            } else {
                resetBallAndPlatform();  // Reset the ball and platform if lives remain
            }
        }
    }

    private void removeDestroyedBlocks() {
        collisionElements.removeIf(element ->
                element instanceof Block && ((Block) element).hp == 0);
    }


    public static class Block extends CollisionElement {
        public int hp = 1;

        public Block(double x, double y, double width, double height) {
            super(x, y, width, height);
        }
    }

    public class BlockCluster {
        public Block[][] cluster;
        int width,height;
        int spacing;
        final double spacingOffset = 0.01;

        public BlockCluster(int n, int m, int clusterWidth, int clusterHeight) {
            cluster = new Block[n][m];

            // Calculate block dimensions
            spacing = (int) (clusterWidth * spacingOffset);
            width = (clusterWidth - (m + 1) * spacing) / m;
            height = (clusterHeight - (n + 1) * spacing) / n;

            // Populate the cluster with blocks
            for (int i = 0; i < n; i++) {
                double top = topWall.y + topWall.height;
                int verticalOffset = (int) ((gameHeight - top) * 0.15);
                double y = top + verticalOffset + (i * (height + spacing)) + spacing; // Add vertical spacing
                for (int j = 0; j < m; j++) {
                    double x = (leftWall.x + leftWall.width) + spacing + (j * (width + spacing)) ; // Add horizontal spacing
                    cluster[i][j] = new Block(x, y, width, height);
                }
            }
        }
    }

    public static class Ball extends CollisionElement {
        public Vec2 direction;
        public double radius;

        public Ball(double x, double y, double radius) {
            super(x, y, radius * 2, radius * 2);
            this.radius=radius;
            direction = new Vec2(0, -1, 0);
        }

        public void move() {
            this.x += direction.getX() * direction.getScalar();
            this.y += direction.getY() * direction.getScalar();
        }
    }

    public class Platform extends CollisionElement {
        public Platform(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public void move(int direction) {
            this.x += direction;

            if (this.x < leftWall.x + leftWall.width) this.x = leftWall.x + leftWall.width;
            if (this.x + this.width > rightWall.x) this.x = rightWall.x - this.width;
        }
    }
}
