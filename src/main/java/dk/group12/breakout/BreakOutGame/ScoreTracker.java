package dk.group12.breakout.BreakOutGame;

public class ScoreTracker {
    private int score;

    public ScoreTracker() {
        this.score=0;
    }

    public void addScore(int points) {
        score += points; // add points to the score
    }

    public void resetScore() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

}
