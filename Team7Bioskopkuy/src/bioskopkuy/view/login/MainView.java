package bioskopkuy.view.login;

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
        // Latar belakang dengan gradien yang menarik
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #3A6D65, #5AAAA0);");

        VBox content = new VBox(25); // Spasi vertikal antar elemen
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60)); // Padding di sekitar konten
        // Gaya untuk kontainer utama login
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" + // Putih semi-transparan
                "-fx-background-radius: 20px;" + // Sudut membulat
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 8);" + // Efek bayangan
                "-fx-padding: 40px;"); // Padding internal

        Label titleLabel = new Label("Selamat Datang di BioskopKuy!");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 38)); // Font yang lebih modern
        titleLabel.setTextFill(Color.web("#3A6D65")); // Warna teks dari palet
        titleLabel.setWrapText(true); // Agar teks bisa wrap jika terlalu panjang
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(350); // Batasi lebar untuk tampilan yang rapi

        Label roleLabel = new Label("Pilih Role Anda:");
        roleLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 19));
        roleLabel.setTextFill(Color.web("#2C3E50"));

        roleComboBox = new ComboBox<>(FXCollections.observableArrayList("Penonton", "Admin Bioskop"));
        roleComboBox.setPromptText("Pilih Role");
        roleComboBox.setPrefWidth(280);
        // Gaya untuk ComboBox
        roleComboBox.setStyle("-fx-font-size: 17px; -fx-padding: 8px; -fx-background-color: white; -fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-border-radius: 5px;");

        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(15); // Spasi horizontal
        loginGrid.setVgap(15); // Spasi vertikal
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setPadding(new Insets(20, 0, 0, 0));

        usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Verdana", 17));
        usernameLabel.setTextFill(Color.web("#2C3E50"));
        usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        usernameField.setPrefWidth(250);
        // Gaya untuk TextField
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Verdana", 17));
        passwordLabel.setTextFill(Color.web("#2C3E50"));
        passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.setPrefWidth(250);
        // Gaya untuk PasswordField
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        loginGrid.add(usernameLabel, 0, 0);
        loginGrid.add(usernameField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);

        // Awalnya sembunyikan field username/password
        usernameLabel.setVisible(false);
        usernameField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);

        // Logika untuk menampilkan/menyembunyikan field berdasarkan role yang dipilih
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
            usernameField.clear(); // Bersihkan field saat role berubah
            passwordField.clear();
        });

        Button actionButton = new Button("Masuk");
        actionButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        actionButton.setPrefSize(220, 55);
        // Gaya tombol modern
        actionButton.setStyle("-fx-background-color: #5AAAA0;" + // Warna utama aplikasi
                "-fx-text-fill: white;" + // Teks putih
                "-fx-border-color: #3A6D65;" + // Border sedikit lebih gelap
                "-fx-border-width: 1.5px;" +
                "-fx-background-radius: 10px;" + // Sudut membulat
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"); // Shadow
        // Efek hover untuk tombol
        actionButton.setOnMouseEntered(e -> actionButton.setStyle(actionButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle(actionButton.getStyle().replace("-fx-scale-y: 1.05; -fx-scale-x: 1.05;", "")));
        actionButton.setOnAction(_ -> handleActionButton());

        content.getChildren().addAll(titleLabel, roleLabel, roleComboBox, loginGrid, actionButton);
        root.getChildren().add(content);

        scene = new Scene(root, 800, 600); // Ukuran scene
    }

    // Menangani aksi tombol "Masuk"
    private void handleActionButton() {
        String selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Mohon pilih role Anda terlebih dahulu.");
            return;
        }

        if ("Penonton".equals(selectedRole)) {
            controller.handleLogin("Penonton", null, null); // Langsung login sebagai penonton
        } else if ("Admin Bioskop".equals(selectedRole)) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Username dan password tidak boleh kosong.");
                return;
            }
            controller.handleLogin("Admin Bioskop", username, password); // Coba login sebagai admin
        }
    }

    // Utility method untuk menampilkan alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showView() {
        // Reset tampilan saat view ditampilkan kembali
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