package bioskopkuy.view;

import bioskopkuy.controller.BioskopController; // Import controller untuk mengelola interaksi
import javafx.collections.FXCollections; // Import untuk mengelola koleksi
import javafx.geometry.Insets; // Import untuk mengatur padding
import javafx.geometry.Pos; // Import untuk mengatur posisi
import javafx.scene.Scene; // Import untuk membuat scene
import javafx.scene.control.*; // Import untuk kontrol UI seperti Button, Label, TextField, dll.
import javafx.scene.layout.GridPane; // Import untuk layout GridPane
import javafx.scene.layout.StackPane; // Import untuk layout StackPane
import javafx.scene.layout.VBox; // Import untuk layout VBox
import javafx.scene.paint.Color; // Import untuk warna
import javafx.scene.text.Font; // Import untuk font
import javafx.scene.text.FontWeight; // Import untuk berat font
import javafx.stage.Stage; // Import untuk stage

public class MainView {
    private final BioskopController controller; // Controller untuk mengelola interaksi
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private ComboBox<String> roleComboBox; // ComboBox untuk memilih role
    private TextField usernameField; // TextField untuk input username
    private PasswordField passwordField; // PasswordField untuk input password
    private Label usernameLabel; // Label untuk username
    private Label passwordLabel; // Label untuk password

    public MainView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        StackPane root = new StackPane(); // Membuat layout utama
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #3A6D65, #5AAAA0);"); // Mengatur warna latar belakang

        VBox content = getVBox(); // Mendapatkan konten VBox

