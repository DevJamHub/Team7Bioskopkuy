package bioskopkuy.view;

import bioskopkuy.controller.BioskopController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private ComboBox<String> roleComboBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private Label passwordLabel;

    public MainView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #3A6D65, #5AAAA0);");

        VBox content = getVBox();

        Label titleLabel = new Label("Selamat Datang di BioskopKuy!");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 38));
        titleLabel.setTextFill(Color.web("#3A6D65"));
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(350);

        Label roleLabel = new Label("Pilih Role Anda:");
        roleLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 19));
        roleLabel.setTextFill(Color.web("#2C3E50"));

        roleComboBox = new ComboBox<>(FXCollections.observableArrayList("Penonton", "Admin Bioskop"));
        roleComboBox.setPromptText("Pilih Role");
        roleComboBox.setPrefWidth(280);
        roleComboBox.setStyle("-fx-font-size: 17px; -fx-padding: 8px; -fx-background-color: white; -fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-border-radius: 5px;");

        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(15);
        loginGrid.setVgap(15);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setPadding(new Insets(20, 0, 0, 0));

        usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Verdana", 17));
        usernameLabel.setTextFill(Color.web("#2C3E50"));
        usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        usernameField.setPrefWidth(250);
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Verdana", 17));
        passwordLabel.setTextFill(Color.web("#2C3E50"));
        passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.setPrefWidth(250);
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        loginGrid.add(usernameLabel, 0, 0);
        loginGrid.add(usernameField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);

        usernameLabel.setVisible(false);
        usernameField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);

        roleComboBox.setOnAction(_ -> {
            String selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
            if ("Admin Bioskop".equals(selectedRole)) {
                usernameLabel.setVisible(true);
                usernameField.setVisible(true);
                passwordLabel.setVisible(true);
                passwordField.setVisible(true);
            } else {
                usernameLabel.setVisible(false);
                usernameField.setVisible(false);
                passwordLabel.setVisible(false);
                passwordField.setVisible(false);
            }
            usernameField.clear();
            passwordField.clear();
        });

        Button actionButton = new Button("Masuk");
        actionButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        actionButton.setPrefSize(220, 55);
        actionButton.setStyle("-fx-background-color: #5AAAA0;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #3A6D65;" +
                "-fx-border-width: 1.5px;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        actionButton.setOnMouseEntered(_ -> actionButton.setStyle(actionButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        actionButton.setOnMouseExited(_ -> actionButton.setStyle(actionButton.getStyle().replace("-fx-scale-y: 1.05; -fx-scale-x: 1.05;", "")));
        actionButton.setOnAction(_ -> handleActionButton());

        content.getChildren().addAll(titleLabel, roleLabel, roleComboBox, loginGrid, actionButton);
        root.getChildren().add(content);

        scene = new Scene(root, 800, 600);
    }

    private static VBox getVBox() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));
        content.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" +
                        "-fx-background-radius: 20px;" +
                        "-fx-border-radius: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                        "-fx-padding: 40px;"
        );
        return content;
    }

    private void handleActionButton() {
        String selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Mohon pilih role Anda terlebih dahulu.");
            return;
        }

        if ("Penonton".equals(selectedRole)) {
            controller.handleLogin("Penonton", null, null);
        } else if ("Admin Bioskop".equals(selectedRole)) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Username dan password tidak boleh kosong.");
                return;
            }
            controller.handleLogin("Admin Bioskop", username, password);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showView() {
        roleComboBox.getSelectionModel().clearSelection();
        usernameLabel.setVisible(false);
        usernameField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        usernameField.clear();
        passwordField.clear();

        stage.setTitle("BioskopKuy! - Pilih Role");
        stage.setScene(scene);
        stage.show();
    }
}