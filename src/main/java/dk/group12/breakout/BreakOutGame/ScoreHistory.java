package dk.group12.breakout.BreakOutGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreHistory {
    private final List<Integer> scores;

    public ScoreHistory() {
        scores = new ArrayList<>();
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void clearHistory() {
        scores.clear();
    }

    public void printScores() {
        System.out.println("Score History:");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println("Game " + (i + 1) + ": " + scores.get(i) + " points");
        }
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int score : scores) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save scores: " + e.getMessage());
        }
    }

    public void loadFromFile(String fileName) {
        scores.clear();

        if (!new File(fileName).exists()) {
            try {
                new File(fileName).createNewFile();
            } catch (IOException e) {
                System.err.println("Failed to create new file: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int score = Integer.parseInt(line);
                scores.add(score);
            }
        } catch (IOException e) {
            System.err.println("Failed to load scores: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid score format in file: " + e.getMessage());
        }
    }

    public int getHighScore() {
        if (scores.isEmpty()) {
            return 0;
        }
        return Collections.max(scores);
    }
}