package kyra.me.chess.scripts.players;

import javafx.scene.image.Image;
import kyra.me.chess.scripts.managers.Database;

import java.util.Objects;

public abstract class Player {
    protected Image profilePicture;
    protected String name;

    protected Player(String imagePath, String name) {
        try {
        profilePicture = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))); }
        catch (Exception e) {
            System.out.println("Error loading profile picture: " + imagePath);
        }
        this.name = name;
        Database.addPlayer(this);
    }

    public static void generateProfiles(){
        new HumanPlayer("/kyra/me/chess/assets/models/colette.jpg", "Colette");
        new LevelOneAI("/kyra/me/chess/assets/models/ganyu.jpg", "Ganyu (bot)");
    }

    public Image getProfilePicture() { return profilePicture; }
    public String getName() { return name; }
}
