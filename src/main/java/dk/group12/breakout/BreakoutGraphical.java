package dk.group12.breakout;

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
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

public class BreakoutGraphical extends Application {
    private final int windowX = 400;
    private final int windowY = 700;
    private GameLoop gameLoop;

    // Track pressed keys
    private Set<String> activeKeys = new HashSet<>();

    @Override
    public void start(Stage stage) throws IOException {
        gameLoop = new GameLoop(8, 8, windowX, windowY);
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Breakout");

        Canvas canvas = new Canvas(windowX, windowY);
        root.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        final long startNanoTime = System.nanoTime();

        // Input handling
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode().toString()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode().toString()));

        // Animation loop running at 60 FPS
        new AnimationTimer(){
            private long lastTime = System.nanoTime();
            private int frameCount = 0;
            public void handle(long currentNanoTime){
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                frameCount ++;
                if ((currentNanoTime - lastTime) >= 1000000000) {
                    System.out.println("FPS: " + frameCount);
                    frameCount = 0;
                    lastTime = currentNanoTime;
                }

                gameLoop.handleInput(activeKeys);
                gameLoop.update();


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
        gc.setFill(Color.LIGHTBLUE);
        GameState.Platform p = gameLoop.gameState.platform;
        gc.fillRect(p.x, p.y, p.width, p.height);
    }

    private void drawBall(GraphicsContext gc) {
        gc.setFill(Color.RED);
        GameState.Ball b = gameLoop.gameState.ball;
        gc.fillOval(
                b.x - b.radius,
                b.y - b.radius,
                b.radius * 2,
                b.radius * 2
        );
    }
    private void drawStaticElements(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        GameState.StaticElements topWall = gameLoop.gameState.topWall;
        GameState.StaticElements leftWall = gameLoop.gameState.leftWall;
        GameState.StaticElements rightWall = gameLoop.gameState.rightWall;
        gc.fillRect(topWall.x, topWall.y, topWall.width, topWall.height);
        gc.fillRect(leftWall.x, leftWall.y, leftWall.width, leftWall.height);
        gc.fillRect(rightWall.x, rightWall.y, rightWall.width, rightWall.height);
    }

    private void drawBlocks(GraphicsContext gc) {
        Color[] rainbow = new Color[]{
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE,
                Color.INDIGO,
                Color.VIOLET
        };
        GameState.BlockCluster cluster = gameLoop.gameState.blockcluster;
        for (int i = 0; i < cluster.cluster.length; i++) {
            gc.setFill(rainbow[i % rainbow.length]);
            for (int j = 0; j < cluster.cluster[i].length; j++) {
                GameState.Block block = cluster.cluster[i][j];
                if (block.hp > 0) {
                    gc.fillRect(block.x, block.y, block.width, block.height);
                }
            }
        }
    }
}