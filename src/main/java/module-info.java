module com.example.breakout {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens dk.group12.breakout to javafx.fxml;
    exports dk.group12.breakout;
}