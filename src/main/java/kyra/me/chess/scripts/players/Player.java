package kyra.me.chess.scripts.players;

import javafx.scene.image.Image;
import kyra.me.chess.scripts.managers.Database;

import java.util.Objects;

public abstract class Player {
    protected Image profilePicture;
    protected String name;
    protected boolean isWhite;

    protected Player(String imagePath, String name){
        try {
            profilePicture = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))); }
        catch (Exception e) {
            System.out.println("Error loading profile picture: " + imagePath);
        }
        this.name = name;
        Database.addPlayer(this);
    }

    public static void generateProfiles(){
        new HumanPlayer("/kyra/me/chess/assets/models/profiles/colette.jpg", "Colette");
        new LevelOneAI("/kyra/me/chess/assets/models/profiles/faith.jpg", "Faith (bot)");
        new LevelTwoAI("/kyra/me/chess/assets/models/profiles/ganyu.jpg", "Ganyu (bot)");
        new LevelThreeAI("/kyra/me/chess/assets/models/profiles/ganyu.jpg", "Colette (bot)");
    }

    public Image getProfilePicture() { return profilePicture; }
    public String getName() { return name; }
    public void setIsWhite(boolean isWhite) { this.isWhite = isWhite; }
}
