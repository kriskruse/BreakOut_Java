package dk.group12.breakout.BreakOutGame;

import dk.group12.breakout.BreakoutGraphical;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class MenuController {
    private final StackPane root;
    //private final Map<String, VBox> menus = new HashMap<String, VBox>();

    // Menu pages
    private VBox startMenu;
    private VBox settingsMenu;
    private VBox pauseMenu;
    private VBox gameOverPage;
    private VBox tutorialScreen;

    // Booleans to track menu visibility
    private boolean isStartMenuVisible = true;
    private boolean isSettingsMenuVisible = false;
    private boolean isPauseMenuVisible = false;
    private boolean isGameOverPageVisible = false;
    private boolean isTutorialScreenVisible = false;


    public MenuController(StackPane root, BreakoutGraphical game) {
        this.root = root;
        createMenus(game);
    }

    //Creation of different menus
    private void createMenus(BreakoutGraphical game) {
        startMenu = createStartMenu(game);
        //settingsMenu = createSettingsMenu();
        pauseMenu = createPauseMenu(game);
        gameOverPage = createGameOverPage(game);
        tutorialScreen = createTutorialScreen(game);

        root.getChildren().addAll(startMenu, pauseMenu, gameOverPage, tutorialScreen);  //add other menus also
        // Upon initialization show only the start menu
        hideMenus();
        showStartMenu();
    }

    /* MENU VISIBILITY SETTINGS */
    public void showStartMenu() {
        isStartMenuVisible = true;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }

    public void showSettingsMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = true;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }

    public void showPauseMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = true;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }
    public void showGameOverPage() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = true;
        isTutorialScreenVisible = false;
        updateMenuVisibility();
    }
    public void showTutorialScreen() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = false;
        isGameOverPageVisible = false;
        isTutorialScreenVisible = true;
        tutorialScreen.setMouseTransparent(true); // Allow mouse clicks to pass through
        updateMenuVisibility();
    }
    public boolean isTutorialScreenVisible() {
        return isTutorialScreenVisible;
    }

    private void updateMenuVisibility() {
        startMenu.setVisible(isStartMenuVisible);
        //settingsMenu.setVisible(isSettingsMenuVisible);
        pauseMenu.setVisible(isPauseMenuVisible);
        gameOverPage.setVisible(isGameOverPageVisible);
        tutorialScreen.setVisible(isTutorialScreenVisible);
    }
    public void hideMenus() {
        startMenu.setVisible(false);
        //settingsMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameOverPage.setVisible(false);
        tutorialScreen.setVisible(false);
    }

    /* CREATING MENU PAGES */

    // START MENU PAGE CREATION
    private VBox createStartMenu(BreakoutGraphical game) {
        // Create label & buttons
        Label pageTitle = new Label("Main Menu");
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> {
            SoundController.menuClickSound();
            game.startGame();
            hideMenus();
            showTutorialScreen();

        });

        Button settingsMenuButton = new Button("Settings");
        settingsMenuButton.setOnAction(e -> {});

        Button howToPlayButton = new Button("How To Play");
        howToPlayButton.setOnAction(e -> {});

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event ->{
            System.out.println("Exit button pressed");
            System.exit(0);
        }); // Exit the application

        // Style buttons & Label
        pageTitle.setStyle(
                "-fx-font-size: 24px;" + // Font size
                        "-fx-font-family: 'Arial';" + // Font family
                        "-fx-font-weight: bold;" + // Bold text
                        "-fx-text-fill: white;" // Text color
        );
        styleButton(startGameButton, "red", "white");
        styleButton(settingsMenuButton, "orange", "white");
        styleButton(howToPlayButton, "green", "white");
        styleButton(exitButton, "yellow",  "black");

        // Hover effect for buttons
        mouseHoverGraphic(startGameButton, settingsMenuButton, howToPlayButton, exitButton);

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        menu.getChildren().addAll(
                pageTitle, startGameButton, settingsMenuButton,howToPlayButton, exitButton
        );

        return menu;
    }

    // PAUSE MENU PAGE CREATION
    private VBox createPauseMenu(BreakoutGraphical game) {
        // Create label & buttons
        Label pageTitle = new Label("Main Menu");
        //resume game button
        Button resumeGameButton = new Button("Resume Game");
        resumeGameButton.setOnAction(e -> {
            game.startGame();
            game.getPauseButton().setVisible(true);
            hideMenus();
        });
        //restart button
        Button restartGameButton = new Button("Restart Game");
        restartGameButton.setOnAction(e -> {
            System.out.println("Restart button pressed");
            game.restartGame();
            hideMenus();
        });
        //Settings Menu Button
        Button settingsMenuButton = new Button("Settings");
        settingsMenuButton.setOnAction(e -> {});

        //How to Play Button
        Button howToPlayButton = new Button("How To Play");
        howToPlayButton.setOnAction(e -> {});

        //exit game button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event ->{
            System.out.println("Exit button pressed");
            System.exit(0);
        }); // Exit the application

        // Style buttons & Label
        pageTitle.setStyle(
                "-fx-font-size: 24px;" + // Font size
                        "-fx-font-family: 'Arial';" + // Font family
                        "-fx-font-weight: bold;" + // Bold text
                        "-fx-text-fill: white;" // Text color
        );
        styleButton(resumeGameButton, "red", "white");
        styleButton(restartGameButton, "orange",  "white");
        styleButton(settingsMenuButton, "green", "white");
        styleButton(howToPlayButton, "yellow", "black");
        styleButton(exitButton, "blue",  "white");

        // Hover effect for buttons
        mouseHoverGraphic(resumeGameButton, restartGameButton, settingsMenuButton, howToPlayButton, exitButton);

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        menu.getChildren().addAll(
                pageTitle, resumeGameButton,restartGameButton, settingsMenuButton,howToPlayButton, exitButton
        );

        return menu;
    }

    //GAME OVER PAGE CREATION
    private VBox createGameOverPage(BreakoutGraphical game) {
        // Create label & buttons
        Label pageTitle = new Label("Game Over");

        //restart button
        Button restartGameButton = new Button("Restart Game");
        restartGameButton.setOnAction(e -> {
            System.out.println("Restart button pressed");
            game.setGameEnded(false);
            game.restartGame();
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
    public VBox createTutorialScreen(BreakoutGraphical game) {
        // Create label & buttons

        Label moveLeftOrRightText = new Label("Move Left/Right arrows");
        Label orText = new Label("or");
        Label moveADText = new Label("A/D keys");


        // Style Labels
        styleLabel(moveLeftOrRightText);
        styleLabel(orText);
        styleLabel(moveADText);

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

    /*buttons Styling & Effects methods*/
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
    public void styleLabel(Label label) {
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
}

