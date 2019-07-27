package fr.bdd.deathrace.view.interfaces;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.tools.Persist;
import fr.bdd.deathrace.view.MainView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;

public class MainMenuView extends BorderPane {

    /**
     * Create main menu view.
     */
    public MainMenuView() {

        String pseudo;
        pseudo = Persist.getUserPseudo();

        if (pseudo == null || pseudo.trim().isEmpty()) {
            pseudo = displayPseudoDialog();
            Persist.saveUserPseudo(pseudo);
        }

        Controller.getInstance().getPlayer().setPseudo(pseudo);

        //creation of the elements
        ImageView logo = new ImageView("&assets&mainmenuicon.jpg"
                .replaceAll("&", Matcher.quoteReplacement(File.separator)));
        logo.setFitHeight(200);
        logo.setFitWidth(200);

        Button newGame = new Button("Nouvelle partie");
        newGame.setPrefSize(200, 50);
        newGame.setOnMouseClicked(event -> {
            Controller.getInstance().createLobby(5);
            LobbyView lobbyView = new LobbyView(true);
            MainView.getInstance().show(lobbyView);
        });

        Button multi = new Button("Multijoueurs");
        multi.setPrefSize(200, 50);
        multi.setOnMouseClicked(event -> MainView.getInstance().show(new MultiplayerView()));

        Button options = new Button("Options");
        options.setPrefSize(200, 50);
        options.setOnMouseClicked(event -> MainView.getInstance().show(new OptionsView()));

        Button exit = new Button("Quitter");
        exit.setPrefSize(200, 50);
        exit.setOnMouseClicked(event -> Platform.exit());

        VBox vbox = new VBox(newGame, multi, options, exit);
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(exit, new Insets(100, 0, 0, 0));

        super.setCenter(vbox);
        super.setTop(logo);
        BorderPane.setAlignment(logo, Pos.TOP_CENTER);

        super.setPadding(new Insets(50, 50, 50, 50));
    }

    /**
     * Display dialog window to get pseudo player.
     *
     * @return pseudo player
     */
    public String displayPseudoDialog() {
        TextInputDialog dialog = new TextInputDialog("");
        final Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setVisible(false);

        dialog.setHeaderText("Coucou, on n'a pas été présenté !");
        dialog.setContentText("Entrer votre pseudo :");

        Optional<String> result = dialog.showAndWait();

        while (!result.isPresent() || result.get().trim().isEmpty()) {
            result = dialog.showAndWait();
        }

        return result.get();
    }

}
