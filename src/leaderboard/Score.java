package leaderboard;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

    private static final long serialVersionUID = 1L;
    private int score;
    private String name;
    private int level;
    private String playTime;

    public Score(String name, int score, int level, String playTime) {
        this.score = score;
        this.name = name;
        this.level = level;
        this.playTime = playTime;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getPlayTime() {
        return playTime;
    }

    @Override
    public int compareTo(Score score1) {
        return Integer.compare((score1.getScore()), getScore());
    }
}
