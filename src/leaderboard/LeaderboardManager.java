package leaderboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardManager {

    private ArrayList<Score> scores;

    private static final String SCORES_FILE = "scores.dat";

    private File file;

    private ObjectOutputStream outputStream = null;

    private static final int MAX_HIGHSCORES = 10;

    public LeaderboardManager() {
        scores = new ArrayList<>();
    }

    public ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
    }

    private void sort() {
        Collections.sort(scores);
    }

    public void addScore(String name, int score, int level, String playTime) {
        loadScoreFile();
        scores.add(new Score(name, score, level, playTime));
        updateScoreFile();
    }

    @SuppressWarnings("unchecked")
    private static <T> T castTo(Object obj) {
        return (T) obj;
    }

    private void loadScoreFile() {
        //if (!new File(SCORES_FILE).isFile()) file = new File(SCORES_FILE);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SCORES_FILE));
            scores = castTo(inputStream.readObject());
        } catch (FileNotFoundException e) {
            System.out.println("[Load] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Load] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Load] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Load] IO Error: " + e.getMessage());
            }
        }
    }

    private void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(SCORES_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    public List<Score> getHighscores() {
        ArrayList<Score> scores = getScores();
        int totalScores = scores.size();
        if (totalScores > MAX_HIGHSCORES) {
            totalScores = MAX_HIGHSCORES;
        }
        return scores.stream().limit(totalScores).collect(Collectors.toList());
    }
}