        Label titleLabel = new Label("Selamat Datang di BioskopKuy!"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 38)); // Mengatur font label
        titleLabel.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks
        titleLabel.setWrapText(true); // Mengatur teks agar membungkus
        titleLabel.setAlignment(Pos.CENTER); // Mengatur posisi label
        titleLabel.setMaxWidth(350); // Mengatur lebar maksimum label

        Label roleLabel = new Label("Pilih Role Anda:"); // Label untuk memilih role
        roleLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 19)); // Mengatur font label
        roleLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks

        roleComboBox = new ComboBox<>(FXCollections.observableArrayList("Penonton", "Admin Bioskop")); // ComboBox untuk memilih role
        roleComboBox.setPromptText("Pilih Role"); // Placeholder untuk ComboBox
        roleComboBox.setPrefWidth(280); // Mengatur lebar ComboBox
        roleComboBox.setStyle("-fx-font-size: 17px; -fx-padding: 8px; -fx-background-color: white; -fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya ComboBox

        GridPane loginGrid = new GridPane(); // Membuat GridPane untuk input login
        loginGrid.setHgap(15); // Mengatur jarak horizontal antar elemen
        loginGrid.setVgap(15); // Mengatur jarak vertikal antar elemen
        loginGrid.setAlignment(Pos.CENTER); // Mengatur posisi konten
        loginGrid.setPadding(new Insets(20, 0, 0, 0)); // Menambahkan padding

        usernameLabel = new Label("Username:"); // Label untuk username
        usernameLabel.setFont(Font.font("Verdana", 17)); // Mengatur font label
        usernameLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        usernameField = new TextField(); // Inisialisasi TextField untuk username
        usernameField.setPromptText("Masukkan username"); // Placeholder untuk TextField
        usernameField.setPrefWidth(250); // Mengatur lebar TextField
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya TextField

        passwordLabel = new Label("Password:"); // Label untuk password
        passwordLabel.setFont(Font.font("Verdana", 17)); // Mengatur font label
        passwordLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        passwordField = new PasswordField(); // Inisialisasi PasswordField untuk password
        passwordField.setPromptText("Masukkan password"); // Placeholder untuk PasswordField
        passwordField.setPrefWidth(250); // Mengatur lebar PasswordField
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya PasswordField

        // Menambahkan elemen ke GridPane
        loginGrid.add(usernameLabel, 0, 0);
        loginGrid.add(usernameField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);

        // Menyembunyikan label dan field username dan password
        usernameLabel.setVisible(false);
        usernameField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);

        // Listener untuk ComboBox role
        roleComboBox.setOnAction(_ -> {
            String selectedRole = roleComboBox.getSelectionModel().getSelectedItem(); // Mengambil role yang dipilih
            if ("Admin Bioskop".equals(selectedRole)) { // Jika role adalah Admin Bioskop
                usernameLabel.setVisible(true); // Menampilkan label username
                usernameField.setVisible(true); // Menampilkan field username
                passwordLabel.setVisible(true); // Menampilkan label password
                passwordField.setVisible(true); // Menampilkan field password
            } else { // Jika role adalah Penonton
                usernameLabel.setVisible(false); // Menyembunyikan label username
                usernameField.setVisible(false); // Menyembunyikan field username
                passwordLabel.setVisible(false); // Menyembunyikan label password
                passwordField.setVisible(false); // Menyembunyikan field password
            }
            usernameField.clear(); // Menghapus field username
            passwordField.clear(); // Menghapus field password
        });

        Button actionButton = new Button("Masuk"); // Tombol untuk masuk
        actionButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font tombol
        actionButton.setPrefSize(220, 55); // Mengatur ukuran tombol
        actionButton.setStyle("-fx-background-color: #5AAAA0;" + // Mengatur gaya tombol
                "-fx-text-fill: white;" +
                "-fx-border-color: #3A6D65;" +
                "-fx-border-width: 1.5px;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        actionButton.setOnMouseEntered(_ -> actionButton.setStyle(actionButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        actionButton.setOnMouseExited(_ -> actionButton.setStyle(actionButton.getStyle().replace("-fx-scale-y: 1.05; -fx-scale-x: 1.05;", ""))); // Gaya saat mouse keluar
        actionButton.setOnAction(_ -> handleActionButton()); // Aksi saat tombol diklik

        content.getChildren().addAll(titleLabel, roleLabel, roleComboBox, loginGrid, actionButton); // Menambahkan elemen ke konten
        root.getChildren().add(content); // Menambahkan konten ke root

        scene = new Scene(root, 800, 600); // Membuat scene dengan ukuran tertentu
    }

    private static VBox getVBox() {
        VBox content = new VBox(25); // Membuat layout VBox untuk konten
        content.setAlignment(Pos.CENTER); // Mengatur posisi konten
        content.setPadding(new Insets(60)); // Menambahkan padding
        content.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur gaya konten
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );
        return content; // Mengembalikan konten
    }

    private void handleActionButton() {
        String selectedRole = roleComboBox.getSelectionModel().getSelectedItem(); // Mengambil role yang dipilih
        if (selectedRole == null) { // Memeriksa apakah role dipilih
            showAlert("Mohon pilih role Anda terlebih dahulu."); // Menampilkan peringatan
            return; // Keluar dari metode
        }

        if ("Penonton".equals(selectedRole)) { // Jika role adalah Penonton
            controller.handleLogin("Penonton", null, null); // Memanggil metode login untuk Penonton
        } else if ("Admin Bioskop".equals(selectedRole)) { // Jika role adalah Admin Bioskop
            String username = usernameField.getText(); // Mengambil username
            String password = passwordField.getText(); // Mengambil password
            if (username.isEmpty() || password.isEmpty()) { // Memeriksa apakah username dan password kosong
                showAlert("Username dan password tidak boleh kosong."); // Menampilkan peringatan
                return; // Keluar dari metode
            }
            controller.handleLogin("Admin Bioskop", username, password); // Memanggil metode login untuk Admin
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Membuat alert
        alert.setTitle("Peringatan"); // Mengatur judul alert
        alert.setHeaderText(null); // Tidak ada header
        alert.setContentText(message); // Mengatur pesan
        alert.showAndWait(); // Menampilkan alert
    }

    public void showView() {
        roleComboBox.getSelectionModel().clearSelection(); // Menghapus pilihan di ComboBox
        usernameLabel.setVisible(false); // Menyembunyikan label username
        usernameField.setVisible(false); // Menyembunyikan field username
        passwordLabel.setVisible(false); // Menyembunyikan label password
        passwordField.setVisible(false); // Menyembunyikan field password
        usernameField.clear(); // Menghapus field username
        passwordField.clear(); // Menghapus field password

        stage.setTitle("BioskopKuy! - Pilih Role"); // Mengatur judul stage
        stage.setScene(scene); // Mengatur scene untuk stage
        stage.show(); // Menampilkan stage
    }
}