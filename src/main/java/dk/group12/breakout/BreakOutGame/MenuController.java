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

    public enum MenuState {
        START_MENU,
        SETTINGS_MENU,
        PAUSE_MENU,
        GAME_OVER,
        TUTORIAL_SCREEN,
        DIFFICULTY_MENU,
        SOUND_MENU,
        SENSITIVITY_MENU,
        HOW_TO_PLAY
    }
    public MenuState currentMenu;
    private final StackPane root;
    private final SoundController soundController;

    //menu pages
    private VBox startMenu;
    private VBox settingsMenu;
    private VBox difficultyMenu;
    private VBox soundMenu;
    private VBox pauseMenu;
    private VBox gameOverPage;
    private VBox tutorialScreen;
    private VBox sensitivityMenu;
    private VBox howToPlayMenu;

    //game start pause end controls
    public Button pauseButton;
    private final Pane overlayPane = new Pane();
    public boolean gameStarted = false;
    public boolean gamePaused = false;
    public boolean gameEnded = false;

    private final GameLoop gameLoop;

    //menu controller constructor
    public MenuController(StackPane root, GameLoop gameLoop, SoundController soundController) {
        this.gameLoop = gameLoop;
        this.root = root;
        this.soundController = soundController;
        overlayPane.getChildren().add(createPauseButton());
        root.getChildren().add(overlayPane);
        createMenus();

    }

    /* MENU VISIBILITY SETTINGS */

    // Shows the specified menu and hides all others
    public void showMenu(MenuState menuState) {
        this.currentMenu = menuState;
        updateMenuVisibility();
    }
    // Updates the visibility of the menus based on the current menu state
    private void updateMenuVisibility() {
        startMenu.setVisible(currentMenu == MenuState.START_MENU);
        settingsMenu.setVisible(currentMenu == MenuState.SETTINGS_MENU);
        difficultyMenu.setVisible(currentMenu == MenuState.DIFFICULTY_MENU);
        soundMenu.setVisible(currentMenu == MenuState.SOUND_MENU);
        pauseMenu.setVisible(currentMenu == MenuState.PAUSE_MENU);
        gameOverPage.setVisible(currentMenu == MenuState.GAME_OVER);
        tutorialScreen.setVisible(currentMenu == MenuState.TUTORIAL_SCREEN);
        sensitivityMenu.setVisible(currentMenu == MenuState.SENSITIVITY_MENU);
        howToPlayMenu.setVisible(currentMenu == MenuState.HOW_TO_PLAY);

        tutorialScreen.setMouseTransparent(currentMenu == MenuState.TUTORIAL_SCREEN); // Allow mouse hover & clicks to pass through
    }
    // Hides all menus except the pause button
    public void hideMenus() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        difficultyMenu.setVisible(false);
        soundMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameOverPage.setVisible(false);
        tutorialScreen.setVisible(false);
        sensitivityMenu.setVisible(false);
        howToPlayMenu.setVisible(false);
        pauseButton.setVisible(true);
    }

    /* INITIALIZATION OF PAGES */

    //Initializes the pause button
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

    // Initializes all menu panes and adds them to the root container.
    private void createMenus() {
        startMenu = createVBoxMenuPage("Main Menu", new String[]{"Start Game", "Settings", "How To Play", "Score History", "Exit"});
        pauseMenu = createVBoxMenuPage("Pause Menu", new String[]{"Resume Game", "Restart Game", "Settings", "How To Play", "Exit"});
        gameOverPage = createGameOverPage();
        tutorialScreen = createTutorialScreen();
        settingsMenu = createVBoxMenuPage("Settings", new String[]{"Difficulty","Sound", "Sensitivity", "Back"});
        difficultyMenu = createDifficultyMenu();
        soundMenu = createVBoxMenuPage("Sound", new String[]{"On", "Off", "Back"});
        sensitivityMenu = createVBoxMenuPage("Sensitivity", new String[]{"Low", "Normal", "High", "Very High", "Back"});        howToPlayMenu = createHowToPlayMenu();
        howToPlayMenu = createHowToPlayMenu();

        root.getChildren().addAll(startMenu, pauseMenu, gameOverPage, tutorialScreen,
                settingsMenu, difficultyMenu, soundMenu, sensitivityMenu, howToPlayMenu);  //add other menus also
        // Upon initialization show only the start menu
        hideMenus();
        showMenu(MenuState.START_MENU);
    }

    // initializes the difficulty menu with the current difficulty label
    private Label currentDifficultyLabel;
    private VBox createDifficultyMenu() {
        VBox menu = createVBoxMenuPage("Difficulty", new String[]{"Easy", "Medium", "Hard", "HARDCORE!", "Back"});
        currentDifficultyLabel = new Label("Current Difficulty: Easy");
        currentDifficultyLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );
        menu.getChildren().add(1, currentDifficultyLabel); // Add the label below the title
        return menu;
    }
    //CREATE HOW TO PLAY MENU
    private VBox createHowToPlayMenu() {
        VBox howToPlayMenu = createVBoxMenuPage("How To Play", new String[]{"Back"});
        howToPlayMenu.getChildren().add(
                1, new Label("How To Play:" +
                        "\n\n" +
                        "1. Use the A/D or LEFT/RIGHT keys to move the platform." +
                        "\n\n" +
                        "2. Break the blocks by hitting them with the ball." +
                        "\n\n" +
                        "3. Catch the power-ups to get an advantage." +
                        "\n\n" +
                        "4. Don't let the ball fall below the platform." +
                        "\n\n" +
                        "5. The game gets harder as you move on" +
                        "\n\n" +
                        "6. Beat the best score of yourself and friends" +
                        "\n\n" +
                        "7. Have fun!"
                ) {{
                    setStyle(
                            "-fx-font-weight: bold;" +              // Bold font
                                    "-fx-text-fill: white;" +       // White text color
                                    "-fx-font-family: 'Arial';" +  // Optional: Specify font family
                                    "-fx-font-size: 16px;"+         // Optional: Adjust font size
                                    "-fx-alignment: center;"      // Center text
                    );
                    setWrapText(true); // Enable text wrapping
                }}
        );
        return howToPlayMenu;
    }

    //GAME OVER PAGE INITIALIZATION
    private VBox createGameOverPage() {
        // Create label & buttons
        Label pageTitle = new Label("Game Over");

        //restart button
        Button restartGameButton = new Button("Restart Game");
        restartGameButton.setOnAction(e -> {
            System.out.println("Restart button pressed");
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

    // TUTORIAL PAGE INITIALIZATION
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
    //VBOX MENU TEMPLATE INITIALIZER
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
                    showMenu(MenuState.TUTORIAL_SCREEN);
                    gameStarted = true;
                }),
                Map.entry("Resume Game", e -> {
                    resumeGame();
                    pauseButton.setVisible(true);
                    hideMenus();
                }),
                Map.entry("Restart Game", e -> {
                    System.out.println("Restart button pressed");
                    restartGame();
                    hideMenus();
                    showMenu(MenuState.TUTORIAL_SCREEN);
                }),
                Map.entry("Settings", e -> showMenu(MenuState.SETTINGS_MENU)),
                Map.entry("How To Play", e -> {
                    System.out.println("How To Play button clicked");
                    showMenu(MenuState.HOW_TO_PLAY);
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
                    showMenu(MenuState.DIFFICULTY_MENU);
                }),
                Map.entry("Sound", e -> {
                    System.out.println("Sound button clicked");
                    showMenu(MenuState.SOUND_MENU);
                }),
                Map.entry("Sensitivity", e -> {
                    System.out.println("Sensitivity button clicked");
                    showMenu(MenuState.SENSITIVITY_MENU);
                }),
                Map.entry("Back", e -> {
                    System.out.println("Back button clicked");
                    if (!gameStarted && currentMenu == MenuState.SETTINGS_MENU) {
                        showMenu(MenuState.START_MENU); // Navigate back to Start Menu
                    } else if (gameStarted && currentMenu == MenuState.SETTINGS_MENU) {
                        showMenu(MenuState.PAUSE_MENU); // Navigate back to Pause Menu
                    } else if (currentMenu == MenuState.DIFFICULTY_MENU) {
                        showMenu(MenuState.SETTINGS_MENU); // Navigate back to Settings Menu
                    } else if (currentMenu == MenuState.SOUND_MENU) {
                        showMenu(MenuState.SETTINGS_MENU); // Navigate back to Settings Menu
                    } else if (currentMenu == MenuState.SENSITIVITY_MENU) {
                        showMenu(MenuState.SETTINGS_MENU); // Navigate back to Settings Menu
                    } else if (currentMenu == MenuState.HOW_TO_PLAY) {
                        showMenu(MenuState.START_MENU); // Navigate back to Start Menu
                    }
                }),
                Map.entry("Easy", e -> {
                    System.out.println("Easy button clicked");
                    currentDifficultyLabel.setText("Current Difficulty: Easy");
                    GameState.platformWidthDifficultyMultiplier = 1.0;
                    // If the width isn't updated, the platform will remain the same size until a new gameState is created
                    gameLoop.gameState.platform.updateWidth(gameLoop.gameState.powerUpHandler.activePowerUps);
                    GameState.ballSpeedDifficultyMultiplier = 1.0;
                    gameLoop.gameState.updateBallSpeed();

                }),
                Map.entry("Medium", e -> {

                    System.out.println("Medium button clicked");
                    currentDifficultyLabel.setText("Current Difficulty: Medium");
                    GameState.platformWidthDifficultyMultiplier = 0.8;
                    gameLoop.gameState.platform.updateWidth(gameLoop.gameState.powerUpHandler.activePowerUps);
                    GameState.ballSpeedDifficultyMultiplier = 1.2;
                    gameLoop.gameState.updateBallSpeed();

                }),
                Map.entry("Hard", e -> {

                    System.out.println("Hard button clicked");
                    currentDifficultyLabel.setText("Current Difficulty: Hard");
                    GameState.platformWidthDifficultyMultiplier = 0.6;
                    gameLoop.gameState.platform.updateWidth(gameLoop.gameState.powerUpHandler.activePowerUps);
                    GameState.ballSpeedDifficultyMultiplier = 1.4;
                    gameLoop.gameState.updateBallSpeed();

                }),
                Map.entry("HARDCORE!", e -> {

                    System.out.println("HARDCORE! button clicked");
                    currentDifficultyLabel.setText("Current Difficulty: HARDCORE!");
                    GameState.platformWidthDifficultyMultiplier = 0.4;
                    gameLoop.gameState.platform.updateWidth(gameLoop.gameState.powerUpHandler.activePowerUps);
                    GameState.ballSpeedDifficultyMultiplier = 1.6;
                    gameLoop.gameState.updateBallSpeed();

                }),
                Map.entry("Off", e -> {
                    System.out.println("Sound Off button clicked");
                    if (SoundController.soundControl) {
                        SoundController.soundControl = false;
                        soundController.stopMusic();
                    }

                }),
                Map.entry("On", e -> {
                    System.out.println("Sound On button clicked");
                    if (!SoundController.soundControl) {
                        SoundController.soundControl = true;
                        soundController.playMusic();
                    }
                }),
                Map.entry("Low", e -> {
                    System.out.println("Low Sensitivity button clicked");
                    GameLoop.playerMovementSpeed = 5;
                }),
                Map.entry("Normal", e -> {
                    System.out.println("Medium Sensitivity button clicked");
                    GameLoop.playerMovementSpeed = 8;
                }),
                Map.entry("High", e -> {
                    System.out.println("High Sensitivity button clicked");
                    GameLoop.playerMovementSpeed = 13;
                }),
                Map.entry("Very High", e -> {
                    System.out.println("Very High Sensitivity button clicked");
                    GameLoop.playerMovementSpeed = 18;
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
        showMenu(MenuState.PAUSE_MENU); // Show the pause menu

    }
    public void restartGame() {
        gamePaused = false;
        gameEnded = false;
        gameLoop.restartGame(); // Reinitialize the game loop
        hideMenus();
        showMenu(MenuState.TUTORIAL_SCREEN);
        pauseButton.setVisible(true);
    }

}

