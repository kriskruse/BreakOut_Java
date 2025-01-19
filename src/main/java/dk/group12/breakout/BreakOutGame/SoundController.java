package dk.group12.breakout.BreakOutGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Objects;

public class SoundController {
    private static MediaPlayer musicPlayer;

    private static final Media clickSound =  new Media(Objects.requireNonNull(
            SoundController.class.getResource(
                    "/dk/group12/breakout/BreakOutGame/sounds/ui/click.mp3")).toString());

    private static final Media[] bounceSounds = new Media[5];
    private static final Media[] hoverSounds = new Media[2];

    public static boolean soundControl = true;

    public SoundController() {

        for (int i = 0; i < bounceSounds.length; i++) {
            bounceSounds[i] = new Media(
                    Objects.requireNonNull(
                            this.getClass().getResource(
                                    "/dk/group12/breakout/BreakOutGame/sounds/bounce" + (i + 1) + ".wav"))
                            .toString());

        }
        for (int i = 0; i < hoverSounds.length; i++) {
            hoverSounds[i] = new Media(
                    Objects.requireNonNull(
                            this.getClass().getResource(
                                    "/dk/group12/breakout/BreakOutGame/sounds/ui/hover" + (i + 1) + ".wav"))
                            .toString());
        }


        String musicFile = Objects.requireNonNull(
                this.getClass().getResource("/dk/group12/breakout/BreakOutGame/sounds/music.mp3")).toString();

        Media musicSound = new Media(musicFile);
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
