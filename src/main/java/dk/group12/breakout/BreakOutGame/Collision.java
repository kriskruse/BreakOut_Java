package dk.group12.breakout.BreakOutGame;

import java.util.List;

public class Collision {

    public static void collisionCheck(GameState gameState){
        GameState.Ball ball = GameState.ball;
        List<CollisionElement> collisionElements = gameState.collisionElements;

        for (CollisionElement object : collisionElements) {
            if (isCollidingWithObject(object, ball)) {
                if(object instanceof GameState.Platform){
                    double midPoint = object.x + object.width / 2;
                    double normal = (ball.x - midPoint) / (object.width / 2) ;

                    GameState.ball.direction = new Vec2(normal, -1, ball.direction.getScalar());
                }else{
                    GameState.ball.direction = calculateNewTrajectory(
                            getNormalVectorOfCollision(object, ball),
                            ball);

                    if (object instanceof GameState.Block) {
                        ((GameState.Block) object).hp--;
                    }

                }
                // we break the loop here because we only want to handle one collision at a time
                break;
            }
        }
    }

    private static Vec2 calculateNewTrajectory(Vec2 normalVectorOfCollision, GameState.Ball ball) {
        Vec2 direction = ball.direction;
        return new Vec2(direction.getX() * normalVectorOfCollision.getX() ,
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

        // figure out which side the ball is colliding with
        double overlapLeft = right - ballLeft;
        double overlapRight = ballRight - left;
        double overlapTop = bottom - ballTop;
        double overlapBottom = ballBottom - top;

        // left and right side collision
        if ((overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom) ||
                (overlapRight < overlapTop && overlapRight < overlapBottom)) {
            return new Vec2(-1, 1, 1);
        }// top and bottom side collision
        else {
            return new Vec2(1, -1, 1);
        }
    }

    private static boolean isCollidingWithObject(CollisionElement object, GameState.Ball ball){
        // find the bounding box of the object
        double left = object.x;
        double top = object.y;
        double right = object.x + object.width;
        double bottom = object.y + object.height;

        // this is a little hacky, but making the bounding box of the ball a little bigger
        // the collisions look a little better
        double ballLeft = ball.x - ball.radius * 2;
        double ballTop = ball.y - ball.radius * 2;
        double ballRight = ball.x + ball.radius * 2;
        double ballBottom = ball.y + ball.radius * 2;

        return  (left < ballRight && ballLeft < right && top < ballBottom && ballTop < bottom);
    }
}

