package dk.group12.breakout.BreakOutGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.util.Map;

public class MenuController {
    private final StackPane root;
    //private final Map<String, VBox> menus = new HashMap<String, VBox>();

    // Menu pages
    private VBox startMenu;
    private VBox settingsMenu;
    private VBox difficultyMenu;
    private VBox pauseMenu;
    private VBox gameOverPage;
    private VBox tutorialScreen;

    // Booleans to track menu visibility
    private boolean isStartMenuVisible = true;
    private boolean isSettingsMenuVisible = false;
    private boolean isDifficultyMenuVisible = false;
    private boolean isPauseMenuVisible = false;
    private boolean isGameOverPageVisible = false;
    private boolean isTutorialScreenVisible = false;

    //game start pause end controls
    private Button pauseButton;
    private Pane overlayPane = new Pane();
    public boolean gameStarted = false;
    public boolean gamePaused = false;
    public boolean gameEnded = false;

    private final GameLoop gameLoop;

    public MenuController(StackPane root, GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.root = root;
        overlayPane.getChildren().add(createPauseButton());
        root.getChildren().add(overlayPane);
        createMenus();

    }
    //Pause button creation
    private Button createPauseButton() {
        pauseButton = new Button("Pause");
        pauseButton.setLayoutX(gameLoop.gameWidth-60);
        pauseButton.setLayoutY(1);
        pauseButton.setOnAction(e -> pauseGame());
        pauseButton.setVisible(false);
        pauseButton.setStyle(
                "-fx-background-color: rgba(0,0,0,0);"+
                        "-fx-font-weight: bold;"+
                        "-fx-text-fill: white;"+
                        "-fx-font-family: 'Arial';"+
                        "-fx-padding: 0 0 10 0;"
        );
        mouseHoverGraphic(pauseButton);
        return pauseButton;
    }

    //Creation of different menus
    private void createMenus() {
        startMenu = createVBoxMenuPage("Main Menu", new String[]{"Start Game", "Settings", "How To Play", "Score History", "Exit"});
        pauseMenu = createVBoxMenuPage("Pause Menu", new String[]{"Resume Game", "Restart Game", "Settings", "How To Play", "Exit"});
        gameOverPage = createGameOverPage();
        tutorialScreen = createTutorialScreen();
        settingsMenu = createVBoxMenuPage("Settings", new String[]{"Difficulty","Sound", "Sensitivity", "Back"});
        difficultyMenu = createVBoxMenuPage("Difficulty", new String[]{"Easy", "Medium", "Hard","HARDCORE", "Back"});

        root.getChildren().addAll(startMenu, pauseMenu, gameOverPage, tutorialScreen, settingsMenu, difficultyMenu);  //add other menus also
        // Upon initialization show only the start menu
        hideMenus();
        showStartMenu();
    }

    /* MENU VISIBILITY SETTINGS */
    public void showStartMenu() {
        isStartMenuVisible = true;
        isSettingsMenuVisible = false;
        isDifficultyMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }

