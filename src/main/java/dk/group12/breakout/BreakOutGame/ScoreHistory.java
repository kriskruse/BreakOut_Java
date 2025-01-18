package dk.group12.breakout.BreakOutGame;

import java.io.*;
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

    public void clearHistory() {
        scores.clear(); // clear the history
    }


    public void printScores() { // Method to print out the scores in the terminal
        System.out.println("Score History:");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println("Game " + (i + 1) + ": " + scores.get(i) + " points");
            // Print out each score with game number
        }
    }

    // Method to save the score to a file
    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int score : scores) {
                writer.write(score + "\n"); // Write each score to the file in a new line
            }
        } catch (IOException e) {
            System.err.println("Failed to save scores: " + e.getMessage()); // An error message to show what went wrong
        }
    }

    // Method to load the scores from a file
    public void loadFromFile(String fileName) {
        scores.clear(); // Clear the history before loading new scores in order to avoid duplicates
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) { // Read the file
            String line;
            while ((line = reader.readLine()) != null) { // Go through each line in the file
                int score = Integer.parseInt(line);
                scores.add(score); // Add each score to the history
            }
        } catch (IOException e) {// if something goes wrong with reading the file, we get a message but the program continues
            System.err.println("Failed to load scores: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid score format in file: " + e.getMessage());
        }
    }

}
