package model.cards;

import controller.Database;
import javafx.scene.image.Image;
import model.cards.monster.Monster;

import java.util.Map;

public class Card {
    private static final Map<String, Card> allCards;
    private static Image unknownImage;


    static {
        allCards = Database.getInstance().getAllCards();
    }

    protected String name;
    protected String Description;
    protected int price;
    protected transient Image image;

    //copy constructor
    public Card(Card o) {
        setName(o.name);
        setDescription(o.Description);
        setPrice(o.price);
        setImage(o.image);
    }

    public Card(String name, String description, int price) {
        setName(name);
        setDescription(description);
        setPrice(price);
    }

    public static Map<String, Card> getAllCards() {
        return allCards;
    }

    public static boolean checkCardNameExistence(String cardName) {
        return allCards.containsKey(cardName);
    }

    public static Card getCard(String name) {
        if (allCards.containsKey(name))
            return allCards.get(name);
        return null;
    }

    public static Image getUnknownImage() {
        if (unknownImage != null)
            return unknownImage;
        String path = "file:" + System.getProperty("user.dir") + "/src/main/resources/Assets/Cards/Monsters/Unknown.jpg";
        unknownImage = new Image(path);
        return unknownImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        if (this.image != null)
            return this.image;
        String cardName;
        cardName = this.name.replaceAll("\\s*", "");
        String path = "file:" + System.getProperty("user.dir") + "/src/main/resources/Assets/Cards/";
        if (this instanceof Monster) path += "Monsters/" + cardName + ".jpg";
        else path += "SpellTrap/" + cardName + ".jpg";
        return this.image = new Image(path);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}