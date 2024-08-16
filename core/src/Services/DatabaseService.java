package Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.*;

import static Managers.SimulationLifecycleManager.LEADERBOARD_FILE;

public class DatabaseService {

    public static class LeaderboardEntry {
        private final String username;
        private String points;

        public LeaderboardEntry(String username, String points) {
            this.username = username;
            this.points = points;
        }
        public String getUsername() {
            return username;
        }

        public String getPoints() {
            return points;
        }
        public void setPoints(String points) {
            this.points = points;
        }
    }
    public static Array<LeaderboardEntry> loadLeaderboardData() {
        Array<LeaderboardEntry> leaderboardData = new Array<>();

        FileHandle fileHandle = Gdx.files.internal(LEADERBOARD_FILE);

        if (!fileHandle.exists()) {
            Gdx.app.error("Leaderboard", "Leaderboard file does not exist: " + LEADERBOARD_FILE);
            return leaderboardData;
        }

        Gdx.app.log("Leaderboard", "Loading leaderboard data from: " + LEADERBOARD_FILE);

        String[] lines = fileHandle.readString().split("\\r?\\n");

        for (String line : lines) {
            String[] parts = line.split("\\s+"); // Split by one or more spaces
            if (parts.length == 2) {
                String username = parts[0];
                String points = parts[1];
                leaderboardData.add(new LeaderboardEntry(username, points));
            }
        }

        return leaderboardData;
    }

    public static void saveLeaderboardData(Array<LeaderboardEntry> leaderboardData) {
        FileHandle fileHandle = Gdx.files.local(LEADERBOARD_FILE);
        StringBuilder stringBuilder = new StringBuilder();

        for (LeaderboardEntry entry : leaderboardData) {
            stringBuilder.append(entry.getUsername()).append(" ").append(entry.getPoints()).append("\n");
        }

        fileHandle.writeString(stringBuilder.toString(), false);
    }
}
