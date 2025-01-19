package dk.group12.breakout;

import dk.group12.breakout.BreakOutGame.CollisionElement;
import dk.group12.breakout.BreakOutGame.GameLoop;
import dk.group12.breakout.BreakOutGame.GameState;
import dk.group12.breakout.BreakOutGame.MenuController;
import dk.group12.breakout.BreakOutGame.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;


import java.util.HashSet;
import java.util.Set;

public class BreakoutGraphical extends Application {
    private int windowX;
    private int windowY;
    private GameLoop gameLoop;
    private static int n;
    private static int m;
    private static int lives;
    private SoundController soundController;
    private GraphicsContext graphicsContext;
    private MenuController menuController;
    private Label highScoreLabel;

    private int gameIterations = 0; // Tracks the number of iterations


    // Quick colorscheme (HUD)
    double hudOpacity = 0.7;
    Color hudOutlineColor = Color.WHITE;
    // PowerUps
    Color widenPlatformColor = Color.DEEPSKYBLUE;
    Color enlargeBallColor = Color.DARKORCHID;
    Color multiballColor = Color.AQUAMARINE;

    public static void main(String[] args) {
        int arg1 = 8;
        int arg2 = 8;
        //int arg3 = 1;
        try {
            arg1 = Integer.parseInt(args[0]);
            arg2 = Integer.parseInt(args[1]);
        //    arg3 = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Default to 8x8, use 2 arguments to change the size of the game board.");
        }
        n = arg1;
        m = arg2;
        lives = 1;


        launch();
    }

    // Track pressed keys
    private final Set<String> activeKeys = new HashSet<>();

    @Override
    public void start(Stage stage) {

        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
        windowY = (int) (screenHeight * 0.9);
        windowX = (int) (windowY * (7.0/9.0));


        //Create StackPane to layer menu scenes on top of Game scene
        StackPane root = new StackPane();

        Canvas canvas = new Canvas(windowX, windowY);
        //So "LEFT" adn "RIGHT" can be used also when there's buttons on screen
        canvas.setFocusTraversable(true); // Ensure the Canvas can receive focus
        canvas.requestFocus(); // Request focus explicitly

        root.getChildren().add(canvas);
        this.graphicsContext = canvas.getGraphicsContext2D();

        Scene gameScene = getScene(root);


        gameLoop = new GameLoop(n, m, windowX, windowY, lives);
        soundController = new SoundController();
        menuController = new MenuController(root, gameLoop, soundController);

        stage.setScene(gameScene);
        stage.setTitle("Breakout");
        stage.show();

        runGameLoop();
    }

    @NotNull
    private Scene getScene(StackPane root) {
        Scene gameScene = new Scene(root, windowX, windowY);

        // Input handling
        //esc button to pause game
        gameScene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode().toString());

