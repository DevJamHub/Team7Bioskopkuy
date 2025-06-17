package bioskopkuy.view.login.penonton;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopDataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class PaymentSelectionView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private VBox paymentMethodsContainer;

    public PaymentSelectionView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #5AAAA0, #7BD4C6);"); // Latar belakang gradien

        HBox topPanel = new HBox(20);
        topPanel.setAlignment(Pos.CENTER_LEFT);
        Button backButton = new Button("Kembali");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> controller.kembaliKeSeatSelectionView());

        Label titleLabel = new Label("Pilih Metode Pembayaran");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, titleLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0));

        paymentMethodsContainer = new VBox(20); // Spasi vertikal antar metode pembayaran
        paymentMethodsContainer.setAlignment(Pos.CENTER);
        paymentMethodsContainer.setPadding(new Insets(30));
        // Gaya panel container metode pembayaran
        paymentMethodsContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        updateMetodePembayaranButtons(); // Panggil saat inisialisasi

        root.setCenter(paymentMethodsContainer);

        scene = new Scene(root, 850, 650);
    }

    public void updateMetodePembayaranButtons() {
        paymentMethodsContainer.getChildren().clear();

        List<BioskopDataStore.PaymentMethod> metodePembayaran = controller.getDaftarMetodePembayaran();

        if (metodePembayaran.isEmpty()) {
            Label noMethodLabel = new Label("Belum ada metode pembayaran tersedia.");
            noMethodLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            noMethodLabel.setTextFill(Color.DARKRED);
            paymentMethodsContainer.getChildren().add(noMethodLabel);
            return;
        }

        for (BioskopDataStore.PaymentMethod metode : metodePembayaran) {
            Button methodButton = new Button(metode.getName());
            methodButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
            methodButton.setPrefSize(300, 70); // Ukuran tombol lebih besar
            // Gaya tombol metode pembayaran
            methodButton.setStyle("-fx-background-color: #E0F2F1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
            // Efek hover
            methodButton.setOnMouseEntered(e -> methodButton.setStyle(methodButton.getStyle() + "-fx-background-color: #A3D8D0; -fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
            methodButton.setOnMouseExited(e -> methodButton.setStyle("-fx-background-color: #E0F2F1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"));

            VBox methodDisplay = new VBox(8); // Spasi antara tombol dan diskon info
            methodDisplay.setAlignment(Pos.CENTER);
            methodDisplay.getChildren().add(methodButton);

            if (metode.getDiscountPercent() > 0) {
                Label discountInfo = new Label(metode.getDiscountPercent() + "% Diskon - " + metode.getDiscountDescription());
                discountInfo.setFont(Font.font("Verdana", FontPosture.ITALIC, 16));
                discountInfo.setTextFill(Color.web("#28A745")); // Warna hijau untuk diskon
                methodDisplay.getChildren().add(discountInfo);
            }

            methodButton.setOnAction(_ -> controller.pilihMetodePembayaran(metode));

            paymentMethodsContainer.getChildren().add(methodDisplay);
        }
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Pembayaran");
        updateMetodePembayaranButtons(); // Pastikan tombol diperbarui saat view ditampilkan
        stage.setScene(scene);
        stage.show();
    }
}