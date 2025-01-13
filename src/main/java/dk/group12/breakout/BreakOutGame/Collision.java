package dk.group12.breakout.BreakOutGame;

import java.util.List;

public class Collision {

    public static void collisionCheck(GameState gameState){
        GameState.Ball ball = GameState.ball;
        List<CollisionElement> collisionElements = gameState.collisionElements;

        for (CollisionElement object : collisionElements) {
            // Skip power-up collision checks with the ball
            if (object instanceof GameState.PowerUp) {
                GameState.PowerUp powerUp = (GameState.PowerUp) object;
                // Check for platform collision with power-up
                if (checkPlatformCollision(gameState.platform, powerUp)) {
                    powerUp.activate();
                    // Trigger power-up effects here
                }
                continue; // Skip further processing for power-ups
            }

            if (isCollidingWithObject(object, ball)) {
                if(object instanceof GameState.Platform){
                    double midPoint = object.x + object.width / 2;
                    double normal = (ball.x - midPoint) / (object.width / 2);

                    GameState.ball.direction = new Vec2(normal, -1, ball.direction.getScalar());
                } else {
                    GameState.ball.direction = calculateNewTrajectory(
                            getNormalVectorOfCollision(object, ball),
                            ball);

                    if (object instanceof GameState.Block) {
                        ((GameState.Block) object).hp--;
                    }
                }
                // Handle one collision at a time
                break;
            }
        }
    }

    private static Vec2 calculateNewTrajectory(Vec2 normalVectorOfCollision, GameState.Ball ball) {
        Vec2 direction = ball.direction;
        return new Vec2(direction.getX() * normalVectorOfCollision.getX(),
                direction.getY() * normalVectorOfCollision.getY(),
                direction.getScalar() * normalVectorOfCollision.getScalar());
    }

    private static Vec2 getNormalVectorOfCollision(CollisionElement object, GameState.Ball ball) {
        double left = object.x;
        double right = object.x + object.width;
        double top = object.y;
        double bottom = object.y + object.height;

        double ballLeft = ball.x;
        double ballRight = ball.x + ball.radius * 2;
        double ballTop = ball.y;
        double ballBottom = ball.y + ball.radius * 2;

        // Determine which side the ball is colliding with
        double overlapLeft = right - ballLeft;
        double overlapRight = ballRight - left;
        double overlapTop = bottom - ballTop;
        double overlapBottom = ballBottom - top;

        // Top and bottom collision
        if ((overlapTop < overlapRight && overlapTop < overlapLeft) ||
                (overlapBottom < overlapRight && overlapBottom < overlapLeft)) {
            return new Vec2(1, -1, 1);
        } else {
            return new Vec2(-1, 1, 1);
        }
    }

    private static boolean isCollidingWithObject(CollisionElement object, GameState.Ball ball){
        // Bounding box of the object
        double left = object.x;
        double top = object.y;
        double right = object.x + object.width;
        double bottom = object.y + object.height;

        // Bounding box of the ball
        double ballLeft = ball.x;
        double ballTop = ball.y;
        double ballRight = ball.x + ball.radius * 2;
        double ballBottom = ball.y + ball.radius * 2;

        return (left < ballRight && ballLeft < right && top < ballBottom && ballTop < bottom);
    }

    private static boolean checkPlatformCollision(GameState.Platform platform, GameState.PowerUp powerUp) {
        double platformLeft = platform.x;
        double platformRight = platform.x + platform.width;
        double platformTop = platform.y;
        double platformBottom = platform.y + platform.height;

        double powerUpLeft = powerUp.x;
        double powerUpRight = powerUp.x + powerUp.width;
        double powerUpTop = powerUp.y;
        double powerUpBottom = powerUp.y + powerUp.height;

        return (platformLeft < powerUpRight &&
                platformRight > powerUpLeft &&
                platformTop < powerUpBottom &&
                platformBottom > powerUpTop);
    }
}
