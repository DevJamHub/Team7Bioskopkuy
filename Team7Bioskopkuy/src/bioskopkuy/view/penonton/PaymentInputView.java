package bioskopkuy.view.penonton;

import bioskopkuy.controller.BioskopController; // Import controller untuk mengelola interaksi
import javafx.geometry.Insets; // Import untuk mengatur padding
import javafx.geometry.Pos; // Import untuk mengatur posisi
import javafx.scene.Scene; // Import untuk membuat scene
import javafx.scene.control.Alert; // Import untuk alert dialog
import javafx.scene.control.Button; // Import untuk tombol
import javafx.scene.control.ButtonType; // Import untuk tipe tombol
import javafx.scene.control.Label; // Import untuk label
import javafx.scene.control.TextField; // Import untuk text field
import javafx.scene.layout.BorderPane; // Import untuk layout BorderPane
import javafx.scene.layout.HBox; // Import untuk layout HBox
import javafx.scene.layout.VBox; // Import untuk layout VBox
import javafx.scene.paint.Color; // Import untuk warna
import javafx.scene.text.Font; // Import untuk font
import javafx.scene.text.FontWeight; // Import untuk berat font
import javafx.stage.Stage; // Import untuk stage
import java.util.Optional; // Import untuk Optional

public class PaymentInputView {
    private final BioskopController controller; // Controller untuk mengelola interaksi
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private Label totalHargaLabel; // Label untuk menampilkan total harga
    private Label diskonDisplayLabel; // Label untuk menampilkan diskon
    private TextField uangDibayarField; // TextField untuk input jumlah uang yang dibayarkan

    private Label totalHargaSebelumDiskonLabel; // Label untuk menampilkan harga sebelum diskon
    private Label metodePembayaranLabel; // Label untuk menampilkan metode pembayaran

    public PaymentInputView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        BorderPane root = new BorderPane(); // Membuat layout utama
        root.setPadding(new Insets(25)); // Menambahkan padding
        root.setStyle("-fx-background-color: linear-gradient(to bottom left, #5AAAA0, #7BD4C6);"); // Mengatur warna latar belakang

        HBox topPanel = new HBox(20); // Panel atas untuk judul dan tombol kembali
        topPanel.setAlignment(Pos.CENTER_LEFT); // Mengatur posisi konten
        Button backButton = new Button("Kembali"); // Tombol kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Mengatur font tombol
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya tombol
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse masuk
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse keluar
        backButton.setOnAction(_ -> controller.kembaliKePaymentSelectionView()); // Aksi saat tombol diklik

