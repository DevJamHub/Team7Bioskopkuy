package bioskopkuy.view.penonton;

// Mengimpor kelas-kelas yang diperlukan dari JavaFX dan paket lainnya
import bioskopkuy.controller.BioskopController; // Mengimpor controller untuk mengelola logika aplikasi
import bioskopkuy.model.BioskopModel; // Mengimpor model untuk data film dan transaksi
import javafx.geometry.Insets; // Mengimpor kelas untuk mengatur padding dan margin
import javafx.geometry.Pos; // Mengimpor kelas untuk mengatur posisi elemen
import javafx.scene.Scene; // Mengimpor kelas untuk membuat scene
import javafx.scene.control.*; // Mengimpor semua kontrol UI seperti Button, Label, dll.
import javafx.scene.image.Image; // Mengimpor kelas untuk menangani gambar
import javafx.scene.image.ImageView; // Mengimpor kelas untuk menampilkan gambar
import javafx.scene.layout.*; // Mengimpor semua layout seperti VBox, HBox, BorderPane, dll.
import javafx.scene.paint.Color; // Mengimpor kelas untuk menangani warna
import javafx.scene.text.Font; // Mengimpor kelas untuk mengatur font
import javafx.scene.text.FontPosture; // Mengimpor kelas untuk mengatur gaya font (miring)
import javafx.scene.text.FontWeight; // Mengimpor kelas untuk mengatur ketebalan font
import javafx.stage.Stage; // Mengimpor kelas untuk membuat jendela aplikasi

import java.util.List; // Mengimpor kelas List untuk menyimpan koleksi data

public class FilmSelectionView {
    private final BioskopController controller; // Controller untuk mengelola interaksi
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private VBox filmListPanel; // Panel untuk menampilkan daftar film

