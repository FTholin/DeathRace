package fr.bdd.deathrace.view.interfaces;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.controller.ControllerClient;
import fr.bdd.deathrace.controller.ControllerServer;
import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.body.Type;
import fr.bdd.deathrace.network.protocol.LobbyOperation;
import fr.bdd.deathrace.network.protocol.LobbyUpdate;
import fr.bdd.deathrace.view.IUpdatable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

public class LobbyView extends BorderPane implements IUpdatable {

    private TableView<RowGenerator> table;
    private Lobby lobby;
    private Player player;
    private boolean master;
    private ControllerServer controllerServer;
    private ControllerClient controllerClient;

    private ChoiceBox<String> map;
    private Map<Player, RowGenerator> players;

    /**
     * Create lobby view.
     *
     * @param master true if master of lobby
     */
    public LobbyView(boolean master) {
        this.player = Controller.getInstance().getPlayer();
        this.master = master;
        this.players = new HashMap<>();

        this.controllerServer = Controller.getInstance().getControllerServer();
        this.controllerClient = Controller.getInstance().getControllerClient();
        this.lobby = Controller.getInstance().getControllerModel().getLobby();

        // buttons
        Button startGame;
        Button addBot = new Button("Ajouter");
        addBot.setPrefSize(200, 50);

        if (master) {
            startGame = new Button("Lancer");
            startGame.setOnMouseClicked(event -> {
                boolean areReady = true;
                for (Player player1 : lobby.getPlayers()) {
                    if (!player1.equals(player) && !player1.isReady()) {
                        areReady = false;
                        break;
                    }
                }
                if (areReady) {
                    Controller.getInstance().createRace();
                }
            });
            addBot.setOnMouseClicked(event -> addBot());
        } else {
            startGame = new Button("Prêt");
            startGame.setOnMouseClicked(event -> checkPret(player));
            addBot.setDisable(true);
        }
        startGame.setPrefSize(200, 50);
        
        HBox buttonBox = new HBox(addBot, startGame);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Label mapName = new Label("Choisissez la map ");
        mapName.setPrefSize(200, 50);
        map = new ChoiceBox<>(
                FXCollections.observableArrayList("map", "map1"));
        map.setPrefSize(200, 50);
        map.setDisable(!master);

        map.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    lobby.setMapName(map.getSelectionModel().getSelectedItem());

                    if (controllerServer != null) {
                        controllerServer.sendToAll(
                                new LobbyUpdate(lobby.getMapName()));
                    }
                });
        map.getSelectionModel().selectFirst();

        HBox mapHBox = new HBox(mapName, map);
        mapHBox.setSpacing(20);
        mapHBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Retour");
        backButton.setPrefSize(200, 50);
        backButton.setOnMouseClicked(event -> Controller.getInstance().goToMainView());

        Label lobbyTtitle = new Label("Nouvelle partie Solo");
        lobbyTtitle.setFont(new Font("Arial", 30));

        // table
        table = buildTable();
        table.setPrefSize(750, 230);
        VBox vbox = new VBox(mapHBox, table, buttonBox, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);
        VBox.setMargin(backButton, new Insets(100, 0, 0, 0));

        ImageView logo = new ImageView("&assets&mainmenuicon.jpg"
                .replaceAll("&", Matcher.quoteReplacement(File.separator)));
        logo.setFitHeight(100);
        logo.setFitWidth(100);

        HBox hBox = new HBox(logo, lobbyTtitle);
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setSpacing(30);

        super.setCenter(vbox);
        super.setTop(hBox);

        super.setPadding(new Insets(20, 20, 20, 20));
    }

    /**
     * Build table of view.
     *
     * @return table
     */
    public TableView<RowGenerator> buildTable() {
        // Set table

        TableColumn<RowGenerator, Label> pseudoColumn = new TableColumn<>("Pseudonyme");
        pseudoColumn.setPrefWidth(270);
        pseudoColumn.setResizable(false);
        pseudoColumn.setCellValueFactory(new PropertyValueFactory<>("pseudo"));

        TableColumn<RowGenerator, ChoiceBox<Type>> persoColumn = new TableColumn<>("Personnage");
        persoColumn.setCellValueFactory(new PropertyValueFactory<>("personnage"));
        persoColumn.setPrefWidth(300);
        persoColumn.setResizable(false);

        TableColumn<RowGenerator, CheckBox> readyColumn = new TableColumn<>("Prêt");
        readyColumn.setPrefWidth(80);
        readyColumn.setResizable(false);
        readyColumn.setCellValueFactory(new PropertyValueFactory<>("ready"));

        TableColumn<RowGenerator, Button> kickColumn = new TableColumn<>("Expulser");
        kickColumn.setPrefWidth(80);
        kickColumn.setResizable(false);
        kickColumn.setCellValueFactory(new PropertyValueFactory<>("kick"));

        table = new TableView<>();
        ObservableList<TableColumn<RowGenerator, ?>> columns = table.getColumns();
        columns.add(pseudoColumn);
        columns.add(persoColumn);
        columns.add(readyColumn);
        columns.add(kickColumn);
        table.setMaxSize(750, 300);

        return table;
    }

    /**
     * adds a bot to the table.
     */
    public void addBot() {
        Optional<Player> created = lobby.createPlayer();

        if (created.isPresent()) {
            Player newBot = created.get();
            newBot.setBot(true);
            newBot.setPseudo("Bot");
            newBot.setReady(true);

            if (controllerServer != null) {
                controllerServer.sendToAll(new LobbyUpdate(newBot, LobbyOperation.ADD));
            }
        }
    }

    private void addRow(Player player) {
        ChoiceBox<Type> typePersonnage = new ChoiceBox<>(
                FXCollections.observableArrayList(Type.PONEY, Type.CROCODILE));
        CheckBox ready = new CheckBox();
        Button kick = new Button("Kick");

        if (player.equals(this.player) || (this.master && player.isBot())) {
            // choix perso
            typePersonnage.getSelectionModel().selectedItemProperty()
                    .addListener((ObservableValue<? extends Type> ov, Type t, Type t1) -> {
                        player.setType(typePersonnage.getSelectionModel().getSelectedItem());
                        if (controllerClient != null) {
                            controllerClient.send(
                                    new LobbyUpdate(player, LobbyOperation.UPDATE_PLAYER));
                        } else if (controllerServer != null) {
                            controllerServer.sendToAll(
                                    new LobbyUpdate(player, LobbyOperation.UPDATE_PLAYER));
                        }
                    });

            ready.setOnMouseClicked(event -> checkPret(player));

        } else {
            ready.setDisable(true);
        }

        if (this.master && (player.isBot() || !player.equals(this.player))) {
            kick.setOnMouseClicked(event -> delete(player));
        } else {
            kick.setDisable(true);
        }

        RowGenerator rowGenerator = new RowGenerator(new Label(), typePersonnage, ready,
                kick, player);
        players.putIfAbsent(player, rowGenerator);
        table.getItems().add(rowGenerator);
    }

    /**
     * Kick player from lobby.
     *
     * @param player player to kick
     */
    private void delete(Player player) {
        Controller.getInstance().deletePlayer(player);

        if (controllerServer != null) {
            controllerServer.removePlayer(player);
        }
    }

    /**
     * Delete row in table view.
     *
     * @param player player row to delete
     */
    private void deleteRow(Player player) {
        table.getItems().remove(players.get(player));
        players.remove(player);
    }

    @Override
    public void update() {
        Lobby newLobby = Controller.getInstance().getControllerModel().getLobby();

        if (newLobby == null) {
            return;
        }

        List<Player> toDelete = new ArrayList<>();
        List<Player> currentLobby = new ArrayList<>(newLobby.getPlayers());

        players.forEach((p, row) -> {
            if (currentLobby.contains(p)) {
                currentLobby.remove(p);
            } else {
                toDelete.add(p);
            }
        });

        toDelete.forEach(this::deleteRow);
        currentLobby.forEach(this::addRow);

        if (!map.getValue().equals(newLobby.getMapName())) {
            map.setValue(newLobby.getMapName());
        }

        players.forEach((p, row) -> row.update());
        this.lobby = newLobby;
    }

    private void checkPret(Player player) {
        player.setReady(!player.isReady());

        if (controllerClient != null) {
            controllerClient.send(new LobbyUpdate(player, LobbyOperation.UPDATE_PLAYER));
        } else if (controllerServer != null) {
            controllerServer.sendToAll(new LobbyUpdate(player, LobbyOperation.UPDATE_PLAYER));
        }
    }

    public static class RowGenerator implements IUpdatable {

        private Label pseudo;
        private ChoiceBox<Type> personnage;
        private CheckBox ready;
        private Button kick;
        private Player player;

        /**
         * init a table row.
         *
         * @param pseudo player name
         * @param personnage caracter choice
         * @param ready check if the player is ready
         * @param kick kicks a player
         */
        public RowGenerator(Label pseudo, ChoiceBox<Type> personnage, CheckBox ready,
                Button kick, Player player) {
            this.pseudo = pseudo;
            this.personnage = personnage;
            this.ready = ready;
            this.kick = kick;
            this.player = player;

            // param
            init();
        }

        @Override
        public void update() {
            if (this.player.getPseudo() != null
                    && !this.pseudo.getText().equals(this.player.getPseudo())) {
                this.pseudo.setText(this.player.getPseudo());
            }

            if (this.player.getType() == null) {
                this.personnage.getSelectionModel().selectFirst();
            } else if (this.personnage.getSelectionModel().getSelectedItem()
                    != this.player.getType()) {
                this.personnage.getSelectionModel().select(this.player.getType());
            }

            this.ready.setSelected(this.player.isReady());
        }

        private void init() {
            pseudo.setAlignment(Pos.CENTER);
            pseudo.setPrefWidth(270);

            personnage.setPrefWidth(300);

            ready.setPrefWidth(80);
            ready.setAlignment(Pos.CENTER);

            if (kick != null) {
                kick.setAlignment(Pos.CENTER);
                kick.setPrefWidth(80);
            }
        }

        public Label getPseudo() {
            return pseudo;
        }

        public ChoiceBox<Type> getPersonnage() {
            return personnage;
        }

        public CheckBox getReady() {
            return ready;
        }

        public Button getKick() {
            return kick;
        }
    }
}