        Label titleLabel = new Label("Detail Pembayaran"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Mengatur font label
        titleLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        topPanel.getChildren().addAll(backButton, titleLabel); // Menambahkan tombol dan label ke panel atas
        root.setTop(topPanel); // Mengatur panel atas di layout
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0)); // Mengatur margin panel atas

        VBox centerContent = getVBox(); // Mendapatkan layout VBox untuk konten tengah

        metodePembayaranLabel = new Label("Metode Pembayaran: -"); // Label untuk metode pembayaran
        metodePembayaranLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font label
        metodePembayaranLabel.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks

        totalHargaSebelumDiskonLabel = new Label("Harga Awal: Rp0"); // Label untuk harga awal
        totalHargaSebelumDiskonLabel.setFont(Font.font("Verdana", 18)); // Mengatur font label
        totalHargaSebelumDiskonLabel.setTextFill(Color.web("#5C6F7E")); // Mengatur warna teks

        diskonDisplayLabel = new Label("Diskon: 0% (Tidak Ada Diskon)"); // Label untuk diskon
        diskonDisplayLabel.setFont(Font.font("Verdana", 17)); // Mengatur font label
        diskonDisplayLabel.setTextFill(Color.GREEN); // Mengatur warna teks

        totalHargaLabel = new Label("Total Bayar: Rp0"); // Label untuk total bayar
        totalHargaLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 26)); // Mengatur font label
        totalHargaLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks

        Label uangDibayarLabel = new Label("Jumlah Uang Dibayar (Rp):"); // Label untuk input uang dibayar
        uangDibayarLabel.setFont(Font.font("Verdana", 19)); // Mengatur font label
        uangDibayarField = new TextField(); // Inisialisasi TextField untuk input uang dibayar
        uangDibayarField.setPromptText("Masukkan jumlah uang Anda"); // Placeholder untuk TextField
        uangDibayarField.setPrefWidth(250); // Mengatur lebar TextField
        uangDibayarField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya TextField
        uangDibayarField.textProperty().addListener((_, oldValue, newValue) -> { // Listener untuk validasi input
            if (!newValue.matches("\\d*([.]?\\d{0,2})?")) { // Memastikan input hanya angka
                uangDibayarField.setText(oldValue); // Mengembalikan nilai lama jika tidak valid
            }
        });

        Button bayarButton = new Button("Bayar Sekarang"); // Tombol untuk melakukan pembayaran
        bayarButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Mengatur font tombol
        bayarButton.setPrefSize(250, 60); // Mengatur ukuran tombol
        bayarButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-border-color: #218838; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"); // Mengatur gaya tombol
        bayarButton.setOnMouseEntered(_ -> bayarButton.setStyle(bayarButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        bayarButton.setOnMouseExited(_ -> bayarButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-border-color: #218838; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);")); // Gaya saat mouse keluar
        bayarButton.setOnAction(_ -> handlePembayaran()); // Aksi saat tombol diklik

        centerContent.getChildren().addAll( // Menambahkan elemen ke konten tengah
                metodePembayaranLabel,
                totalHargaSebelumDiskonLabel,
                diskonDisplayLabel,
                totalHargaLabel,
                uangDibayarLabel,
                uangDibayarField,
                bayarButton
        );
        root.setCenter(centerContent); // Mengatur konten tengah di layout

        scene = new Scene(root, 850, 700); // Membuat scene dengan ukuran tertentu
    }

    private static VBox getVBox() {
        VBox centerContent = new VBox(25); // Membuat layout VBox untuk konten tengah
        centerContent.setAlignment(Pos.CENTER); // Mengatur posisi konten
        centerContent.setPadding(new Insets(40)); // Menambahkan padding
        centerContent.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur gaya konten tengah
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );
        return centerContent; // Mengembalikan konten tengah
    }

    public void setTotalHargaDisplay(String totalHargaFormatted) {
        this.totalHargaLabel.setText("Total Bayar: " + totalHargaFormatted); // Mengatur total bayar
        this.totalHargaSebelumDiskonLabel.setText("Harga Awal: " + controller.getModel().getTotalHargaSebelumDiskonFormatted()); // Mengatur harga awal

        if (controller.getModel().getMetodePembayaranTerpilih() != null) { // Memeriksa apakah metode pembayaran terpilih
            metodePembayaranLabel.setText("Metode Pembayaran: " + controller.getModel().getMetodePembayaranTerpilih().getName()); // Mengatur metode pembayaran
        } else {
            metodePembayaranLabel.setText("Metode Pembayaran: -"); // Jika tidak ada metode pembayaran
        }

        controller.getModel().hitungTotalHarga(); // Menghitung total harga
    }

    public void updateDiskonDisplay(int persen, String keterangan) {
        if (persen > 0) { // Memeriksa apakah ada diskon
            diskonDisplayLabel.setText("Diskon: " + persen + "% (" + keterangan + ")"); // Mengatur label diskon
            diskonDisplayLabel.setTextFill(Color.GREEN); // Mengatur warna teks
        } else {
            diskonDisplayLabel.setText("Diskon: 0% (Tidak Ada Diskon)"); // Jika tidak ada diskon
            diskonDisplayLabel.setTextFill(Color.GRAY); // Mengatur warna teks
        }
    }

    private void handlePembayaran() {
        String uangText = uangDibayarField.getText().trim().replace(',', '.'); // Mengambil input uang dibayar
        if (uangText.isEmpty()) { // Memeriksa apakah input kosong
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Masukkan jumlah uang yang dibayarkan."); // Menampilkan peringatan
            return; // Keluar dari metode
        }

        try {
            double uangDibayar = Double.parseDouble(uangText); // Parsing input ke double

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION); // Membuat dialog konfirmasi
            confirmationAlert.setTitle("Konfirmasi Pembelian Tiket"); // Judul dialog
            confirmationAlert.setHeaderText("Detail Pembelian Anda:"); // Header dialog
            String detailPembelian = // Menyusun detail pembelian
                    "Film: " + controller.getModel().getFilmTerpilih().getJudul() + "\n" +
                            "Jam: " + controller.getModel().getJamTerpilih() + "\n" +
                            "Kursi: " + String.join(", ", controller.getModel().getKursiTerpilih()) + "\n" +
                            "Metode Pembayaran: " + controller.getModel().getMetodePembayaranTerpilih().getName() + "\n" +
                            "Diskon: " + controller.getModel().getDiskonPersenDariMetodeTerpilih() + "% (" + controller.getModel().getDiskonKeteranganDariMetodeTerpilih() + ")\n" +
                            "Harga Awal: " + controller.getModel().getTotalHargaSebelumDiskonFormatted() + "\n" +
                            "Total Bayar: " + controller.getModel().getTotalHargaSetelahDiskonFormatted() + "\n" +
                            "Uang Dibayar: Rp" + String.format("%,.0f", uangDibayar) + "\n\n" +
                            "Apakah Anda yakin untuk melanjutkan pembelian tiket ini?";
            confirmationAlert.setContentText(detailPembelian); // Mengatur konten dialog

            Optional<ButtonType> result = confirmationAlert.showAndWait(); // Menampilkan dialog dan menunggu respon

            if (result.isPresent() && result.get() == ButtonType.OK) { // Jika user mengkonfirmasi
                controller.prosesPembayaran(uangDibayar); // Memproses pembayaran
                uangDibayarField.clear(); // Menghapus field input uang dibayar
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Pembayaran Dibatalkan", "Pembayaran telah dibatalkan."); // Menampilkan informasi jika pembayaran dibatalkan
            }

        } catch (NumberFormatException e) { // Menangani exception jika parsing gagal
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Jumlah uang harus berupa angka yang valid."); // Menampilkan error
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type); // Membuat alert
        alert.setTitle(title); // Mengatur judul
        alert.setHeaderText(null); // Tidak ada header
        alert.setContentText(message); // Mengatur pesan
        alert.showAndWait(); // Menampilkan alert
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pembayaran"); // Mengatur judul stage
        setTotalHargaDisplay(controller.getModel().getTotalHargaSetelahDiskonFormatted()); // Mengatur total harga
        updateDiskonDisplay(controller.getModel().getDiskonPersenDariMetodeTerpilih(), controller.getModel().getDiskonKeteranganDariMetodeTerpilih()); // Mengatur diskon
        stage.setScene(scene); // Mengatur scene untuk stage
        stage.show(); // Menampilkan stage
    }
}
