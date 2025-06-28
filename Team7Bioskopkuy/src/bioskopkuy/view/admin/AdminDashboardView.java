package bioskopkuy.view.admin; // Package untuk tampilan admin

import bioskopkuy.controller.BioskopController; // Import controller untuk menghubungkan tampilan dan logika
import javafx.geometry.Insets; // Import untuk mengatur padding
import javafx.geometry.Pos; // Import untuk mengatur posisi
import javafx.scene.Scene; // Import untuk membuat scene
import javafx.scene.control.Button; // Import untuk tombol
import javafx.scene.control.Label; // Import untuk label
import javafx.scene.layout.BorderPane; // Import untuk layout BorderPane
import javafx.scene.layout.HBox; // Import untuk layout HBox
import javafx.scene.layout.VBox; // Import untuk layout VBox
import javafx.scene.paint.Color; // Import untuk warna
import javafx.scene.text.Font; // Import untuk font
import javafx.scene.text.FontWeight; // Import untuk berat font
import javafx.stage.Stage; // Import untuk stage

// Kelas untuk menampilkan tampilan dashboard admin
public class AdminDashboardView {
    private final BioskopController controller; // Referensi ke controller untuk menghubungkan tampilan dan logika
    private final Stage stage;                 // Stage utama yang digunakan untuk menampilkan scene
    private Scene scene;                       // Scene dari dashboard admin

    // Konstruktor, menerima controller dan stage utama
    public AdminDashboardView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage;           // Inisialisasi stage
        initialize(); // Inisialisasi tampilan
    }

    // Metode untuk membangun isi tampilan dashboard
    private void initialize() {
        // === Root layout utama ===
        BorderPane root = new BorderPane(); // Membuat layout BorderPane
        root.setPadding(new Insets(25)); // Mengatur padding
        root.setStyle("-fx-background-color: linear-gradient(to top right, #3A6D65, #5AAAA0);"); // Mengatur warna latar belakang

        // === Panel kiri atas berisi tombol logout ===
        HBox topLeftPanel = new HBox(); // Membuat layout HBox untuk panel kiri atas
        topLeftPanel.setAlignment(Pos.TOP_LEFT); // Mengatur posisi panel

        Button logoutButton = new Button("Logout"); // Membuat tombol logout
        logoutButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        logoutButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya tombol

        // Efek hover untuk tombol logout
        logoutButton.setOnMouseEntered(_ -> logoutButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        logoutButton.setOnMouseExited(_ -> logoutButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));

        // Aksi klik logout: kembali ke tampilan utama
        logoutButton.setOnAction(_ -> controller.kembaliKeMainView());

        // Tambahkan tombol logout ke panel kiri atas
        topLeftPanel.getChildren().add(logoutButton); // Menambahkan tombol ke panel
        root.setTop(topLeftPanel); // Mengatur panel sebagai bagian atas dari layout
        BorderPane.setMargin(topLeftPanel, new Insets(0, 0, 30, 0)); // Margin bawah

        // === Panel utama tengah berisi tombol-tombol manajemen ===
        VBox optionsPanel = new VBox(25); // Jarak antar komponen 25px
        optionsPanel.setAlignment(Pos.CENTER); // Mengatur posisi panel
        optionsPanel.setPadding(new Insets(40)); // Mengatur padding
        optionsPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur warna latar belakang
                        "-fx-background-radius: 20px;" + // Mengatur radius latar belakang
                        "-fx-border-radius: 20px;" + // Mengatur radius border
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" + // Mengatur efek bayangan
                        "-fx-padding: 40px;" // Mengatur padding
        );

        // Label judul dashboard
        Label titleLabel = new Label("Admin Dashboard"); // Membuat label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 34)); // Mengatur font label
        titleLabel.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks
        VBox.setMargin(titleLabel, new Insets(0, 0, 25, 0)); // Margin bawah

        // Tombol untuk masuk ke Manajemen Film
        Button filmManagementButton = new Button("Manajemen Film"); // Membuat tombol manajemen film
        filmManagementButton.setPrefSize(300, 70); // Ukuran tombol
        filmManagementButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font tombol
        filmManagementButton.setStyle(
                "-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65;" + // Mengatur gaya tombol
                        "-fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" // Mengatur efek bayangan
        );

        // Efek hover tombol manajemen film
        filmManagementButton.setOnMouseEntered(_ -> filmManagementButton.setStyle(filmManagementButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Efek saat mouse masuk
        filmManagementButton.setOnMouseExited(_ -> filmManagementButton.setStyle(
                "-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65;" + // Gaya saat mouse keluar
                        "-fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        ));

        // Aksi klik tombol: buka tampilan manajemen film
        filmManagementButton.setOnAction(_ -> controller.tampilFilmManagement());

        // Tombol untuk masuk ke Manajemen Pembayaran
        Button paymentMethodManagementButton = new Button("Manajemen Pembayaran"); // Membuat tombol manajemen pembayaran
        paymentMethodManagementButton.setPrefSize(300, 70); // Ukuran tombol
        paymentMethodManagementButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        paymentMethodManagementButton.setStyle(
                "-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65;" + // Mengatur gaya tombol
                        "-fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" // Mengatur efek bayangan
        );

        // Efek hover tombol pembayaran
        paymentMethodManagementButton.setOnMouseEntered(_ -> paymentMethodManagementButton.setStyle(paymentMethodManagementButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Efek saat mouse masuk
        paymentMethodManagementButton.setOnMouseExited(_ -> paymentMethodManagementButton.setStyle(
                "-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65;" + // Gaya saat mouse keluar
                        "-fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        ));

        // Aksi klik tombol: buka tampilan manajemen metode pembayaran
        paymentMethodManagementButton.setOnAction(_ -> controller.tampilPaymentMethodManagement());

        // Tambahkan semua komponen ke panel tengah
        optionsPanel.getChildren().addAll(
                titleLabel, // Menambahkan label judul
                filmManagementButton, // Menambahkan tombol manajemen film
                paymentMethodManagementButton // Menambahkan tombol manajemen pembayaran
        );

        // Set panel tengah sebagai isi utama dari layout
        root.setCenter(optionsPanel); // Mengatur panel sebagai bagian tengah dari layout
        BorderPane.setAlignment(optionsPanel, Pos.TOP_CENTER); // Mengatur posisi panel

        // Buat scene dengan ukuran tetap
        scene = new Scene(root, 850, 650); // Membuat scene dengan ukuran 850x650
    }

    // Metode untuk menampilkan tampilan dashboard admin di stage utama
    public void showView() {
        stage.setTitle("BioskopKuy! - Admin Dashboard"); // Mengatur judul stage
        stage.setScene(scene); // Mengatur scene
        stage.show(); // Menampilkan stage
    }
}
