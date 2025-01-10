package dk.group12.breakout.BreakOutGame;

import java.util.List;

public class Collision2 {

    public static void collisionCheck(GameState gameState){
        GameState.Ball ball = GameState.ball;
        List<CollisionElement> collisionElements = gameState.collisionElements;

        for (CollisionElement object : collisionElements) {
            if (isCollidingWithObject(object, ball)) {
                GameState.ball.direction = calculateNewTrajectory(
                        getNormalVectorOfCollision(object, ball),
                        ball);

                if (object instanceof GameState.Block) {
                    ((GameState.Block) object).hp--;
                }
            }
        }
    }

    private static Vec2 calculateNewTrajectory(Vec2 normalVectorOfCollision, GameState.Ball ball) {
        Vec2 direction = ball.direction;
        return new Vec2(direction.x * normalVectorOfCollision.x ,
                direction.y * normalVectorOfCollision.y,
                direction.scalar * normalVectorOfCollision.scalar);
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

        // figure out which side the ball is colliding with
        double overlapLeft = right - ballLeft;
        double overlapRight = ballRight - left;
        double overlapTop = bottom - ballTop;
        double overlapBottom = ballBottom - top;

        // return the normalVector of the side the ball is colliding with
        if (overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom) {
            return new Vec2(-1, 1, 1);
        } else if (overlapRight < overlapTop && overlapRight < overlapBottom) {
            return new Vec2(1, 1, 1);
        } else if (overlapTop < overlapBottom) {
            return new Vec2(1, -1, 1);
        } else {
            return new Vec2(1, 1, 1);
        }
    }

    private static boolean isCollidingWithObject(CollisionElement object, GameState.Ball ball){
        // find the bounding box of the object
        double left = object.x;
        double top = object.y;
        double right = object.x + object.width;
        double bottom = object.y + object.height;

        double ballLeft = ball.x;
        double ballTop = ball.y;
        double ballRight = ball.x + ball.radius * 2;
        double ballBottom = ball.y + ball.radius * 2;

        return  (left < ballRight && ballLeft < right && top < ballBottom && ballTop < bottom);
    }
}

