package bioskopkuy.view.penonton;

import bioskopkuy.controller.BioskopController; // Import controller untuk mengelola interaksi
import bioskopkuy.model.BioskopModel; // Import model untuk mendapatkan data film dan kursi
import javafx.geometry.Insets; // Import untuk mengatur padding
import javafx.geometry.Pos; // Import untuk mengatur posisi
import javafx.scene.Scene; // Import untuk membuat scene
import javafx.scene.control.Button; // Import untuk tombol
import javafx.scene.control.Label; // Import untuk label
import javafx.scene.layout.BorderPane; // Import untuk layout BorderPane
import javafx.scene.layout.GridPane; // Import untuk layout GridPane
import javafx.scene.layout.HBox; // Import untuk layout HBox
import javafx.scene.layout.VBox; // Import untuk layout VBox
import javafx.scene.paint.Color; // Import untuk warna
import javafx.scene.text.Font; // Import untuk font
import javafx.scene.text.FontWeight; // Import untuk berat font
import javafx.stage.Stage; // Import untuk stage

import java.util.Set; // Import untuk Set

public class SeatSelectionView {
    private final BioskopController controller; // Controller untuk mengelola interaksi
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private Label filmJudulLabel; // Label untuk menampilkan judul film
    private Label kursiTerpilihLabel; // Label untuk menampilkan kursi yang dipilih
    private Label totalHargaLabel; // Label untuk menampilkan total harga
    private GridPane kursiGridPane; // GridPane untuk menampilkan kursi

