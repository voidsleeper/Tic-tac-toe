// === Player.java ===
import javax.swing.*;

public class Player {
    private String name;
    private ImageIcon profilePicture;
    private int wins;

    public Player(String name, ImageIcon profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.wins = 0;
    }

    public String getName() { return name; }
    public ImageIcon getProfilePicture() { return profilePicture; }
    public int getWins() { return wins; }
    public void incrementWins() { wins++; }
    public void resetWins() { wins = 0; }
}