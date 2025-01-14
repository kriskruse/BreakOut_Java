package dk.group12.breakout.BreakOutGame;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundController {
    private static MediaPlayer pingPlayer;
    private static MediaPlayer musicPlayer;
    private static Media pingSound;
    private static Media musicSound;
    private static AudioClip pingTone;

    private static final String pingFile = "src/main/resources/dk/group12/breakout/sounds/beep.wav";
    private static final String musicFile = "src/main/resources/dk/group12/breakout/sounds/music.mp3";

    public static boolean soundControl = true;

    public SoundController() {
        pingSound = new Media(new File(pingFile).toURI().toString());


        musicSound = new Media(new File(musicFile).toURI().toString());
        musicPlayer = new MediaPlayer(musicSound);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.6);

    }
    public static void playPing() {
        if (soundControl) {
           pingPlayer = new MediaPlayer(pingSound);
           pingPlayer.play();
        } else {
            pingTone.stop();
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
