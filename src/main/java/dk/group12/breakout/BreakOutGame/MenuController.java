

// MenuController.java
package dk.group12.breakout.BreakOutGame;


import dk.group12.breakout.BreakoutGraphical;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;

public class MenuController {
    private VBox startMenu;
    private VBox settingsMenu;
    private VBox pauseMenu;
    private final StackPane root;
    //private final Map<String, VBox> menus = new HashMap<String, VBox>();

    // Booleans to track menu visibility
    private boolean isStartMenuVisible = true;
    private boolean isSettingsMenuVisible = false;
    private boolean isPauseMenuVisible = false;

    public MenuController(StackPane root, BreakoutGraphical game) {
        this.root = root;
        createMenus(game);
    }

    //Creation of different menus
    private void createMenus(BreakoutGraphical game) {
        startMenu = createStartMenu(game);
        //settingsMenu = createSettingsMenu();
        pauseMenu = createPauseMenu(game);

        root.getChildren().addAll(startMenu, pauseMenu);  //add other menus also

        // Initially show only the start men
        hideMenus();
        showStartMenu();
    }

    /* Menu visibility Settings */
    public void showStartMenu() {
        isStartMenuVisible = true;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = false;
        updateMenuVisibility();
    }

    public void showSettingsMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = true;
        isPauseMenuVisible = false;
        updateMenuVisibility();
    }

    public void showPauseMenu() {
        isStartMenuVisible = false;
        isSettingsMenuVisible = false;
        isPauseMenuVisible = true;
        updateMenuVisibility();

    }

    private void updateMenuVisibility() {
        startMenu.setVisible(isStartMenuVisible);
        //settingsMenu.setVisible(isSettingsMenuVisible);
        pauseMenu.setVisible(isPauseMenuVisible);
    }
    public void hideMenus() {
        startMenu.setVisible(false);
        //settingsMenu.setVisible(false);
        pauseMenu.setVisible(false);
    }

    /* CREATING MENU PAGES */

    // START MENU CREATION
    private VBox createStartMenu(BreakoutGraphical game) {
        // Create label & buttons
        Label pageTitle = new Label("Main Menu");
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> {
            game.startGame();
            hideMenus();
        });

        Button settingsMenuButton = new Button("Settings");
        settingsMenuButton.setOnAction(e -> {});

        Button howToPlayButton = new Button("How To Play");
        howToPlayButton.setOnAction(e -> {});
        /*
        Button difficultyButton = new Button("Difficulty");

        Button sensitivityButton = new Button("Sensitivity");

        Button soundButton = new Button("Sound");
        */

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
        /*
        styleButton(difficultyButton, "orange", "white");
        styleButton(sensitivityButton, "green", "white");
        styleButton(soundButton, "yellow",  "black");
         */

        // Hover effect for buttons
        mouseHoverGraphic(startGameButton);
        mouseHoverGraphic(settingsMenuButton);
        mouseHoverGraphic(howToPlayButton);
        mouseHoverGraphic(exitButton);
        /*
        mouseHoverGraphic(difficultyButton);
        mouseHoverGraphic(sensitivityButton);
        mouseHoverGraphic(soundButton);
         */

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        menu.getChildren().addAll(
                pageTitle, startGameButton, settingsMenuButton,howToPlayButton, exitButton
        );
        //root.getChildren().add(menu);

        return menu;
    }

    // PAUSE MENU CREATION
    private VBox createPauseMenu(BreakoutGraphical game) {
        // Create label & buttons
        Label pageTitle = new Label("Main Menu");
        Button resumeGameButton = new Button("Resume Game");
        resumeGameButton.setOnAction(e -> {
            game.startGame();
            game.getPauseButton().setVisible(true);
            hideMenus();
        });

        Button settingsMenuButton = new Button("Settings");
        settingsMenuButton.setOnAction(e -> {});

        Button howToPlayButton = new Button("How To Play");
        howToPlayButton.setOnAction(e -> {});
        /*
        Button difficultyButton = new Button("Difficulty");

        Button sensitivityButton = new Button("Sensitivity");

        Button soundButton = new Button("Sound");
        */

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
        styleButton(settingsMenuButton, "orange", "white");
        styleButton(howToPlayButton, "green", "white");
        styleButton(exitButton, "yellow",  "black");
        /*
        styleButton(difficultyButton, "orange", "white");
        styleButton(sensitivityButton, "green", "white");
        styleButton(soundButton, "yellow",  "black");
         */

        // Hover effect for buttons
        mouseHoverGraphic(resumeGameButton);
        mouseHoverGraphic(settingsMenuButton);
        mouseHoverGraphic(howToPlayButton);
        mouseHoverGraphic(exitButton);
        /*
        mouseHoverGraphic(difficultyButton);
        mouseHoverGraphic(sensitivityButton);
        mouseHoverGraphic(soundButton);
         */

        // VBox layout for vertical alignment
        VBox menu = new VBox(15); // Spacing between buttons
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        menu.getChildren().addAll(
                pageTitle, resumeGameButton, settingsMenuButton,howToPlayButton, exitButton
        );
        //root.getChildren().add(menu);

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

    // Helper method to add effect on buttons, when hovered over
    public static Button mouseHoverGraphic(Button button) {
        String originalStyle = button.getStyle(); // Store original style
        // On hover: Scale the button
        button.setOnMouseEntered(event -> {
            button.setScaleX(1.1); // Increase size horizontally
            button.setScaleY(1.1); // Increase size vertically
            button.setStyle(originalStyle+"-fx-cursor: hand;"); // Revert to original style + hand cursor
        });

        // On hover exit: Revert scale
        button.setOnMouseExited(event -> {
            button.setScaleX(1.0); // Reset horizontal size
            button.setScaleY(1.0); // Reset vertical size
            button.setStyle(originalStyle); // Revert to original style
        });

        return button;
    }


}

