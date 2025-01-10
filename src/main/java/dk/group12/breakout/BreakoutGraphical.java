package dk.group12.breakout;

import dk.group12.breakout.BreakOutGame.CollisionElement;
import dk.group12.breakout.BreakOutGame.GameLoop;
import dk.group12.breakout.BreakOutGame.GameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class BreakoutGraphical extends Application {
    private final int windowX = 400;
    private final int windowY = 700;
    private GameLoop gameLoop;
    private static int n;
    private static int m;
    private static int lives;

    public static void main(String[] args) {
        int arg1 = 8;
        int arg2 = 8;
        int arg3 = 1;
        try {
            arg1 = Integer.parseInt(args[0]);
            arg2 = Integer.parseInt(args[1]);
            arg3 = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Default to 8x8, use 2 arguments to change the size of the game board.");
        }
        n = arg1;
        m = arg2;
        lives = arg3;
        launch();
    }

    // Track pressed keys
    private final Set<String> activeKeys = new HashSet<>();

    @Override
    public void start(Stage stage) {
        gameLoop = new GameLoop(n, m, windowX, windowY, lives);
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Breakout");

        Canvas canvas = new Canvas(windowX, windowY);
        root.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // Input handling
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode().toString()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode().toString()));

        // Animation loop running at 60 FPS
        new AnimationTimer(){
            private long lastTime = System.nanoTime();
            private int frameCount = 0;
            long previousTime = 0;
            public void handle(long currentNanoTime){
                if (previousTime == 0){
                    previousTime = currentNanoTime;
                }
                long elapsedTime = (currentNanoTime - previousTime) / 1_000_000;

                if (elapsedTime >= 13){ // 13 ms about 60 fps cap
                    frameCount ++;
                    gameLoop.handleInput(activeKeys);
                    gameLoop.update();
                    previousTime = currentNanoTime;
                }

                if ((currentNanoTime - lastTime) >= 1000000000) {
                    System.out.println("FPS: " + frameCount);
                    frameCount = 0;
                    lastTime = currentNanoTime;
                }

                // This is here to clear the screen from the previous frame
                graphicsContext.clearRect(0, 0, windowX, windowY);
                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillRect(0, 0, windowX, windowY);

                drawStaticElements(graphicsContext);
                drawPlatform(graphicsContext);
                drawBall(graphicsContext);
                drawBlocks(graphicsContext);

            }
        }.start();

        stage.show();
    }
    private void drawPlatform(GraphicsContext gc) {
        gc.setFill(Color.DEEPSKYBLUE);
        gc.fillRect(
                gameLoop.gameState.platform.x,
                gameLoop.gameState.platform.y,
                gameLoop.gameState.platform.width,
                gameLoop.gameState.platform.height);
    }

    private void drawBall(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(
                GameState.ball.x,
                GameState.ball.y,
                GameState.ball.radius * 2,
                GameState.ball.radius * 2
        );
    }
    private void drawStaticElements(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGREY);
        CollisionElement topWall = gameLoop.gameState.topWall;
        CollisionElement leftWall = gameLoop.gameState.leftWall;
        CollisionElement rightWall = gameLoop.gameState.rightWall;
        gc.fillRect(topWall.x, topWall.y, topWall.width, topWall.height);
        gc.fillRect(leftWall.x, leftWall.y, leftWall.width, leftWall.height);
        gc.fillRect(rightWall.x, rightWall.y, rightWall.width, rightWall.height);
    }

    private void drawBlocks(GraphicsContext gc) {
        Color[] rainbow = new Color[]{
                Color.RED,
                Color.ORANGE,
                Color.GREEN,
                Color.YELLOW,
                Color.BLUE,
                Color.INDIGO,
                Color.VIOLET
        };
        GameState.BlockCluster cluster = gameLoop.gameState.blockCluster;
        for (int i = 0; i < cluster.cluster.length; i++) {
            gc.setFill(rainbow[(i / 2) % rainbow.length]);
            for (int j = 0; j < cluster.cluster[i].length; j++) {
                GameState.Block block = cluster.cluster[i][j];
                if (block.hp > 0) {
                    gc.fillRect(block.x, block.y, block.width, block.height);
                }
            }
        }
    }
}