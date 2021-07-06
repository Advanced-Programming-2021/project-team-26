package fxmlController;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Deck;
import model.cards.Card;
import view.Printt;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import static fxmlController.App.getFXML;


public class DeckMenu extends MenuParent implements Initializable {
    public TableView<Deck> deckNameTable;

    @FXML
    private GridPane mainDeck;
    @FXML
    private GridPane sideDeck;

    public DeckMenu() {
        super("Deck Menu");
    }

    public void run() throws IOException {
        AnchorPane root = (AnchorPane) getFXML("deckMenu");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    private void updateMainDeck(Deck deck) {
        ArrayList<String> mainDeckArray = deck.getMainDeck();
        for (int i = 0; i < mainDeckArray.size(); i++) {
            mainDeck.add(getCardImage(Card.getCard(mainDeckArray.get(i))), i % 10, i / 10);
        }
    }

    private void updateSideDeck(Deck deck) {
        ArrayList<String> sideDeckArray = deck.getSideDeck();
        for (int i = 0; i < sideDeckArray.size(); i++) {
            sideDeck.add(getCardImage(Card.getCard(sideDeckArray.get(i))), i % 10, i / 10);
        }
    }

    public ImageView getCardImage(Card card) {
        ImageView imageView = new ImageView(card.getImage());
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_DECK.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_DECK.getValue());
        return imageView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        deckNameTable.setEditable(false);
        deckNameTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initDeckNameTable();
        updateDecksName();
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
    }

    private void updateDecksName() {
        deckNameTable.getItems().clear();
        Collection<Deck> allDecks = Database.getInstance().getCurrentUser().getAllDecks().values();
        Deck activeDeck = Database.getInstance().getCurrentUser().getActiveDeck();
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
                DeckController.getInstance().createDeck(deckName.getText());
                Printt.getInstance().successfulPrint("deck added");
            } catch (Exception e) {
                Printt.getInstance().errorPrint(e.getMessage());
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
            Printt.getInstance().errorPrint("No deck is selected");
            return;
        }
        try {
            DeckController.getInstance().removeDeck(selectedDeck.getName());
            Printt.getInstance().successfulPrint("Deck removed");
            updateDecksName();
        } catch (Exception e) {
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }

    public void setActive() {
        Deck selectedDeck = deckNameTable.getSelectionModel().getSelectedItem();
        if (selectedDeck == null) {
            Printt.getInstance().errorPrint("No deck is selected");
            return;
        }
        try {
            DeckController.getInstance().setActive(selectedDeck.getName());
            Printt.getInstance().successfulPrint("done");
            updateDecksName();
        } catch (Exception e) {
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }
}