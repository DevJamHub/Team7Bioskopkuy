package bioskopkuy.view.login.penonton;

import bioskopkuy.controller.BioskopController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;

public class PaymentInputView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private Label totalHargaLabel;
    private Label diskonDisplayLabel;
    private TextField uangDibayarField;

    private Label totalHargaSebelumDiskonLabel;
    private Label metodePembayaranLabel;

    public PaymentInputView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom left, #5AAAA0, #7BD4C6);");

        HBox topPanel = new HBox(20);
        topPanel.setAlignment(Pos.CENTER_LEFT);
        Button backButton = new Button("Kembali");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> controller.kembaliKePaymentSelectionView());

        Label titleLabel = new Label("Detail Pembayaran");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, titleLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0));

        VBox centerContent = new VBox(25); // Spasi vertikal antar elemen
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));
        // Gaya panel konten
        centerContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        metodePembayaranLabel = new Label("Metode Pembayaran: -");
        metodePembayaranLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        metodePembayaranLabel.setTextFill(Color.web("#3A6D65"));

        totalHargaSebelumDiskonLabel = new Label("Harga Awal: Rp0");
        totalHargaSebelumDiskonLabel.setFont(Font.font("Verdana", 18));
        totalHargaSebelumDiskonLabel.setTextFill(Color.web("#5C6F7E")); // Warna teks abu-abu gelap

        diskonDisplayLabel = new Label("Diskon: 0% (Tidak Ada Diskon)");
        diskonDisplayLabel.setFont(Font.font("Verdana", 17));
        diskonDisplayLabel.setTextFill(Color.GREEN); // Tetap hijau untuk diskon

        totalHargaLabel = new Label("Total Bayar: Rp0");
        totalHargaLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        totalHargaLabel.setTextFill(Color.web("#2C3E50")); // Lebih menonjol

        Label uangDibayarLabel = new Label("Jumlah Uang Dibayar (Rp):");
        uangDibayarLabel.setFont(Font.font("Verdana", 19));
        uangDibayarField = new TextField();
        uangDibayarField.setPromptText("Masukkan jumlah uang Anda");
        uangDibayarField.setPrefWidth(250);
        uangDibayarField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");
        uangDibayarField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([.]?\\d{0,2})?")) { // Allow numbers and one decimal point
                uangDibayarField.setText(oldValue);
            }
        });

        Button bayarButton = new Button("Bayar Sekarang");
        bayarButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        bayarButton.setPrefSize(250, 60);
        // Gaya tombol bayar
        bayarButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-border-color: #218838; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        bayarButton.setOnMouseEntered(e -> bayarButton.setStyle(bayarButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        bayarButton.setOnMouseExited(e -> bayarButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-border-color: #218838; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        bayarButton.setOnAction(_ -> handlePembayaran());

        centerContent.getChildren().addAll(
                metodePembayaranLabel,
                totalHargaSebelumDiskonLabel,
                diskonDisplayLabel,
                totalHargaLabel,
                uangDibayarLabel,
                uangDibayarField,
                bayarButton
        );
        root.setCenter(centerContent);

        scene = new Scene(root, 850, 700);
    } // <-- Ini adalah penutup untuk metode initialize()

    public void setTotalHargaDisplay(String totalHargaFormatted) {
        this.totalHargaLabel.setText("Total Bayar: " + totalHargaFormatted);
        this.totalHargaSebelumDiskonLabel.setText("Harga Awal: " + controller.getModel().getTotalHargaSebelumDiskonFormatted());

        if (controller.getModel().getMetodePembayaranTerpilih() != null) {
            metodePembayaranLabel.setText("Metode Pembayaran: " + controller.getModel().getMetodePembayaranTerpilih().getName());
        } else {
            metodePembayaranLabel.setText("Metode Pembayaran: -");
        }

        controller.getModel().hitungTotalHarga();
    } // <-- Ini adalah penutup untuk metode setTotalHargaDisplay()

    public void updateDiskonDisplay(int persen, String keterangan) {
        if (persen > 0) {
            diskonDisplayLabel.setText("Diskon: " + persen + "% (" + keterangan + ")");
            diskonDisplayLabel.setTextFill(Color.GREEN);
        } else {
            diskonDisplayLabel.setText("Diskon: 0% (Tidak Ada Diskon)");
            diskonDisplayLabel.setTextFill(Color.GRAY);
        }
    }

    private void handlePembayaran() {
        String uangText = uangDibayarField.getText().trim().replace(',', '.');
        if (uangText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Masukkan jumlah uang yang dibayarkan.");
            return;
        }

        try {
            double uangDibayar = Double.parseDouble(uangText);

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Konfirmasi Pembelian Tiket");
            confirmationAlert.setHeaderText("Detail Pembelian Anda:");
            String detailPembelian =
                    "Film: " + controller.getModel().getFilmTerpilih().getJudul() + "\n" +
                            "Jam: " + controller.getModel().getJamTerpilih() + "\n" +
                            "Kursi: " + String.join(", ", controller.getModel().getKursiTerpilih()) + "\n" +
                            "Metode Pembayaran: " + controller.getModel().getMetodePembayaranTerpilih().getName() + "\n" +
                            "Diskon: " + controller.getModel().getDiskonPersenDariMetodeTerpilih() + "% (" + controller.getModel().getDiskonKeteranganDariMetodeTerpilih() + ")\n" +
                            "Harga Awal: " + controller.getModel().getTotalHargaSebelumDiskonFormatted() + "\n" +
                            "Total Bayar: " + controller.getModel().getTotalHargaSetelahDiskonFormatted() + "\n" +
                            "Uang Dibayar: Rp" + String.format("%,.0f", uangDibayar) + "\n\n" +
                            "Apakah Anda yakin untuk melanjutkan pembelian tiket ini?";
            confirmationAlert.setContentText(detailPembelian);

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.prosesPembayaran(uangDibayar);
                uangDibayarField.clear(); // Bersihkan field setelah pembayaran
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Pembayaran Dibatalkan", "Pembayaran telah dibatalkan.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Jumlah uang harus berupa angka yang valid.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pembayaran");
        // Pastikan info pembayaran terupdate saat view ditampilkan
        setTotalHargaDisplay(controller.getModel().getTotalHargaSetelahDiskonFormatted());
        updateDiskonDisplay(controller.getModel().getDiskonPersenDariMetodeTerpilih(), controller.getModel().getDiskonKeteranganDariMetodeTerpilih());
        stage.setScene(scene);
        stage.show();
    }
} // <-- Ini adalah penutup untuk kelas PaymentInputView