package dk.group12.breakout.BreakOutGame;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameState {
    public Platform platform;
    public List<Ball> ballList;
    public BlockCluster blockCluster;
    public CollisionElement topWall;
    public CollisionElement leftWall;
    public CollisionElement rightWall;
    public boolean gameRunning = false;
    public boolean gameEnded = false;
    private int lives;
    private final int gameWidth;
    private final int gameHeight;
    public List<CollisionElement> collisionElements;
    public PowerUpHandler powerUpHandler;
    public Random randomGenerator = new Random(432234);
    public ScoreTracker scoreTracker; // Tracks the score
    public boolean gameWon = false;


    public GameState(int n, int m, int gameWidth, int gameHeight, int lives, ScoreTracker scoreTracker) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.lives = lives;
        this.scoreTracker = scoreTracker;
        int platformWidth = gameWidth / 6;
        int platformX = 1;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);

        topWall = new CollisionElement(0, 20, gameWidth, 10);
        leftWall = new CollisionElement(-100, 0, 100, gameHeight);
        rightWall = new CollisionElement(gameWidth, 0, 100, gameHeight);

        // we want to add the ball right on top of the platform
        ballList = new ArrayList<>();
        double radius = 0.015*((double) (gameWidth + gameHeight) / 2);
        double x = 140;
        double y = platformY - 174;
        ballList.add(new Ball(x, y - 1, radius));

        int clusterHeight = (int) (gameHeight * 0.25);
        blockCluster = new BlockCluster(n, m, gameWidth, clusterHeight);
        powerUpHandler = new PowerUpHandler(this);


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
        ballList.get(0).direction = new Vec2(-1, 1, 4);
    }
    public void endGame() {
        ballList.get(0).direction = new Vec2(0, -1, 0);
        gameRunning = false;
        gameEnded = true;
    }

    // Reset the ball and platform after losing a life
    public void resetBallAndPlatform() {
        platform.x = (gameWidth - platform.width) / 2;  // Reset platform to center
        ballList.get(0).resetPosition();  // Reset ball to original position
    }

    public void update() {
        Collision.collisionCheck(this);
        removeDestroyedBlocks();

        for (Ball ball : ballList) {
            ball.move();
        }

        powerUpHandler.movePowerUps();
        powerUpHandler.checkForCatch(gameHeight);
        powerUpHandler.handlePowerUpExpiration();

        // Check if the ball has crossed the bottom
        for (Ball ball : ballList) {
            if (ball.y - ball.radius > gameHeight) {
                if (ballList.size() == 1) {
                    lives--;
                    if (lives <= 0) {
                        endGame();  // End the game if no lives are left
                    } else {
                        resetBallAndPlatform();  // Reset the ball and platform if lives remain
                    }
                } else {
                    ballList.remove(ball);
                    break;
                }
            }
        }

    }

    private void removeDestroyedBlocks() {
        collisionElements.removeIf(element -> {
            if (element instanceof Block) {
                Block block = (Block) element;
                if (block.hp == 0) {
                    powerUpHandler.spawnPowerUp(block);
                    return true;
                }
            }
            return false;
        });
        // if all blocks are destroyed, spawn a new block cluster
        if (collisionElements.stream().noneMatch(element -> element instanceof Block)) {
            if (ballList.size() > 1) {
                ballList.subList(1, ballList.size()).clear();
            }
            gameRunning = false;
            gameWon = true;
        }
    }


    public class Block extends CollisionElement {
        public int hp = 1;
        public powerUpType powerUp = powerUpType.NONE;

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

    public void spawnNewBlockCluster(int n, int m) {
        int clusterHeight = (int) (gameHeight * 0.25);
        blockCluster = new BlockCluster(n, m, gameWidth, clusterHeight);
        collisionElements.addAll(flattenBlockCluster(blockCluster));
    }

    public enum powerUpType {
        NONE,
        WIDEN_PLATFORM,
        ENLARGE_BALL,
        MULTIBALL
    }


    public class Ball extends CollisionElement {
        public Vec2 direction;
        public double radius;
        Point2D originalPosition;

        public Ball(double x, double y, double radius) {
            super(x, y, radius * 2, radius * 2);
            this.originalPosition = new Point2D(x, y);
            this.radius=radius;
            direction = new Vec2(0, -1, 0);
        }

        public void resetPosition() {
            this.x = originalPosition.getX();
            this.y = originalPosition.getY();
            direction = new Vec2(0, -1, 0);
        }


        public void move() {
            this.x += direction.getX() * direction.getScalar();
            this.y += direction.getY() * direction.getScalar();
        }
    }

    public void spawnAdditionalBall() {
        Ball newBall = new Ball(platform.x + platform.width / 2 - ballList.get(0).radius,
                platform.y - ballList.get(0).radius * 2,
                ballList.get(0).radius);
        newBall.direction = new Vec2((randomGenerator.nextDouble() - 0.5) * 2, -1, 4);
        ballList.add(newBall);
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
