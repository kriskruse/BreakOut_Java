package dk.group12.breakout.BreakOutGame;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.List;

public class Collision {
    public static List<GameState.Ball> ballList;

    public static void otherCollisionCheck(GameState gameState) {
        ballList = gameState.ballList;

        List<PowerUpHandler.PowerUp> fallingPowerUps = gameState.powerUpHandler.fallingPowerUps;

        for (PowerUpHandler.PowerUp powerUp : fallingPowerUps) {
            if (isCollidingWithObject(gameState.platform, powerUp)) {
                powerUp.activate();
            }
        }

    }

    public static void ballCollisionCheck(GameState gameState, GameState.Ball ball) {
        List<CollisionElement> collisionElements = gameState.collisionElements;

        for (CollisionElement object : collisionElements) {
            if (isCollidingWithObject(object, ball)) {
                if (object instanceof GameState.Platform) {
                    // we want a direction vector that changes the angle of the ball
                    // depending on where it hits the platform
                    double midPoint = object.x + object.width / 2;
                    double ballMid = ball.x + ball.radius;
                    double normal = (ballMid - midPoint) / (object.width / 2);
                    ball.direction = new Vec2(normal, -1, ball.direction.getScalar());

                } else {
                    handleCollisionWithObject(object, ball);
                    // we want to update the block hp and score if the object is a block in our cluster
                    if (object instanceof GameState.Block) {
                        ((GameState.Block) object).hp--;
                        gameState.scoreTracker.addScore(1);
                    }

                }
                SoundController.playPing();
                // we break the loop here because we only want to handle one collision at each frame
                return;
            }

        }


    }

    private static void handleCollisionWithObject(CollisionElement object, GameState.Ball ball) {
        double left = object.x;
        double right = object.x + object.width;
        double top = object.y;
        double bottom = object.y + object.height;

        // Distance from balls mid-point to the object sides
        double distanceLeft = Math.abs(ball.x - left);
        double distanceRight = Math.abs(ball.x - right);
        double distanceTop = Math.abs(ball.y - top);
        double distanceBottom = Math.abs(ball.y - bottom);

        // Left
        if (distanceLeft < distanceRight && distanceLeft < distanceTop && distanceLeft < distanceBottom) {
            ball.direction.setX(-Math.abs(ball.direction.getX()));
        } else if (distanceRight < distanceLeft && distanceRight < distanceTop && distanceRight < distanceBottom) {
            ball.direction.setX(Math.abs(ball.direction.getX()));
        } else if (distanceTop < distanceLeft && distanceTop < distanceRight && distanceTop < distanceBottom) {
            ball.direction.setY(-Math.abs(ball.direction.getY()));
        } else {
            ball.direction.setY(Math.abs(ball.direction.getY()));
        }

    }

    private static boolean isCollidingWithObject(CollisionElement object, CollisionElement collidingObject) {
        // Bounding box of the object
        double left = object.x;
        double top = object.y;
        double right = object.x + object.width;
        double bottom = object.y + object.height;

        double collidingObjectLeft;
        double collidingObjectTop;
        double collidingObjectRight;
        double collidingObjectBottom;

        // Bounding box of the ball
        if (collidingObject instanceof GameState.Ball) {
            return intersectCircleRectangle((GameState.Ball) collidingObject, object);
        } else {
            collidingObjectLeft = collidingObject.x;
            collidingObjectTop = collidingObject.y;
            collidingObjectRight = collidingObject.x + collidingObject.width;
            collidingObjectBottom = collidingObject.y + collidingObject.height;
        }


        return (left < collidingObjectRight &&
                collidingObjectLeft < right &&
                top < collidingObjectBottom &&
                collidingObjectTop < bottom);
    }

    private static boolean intersectCircleRectangle(GameState.Ball ball, CollisionElement rectangle) {
        Point2D ballCenter = new Point2D(ball.x + ball.radius, ball.y + ball.radius);
        Rectangle2D rect = new Rectangle2D(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        Point2D A = new Point2D(rect.getMinX(), rect.getMinY());
        Point2D B = new Point2D(rect.getMaxX(), rect.getMinY());
        Point2D C = new Point2D(rect.getMinX(), rect.getMaxY());
        Point2D D = new Point2D(rect.getMaxX(), rect.getMaxY());

        return rect.contains(ballCenter) ||
                intersectCircle(ball, A, B) ||
                intersectCircle(ball, B, D) ||
                intersectCircle(ball, D, C) ||
                intersectCircle(ball, C, A);
    }

    private static boolean intersectCircle(GameState.Ball ball, Point2D p1, Point2D p2) {
        Point2D ballCenter = new Point2D(ball.x + ball.radius, ball.y + ball.radius);
        Point2D v = p2.subtract(p1);
        Point2D w = ballCenter.subtract(p1);

        double c1 = w.dotProduct(v);
        if (c1 <= 0) {
            return w.magnitude() <= ball.radius;
        }

        double c2 = v.dotProduct(v);
        if (c2 <= c1) {
            return ballCenter.subtract(p2).magnitude() <= ball.radius;
        }

        double b = c1 / c2;
        Point2D pb = p1.add(v.multiply(b));
        return ballCenter.subtract(pb).magnitude() <= ball.radius;
    }
}
