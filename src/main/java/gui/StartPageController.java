package gui;

import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Checker;

import java.util.HashMap;

public class StartPageController {

    private AnchorPane ap;
    private Stage stage;

    @FXML
    Button ButtonHC;

    @FXML
    Button ButtonHH;

    @FXML
    public void startHumanHuman() {
        stage.hide();
        ButtonHH.setDisable(true);
        try {
            Stage mainStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/MainScene.fxml"));
            Parent root = loader.load();
            GameController controller = (GameController) loader.getController();
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.sizeToScene();
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setTitle("Backgammon");
            mainStage.show();
            controller.init(mainStage, root);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startHumanComputer() {
        stage.hide();
        try {
            Stage mainStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/AiMainScene.fxml"));
            Parent root = loader.load();
            AiGameController controller = (AiGameController) loader.getController();
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.sizeToScene();
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setTitle("Backgammon");
            mainStage.show();
            controller.init(mainStage, root);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Stage stage, Parent root) {
        ap = (AnchorPane) root;
        this.stage = stage;
//        background.setImage(new Image("gui/images/background.jpg"));
    }

}
