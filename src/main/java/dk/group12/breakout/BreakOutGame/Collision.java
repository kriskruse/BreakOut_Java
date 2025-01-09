package dk.group12.breakout.BreakOutGame;

public class Collision {
    public boolean collision = false;
    public double x;
    public double y;
    public int lives = 1;



    // a method to check if the ball has collided with the platform
    public void platformCollision(GameState.Ball ball, GameState.Platform platform) {
        // if the ball hits the platform then two things happen:
        // 1. the ball will bounce back up which means it will go in the opposite direction of the y-axis
        // 2. the angle of which the ball will go is dependent on where the ball hits the platform
        // if the ball hits the platform on the left side then the ball will go to the left and vice versa

        // game coordinate system y= is at the top
        int bottomBall= (int) (ball.y + ball.radius);

        if (bottomBall >= platform.x){ // check if the bottom of the ball overlaps the top of the platform
            if (ball.x >= platform.x && ball.x <= platform.x + platform.width ){ // check to see if the ball is inside the interval of the platform width

                //Change the direction of y
                ball.direction.y=-ball.direction.y;

                double platformCenter = platform.x + platform.width / 2.0; // the center of the platform

                // the distance from the center of the platform
                double distanceFromCenter = (ball.x - platformCenter) / (platform.width / 2.0);
                // normalising the length of the platform så it is between -1 to 1. this makes it possible for us
                // to give the player som power up to make the platform bigger without disturbing the program
                //(platform.width / 2.0) is used to normalise it

                ball.direction.x = distanceFromCenter;
                // We change the direction of the ball to the normalised distance of the platform
            }

        }




    }

    // a method to check if the ball has collided with the walls and top
    public void wallCollision(GameState.Ball ball, GameState.StaticElements topWall, GameState.StaticElements leftWall, GameState.StaticElements rightWall) {

        double topBall = ball.y - Ball.radius;
        double leftSideBall = ball.x-Ball.radius;
        double rightSideBall = ball.x+Ball.radius;



        // Top wall
        if (topBall >= topWall.y + topWall.height) {
            ball.direction.y= -ball.direction.y;  // change the direction of the y direction (The opposite of what it originally was)

        }

        // Left wall
        if (leftSideBall <= leftWall.x + leftWall.width) {
            ball.direction.x = -ball.direction.x; // change the direction of the x direction (The opposite of what it originally was)

        }

        // right wall
        if (rightSideBall >= rightWall.x) {
            ball.direction.x = -ball.direction.x; // change the direction of the x direction (The opposite of what it originally was)

        }
    }



    // a method to check if the ball has collided with a block
    public void blockCollision(Ball ball, Block block) {

    }

    // a method to check if the ball has collided with the bottom wall
    public void bottomCollision(GameState.Ball ball, int gameHeight) {
        // if the ball hits the bottom wall then the player loses a life
        // the ball will be reset to the middle of the screen


        if (ball.y + GameState.Ball.ball.radius > GameState.StaticElements.height) { // when the ball is vertically lower than the gameheigth
            // reduce the number of lives
            lives--;

            // To reset the ball to the startpoint
            ball.x= GameState.StaticElements.width/2;
            ball.y= GameState.StaticElements.heigth/2;


            ball.direction.x=Math.random()*2-1; // 0<=Math.random<1 Bolden bliver skudt op med random vinkel
            ball.direction.y=-1; // The ball HAS to go up


            if (lives==0){
                GameState.endGame();
            }
        }


    }

    // method to check what type of collision the ball made, so is it a wall, platform or block etc.
    public CollisionType detectCollision(Ball ball, GameState gamestate){

        return null;
    }



    // method to check if the ball has collided with multiple objects at the same time
    public void multipleCollision(Ball ball, GameState gameState){
        // if the ball hits multiple objects at the same time

    }





}
