package bioskopkuy.view.penonton;

import bioskopkuy.controller.BioskopController; // Import controller untuk mengelola interaksi
import bioskopkuy.model.BioskopDataStore; // Import model untuk mendapatkan data metode pembayaran
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
import javafx.scene.text.FontPosture; // Import untuk gaya font
import javafx.scene.text.FontWeight; // Import untuk berat font
import javafx.stage.Stage; // Import untuk stage
import java.util.List; // Import untuk list

public class PaymentSelectionView {
    private final BioskopController controller; // Controller untuk mengelola interaksi
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private VBox paymentMethodsContainer; // Kontainer untuk menampilkan metode pembayaran

    public PaymentSelectionView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        BorderPane root = new BorderPane(); // Membuat layout utama
        root.setPadding(new Insets(25)); // Menambahkan padding
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #5AAAA0, #7BD4C6);"); // Mengatur warna latar belakang

        HBox topPanel = new HBox(20); // Panel atas untuk judul dan tombol kembali
        topPanel.setAlignment(Pos.CENTER_LEFT); // Mengatur posisi konten
        Button backButton = new Button("Kembali"); // Tombol kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Mengatur font tombol
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya tombol
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse masuk
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F0; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse keluar
        backButton.setOnAction(_ -> controller.kembaliKeSeatSelectionView()); // Aksi saat tombol diklik

        Label titleLabel = new Label("Pilih Metode Pembayaran"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Mengatur font label
        titleLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        topPanel.getChildren().addAll(backButton, titleLabel); // Menambahkan tombol dan label ke panel atas
        root.setTop(topPanel); // Mengatur panel atas di layout
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0)); // Mengatur margin panel atas

        paymentMethodsContainer = new VBox(20); // Kontainer untuk metode pembayaran
        paymentMethodsContainer.setAlignment(Pos.CENTER); // Mengatur posisi konten
        paymentMethodsContainer.setPadding(new Insets(30)); // Menambahkan padding
        paymentMethodsContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur gaya kontainer
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );
        updateMetodePembayaranButtons(); // Memperbarui tombol metode pembayaran

        root.setCenter(paymentMethodsContainer); // Mengatur kontainer metode pembayaran di tengah layout

        scene = new Scene(root, 850, 650); // Membuat scene dengan ukuran tertentu
    }

    public void updateMetodePembayaranButtons() {
        paymentMethodsContainer.getChildren().clear(); // Menghapus semua tombol metode pembayaran yang ada

        List<BioskopDataStore.PaymentMethod> metodePembayaran = controller.getDaftarMetodePembayaran(); // Mengambil daftar metode pembayaran

        if (metodePembayaran.isEmpty()) { // Memeriksa apakah daftar metode pembayaran kosong
            Label noMethodLabel = new Label("Belum ada metode pembayaran tersedia."); // Label jika tidak ada metode pembayaran
            noMethodLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font label
            noMethodLabel.setTextFill(Color.DARKRED); // Mengatur warna teks
            paymentMethodsContainer.getChildren().add(noMethodLabel); // Menambahkan label ke kontainer
            return; // Keluar dari metode
        }

        for (BioskopDataStore.PaymentMethod metode : metodePembayaran) { // Iterasi melalui daftar metode pembayaran
            Button methodButton = new Button(metode.getName()); // Membuat tombol untuk metode pembayaran
            methodButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font tombol
            methodButton.setPrefSize(300, 70); // Mengatur ukuran tombol
            methodButton.setStyle("-fx-background-color: #E0F2F1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"); // Mengatur gaya tombol
            methodButton.setOnMouseEntered(_ -> methodButton.setStyle(methodButton.getStyle() + "-fx-background-color: #A3D8D0; -fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
            methodButton.setOnMouseExited(_ -> methodButton.setStyle("-fx-background-color: #E0F2F1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);")); // Gaya saat mouse keluar

            VBox methodDisplay = new VBox(8); // Kontainer untuk menampilkan tombol dan informasi diskon
            methodDisplay.setAlignment(Pos.CENTER); // Mengatur posisi konten
            methodDisplay.getChildren().add(methodButton); // Menambahkan tombol ke kontainer

            if (metode.getDiscountPercent() > 0) { // Memeriksa apakah ada diskon
                Label discountInfo = new Label(metode.getDiscountPercent() + "% Diskon - " + metode.getDiscountDescription()); // Label untuk informasi diskon
                discountInfo.setFont(Font.font("Verdana", FontPosture.ITALIC, 16)); // Mengatur font label
                discountInfo.setTextFill(Color.web("#28A745")); // Mengatur warna teks
                methodDisplay.getChildren().add(discountInfo); // Menambahkan label diskon ke kontainer
            }

            methodButton.setOnAction(_ -> controller.pilihMetodePembayaran(metode)); // Aksi saat tombol metode pembayaran diklik

            paymentMethodsContainer.getChildren().add(methodDisplay); // Menambahkan kontainer metode pembayaran ke kontainer utama
        }
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Pembayaran"); // Mengatur judul stage
        updateMetodePembayaranButtons(); // Memperbarui tombol metode pembayaran
        stage.setScene(scene); // Mengatur scene untuk stage
        stage.show(); // Menampilkan stage
    }
}
