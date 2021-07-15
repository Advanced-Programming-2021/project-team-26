package fxmlController;

import Utilities.GetFXML;
import Utilities.Alert;
import controller.Database;
import controller.DeckController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Deck;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeckMenu extends MenuParent implements Initializable {
    public TableView<Deck> deckNameTable;
    public GridPane allCards;
    public ImageView cardInfo;

    @FXML
    private GridPane mainDeck;
    @FXML
    private GridPane sideDeck;

    public DeckMenu() {
        super("Deck Menu");
    }

    public void run() throws IOException {
        AnchorPane root = (AnchorPane) GetFXML.getFXML("deckMenu");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    private void updateMainDeck() {
        mainDeck.getChildren().clear();
        Deck deck = deckNameTable.getSelectionModel().getSelectedItem();
        if (deck == null)
            return;
        ArrayList<String> mainDeckArray = deck.getMainDeck();
        for (int i = 0; i < mainDeckArray.size(); i++) {
            mainDeck.add(getCardImage(Card.getCard(mainDeckArray.get(i)), "mainDeck"), i % 10, i / 10);
        }
    }

    private void updateSideDeck() {
        sideDeck.getChildren().clear();
        Deck deck = deckNameTable.getSelectionModel().getSelectedItem();
        if (deck == null)
            return;
        ArrayList<String> sideDeckArray = deck.getSideDeck();
        for (int i = 0; i < sideDeckArray.size(); i++) {
            sideDeck.add(getCardImage(Card.getCard(sideDeckArray.get(i)), "sideDeck"), i % 10, i / 10);
        }
    }

    private void updateAllCards() {
        allCards.getChildren().clear();
        Deck deck = deckNameTable.getSelectionModel().getSelectedItem();
        if (deck == null)
            return;
        HashMap<String, Integer> cards = Database.getInstance().getCurrentUser().getAllCards();

        int i = 0;
        for (String cardName : cards.keySet()) {
            int number = cards.get(cardName) - deck.getNumberOfCardINDeck(cardName);
            while (number > 0) {
                number--;
                allCards.add(getCardImage(Card.getCard(cardName), "allCards"), i % 8, i / 8);
                i++;
            }
        }
    }

    public ImageView getCardImage(Card card, String source) {
        ImageView imageView = new ImageView(card.getImage());
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_DECK.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_DECK.getValue());

        imageView.setOnDragDetected(e -> {
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            //clipboardContent.putImage(imageView.getImage());
            clipboardContent.putString(source + " " + card.getName());

            dragboard.setContent(clipboardContent);
            e.consume();
        });

        imageView.setOnMouseEntered(e -> {
            cardInfo.setVisible(true);
            cardInfo.setImage(imageView.getImage());
        });
        imageView.setOnMouseExited(e -> cardInfo.setVisible(false));
        return imageView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        deckNameTable.setEditable(false);
        deckNameTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        deckNameTable.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/Scoreboard.css")).toExternalForm());
        initDeckNameTable();
        updateDecksName();
        addDropOptionAllCards();
        addDropOptionMainDeck();
        addDropOptionSideDeck();
    }

    private void addDropOptionAllCards() {
        allCards.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                switch (source) {
                    case "allCards":
                        return;
                    case "mainDeck":
                    case "sideDeck":
                        e.acceptTransferModes(TransferMode.ANY);
                        break;
                }
            }
        });
        allCards.setOnDragDropped(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                String cardName = matcher.group(2);
                try {
                    switch (source) {
                        case "allCards":
                            return;
                        case "mainDeck":
//                            DeckController.getInstance().removeCard(cardName, getDeckName(), false);
                            updateMainDeck();
                            break;
                        case "sideDeck":
//                            DeckController.getInstance().removeCard(cardName, getDeckName(), true);
                            updateSideDeck();
                            break;
                    }
                    updateAllCards();
                } catch (Exception exception) {
                    Alert.getInstance().errorPrint(exception.getMessage());
                }
            }
        });
    }

    private void addDropOptionMainDeck() {
        mainDeck.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                switch (source) {
                    case "mainDeck":
                        return;
                    case "allCards":
                    case "sideDeck":
                        e.acceptTransferModes(TransferMode.ANY);
                        break;
                }
            }
        });
        mainDeck.setOnDragDropped(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                String cardName = matcher.group(2);
                try {
                    switch (source) {
                        case "mainDeck":
                            return;
                        case "allCards":
//                            DeckController.getInstance().addCard(cardName, getDeckName(), false);
                            updateAllCards();
                            break;
                        case "sideDeck":
//                            DeckController.getInstance().removeCard(cardName, getDeckName(), true);
//                            DeckController.getInstance().addCard(cardName, getDeckName(), false);
                            updateSideDeck();
                            break;
                    }
                    updateMainDeck();
                } catch (Exception exception) {
                    Alert.getInstance().errorPrint(exception.getMessage());
                }
            }
        });
    }

    private void addDropOptionSideDeck() {
        sideDeck.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                switch (source) {
                    case "sideDeck":
                        return;
                    case "allCards":
                    case "mainDeck":
                        e.acceptTransferModes(TransferMode.ANY);
                        break;
                }
            }
        });
        sideDeck.setOnDragDropped(e -> {
            if (e.getDragboard().hasString()) {
                String input = e.getDragboard().getString();
                Matcher matcher = Pattern.compile("(\\w+?) (.+)").matcher(input);
                if (!matcher.find())
                    return;
                String source = matcher.group(1);
                String cardName = matcher.group(2);
                try {
                    switch (source) {
                        case "sideDeck":
                            return;
                        case "allCards":
//                            DeckController.getInstance().addCard(cardName, getDeckName(), true);
                            updateAllCards();
                            break;
                        case "mainDeck":
//                            DeckController.getInstance().removeCard(cardName, getDeckName(), false);
//                            DeckController.getInstance().addCard(cardName, getDeckName(), true);
                            updateMainDeck();
                            break;
                    }
                    updateSideDeck();
                } catch (Exception exception) {
                    Alert.getInstance().errorPrint(exception.getMessage());
                }
            }
        });
    }

    private String getDeckName() {
        Deck deck = deckNameTable.getSelectionModel().getSelectedItem();
        if (deck == null)
            return null;
        return deck.getName();
    }

    private void initDeckNameTable() {
        deckNameTable.setRowFactory(tv -> new TableRow<Deck>() {
            @Override
            protected void updateItem(Deck item, boolean empty) {
                super.updateItem(item, empty);
                Deck activeDeck = Database.getInstance().getCurrentUser().getActiveDeck();
                if (item == null || item.getName() == null)
                    setStyle("");
                else if (activeDeck != null &&
                        item.getName().equals(Database.getInstance().getCurrentUser().getActiveDeck().getName()))
                    setStyle("-fx-background-color: #01e00c;");
                else
                    setStyle("");
            }
        });
        deckNameTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, deck, t1) -> {
                    updateAllCards();
                    updateMainDeck();
                    updateSideDeck();
                }
        );
    }

    private void updateDecksName() {
        deckNameTable.getItems().clear();
        Collection<Deck> allDecks = Database.getInstance().getCurrentUser().getAllDecks().values();
        TableColumn<Deck, String> deckNameColumn = (TableColumn<Deck, String>) deckNameTable.getColumns().get(0);
        deckNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        for (Deck deck : allDecks) {
            deckNameTable.getItems().add(deck);
        }
    }

    public void newDeck() {
        Stage stage = new Stage();

        TextField deckName = new TextField();
        deckName.setPromptText("deck name");

        Button add = new Button("make");
        add.setOnAction(event -> {
            try {
//                DeckController.getInstance().createDeck(deckName.getText());
                Alert.getInstance().successfulPrint("deck added");
            } catch (Exception e) {
                Alert.getInstance().errorPrint(e.getMessage());
            }
            stage.close();
        });

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(5));
        root.getChildren().addAll(deckName, add);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        updateDecksName();
    }

    public void removeDeck() {
        Deck selectedDeck = deckNameTable.getSelectionModel().getSelectedItem();
        if (selectedDeck == null) {
            Alert.getInstance().errorPrint("No deck is selected");
            return;
        }
        try {
//            DeckController.getInstance().removeDeck(selectedDeck.getName());
            Alert.getInstance().successfulPrint("Deck removed");
            updateDecksName();
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }

    public void setActive() {
        Deck selectedDeck = deckNameTable.getSelectionModel().getSelectedItem();
        if (selectedDeck == null) {
            Alert.getInstance().errorPrint("No deck is selected");
            return;
        }
        try {
//            DeckController.getInstance().setActive(selectedDeck.getName());
            Alert.getInstance().successfulPrint("done");
            updateDecksName();
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }
}