    public void showSettingsMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = true;
        isDifficultyMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }

    public void showPauseMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isDifficultyMenuVisible = false;
        isPauseMenuVisible = true;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }
    public void showGameOverPage() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isDifficultyMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = true;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }
    public void showTutorialScreen() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isDifficultyMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = true;
        tutorialScreen.setMouseTransparent(true); // Allow mouse clicks to pass through
        updateMenuVisibility();
    }
    public void showDifficultyMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isDifficultyMenuVisible = true;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }
    public boolean isTutorialScreenVisible() {
        return isTutorialScreenVisible;
    }

    private void updateMenuVisibility() {
        startMenu.setVisible(isStartMenuVisible);
        settingsMenu.setVisible(isSettingsMenuVisible);
        pauseMenu.setVisible(isPauseMenuVisible);
        gameOverPage.setVisible(isGameOverPageVisible);
        tutorialScreen.setVisible(isTutorialScreenVisible);
        difficultyMenu.setVisible(isDifficultyMenuVisible);

    }
    public void hideMenus() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        difficultyMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameOverPage.setVisible(false);
        tutorialScreen.setVisible(false);
        pauseButton.setVisible(true);
    }

    /* CREATING PAGES */

    //GAME OVER PAGE CREATION
    private VBox createGameOverPage() {
        // Create label & buttons
        Label pageTitle = new Label("Game Over");

        //restart button
        Button restartGameButton = new Button("Restart Game");
        restartGameButton.setOnAction(e -> {
            System.out.println("Restart button pressed");
            checkForGameEnded();
            restartGame();
            hideMenus();
        });

        //exit game button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event ->{
            System.out.println("Exit button pressed");
            System.exit(0);
        }); // Exit the application

        // Style buttons & Label
        pageTitle.setStyle(
                "-fx-font-size: 35px;" + // Font size
                        "-fx-font-family: 'Arial';" + // Font family
                        "-fx-font-weight: bold;" + // Bold text
                        "-fx-text-fill: red;" // Text color
        );
        styleButton(restartGameButton, "rgba(0,0,0,0)",  "white");
        styleButton(exitButton, "rgba(0,0,0,0)",  "white");

        // Hover effect for buttons
        mouseHoverGraphic(restartGameButton,exitButton);

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        menu.getChildren().addAll(
                pageTitle, restartGameButton, exitButton
        );

        return menu;
    }

    // TUTORIAL PAGE CREATION
    public VBox createTutorialScreen() {
        // Create label & buttons

        Label moveLeftOrRightText = new Label("Move Left/Right arrows");
        Label orText = new Label("or");
        Label moveADText = new Label("A/D keys");


        // Style Labels
        styleLabel(moveLeftOrRightText, orText, moveADText);

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.BOTTOM_CENTER);
        StackPane.setMargin(menu,new Insets(0, 0, 150, 0)); // Adjust the bottom padding
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        menu.getChildren().addAll(
                moveLeftOrRightText, orText, moveADText
        );
        //add pulse animation
        moveLeftOrRightGraphics(moveLeftOrRightText, orText, moveADText);

        return menu;
    }
    /*VBOX MENU CREATOR*/
    public VBox createVBoxMenuPage(String title, String[] buttonLabels) {
        // Create label & buttons
        Label pageTitle = new Label(title);
        // Style Label
        pageTitle.setStyle(
                "-fx-font-size: 24px;" + // Font size
                        "-fx-font-family: 'Arial';" + // Font family
                        "-fx-font-weight: bold;" + // Bold text
                        "-fx-text-fill: white;" // Text color
        );

        // Define colors for buttons based on position
        String[] colors = {"red", "orange", "green", "yellow", "blue", "indigo", "cyan", "violet"};

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        for (int i = 0; i < buttonLabels.length; i++) {
            String buttonLabel = buttonLabels[i];
            Button button = new Button(buttonLabel);
            if (colors[i].equals("yellow")) {
                styleButton(button, colors[i], "black");
            }
            else {
                styleButton(button, colors[i], "white");
            }
            button.setOnAction(e -> SoundController.menuClickSound());
            mouseHoverGraphic(button);
            button.setOnAction(getButtonAction(buttonLabel));

            // Add the button to the menu
            menu.getChildren().add(button);
        }
        // Add the title to the top of the menu
        menu.getChildren().add(0, pageTitle);

        return menu;
    }

    // Creating a score history page
    private VBox createScoreHistoryDisplay() {
        VBox scoreHistoryBox = new VBox(10); // Spacing between scores
        scoreHistoryBox.setAlignment(Pos.CENTER);
        scoreHistoryBox.setPadding(new Insets(20));
        scoreHistoryBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        Label historyLabel = new Label("Score History");
        historyLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        scoreHistoryBox.getChildren().add(historyLabel);

        // Get scores from GameLoop's scoreHistory
        for (int i = 0; i < gameLoop.gameState.scoreHistory.getScores().size(); i++) {
            int score = gameLoop.gameState.scoreHistory.getScores().get(i);
            Label scoreLabel = new Label("Game " + (i + 1) + ": " + score + " points");
            scoreLabel.setStyle(
                    "-fx-font-size: 18px;" +
                            "-fx-text-fill: white;"
            );

            scoreHistoryBox.getChildren().add(scoreLabel); // Add each score
        }

        return scoreHistoryBox;
    }


    /*BUTTON ACTIONS*/

    // button event handler
    private EventHandler<ActionEvent> getButtonAction(String buttonLabel) {
        Map<String, EventHandler<ActionEvent>> buttonActions = Map.ofEntries(
                Map.entry("Start Game", e -> {
                    resumeGame();
                    hideMenus();
                    showTutorialScreen();
                    gameStarted = true;
                }),
                Map.entry("Resume Game", e -> {
                    resumeGame();
                    pauseButton.setVisible(true);
                    hideMenus();
                }),
                Map.entry("Restart Game", e -> {
                    System.out.println("Restart button pressed");
                    checkForGameEnded();
                    restartGame();
                    hideMenus();
                    showTutorialScreen();
                }),
                Map.entry("Settings", e -> {
                    showSettingsMenu();
                }),
                Map.entry("How To Play", e -> {
                    System.out.println("How To Play button clicked");
                    // Add "How To Play" functionality here
                }),
                Map.entry("Score History", e -> {
                    System.out.println("Score History button clicked");

                    VBox scoreHistoryDisplay = createScoreHistoryDisplay(); // Create the display
                    root.getChildren().add(scoreHistoryDisplay); // Add to the root pane

                    // Add a back button to close the score history page
                    Button backButton = new Button("Back");
                    backButton.setStyle(
                            "-fx-font-size: 18px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-color: red;"
                    );
                    backButton.setOnAction(event -> {
                        root.getChildren().remove(scoreHistoryDisplay);
                    });
                    mouseHoverGraphic(backButton);

                    //Add a delete button to delete the score history
                    Button deleteButton = new Button("Delete History");
                    deleteButton.setStyle(
                            "-fx-font-size: 18px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-color: red;"
                    );

                    deleteButton.setOnAction(event -> {
                        gameLoop.gameState.scoreHistory.clearHistory(); // Clear the history
                        gameLoop.gameState.scoreHistory.saveToFile("scores.txt"); // Save the empty history
                        System.out.println("Score history cleared!");
                    });
                    mouseHoverGraphic(deleteButton);

                    scoreHistoryDisplay.getChildren().addAll(backButton, deleteButton); // Add the back- and delete button

                }),
                Map.entry("Exit", e -> {
                    System.exit(0); // Exit the application
                }),
                Map.entry("Difficulty", e -> {
                    System.out.println("Difficulty button clicked");
                    showDifficultyMenu();
                }),
                Map.entry("Sound", e -> {
                    System.out.println("Sound button clicked");
                    // Add "Sound" functionality here
                }),
                Map.entry("Sensitivity", e -> {
                    System.out.println("Sensitivity button clicked");
                    // Add "Sensitivity" functionality here
                }),
                Map.entry("Back", e -> {
                    if (gameStarted) {
                        showPauseMenu();
                    } else {
                        showStartMenu();
                    }
                }),
                Map.entry("Easy", e -> {
                    System.out.println("Easy button clicked");
                    /*
                    gameLoop.gameState.platformWidth = gameLoop.gameState.platformWidth*1;
                    gameLoop.gameState.ballSpeed = gameLoop.gameState.ballSpeed*1;
                    gameStarted = true;
                    resumeGame();
                    hideMenus();

                     */
                }),
                Map.entry("Medium", e -> {

                    System.out.println("Medium button clicked");
                     /*
                    gameLoop.gameState.platformWidth = gameLoop.gameState.platformWidth*0.8;
                    gameLoop.gameState.ballSpeed = gameLoop.gameState.ballSpeed*1.2;
                    if (gameStarted) {
                        showPauseMenu();
                    } else {
                        showStartMenu();
                    }

                     */
                }),
                Map.entry("Hard", e -> {

                    System.out.println("Hard button clicked");
                     /*
                    gameLoop.gameState.platformWidth = gameLoop.gameState.platformWidth*0.6;
                    gameLoop.gameState.ballSpeed = gameLoop.gameState.ballSpeed*1.4;
                    if (gameStarted) {
                        showPauseMenu();
                    } else {
                        showStartMenu();
                    }

                     */
                }),
                Map.entry("HARDCORE", e -> {

                    System.out.println("HARDCORE button clicked");
                    /*
                    gameLoop.gameState.platformWidth = gameLoop.gameState.platformWidth*0.4;
                    gameLoop.gameState.ballSpeed = gameLoop.gameState.ballSpeed*1.6;
                    if (gameStarted) {
                        showPauseMenu();
                    } else {
                        showStartMenu();
                    }

                     */
                })
        );
        // Return the matching action or a default one
        return buttonActions.getOrDefault(buttonLabel, e ->
                System.out.println("No action assigned for: " + buttonLabel)
        );
    }



    /*BUTTONS & LABELS STYLING*/

    // Helper method to style buttons
    private void styleButton(Button button, String color, String textColor) {
        button.setStyle(
                "-fx-background-color: " + color + ";" +  // Button color
                "-fx-font-family: 'Arial';"+ // Font family
                "-fx-font-size: 18px;" +      // Font size
                "-fx-font-weight: bold;" + // Bold text
                "-fx-padding: 10 0 10 0;" +// Top, Right, Bottom, Left padding
                "-fx-text-fill: " + textColor + ";" // Text color



        );
        button.setPrefWidth(200); // Set uniform width for buttons
    }
    //Helper method to style labels
    public void styleLabel(Label... labels) {
        for (Label label : labels) {
            label.setStyle(
                    "-fx-font-family: 'Arial';"+ // Font family
                            "-fx-font-size: 18px;" +      // Font size
                            "-fx-font-weight: bold;" + // Bold text
                            "-fx-padding: 0 0 0 0;" +// Top, Right, Bottom, Left padding
                            "-fx-text-fill: rgba(255, 255, 255,0.7) ;"+ // Text color
                            "-fx-alignment: center;" // Center text

            );
            label.setPrefWidth(200); // Set uniform width for buttons
        }
    }

    // Helper method to add effect on buttons, when hovered over
    public static void mouseHoverGraphic(Button... buttons) {
        for (Button button : buttons) {
            String originalStyle = button.getStyle(); // Store original style
            // On hover-> Scale the button
            button.setOnMouseEntered(event -> {
                button.setScaleX(1.1); // Increase size horizontally
                button.setScaleY(1.1); // Increase size vertically
                button.setStyle(originalStyle+"-fx-cursor: hand;"); // Revert to original style + hand cursor
                SoundController.menuHoverSound();
            });

            // On hover exit -> Revert scale
            button.setOnMouseExited(event -> {
                button.setScaleX(1.0); // Reset horizontal size
                button.setScaleY(1.0); // Reset vertical size
                button.setStyle(originalStyle); // Revert to original style
            });
        }

    }
    //
    public void moveLeftOrRightGraphics(Label... labels){
        for (Label label : labels) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), label);
            scaleTransition.setFromX(0.9);
            scaleTransition.setFromY(0.8);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1);
            scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
            scaleTransition.setAutoReverse(true);
            scaleTransition.play();
        }
    }

    /*GAME MENU LOGIC*/
    public void resumeGame() {
        gamePaused = false;
        gameEnded = false;
        pauseButton.setVisible(true);
    }

    public void pauseGame() {
        gamePaused = true;
        pauseButton.setVisible(false);
        showPauseMenu(); // Show the pause menu

    }
    public void restartGame() {
        gamePaused = false;
        gameEnded = false;
        gameLoop.restartGame(); // Reinitialize the game loop
        pauseButton.setVisible(true);
    }

    //helper method to set gameEnded to true/false
    public void checkForGameEnded() {

    }

    public void gamePausedCheck() {
        if (gamePaused) {
            resumeGame();
            pauseButton.setVisible(true);
            hideMenus();
        } else {
            pauseGame();
        }
    }
}

