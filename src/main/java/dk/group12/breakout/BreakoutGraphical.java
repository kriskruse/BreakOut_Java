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
    private int windowx = 400;
    private int windowy = 700;
    private GameLoop gameLoop;
    private GameState gameState;

    // Track pressed keys
    private Set<String> activeKeys = new HashSet<>();

    @Override
    public void start(Stage stage) throws IOException {
        gameLoop = new GameLoop(8, 8, windowx, windowy);
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Breakout");

        Canvas canvas = new Canvas(windowx, windowy);
        root.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        final long startNanoTime = System.nanoTime();

        // Input handling
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode().toString()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode().toString()));

        // Animation loop running at 60 FPS
        new AnimationTimer(){
            public void handle(long currentNanoTime){
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                System.out.println("Animation loop running...");

                // Pass active keys to game loop
                gameLoop.handleInput(activeKeys);

                // Update what to draw
                gameState = gameLoop.update();

                // DRAW ----

                // Draw platform
                // drawPlatform(gameState)
                // ||
                // drawController.drawPlatform(x,y,width,Height)

                // draw ball
                // drawBall(gameState)
                // ||
                // drawBall(x,y,width,Height, Color)

                // draw Boxes
                // for all boxes
                // drawBoxes(gameState)
                // ||
                // drawBox(x, y, width, height, color)

                // used to clear the screen from the previous frame
                graphicsContext.clearRect(0, 0, windowx, windowy);
                // set color black
                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillRect(10, 0, windowx * 0.015, windowy);
                graphicsContext.fillRect(windowx - 10 - windowx * 0.015, 0, windowx * 0.015, windowy);
                graphicsContext.fillRect(0, windowy * 0.10, windowx, windowy * 0.008);
                System.out.println("Drawing graphics...");
            }
        }.start();

        stage.show();
    }
}