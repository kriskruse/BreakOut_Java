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
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import java.util.HashSet;
import java.util.Set;

public class BreakoutGraphical extends Application {
    private final int windowX = 400;
    private final int windowY = 700;
    private GameLoop gameLoop;
    private static int n;
    private static int m;
    private static int lives;
    private SoundController soundController;
    private GraphicsContext graphicsContext;
    private MenuController menuController;
    private boolean gamePaused = false;
    private Button pauseButton;
    private int gameIterations = 0; // Tracks the number of iterations


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
        gameLoop = new GameLoop(n, m, windowX, windowY, lives);
        soundController = new SoundController();

        //Create StackPane to layer menu scenes on top of Game scene
        StackPane root = new StackPane();

        Canvas canvas = new Canvas(windowX, windowY);
        //So "LEFT" adn "RIGHT" can be used also when there's buttons on screen
        canvas.setFocusTraversable(true); // Ensure the Canvas can receive focus
        canvas.requestFocus(); // Request focus explicitly

        root.getChildren().add(canvas);
        this.graphicsContext = canvas.getGraphicsContext2D();

        //Pause button creation and settings
        Pane overlayPane = new Pane();
        root.getChildren().add(overlayPane);
        pauseButton = new Button("Pause");
        pauseButton.setLayoutX(windowX-60);
        pauseButton.setLayoutY(1);
        pauseButton.setOnAction(e -> pauseGame());
        overlayPane.getChildren().add(pauseButton);
        getPauseButton().setVisible(false);
        pauseButton.setStyle(
                "-fx-background-color: rgba(0,0,0,0);"+
                        "-fx-font-weight: bold;"+
                        "-fx-text-fill: white;"+
                        "-fx-font-family: 'Arial';"+
                        "-fx-padding: 0 0 10 0;"
        );
        MenuController.mouseHoverGraphic(pauseButton); //button graphics added to button

        Scene gameScene = new Scene(root, windowX, windowY);

        // Input handling
        gameScene.setOnKeyPressed(event -> activeKeys.add(event.getCode().toString()));
        gameScene.setOnKeyReleased(event -> activeKeys.remove(event.getCode().toString()));

        stage.setScene(gameScene);
        stage.setTitle("Breakout");
        stage.show();

        runGameLoop();

        menuController = new MenuController(root, this);
       // menuController.showStartMenu();
    }

    /*GAME MENU LOGIC*/
    public Button getPauseButton(){
        return pauseButton;
    }

    public void startGame() {
        System.out.println("Start Game Button Pressed");
        gamePaused = false;
        if (!gamePaused) {System.out.println("gamePaused is false");}
        getPauseButton().setVisible(true);

    }
    public void pauseGame() {
        System.out.println("Pause Game Button Pressed");
        gamePaused = true;
        getPauseButton().setVisible(false);
        menuController.showPauseMenu(); // Show the pause menu

    }
    public void restartGame() {
        gamePaused = false;
        // Reset game logic (GameLoop reset method)
        startGame();
    }

    /* GAME LOOP RUNNER*/
    public void runGameLoop(){
        // Animation loop running at 60 FPS
        new AnimationTimer() {
            private long lastTime = System.nanoTime();
            private int frameCount = 0;
            long previousTime = 0;

            public void handle(long currentNanoTime) {
                //When game starts
                if (gameIterations == 1){
                    gamePaused = true;
                }
                if (gameIterations > 1){
                    if (gamePaused) {
                        return; // Skip the rest of the frame's logic
                    }
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
                GameState.platform.x,
                GameState.platform.y,
                GameState.platform.width,
                GameState.platform.height);
    }

    private void drawBall(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        for (GameState.Ball ball : GameState.ballList) {
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
        CollisionElement leftWall = GameState.leftWall;
        CollisionElement rightWall = GameState.rightWall;
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
        GameState.BlockCluster cluster = GameState.blockCluster;
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
                gc.setFill(Color.DEEPSKYBLUE); }
            if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
                gc.setFill(Color.GOLD); }
            if (powerUp.type == GameState.powerUpType.MULTIBALL) {
                gc.setFill(Color.HOTPINK); }
            gc.fillOval(powerUp.x, powerUp.y, powerUp.width, powerUp.height);
        }
    }

    private void drawActivePowerUps(GraphicsContext gc) {
        gc.setFill(Color.color(Color.HOTPINK.getRed(), Color.HOTPINK.getGreen(), Color.HOTPINK.getBlue(),0.7));
        gc.setFont(javafx.scene.text.Font.font("Arial", 12));

        int offsetX = 15;
        int initialOffsetY = 45;
        int spacingY = 0;

        if (GameState.ballList.size() > 1) {
            gc.fillText(
                    "MULTIBALL x" + GameState.ballList.size(),
                    offsetX,
                    initialOffsetY + spacingY
            );
            spacingY += 20;
        }

        for (PowerUpHandler.PowerUp powerUp : gameLoop.gameState.powerUpHandler.activePowerUps.values()) {
            if (powerUp.duration > 0) {
                if (powerUp.type == GameState.powerUpType.WIDEN_PLATFORM) {
                    gc.setFill(Color.color(Color.DEEPSKYBLUE.getRed(), Color.DEEPSKYBLUE.getGreen(), Color.DEEPSKYBLUE.getBlue(), 0.7));
                }
                if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
                    gc.setFill(Color.color(Color.GOLD.getRed(), Color.GOLD.getGreen(), Color.GOLD.getBlue(), 0.7));
                }
                long remainingTime = (powerUp.duration - (System.currentTimeMillis() - powerUp.startTime)) / 1000; // To show remaining time in secs

                if (remainingTime > 0) {
                    gc.fillText(
                            powerUp.type + ": " + (remainingTime) + "s",
                            offsetX,
                            initialOffsetY + spacingY
                    );
                    spacingY += 20;
                }
            }
        }
    }
}