    public SeatSelectionView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        BorderPane root = new BorderPane(); // Membuat layout utama
        root.setPadding(new Insets(25)); // Menambahkan padding
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur warna latar belakang
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );

        HBox topPanel = new HBox(20); // Panel atas untuk judul dan tombol kembali
        topPanel.setAlignment(Pos.CENTER_LEFT); // Mengatur posisi konten
        Button backButton = new Button("Kembali"); // Tombol kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Mengatur font tombol
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya tombol
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse masuk
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse keluar
        backButton.setOnAction(_ -> controller.kembaliKeFilmSelectionView()); // Aksi saat tombol diklik

        filmJudulLabel = new Label("Film: "); // Label untuk judul film
        filmJudulLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Mengatur font label
        filmJudulLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        topPanel.getChildren().addAll(backButton, filmJudulLabel); // Menambahkan tombol dan label ke panel atas
        root.setTop(topPanel); // Mengatur panel atas di layout
        BorderPane.setMargin(topPanel, new Insets(0, 0, 30, 0)); // Mengatur margin panel atas

        VBox centerContent = new VBox(25); // Kontainer untuk konten tengah
        centerContent.setAlignment(Pos.CENTER); // Mengatur posisi konten

        Label screenLabel = new Label("LAYAR"); // Label untuk layar
        screenLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font label
        screenLabel.setTextFill(Color.WHITE); // Mengatur warna teks
        screenLabel.setStyle("-fx-background-color: #4A8C80; -fx-padding: 12px 60px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"); // Mengatur gaya label
        screenLabel.setMaxWidth(400); // Mengatur lebar maksimum label
        screenLabel.setAlignment(Pos.CENTER); // Mengatur posisi label

        kursiGridPane = new GridPane(); // Membuat GridPane untuk kursi
        kursiGridPane.setHgap(10); // Mengatur jarak horizontal antar kursi
        kursiGridPane.setVgap(10); // Mengatur jarak vertikal antar kursi
        kursiGridPane.setPadding(new Insets(30)); // Menambahkan padding
        kursiGridPane.setAlignment(Pos.CENTER); // Mengatur posisi konten
        kursiGridPane.setStyle("-fx-background-color: #E0F2F1; -fx-border-color: #5AAAA0; -fx-border-width: 2px; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"); // Mengatur gaya GridPane

        updateKursiGrid(); // Memperbarui tampilan kursi

        centerContent.getChildren().addAll(screenLabel, kursiGridPane); // Menambahkan elemen ke konten tengah
        root.setCenter(centerContent); // Mengatur konten tengah di layout

        VBox bottomPanel = new VBox(12); // Kontainer untuk panel bawah
        bottomPanel.setAlignment(Pos.CENTER); // Mengatur posisi konten
        bottomPanel.setPadding(new Insets(15)); // Menambahkan padding
        bottomPanel.setStyle("-fx-background-color: #3A6D65; -fx-border-color: #2C3E50; -fx-border-width: 1px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"); // Mengatur gaya panel bawah

        kursiTerpilihLabel = new Label("Kursi Terpilih: Tidak Ada"); // Label untuk kursi yang dipilih
        kursiTerpilihLabel.setFont(Font.font("Verdana", 17)); // Mengatur font label
        kursiTerpilihLabel.setTextFill(Color.WHITE); // Mengatur warna teks

        totalHargaLabel = new Label("Total Harga: Rp0"); // Label untuk total harga
        totalHargaLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font label
        totalHargaLabel.setTextFill(Color.YELLOW); // Mengatur warna teks

        Button lanjutButton = new Button("Lanjut ke Pembayaran"); // Tombol untuk melanjutkan ke pembayaran
        lanjutButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        lanjutButton.setPrefSize(280, 55); // Mengatur ukuran tombol
        lanjutButton.setStyle("-fx-background-color: #FFD700;" + // Mengatur gaya tombol
                "-fx-text-fill: #2C3E50;" +
                "-fx-border-color: #B8860B;" +
                "-fx-border-width: 1.5px;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        lanjutButton.setOnMouseEntered(_ -> lanjutButton.setStyle(lanjutButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        lanjutButton.setOnMouseExited(_ -> lanjutButton.setStyle(lanjutButton.getStyle().replace("-fx-scale-y: 1.05; -fx-scale-x: 1.05;", ""))); // Gaya saat mouse keluar
        lanjutButton.setOnAction(_ -> controller.lanjutKePembayaran()); // Aksi saat tombol diklik

        bottomPanel.getChildren().addAll(kursiTerpilihLabel, totalHargaLabel, lanjutButton); // Menambahkan elemen ke panel bawah
        root.setBottom(bottomPanel); // Mengatur panel bawah di layout
        BorderPane.setMargin(bottomPanel, new Insets(25, 0, 0, 0)); // Mengatur margin panel bawah

        scene = new Scene(root, 900, 700); // Membuat scene dengan ukuran tertentu
    }

    public void setFilmJudul(String judul) {
        filmJudulLabel.setText("Film: " + judul + " - " + controller.getModel().getJamTerpilih()); // Mengatur judul film
    }

    public void updateKursiGrid() {
        kursiGridPane.getChildren().clear(); // Menghapus semua kursi yang ada di GridPane

        controller.getModel().getAllKursiNames(); // Mengambil semua nama kursi
        BioskopModel.Film currentFilm = controller.getModel().getFilmTerpilih(); // Mengambil film yang dipilih
        String currentJam = controller.getModel().getJamTerpilih(); // Mengambil jam yang dipilih
        Set<String> selectedKursi = controller.getModel().getKursiTerpilih(); // Mengambil kursi yang dipilih

        char[] rows = {'A', 'B', 'C', 'D'}; // Baris kursi
        int colCount = 10; // Jumlah kolom kursi

        for (int rowIdx = 0; rowIdx < rows.length; rowIdx++) { // Iterasi melalui baris
            char rowChar = rows[rowIdx]; // Mengambil karakter baris
            for (int colIdx = 0; colIdx < colCount; colIdx++) { // Iterasi melalui kolom
                String kursiName = String.valueOf(rowChar) + (colIdx + 1); // Membuat nama kursi
                Button seatButton = new Button(kursiName); // Membuat tombol untuk kursi
                seatButton.setPrefSize(55, 45); // Mengatur ukuran tombol
                seatButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15)); // Mengatur font tombol

                String baseStyle = "-fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"; // Gaya dasar tombol

                if (currentFilm != null && currentJam != null && controller.getModel().isKursiTerisi(currentFilm, currentJam, kursiName)) { // Memeriksa apakah kursi terisi
                    seatButton.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-border-color: #8D2A1F;" + baseStyle); // Gaya untuk kursi terisi
                    seatButton.setDisable(true); // Menonaktifkan tombol
                } else if (selectedKursi.contains(kursiName)) { // Memeriksa apakah kursi dipilih
                    seatButton.setStyle("-fx-background-color: #FFEB3B; -fx-text-fill: #2C3E50; -fx-border-color: #FFC107; -fx-border-width: 2px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" + baseStyle.replace("-fx-border-width: 1px;", "")); // Gaya untuk kursi yang dipilih
                } else { // Jika kursi tidak terisi dan tidak dipilih
                    seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #BDC3C7;" + baseStyle); // Gaya untuk kursi yang tersedia
                    seatButton.setOnMouseEntered(_ -> seatButton.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);")); // Gaya saat mouse masuk
                    seatButton.setOnMouseExited(_ -> seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #BDC3C7;" + baseStyle)); // Gaya saat mouse keluar
                }

                seatButton.setOnAction(_ -> controller.toggleKursiTerpilih(kursiName)); // Aksi saat tombol kursi diklik

                kursiGridPane.add(seatButton, colIdx, rowIdx); // Menambahkan tombol kursi ke GridPane
            }
        }
    }

    public void updateKursiTerpilihDisplay(Set<String> kursiTerpilih, String totalHargaSebelumDiskonFormatted) {
        if (kursiTerpilih.isEmpty()) { // Memeriksa apakah tidak ada kursi yang dipilih
            kursiTerpilihLabel.setText("Kursi Terpilih: Tidak Ada"); // Mengatur label kursi terpilih
        } else {
            kursiTerpilihLabel.setText("Kursi Terpilih: " + String.join(", ", kursiTerpilih)); // Mengatur label kursi terpilih
        }
        totalHargaLabel.setText("Total Harga: " + totalHargaSebelumDiskonFormatted); // Mengatur label total harga
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Kursi"); // Mengatur judul stage
        updateKursiGrid(); // Memperbarui tampilan kursi
        stage.setScene(scene); // Mengatur scene untuk stage
        stage.show(); // Menampilkan stage
    }
}
