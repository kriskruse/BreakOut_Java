package dk.group12.breakout.BreakOutGame;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    public static List<GameState.Ball> ballList;

    public static void collisionCheck(GameState gameState){
        ballList = GameState.ballList;

        List<CollisionElement> collisionElements = gameState.collisionElements;
        List<PowerUpHandler.PowerUp> fallingPowerUps = gameState.powerUpHandler.fallingPowerUps;

        for (PowerUpHandler.PowerUp powerUp : fallingPowerUps) {
            if (isCollidingWithObject(GameState.platform, powerUp)) {
                powerUp.activate();
            }
        }

        for (CollisionElement object : collisionElements) {
            for (GameState.Ball ball : ballList) {
                if (isCollidingWithObject(object, ball)) {
                    if (object instanceof GameState.Platform) {
                        double midPoint = object.x + object.width / 2;
                        double normal = (ball.x - midPoint) / (object.width / 2);

                        ball.direction = new Vec2(normal, -1, ball.direction.getScalar());
                    } else {
                        ball.direction = calculateNewTrajectory(
                                getNormalVectorOfCollision(object, ball),
                                ball);

                        if (object instanceof GameState.Block) {
                            ((GameState.Block) object).hp--;
                        }

                    }
                    SoundController.playPing();
                    // we break the loop here because we only want to handle one collision at a time
                    break;
                }
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

    private static boolean isCollidingWithObject(CollisionElement object, CollisionElement collidingObject) {
        // Bounding box of the object
        double left = object.x;
        double top = object.y;
        double right = object.x + object.width;
        double bottom = object.y + object.height;

        double collidingObejctLeft;
        double collidingObejctTop;
        double collidingObejctRight;
        double collidingObejctBottom;

        // Bounding box of the ball
        if (collidingObject instanceof GameState.Ball) {
            collidingObejctLeft = collidingObject.x;
            collidingObejctTop = collidingObject.y;
            collidingObejctRight = collidingObject.x + ((GameState.Ball) collidingObject).radius * 2;
            collidingObejctBottom = collidingObject.y + ((GameState.Ball) collidingObject).radius * 2;
        }
        else {
            collidingObejctLeft = collidingObject.x;
            collidingObejctTop = collidingObject.y;
            collidingObejctRight = collidingObject.x + collidingObject.width;
            collidingObejctBottom = collidingObject.y + collidingObject.height;
        }


        return (left < collidingObejctRight &&
                collidingObejctLeft < right &&
                top < collidingObejctBottom &&
                collidingObejctTop < bottom);
    }

}
