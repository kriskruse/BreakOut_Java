package dk.group12.breakout.BreakOutGame;

public class Collision {

    // Method to check and handle collision with walls
    public static void WallCollision(GameState gameState) {
        GameState.Ball ball = gameState.ball;
        // Left and right walls collision
        if (ball.x - ball.radius <= gameState.leftWall.x + gameState.leftWall.width ||
                ball.x + ball.radius >= gameState.rightWall.x) {
            ball.direction.x *= -1; // Reflect the ball horizontally
        }

        // Top wall collision
        if (ball.y - ball.radius <= gameState.topWall.y + gameState.topWall.height) {
            ball.direction.y *= -1; // Reflect the ball vertically
        }
    }

    // Method to check and handle collision with the platform
    public static void PlatformCollision(GameState gameState) {
        GameState.Ball ball = gameState.ball;
        GameState.Platform platform = gameState.platform;

        // Check if the ball is in contact with the platform
        if (ball.y + ball.radius >= platform.y && ball.y - ball.radius <= platform.y + platform.height) {
            // Check if the ball is horizontally over the platform
            if (ball.x + ball.radius >= platform.x && ball.x - ball.radius <= platform.x + platform.width) {
                // Calculate the relative position of the ball on the platform
                double platformCenterX = platform.x + platform.width / 2.0; // the center of the platform
                // the distance from the center of the platform
                double distanceFromCenterX = (ball.x - platformCenterX) / (platform.width / 2.0);
                // normalising the length of the platform such that it is between -1 to 1. this makes it possible for us
                // to give the player some power up to make the platform bigger without disturbing the program
                //(platform.width / 2.0) is used to normalise it
                ball.y = platform.y - ball.radius;
                ball.direction.x = 2 * distanceFromCenterX;
                ball.direction.y *= -1;
                // We change the direction of the ball to the normalised distance of the platform. Max 30 degrees
            }
        }
    }

    // Method to check and handle collision with blocks
    public static void BlockCollision(GameState gameState) {
        GameState.Ball ball = gameState.ball;
        GameState.BlockCluster blockCluster = gameState.blockCluster;

        for (int i = 0; i < blockCluster.cluster.length; i++) {
            for (int j = 0; j < blockCluster.cluster[i].length; j++) {
                GameState.Block block = blockCluster.cluster[i][j];

                // Check if the block exists (hp > 0)
                if (block.hp > 0) {
                    // Check if the ball intersects with the block
                    if (isBallIntersectingBlock(ball, block)) {
                        // Determine which edge was hit (based on distances to edges)
                        double overlapTop = calculateOverlap(ball.y, block.y); // Distance to top edge
                        double overlapBottom = calculateOverlap(ball.y, block.y + block.height); // Distance to bottom edge
                        double overlapLeft = calculateOverlap(ball.x, block.x); // Distance to left edge
                        double overlapRight = calculateOverlap(ball.x, block.x + block.width); // Distance to right edge

                        // Check which edge the ball is closest to and reflect appropriately
                        if (overlapTop < overlapBottom && overlapTop < overlapLeft && overlapTop < overlapRight) {
                            ball.y = block.y - ball.radius;
                            ball.direction.y *= -1; // Top edge reflection
                        } else if (overlapBottom < overlapTop && overlapBottom < overlapLeft && overlapBottom < overlapRight) {
                            ball.y = block.y + block.height + ball.radius;
                            ball.direction.y *= -1; // Bottom edge reflection
                        } else if (overlapLeft < overlapRight) {
                            ball.x = block.x - ball.radius;
                            ball.direction.x *= -1; // Left edge reflection
                        } else {
                            ball.x = block.x + block.width + ball.radius;
                            ball.direction.x *= -1; // Right edge reflection
                        }

                        // Decrease the block's hp and destroy it if necessary
                        block.hp--;
                    }
                }
            }
        }
    }

    // Helper function to check ball-block intersection
    private static boolean isBallIntersectingBlock(GameState.Ball ball, GameState.Block block) {
        return ball.x + ball.radius > block.x && ball.x - ball.radius < block.x + block.width &&
                ball.y + ball.radius > block.y && ball.y - ball.radius < block.y + block.height;
    }

    // Helper function to calculate overlap distance
    private static double calculateOverlap(double point, double edge) {
        return Math.abs(point - edge);
    }
}

