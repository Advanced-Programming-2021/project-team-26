package fxmlController;

import javafx.scene.image.ImageView;
import model.cards.Card;

public class CardImageView extends ImageView {
    GameView gameView;
    Card card;
    boolean isShown;

    public CardImageView() {
    }

    public CardImageView(GameView gameView, Card card, boolean isShown) {
        this.gameView = gameView;
        this.card = card;
        this.isShown = isShown;

        updateImage();
    }

    private void updateImage() {
        if (card == null) {
            setImage(null);
            setOnMouseEntered(null);
        } else {
            if (isShown)
                setImage(card.getImage());
            else
                setImage(Card.getUnknownImage());

            setOnMouseEntered(e -> {
                if (isShown) {
                    gameView.cardInfo.setImage(card.getImage());
                    gameView.cardDetails.setText(card.getDescription());
                } else {
                    gameView.cardInfo.setImage(Card.getUnknownImage());
                    gameView.cardDetails.setText("");
                }
            });
        }
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void removeCard() {
        this.card = null;
        updateImage();
    }

    public void addCard(Card card, boolean isShown) {
        this.card = card;
        this.isShown = isShown;
        updateImage();
    }
}
