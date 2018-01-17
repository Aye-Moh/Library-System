/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class ServerController implements Initializable {

    @FXML
    private TextField hostField;
    @FXML
    private Spinner<Integer> portSpinner;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private Preferences prefs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        prefs = Preferences.userRoot().node("lbdb");
        String host = prefs.get("host", "localhost");
        String username = prefs.get("username", "root");
        int port = prefs.getInt("port", 3306);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3300, 3320, port);
        String password = prefs.get("pass", "");

        hostField.setText(host);
        nameField.setText(username);
        passField.setText(password);
        portSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void saveServerCofig(ActionEvent event) throws SQLException {
        String host = hostField.getText();
        String username = nameField.getText();
        int port = portSpinner.getValue();
        String password = passField.getText();

        prefs.put("host", host);
        prefs.put("username", username);
        prefs.putInt("port", port);
        prefs.put("pass", password);

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

}
