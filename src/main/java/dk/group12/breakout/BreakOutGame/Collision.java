package dk.group12.breakout.BreakOutGame;

import dk.group12.breakout.BreakoutGraphical;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

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
                        // we want a direction vector that changes the angle of the ball
                        // depending on where it hits the platform
                        double midPoint = object.x + object.width / 2;
                        double normal = (ball.x - midPoint) / (object.width / 2);
                        ball.direction = new Vec2(normal, -1, ball.direction.getScalar());

                    } else {
                        ball.direction = calculateNewTrajectory(
                                getNormalVectorOfCollision(object, ball),
                                ball);

                        // we want to update the block hp and score if the object is a block in our cluster
                        if (object instanceof GameState.Block) {
                            ((GameState.Block) object).hp--;
                            BreakoutGraphical.scoreTracker.addScore(1);
                        }

                    }
                    SoundController.playPing();
                    // we break the loop here because we only want to handle one collision at each frame
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

        // Distance from balls mid-point to the object sides
        double distanceLeft = Math.abs(ball.x - left);
        double distanceRight = Math.abs(ball.x - right);
        double distanceTop = Math.abs(ball.y - top);
        double distanceBottom = Math.abs(ball.y - bottom);


        // Top and bottom collision
        if ((distanceLeft < distanceTop && distanceLeft < distanceBottom) ||
                (distanceRight < distanceTop && distanceRight < distanceBottom)) {
            return new Vec2(-1, 1, 1);
        } else {
            return new Vec2(1, -1, 1);
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
            return intersectCircleRectangle((GameState.Ball) collidingObject, object);
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
