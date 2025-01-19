package dk.group12.breakout.BreakOutGame;

import javafx.geometry.Point2D;

import java.util.*;

public class GameState {
    public Platform platform;
    public List<Ball> ballList;
    public BlockCluster blockCluster;
    public CollisionElement topWall;
    public CollisionElement leftWall;
    public CollisionElement rightWall;
    public boolean gameRunning = false;
    public boolean gameEnded = false;
    public double ballSpeed;
    public static double ballSpeedDifficultyMultiplier = 1.0;
    public static double platformWidthDifficultyMultiplier = 1.0;
    private int lives;
    private final int gameWidth;
    private final int gameHeight;
    public List<CollisionElement> collisionElements;
    public PowerUpHandler powerUpHandler;
    public Random randomGenerator = new Random(432234);
    public ScoreTracker scoreTracker; // Tracks the score
    public boolean gameWon = false;
    public ScoreHistory scoreHistory;



    public GameState(int n, int m, int gameWidth, int gameHeight, int lives, ScoreTracker scoreTracker) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.lives = lives;
        this.scoreTracker = scoreTracker;
        this.scoreHistory = new ScoreHistory();
        int platformWidth = (int) ((gameWidth / 6.) * platformWidthDifficultyMultiplier);
        int platformX = (gameWidth - platformWidth) / 2;
        int platformHeight = 10;
        int platformY = (int) (gameHeight - platformHeight - gameHeight * 0.05);
        platform = new Platform(platformX, platformY, platformWidth, platformHeight);

        topWall = new CollisionElement(0, 20, gameWidth, 10);
        leftWall = new CollisionElement(-100, -gameHeight, 100, gameHeight * 2);
        rightWall = new CollisionElement(gameWidth, -gameHeight, 100, gameHeight * 2);


        // ball speed is proportional to the game height
        ballSpeed = ((gameHeight / 200.) * (1 + scoreTracker.getScore() / 600.)) * ballSpeedDifficultyMultiplier;


        // we want to add the ball right on top of the platform
        ballList = new ArrayList<>();
        double radius = 0.015*((double) (gameWidth + gameHeight) / 2);
        double x = platform.x + platform.width / 2 - radius;
        double y = platform.y - radius * 2;
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
        ballList.get(0).direction = new Vec2((randomGenerator.nextDouble() - 0.5) * 2, -1, ballSpeed);
    }
    public void endGame() {
        // if gameEnded is called several times, we only want to end the game once.
        if (gameEnded) {
            return;
        }

        ballList.get(0).direction = new Vec2(0, -1, 0);
        gameRunning = false;
        gameEnded = true;

        // get the score and add it to the history
        int finalScore = scoreTracker.getScore();
        scoreHistory.addScore(finalScore);

        scoreHistory.saveToFile("scores.txt");

        scoreHistory.printScores();
    }

    // Reset the ball and platform after losing a life
    public void resetBallAndPlatform() {
        platform.x = (gameWidth - platform.width) / 2;
        ballList.get(0).resetPosition();
    }

    public void update() {
        Collision.otherCollisionCheck(this);
        removeDestroyedBlocks();

        for (Ball ball : ballList) {
            ball.move(this);
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


        public void move(GameState gameState) {
            for (double i = 0; i < 10; i++) {
                this.x += (direction.getX() * direction.getScalar()) / 10;
                this.y += (direction.getY() * direction.getScalar()) / 10;
                Collision.ballCollisionCheck(gameState, this);
                removeDestroyedBlocks();
            }
        }
    }

    public void spawnAdditionalBall() {
        Ball newBall = new Ball(platform.x + platform.width / 2 - ballList.get(0).radius,
                platform.y - ballList.get(0).radius * 2,
                ballList.get(0).radius);
        newBall.direction = new Vec2((randomGenerator.nextDouble() - 0.5) * 2, -1, ballSpeed);
        ballList.add(newBall);
    }

    public void updateBallSpeed() {
        ballSpeed = ((gameHeight / 200.) * (1 + scoreTracker.getScore() / 600.)) * ballSpeedDifficultyMultiplier;
        for (Ball ball : ballList) {
            ball.direction.setScalar(ballSpeed);
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

        public void updateWidth(Map<powerUpType, PowerUpHandler.PowerUp> activePowerUps) {
            double midPoint = this.x + this.width / 2;
            if (activePowerUps.containsKey(powerUpType.WIDEN_PLATFORM)) {
                this.width = (int) ((gameWidth / 6.) * platformWidthDifficultyMultiplier * 1.5);
            } else {
                this.width = (int) ((gameWidth / 6.) * platformWidthDifficultyMultiplier);
            }
            this.x = (int) (midPoint - this.width / 2);
        }
    }
}
