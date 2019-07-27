package fr.bdd.deathrace.view.interfaces;

import fr.bdd.deathrace.tools.Persist;
import fr.bdd.deathrace.view.MainView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.util.regex.Matcher;

public class OptionsView extends BorderPane {

    TableView<Statistics> statTable;
    private static final String GAMESPLAYED = "Nombre de parties jouées";
    private static final String GAMESWON = "Nombre de victoires";
    private static final String BESTTIME = "Meilleur temps";
    private static final String WORSTTIME = "Pire Temps";

    /**
     * initilize the Option interface.
     */
    public OptionsView() {
        // Gestion du pseudonyme 
        Label pseudoLabel = new Label("Pseudonyme ");
        pseudoLabel.setPrefSize(200, 50);
        pseudoLabel.setAlignment(Pos.CENTER);

        TextField pseudoTextField = new TextField(Persist.getUserPseudo());
        pseudoTextField.setPrefSize(200, 50);

        Button changePseudo = new Button("Changer");
        changePseudo.setPrefSize(200, 50);
        changePseudo.setOnMouseClicked(event -> {
            Persist.setUserPseudonyme(pseudoTextField.getText());
            Alert changedAlert = new Alert(Alert.AlertType.INFORMATION);
            changedAlert.setTitle("Changement de pseudonyme");
            changedAlert.setHeaderText("Félicitations " + Persist.getUserPseudo() + " !");
            changedAlert.setContentText("Pseudonyme changé avec succès !");
            changedAlert.showAndWait();
        });

        HBox pseudoHBox = new HBox(pseudoLabel, pseudoTextField, changePseudo);
        pseudoHBox.setSpacing(30);
        pseudoHBox.setAlignment(Pos.CENTER);

        //table 
        statTable = buildTable();
        statTable.setPrefSize(600, 250);

        //reset Button 
        Button reset = new Button("Reset");
        reset.setPrefSize(200, 50);
        reset.setOnMouseClicked(event -> {
            Persist.reset();
            updateTable();
        });

        // back Button
        Button back = new Button("Retour");
        back.setPrefSize(200, 50);
        back.setOnMouseClicked(event -> MainView.getInstance().show(new MainMenuView()));

        //contaoiner 
        VBox container = new VBox(pseudoHBox, statTable, reset, back);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(30);
        VBox.setMargin(back, new Insets(100, 0, 0, 0));

        // header 
        Label lobbyTtitle = new Label("Options");
        lobbyTtitle.setFont(new Font("Arial", 30));

        ImageView logo = new ImageView("&assets&mainmenuicon.jpg"
                .replaceAll("&", Matcher.quoteReplacement(File.separator)));
        logo.setFitHeight(100);
        logo.setFitWidth(100);

        HBox head = new HBox(logo, lobbyTtitle);
        head.setSpacing(50);

        //filling the interface 
        super.setTop(head);
        super.setCenter(container);
        super.setPadding(new Insets(20, 20, 20, 20));
    }

    private TableView buildTable() {
        // init first column
        TableColumn<Statistics, String> stat = new TableColumn<>("Attribut");
        stat.setPrefWidth(400);
        stat.setResizable(false);
        stat.setCellValueFactory(new PropertyValueFactory<>("stat"));
        //init second column
        TableColumn<Statistics, String> statValue = new TableColumn<>("Statistiques");
        statValue.setPrefWidth(200);
        stat.setResizable(true);
        statValue.setCellValueFactory(new PropertyValueFactory<>("statValue"));
        //init table
        statTable = new TableView<>();
        statTable.setItems(initTable());
        statTable.getColumns().addAll(stat, statValue);
        statTable.setMaxSize(602, 125);
        return statTable;
    }

    /**
     * updates the view when reset button clicked.
     */
    public void updateTable() {
        statTable.getItems().set(0,
                new Statistics(GAMESPLAYED, Persist.getGamesNumber()));
        statTable.getItems().set(1,
                new Statistics(GAMESWON, Persist.getWinsNumber()));
        statTable.getItems().set(2,
                new Statistics(BESTTIME, Persist.getBestTime()));
        statTable.getItems().set(3,
                new Statistics(WORSTTIME, Persist.getWorstTime()));
    }

    /**
     * default content of the table.
     *
     * @return data to be inserted into table
     */
    public ObservableList<Statistics> initTable() {
        return FXCollections.observableArrayList(
                new Statistics(GAMESPLAYED, Persist.getGamesNumber()),
                new Statistics(GAMESWON, Persist.getWinsNumber()),
                new Statistics(BESTTIME, Persist.getBestTime()),
                new Statistics(WORSTTIME, Persist.getWorstTime()));
    }

    /**
     * create a custom elemnt for the table.
     *
     * @param nbGames number of games played
     * @param nbWins number of wins.
     * @param bestTime the player's best time
     * @param worstTime the player's worst time
     * @return the information to add to the TableView
     */
    public ObservableList<Statistics> getstats(String nbGames, String nbWins,
            String bestTime, String worstTime) {
        return FXCollections.observableArrayList(new Statistics(GAMESPLAYED, nbGames),
                new Statistics(GAMESWON, nbWins),
                new Statistics(bestTime, bestTime),
                new Statistics(worstTime, worstTime)
        );
    }

    public class Statistics {

        private final String stat;
        private final String statValue;

        public Statistics(String stat, String statValue) {
            this.stat = stat;
            this.statValue = statValue;
        }

        public String getStat() {
            return stat;
        }

        public String getStatValue() {
            return statValue;
        }

    }
}