            // Hide tutorial screen if visible and movement keys are pressed
            if (menuController.currentMenu == MenuController.MenuState.TUTORIAL_SCREEN) {
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.D ||
                        event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                    menuController.hideMenus(); // Hide the tutorial screen
                }
            }

            // ESC key behavior for pause/resume/start
            if (event.getCode() == KeyCode.ESCAPE) {
                if (menuController.gameStarted && menuController.gamePaused) {
                    menuController.resumeGame();
                    menuController.pauseButton.setVisible(true);
                    menuController.hideMenus();
                } else if (menuController.gameStarted && !menuController.gamePaused) {
                    menuController.pauseGame();
                } else {
                    menuController.showMenu(MenuController.MenuState.START_MENU);
                }
            }
        });
        gameScene.setOnKeyReleased(event -> activeKeys.remove(event.getCode().toString()));
        return gameScene;
    }


    /* GAME LOOP RUNNER*/
    public void runGameLoop(){
        // Animation loop running at ≈ 60 FPS
        new AnimationTimer() {
            private long lastTime = System.nanoTime();
            private int frameCount = 0;
            long previousTime = 0;

            public void handle(long currentNanoTime) {
                // Check if the game has ended
                if (gameLoop.gameEnded) {
                    menuController.gameEnded = true;
                    menuController.showMenu(MenuController.MenuState.GAME_OVER);
                    return;
                }
                if (gameIterations > 1 && !menuController.gameStarted) {
                    return;
                }
                // Check if the game is paused
                if (gameIterations > 1 && menuController.gamePaused) {
                    return; // Skip the rest of the frame's logic if gamePaused is true
                }


                if (previousTime == 0) {
                    previousTime = currentNanoTime;
                }
                long elapsedTime = (currentNanoTime - previousTime) / 1_000_000;

                if (elapsedTime >= 13) { // 13 ms about 60 fps cap
                    gameIterations++;
                    frameCount++;
                    soundController.playMusic();

                    gameLoop.handleInput(activeKeys);
                    gameLoop.update();
                    previousTime = currentNanoTime;
                    // This is here to clear the screen from the previous frame
                    graphicsContext.clearRect(0, 0, windowX, windowY);
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(0, 0, windowX, windowY);

                    drawStaticElements(graphicsContext);
                    drawPlatform(graphicsContext);
                    drawBall(graphicsContext);
                    drawBlocks(graphicsContext);
                    drawFallingPowerUps(graphicsContext);
                    drawActivePowerUps(graphicsContext);
                    drawScore(graphicsContext);
                    drawHighScore(graphicsContext);
                }

                if ((currentNanoTime - lastTime) >= 1000000000) {
                    System.out.println("FPS: " + frameCount);
                    frameCount = 0;
                    lastTime = currentNanoTime;
                }

            }
        }.start();
    }

    /* DRAW GAME STATE HELPER METHODS*/

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
        for (GameState.Ball ball : gameLoop.gameState.ballList) {
            gc.fillOval(
                    ball.x,
                    ball.y,
                    ball.radius * 2,
                    ball.radius * 2
            );
        }
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

    private void drawFallingPowerUps(GraphicsContext gc) {
        for (PowerUpHandler.PowerUp powerUp : gameLoop.gameState.powerUpHandler.fallingPowerUps) {
            if (powerUp.type == GameState.powerUpType.WIDEN_PLATFORM) {
                gc.setFill(widenPlatformColor); }
            if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
                gc.setFill(enlargeBallColor); }
            if (powerUp.type == GameState.powerUpType.MULTIBALL) {
                gc.setFill(multiballColor); }
            gc.fillOval(powerUp.x, powerUp.y, powerUp.width, powerUp.height);
        }
    }

    private void drawActivePowerUps(GraphicsContext gc) {
        gc.setFont(javafx.scene.text.Font.font("Press Start 2P", 12));
        gc.setLineWidth(0.75);

        int offsetX = 15;
        int initialOffsetY = 45;
        int spacingY = 0;

        if (gameLoop.gameState.ballList.size() > 1) {
            gc.setFill(Color.color(multiballColor.getRed(), multiballColor.getGreen(), multiballColor.getBlue(), hudOpacity));
            gc.setStroke(Color.color(hudOutlineColor.getRed(), hudOutlineColor.getGreen(), hudOutlineColor.getBlue(), hudOpacity));
            gc.fillText(
                    "MULTIBALL x" + gameLoop.gameState.ballList.size(),
                    offsetX,
                    initialOffsetY + spacingY
            );

            gc.strokeText(
                    "MULTIBALL x" + gameLoop.gameState.ballList.size(),
                    offsetX,
                    initialOffsetY + spacingY
            );

            spacingY += 20;
        }

        for (PowerUpHandler.PowerUp powerUp : gameLoop.gameState.powerUpHandler.activePowerUps.values()) {
            if (powerUp.duration > 0) {
                if (powerUp.type == GameState.powerUpType.WIDEN_PLATFORM) {
                    gc.setFill(Color.color(widenPlatformColor.getRed(),
                            widenPlatformColor.getGreen(),
                            widenPlatformColor.getBlue(),
                            hudOpacity));
                }
                if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
                    gc.setFill(Color.color(enlargeBallColor.getRed(),
                            enlargeBallColor.getGreen(),
                            enlargeBallColor.getBlue(),
                            hudOpacity));
                }
                gc.setStroke(Color.color(hudOutlineColor.getRed(),
                        hudOutlineColor.getGreen(),
                        hudOutlineColor.getBlue(),
                        hudOpacity));
                long remainingTime = powerUp.duration - (System.currentTimeMillis() - powerUp.startTime); // To show remaining time in secs

                if (remainingTime > 0) {
                    gc.fillText(
                            powerUp.type + ": " + (remainingTime / 1000) + "s",
                            offsetX,
                            initialOffsetY + spacingY
                    );

                    double barWidth = 100; // Total width of the loading bar
                    double barHeight = 10; // Height of the loading bar
                    double remainingRatio = (double) remainingTime / powerUp.duration;
                    double currentBarWidth = barWidth * remainingRatio;

                    gc.fillRect(offsetX, initialOffsetY + spacingY + 5, currentBarWidth, barHeight);
                    gc.strokeRect(offsetX, initialOffsetY + spacingY + 5, barWidth, barHeight);

                    gc.strokeText(
                            powerUp.type + ": " + (remainingTime / 1000) + "s",
                            offsetX,
                            initialOffsetY + spacingY
                    );

                    spacingY += 30;
                }
            }
        }
    }

    private void drawScore(GraphicsContext gc) {

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 35));

        double centerX = (double) windowX / 2;

        gc.fillText("Score: " + gameLoop.gameState.scoreTracker.getScore(), centerX-70, 70);
    }

    private void drawHighScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 15));

        double spacex = windowX - 125;
        double spacey = windowY - 10;

        gc.fillText("High Score: " + gameLoop.gameState.scoreHistory.getHighScore(), spacex, spacey);
    }


}