    public FilmSelectionView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        BorderPane root = new BorderPane(); // Membuat layout utama
        root.setPadding(new Insets(25)); // Menambahkan padding
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" +
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );

        HBox topPanel = new HBox(20); // Panel atas untuk judul dan tombol
        topPanel.setAlignment(Pos.CENTER_LEFT); // Menyusun elemen di kiri

        Button backButton = getBackButtonStyled(); // Mendapatkan tombol kembali yang sudah distyling
        Label titleLabel = new Label("Pilih Film dan Jam Tayang"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Mengatur font
        titleLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks

        // Ikon troli checkout
        ImageView cartIcon = new ImageView(new Image("file:src/bioskopkuy/view/images/troli.jpeg")); // Mengambil gambar troli
        cartIcon.setFitWidth(80); // Mengatur lebar ikon
        cartIcon.setFitHeight(80); // Mengatur tinggi ikon
        cartIcon.setPreserveRatio(true); // Mempertahankan rasio gambar
        cartIcon.setStyle("-fx-cursor: hand;"); // Mengubah kursor saat hover
        cartIcon.setOnMouseClicked(_ -> showCartWindow()); // Menampilkan jendela troli saat diklik

        Region spacer = new Region(); // Ruang kosong untuk mengisi ruang
        HBox.setHgrow(spacer, Priority.ALWAYS); // Mengizinkan spacer untuk tumbuh

        topPanel.getChildren().addAll(backButton, titleLabel, spacer, cartIcon); // Menambahkan elemen ke panel atas
        root.setTop(topPanel); // Menetapkan panel atas ke root
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0)); // Mengatur margin untuk panel atas

        filmListPanel = new VBox(20); // Panel untuk daftar film
        filmListPanel.setAlignment(Pos.TOP_CENTER); // Menyusun elemen di tengah atas
        filmListPanel.setPadding(new Insets(25)); // Menambahkan padding
        filmListPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        ScrollPane scrollPane = new ScrollPane(filmListPanel); // Membuat ScrollPane untuk panel film
        scrollPane.setFitToWidth(true); // Mengatur agar lebar sesuai
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Mengatur gaya

        root.setCenter(scrollPane); // Menetapkan ScrollPane ke tengah root

        scene = new Scene(root, 850, 650); // Membuat scene dengan ukuran tertentu
    }

    private Button getBackButtonStyled() {
        Button backButton = new Button("Kembali"); // Membuat tombol kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Mengatur font
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya

        // Mengatur efek hover untuk tombol kembali
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));

        // Menangani aksi saat tombol diklik
        backButton.setOnAction(_ -> {
            if (controller.isAdminLoggedIn()) {
                controller.kembaliKeAdminDashboard(); // Kembali ke dashboard admin
            } else {
                controller.kembaliKeMainView(); // Kembali ke tampilan utama
            }
        });
        return backButton; // Mengembalikan tombol kembali
    }

    public void refreshFilmList() {
        filmListPanel.getChildren().clear(); // Menghapus semua elemen dari panel film

        List<BioskopModel.Film> films = controller.getModel().getAll(); // Mengambil daftar film dari model
        if (films.isEmpty()) { // Jika tidak ada film
            Label noFilmLabel = new Label("Belum ada film yang tersedia saat ini."); // Label untuk tidak ada film
            noFilmLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font
            noFilmLabel.setTextFill(Color.DARKRED); // Mengatur warna teks
            filmListPanel.getChildren().add(noFilmLabel); // Menambahkan label ke panel film
            return; // Keluar dari metode
        }

        // Menampilkan setiap film dalam daftar
        for (BioskopModel.Film film : films) {
            HBox filmCard = getHBox(); // Mendapatkan panel untuk film

            ImageView posterImageView = new ImageView(); // Membuat ImageView untuk poster film
            posterImageView.setFitWidth(100); // Mengatur lebar poster
            posterImageView.setFitHeight(150); // Mengatur tinggi poster
            posterImageView.setPreserveRatio(true); // Mempertahankan rasio gambar
            posterImageView.setStyle("-fx-border-color: #B2D8D3; -fx-border-width: 1px;"); // Mengatur gaya border

            Image poster = film.getPosterImage(); // Mengambil gambar poster
            if (poster != null) {
                posterImageView.setImage(poster); // Mengatur gambar poster jika ada
            } else {
                System.err.println("Poster untuk film '" + film.getJudul() + "' tidak dapat dimuat (getPosterImage() mengembalikan null)."); // Menampilkan pesan error jika poster tidak ada
            }

            VBox filmInfo = new VBox(8); // Panel untuk informasi film
            filmInfo.setAlignment(Pos.CENTER_LEFT); // Menyusun elemen di kiri

            Label filmTitleLabel = new Label(film.getJudul()); // Label untuk judul film
            filmTitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font
            filmTitleLabel.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks

            Label filmPriceLabel = new Label("Harga: Rp" + String.format("%,.0f", film.getHargaDasar()) + "/kursi"); // Label untuk harga film
            filmPriceLabel.setFont(Font.font("Verdana", 16)); // Mengatur font
            filmPriceLabel.setTextFill(Color.web("#5C6F7E")); // Mengatur warna teks

            HBox jamTayangButtons = new HBox(15); // Panel untuk tombol jam tayang
            jamTayangButtons.setAlignment(Pos.CENTER_LEFT); // Menyusun elemen di kiri

            if (film.getJamTayang().isEmpty()) { // Jika tidak ada jam tayang
                Label noJamLabel = new Label("Tidak ada jam tayang tersedia."); // Label untuk tidak ada jam tayang
                noJamLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 15)); // Mengatur font
                noJamLabel.setTextFill(Color.GRAY); // Mengatur warna teks
                jamTayangButtons.getChildren().add(noJamLabel); // Menambahkan label ke panel jam tayang
            } else {
                // Menampilkan tombol untuk setiap jam tayang
                for (String jam : film.getJamTayang()) {
                    Button jamButton = new Button(jam); // Membuat tombol untuk jam tayang
                    jamButton.setFont(Font.font("Verdana", 17)); // Mengatur font
                    jamButton.setPrefWidth(90); // Mengatur lebar tombol
                    jamButton.setPrefHeight(40); // Mengatur tinggi tombol
                    jamButton.setStyle("-fx-background-color: #FFFFFF;" +
                            "-fx-text-fill: #2C3E50;" +
                            "-fx-border-color: #5AAAA0;" +
                            "-fx-border-width: 1px;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"); // Mengatur gaya tombol

                    // Mengatur efek hover untuk tombol jam tayang
                    jamButton.setOnMouseEntered(_ -> jamButton.setStyle(jamButton.getStyle() + "-fx-background-color: #A3D8D0; -fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
                    jamButton.setOnMouseExited(_ -> jamButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"));

                    jamButton.setOnAction(_ -> controller.pilihFilmDanJam(film, jam)); // Menangani aksi saat tombol jam tayang diklik
                    jamTayangButtons.getChildren().add(jamButton); // Menambahkan tombol ke panel jam tayang
                }
            }

            filmInfo.getChildren().addAll(filmTitleLabel, filmPriceLabel, jamTayangButtons); // Menambahkan informasi film ke panel
            filmCard.getChildren().addAll(posterImageView, filmInfo); // Menambahkan poster dan informasi film ke kartu film
            filmListPanel.getChildren().add(filmCard); // Menambahkan kartu film ke panel daftar film
        }
    }

    private static HBox getHBox() {
        HBox filmCard = new HBox(15); // Membuat panel horizontal untuk kartu film
        filmCard.setAlignment(Pos.CENTER_LEFT); // Menyusun elemen di kiri
        filmCard.setPadding(new Insets(15)); // Menambahkan padding
        filmCard.setStyle("-fx-background-color: #E0F2F1;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);"); // Mengatur gaya
        return filmCard; // Mengembalikan kartu film
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Film"); // Mengatur judul stage
        refreshFilmList(); // Memperbarui daftar film
        stage.setScene(scene); // Menetapkan scene ke stage
        stage.show(); // Menampilkan stage
    }

    private void showCartWindow() {
        Stage cartStage = new Stage(); // Membuat stage baru untuk jendela troli
        cartStage.setTitle("Riwayat Pesanan Anda"); // Mengatur judul jendela troli

        VBox cartContent = new VBox(15); // Panel untuk konten troli
        cartContent.setPadding(new Insets(20)); // Menambahkan padding
        cartContent.setStyle("-fx-background-color: #ffffff; -fx-border-color: #5AAAA0; -fx-border-width: 2px; -fx-background-radius: 10px;"); // Mengatur gaya

        Label headerLabel = new Label("Riwayat Pesanan"); // Label untuk header troli
        headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font
        cartContent.getChildren().add(headerLabel); // Menambahkan header ke konten troli

        // Ambil riwayat dari Model menggunakan getTransactionHistory()
        List<BioskopModel.TransactionReceipt> receipts = controller.getTransactionHistory(); // Mengambil riwayat transaksi

        if (receipts.isEmpty()) { // Jika tidak ada riwayat transaksi
            Label emptyLabel = new Label("Belum ada pesanan."); // Label untuk tidak ada pesanan
            emptyLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 16)); // Mengatur font
            cartContent.getChildren().add(emptyLabel); // Menambahkan label ke konten troli
        } else {
            // Menampilkan setiap resi transaksi
            for (BioskopModel.TransactionReceipt receipt : receipts) {
                VBox receiptBox = new VBox(5); // Panel untuk setiap resi
                receiptBox.setStyle("-fx-background-color: #F0F8FF; -fx-padding: 10px; -fx-border-color: #ADD8E6; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya

                Label filmLabel = new Label("Film: " + receipt.getFilmTitle()); // Label untuk judul film
                filmLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14)); // Mengatur font
                filmLabel.setTextFill(Color.BLACK); // Mengatur warna teks menjadi hitam

                Label timeLabel = new Label("Jam: " + receipt.getShowtime()); // Label untuk jam tayang
                timeLabel.setTextFill(Color.BLACK); // Mengatur warna teks menjadi hitam

                Label seatsLabel = new Label("Kursi: " + String.join(", ", receipt.getSeats())); // Label untuk kursi yang dipesan
                seatsLabel.setTextFill(Color.BLACK); // Mengatur warna teks menjadi hitam

                Label priceLabel = new Label("Total: Rp" + String.format("%,.0f", receipt.getTotalPrice())); // Label untuk total harga
                priceLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14)); // Mengatur font
                priceLabel.setTextFill(Color.web("#00695C")); // Mengatur warna teks

                HBox actionButtons = new HBox(10); // Panel untuk tombol aksi
                actionButtons.setAlignment(Pos.CENTER_RIGHT); // Menyusun elemen di kanan

                Button printButton = new Button("Cetak"); // Tombol untuk mencetak resi
                printButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"); // Mengatur gaya tombol
                printButton.setOnAction(_ -> controller.cetakResi(receipt)); // Menangani aksi saat tombol diklik

                Button deleteButton = new Button("Hapus"); // Tombol untuk menghapus resi
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;"); // Mengatur gaya tombol
                deleteButton.setOnAction(_ -> {
                    // Hapus berdasarkan ID unik
                    controller.hapusResi(receipt.getId()); // Menghapus resi berdasarkan ID
                    // Refresh jendela troli
                    cartStage.close(); // Menutup jendela troli
                    showCartWindow(); // Menampilkan kembali jendela troli
                });

                actionButtons.getChildren().addAll(printButton, deleteButton); // Menambahkan tombol aksi ke panel
                receiptBox.getChildren().addAll(filmLabel, timeLabel, seatsLabel, priceLabel, actionButtons); // Menambahkan elemen ke panel resi
                cartContent.getChildren().add(receiptBox); // Menambahkan panel resi ke konten troli
            }
        }

        ScrollPane historyScrollPane = new ScrollPane(cartContent); // Membuat ScrollPane untuk konten troli
        historyScrollPane.setFitToWidth(true); // Mengatur agar lebar sesuai
        historyScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Mengatur gaya

        Button closeBtn = new Button("Kembali"); // Tombol untuk menutup jendela troli
        closeBtn.setOnAction(_ -> cartStage.close()); // Menangani aksi saat tombol diklik

        BorderPane root = new BorderPane(); // Membuat layout utama untuk jendela troli
        root.setCenter(historyScrollPane); // Menetapkan ScrollPane ke tengah layout
        root.setBottom(closeBtn); // Menetapkan tombol kembali ke bagian bawah layout
        BorderPane.setMargin(closeBtn, new Insets(10, 20, 10, 20)); // Mengatur margin untuk tombol
        root.setPadding(new Insets(10)); // Menambahkan padding ke layout
        root.setStyle("-fx-background-color: #FFFFFF;"); // Mengatur warna latar belakang

        Scene cartScene = new Scene(root, 450, 500); // Membuat scene untuk jendela troli dengan ukuran tertentu
        cartStage.setScene(cartScene); // Menetapkan scene ke stage troli
        cartStage.show(); // Menampilkan jendela troli
    }
}
