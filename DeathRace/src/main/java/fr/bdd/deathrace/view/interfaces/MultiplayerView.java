package fr.bdd.deathrace.view.interfaces;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.view.MainView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class MultiplayerView extends BorderPane {

    private boolean hostChoice;
    private Button start;
    private ChoiceBox nbPlayer;
    private TextField ipAdress;
    private TextField port;
    private ToggleButton host;
    private ToggleButton join;

    /**
     * Create multiplayer view.
     */
    public MultiplayerView() {
        hostChoice = true;
        initHost("192.168.0.1");
    }

    /**
     * intitialize the host interface.
     *
     * @param ip     the public ip of the host.
     */
    private void initHost(String ip) {
        // creating elements

        VBox container = new VBox();

        // ip address
        Label ipAdrLabel = new Label("L'adresse IP ");
        ipAdress = new TextField(ip);//contains the public ip adress to share

        HBox ipHBox = new HBox(ipAdrLabel, ipAdress);
        container.getChildren().add(ipHBox);
        ipHBox.setSpacing(5);
        ipHBox.setAlignment(Pos.CENTER);

        // port
        Label portLabel = new Label("Le port ");
        port = new TextField("9001");

        HBox portHBox = new HBox(portLabel, port);
        container.getChildren().add(portHBox);
        portHBox.setSpacing(5);
        portHBox.setAlignment(Pos.CENTER);

        // password
        Label passwLabel = new Label("Password ");

        PasswordField password = new PasswordField();
        password.setPromptText("Optionnel");

        HBox passwHBox = new HBox(passwLabel, password);
        container.getChildren().add(passwHBox);
        passwHBox.setSpacing(5);
        passwHBox.setAlignment(Pos.CENTER);

        // nb player
        Label nbPlayerLabel = new Label("Le nombre de joueurs");
        nbPlayer = new ChoiceBox(FXCollections.observableArrayList("2", "3", "4", "5"));
        nbPlayer.getSelectionModel().selectFirst();

        HBox nbPlayerHbox = new HBox(nbPlayerLabel, nbPlayer);
        container.getChildren().add(nbPlayerHbox);
        nbPlayerHbox.setSpacing(5);
        nbPlayerHbox.setAlignment(Pos.CENTER);

        // start
        start = new Button("Lancer");
        start.setOnMouseClicked(event -> tryStart());

        container.getChildren().add(start);

        // back
        Button back = new Button("Retour");
        back.setPrefSize(200, 50);
        back.setOnMouseClicked(event -> MainView.getInstance().show(new MainMenuView()));

        container.getChildren().add(back);

        VBox.setMargin(back, new Insets(100, 0, 0, 0));

        // elements specifications
        ipAdrLabel.setPrefSize(200, 50);
        ipAdress.setPrefSize(200, 50);
        portLabel.setPrefSize(200, 50);
        port.setPrefSize(200, 50);
        passwLabel.setPrefSize(200, 50);
        password.setPrefSize(200, 50);
        nbPlayerLabel.setPrefSize(200, 50);
        nbPlayer.setPrefSize(200, 50);
        start.setPrefSize(200, 50);

        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        super.setCenter(container);

        final ToggleGroup multiplayerModeGroup = new ToggleGroup();

        //creating interfaces elements
        host = new ToggleButton("Héberger");
        host.setPrefSize(125, 125);
        host.setOnMouseClicked(event -> switchHostJoin(true));
        host.setToggleGroup(multiplayerModeGroup);
        join = new ToggleButton("Rejoindre");
        join.setPrefSize(125, 125);
        join.setOnMouseClicked(event -> switchHostJoin(false));
        join.setToggleGroup(multiplayerModeGroup);
        join.setSelected(true);

        HBox topBtns = new HBox(host, join);

        //elements specifications
        topBtns.setSpacing(40);
        topBtns.setAlignment(Pos.CENTER);


        super.setTop(topBtns);
        BorderPane.setAlignment(topBtns, Pos.TOP_CENTER);

        switchHostJoin(true);
    }

    private void switchHostJoin(boolean isHostSelected) {
        ipAdress.setDisable(!isHostSelected);
        ipAdress.setDisable(isHostSelected);
        nbPlayer.setDisable(!isHostSelected);

        hostChoice = isHostSelected;

        host.setSelected(isHostSelected);
        join.setSelected(!isHostSelected);

        updateText();
    }

    private void updateText() {
        start.setText(hostChoice ? "Héberger" : "Rejoindre");
    }

    private void tryStart() {
        if (hostChoice) {
            int players = Integer.parseInt((String)nbPlayer.getValue());
            Controller.getInstance().hostRace(players);
            LobbyView lobbyView = new LobbyView(true);
            MainView.getInstance().show(lobbyView);
        } else {
            if (ipAdress.getText().isEmpty() || port.getText().isEmpty()) {
                return;
            }
            int portChoice = Integer.parseInt(port.getText());

            if (Controller.getInstance().joinRace(ipAdress.getText(), portChoice)) {
                // wait for lobby before show it
                await().atMost(5, TimeUnit.SECONDS).until(() ->
                        null != Controller.getInstance().getControllerModel().getLobby());
                LobbyView lobbyView = new LobbyView(false);
                MainView.getInstance().show(lobbyView);
            }
        }
    }
}
