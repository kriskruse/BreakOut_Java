package dk.group12.breakout.BreakOutGame;

import java.util.ArrayList;
import java.util.List;

public class ScoreHistory {
    private List<Integer> scores;

    public ScoreHistory() {
        scores = new ArrayList<>();
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public List<Integer> getScores() {
        return scores;
    }


    public void getScore() { // Method to print out the scores in the terminal
        for (int i = 0; i < scores.size(); i++) {
            System.out.println("Scores: " + scores.get(i));
        }
    }





}
