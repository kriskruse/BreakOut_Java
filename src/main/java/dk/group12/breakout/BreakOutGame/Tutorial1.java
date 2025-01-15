package dk.group12.breakout.BreakOutGame;


import dk.group12.breakout.BreakoutGraphical;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tutorial1  {

    public static void startScreen(Stage primaryStage, BreakoutGraphical game) {
        Text Text1 = new Text("Press any key to start a new game! ");
        Text1.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-fill: lime green;");

        Text Text2 = new Text("Move: Left/Right arrows or A/D keys");
        Text2.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: white;");


        // Brug VBox til at arrangere labels og knappen
        VBox layout = new VBox(10); // 10 er spacing mellem elementerne
        layout.getChildren().addAll(Text1, Text2);
        layout.setStyle("-fx-alignment: center; -fx-padding: 50px;");

        //Lav animationen
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), Text1);
        scaleTransition.setFromX(0.9);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();



        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: black;"); //Skal ændres til spillet bagrund

        Scene scene = new Scene(root, 400, 700);

        scene.setOnKeyPressed(e -> {
            System.out.println("Game started");
            game.startGame();
        }); // skal ændre det til at spillet starter

        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout Game");
        primaryStage.show();

    }
}
