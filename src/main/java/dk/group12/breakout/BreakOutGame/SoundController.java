package dk.group12.breakout.BreakOutGame;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundController {
    private static MediaPlayer musicPlayer;
    private static final Media[] bounceSounds = new Media[5];

    private static final String musicFile = "src/main/resources/dk/group12/breakout/sounds/music.mp3";
    private static final String[] bounceFiles = {
            "src/main/resources/dk/group12/breakout/sounds/bounce1.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce2.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce3.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce4.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce5.wav"};

    public static boolean soundControl = true;

    public SoundController() {

        for (int i = 0; i < 5; i++) {
            bounceSounds[i] = new Media(new File(bounceFiles[i]).toURI().toString());
        }


        Media musicSound = new Media(new File(musicFile).toURI().toString());
        musicPlayer = new MediaPlayer(musicSound);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.1);

    }
    public static void playPing() {
        if (soundControl) {
            MediaPlayer bouncePlayer = new MediaPlayer(bounceSounds[(int) (Math.random() * 5)]);
            bouncePlayer.play();
        }
    }

    public void playMusic() {
        if (soundControl) {
            musicPlayer.play();
        }else {
            musicPlayer.stop();
        }
    }
}
