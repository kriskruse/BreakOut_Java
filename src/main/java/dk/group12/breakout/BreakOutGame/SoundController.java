package dk.group12.breakout.BreakOutGame;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundController {
    private static MediaPlayer musicPlayer;

    private static final String musicFile = "src/main/resources/dk/group12/breakout/sounds/music.mp3";
    private static final String[] bounceFiles = {
            "src/main/resources/dk/group12/breakout/sounds/bounce1.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce2.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce3.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce4.wav",
            "src/main/resources/dk/group12/breakout/sounds/bounce5.wav"};
    private static final String[] hoverFiles = {
            "src/main/resources/dk/group12/breakout/sounds/ui/hover1.wav",
            "src/main/resources/dk/group12/breakout/sounds/ui/hover2.wav"
    };

    private static final Media clickSound = new Media(
            new File("src/main/resources/dk/group12/breakout/sounds/ui/click.mp3").toURI().toString());

    private static final Media[] bounceSounds = new Media[bounceFiles.length];
    private static final Media[] hoverSounds = new Media[hoverFiles.length];

    public static boolean soundControl = true;

    public SoundController() {

        for (int i = 0; i < bounceFiles.length; i++) {
            bounceSounds[i] = new Media(new File(bounceFiles[i]).toURI().toString());
        }
        for (int i = 0; i < hoverFiles.length; i++) {
            hoverSounds[i] = new Media(new File(hoverFiles[i]).toURI().toString());
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

    public static void menuHoverSound() {
        if (soundControl) {
            MediaPlayer hoverPlayer = new MediaPlayer(hoverSounds[(int) (Math.random() * 2)]);
            hoverPlayer.setVolume(0.05);
            hoverPlayer.play();
        }
    }

    public static void menuClickSound() {
        if (soundControl) {
            MediaPlayer clickPlayer = new MediaPlayer(clickSound);
            clickPlayer.setVolume(0.3);
            clickPlayer.play();
        }
    }

    public void playMusic() {
        if (soundControl) {
            musicPlayer.play();
        }else {
            musicPlayer.stop();
        }
    }

    public void stopMusic() {
        musicPlayer.stop();
    }
}
