package bioskopkuy.view.penonton;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Set;

public class SeatSelectionView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private Label filmJudulLabel;
    private Label kursiTerpilihLabel;
    private Label totalHargaLabel;
    private GridPane kursiGridPane;

    public SeatSelectionView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" +
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );
        HBox topPanel = new HBox(20);
        topPanel.setAlignment(Pos.CENTER_LEFT);
        Button backButton = new Button("Kembali");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> controller.kembaliKeFilmSelectionView());

        filmJudulLabel = new Label("Film: ");
        filmJudulLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        filmJudulLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, filmJudulLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 30, 0));

        VBox centerContent = new VBox(25);
        centerContent.setAlignment(Pos.CENTER);

        Label screenLabel = new Label("LAYAR");
        screenLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        screenLabel.setTextFill(Color.WHITE);
        screenLabel.setStyle("-fx-background-color: #4A8C80; -fx-padding: 12px 60px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");
        screenLabel.setMaxWidth(400);
        screenLabel.setAlignment(Pos.CENTER);

        kursiGridPane = new GridPane();
        kursiGridPane.setHgap(10);
        kursiGridPane.setVgap(10);
        kursiGridPane.setPadding(new Insets(30));
        kursiGridPane.setAlignment(Pos.CENTER);
        kursiGridPane.setStyle("-fx-background-color: #E0F2F1; -fx-border-color: #5AAAA0; -fx-border-width: 2px; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        updateKursiGrid();

        centerContent.getChildren().addAll(screenLabel, kursiGridPane);
        root.setCenter(centerContent);

        VBox bottomPanel = new VBox(12);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15));
        bottomPanel.setStyle("-fx-background-color: #3A6D65; -fx-border-color: #2C3E50; -fx-border-width: 1px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        kursiTerpilihLabel = new Label("Kursi Terpilih: Tidak Ada");
        kursiTerpilihLabel.setFont(Font.font("Verdana", 17));
        kursiTerpilihLabel.setTextFill(Color.WHITE);

        totalHargaLabel = new Label("Total Harga: Rp0");
        totalHargaLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        totalHargaLabel.setTextFill(Color.YELLOW);

        Button lanjutButton = new Button("Lanjut ke Pembayaran");
        lanjutButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        lanjutButton.setPrefSize(280, 55);
        lanjutButton.setStyle("-fx-background-color: #FFD700;" +
                "-fx-text-fill: #2C3E50;" +
                "-fx-border-color: #B8860B;" +
                "-fx-border-width: 1.5px;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        lanjutButton.setOnMouseEntered(_ -> lanjutButton.setStyle(lanjutButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        lanjutButton.setOnMouseExited(_ -> lanjutButton.setStyle(lanjutButton.getStyle().replace("-fx-scale-y: 1.05; -fx-scale-x: 1.05;", "")));
        lanjutButton.setOnAction(_ -> controller.lanjutKePembayaran());

        bottomPanel.getChildren().addAll(kursiTerpilihLabel, totalHargaLabel, lanjutButton);
        root.setBottom(bottomPanel);
        BorderPane.setMargin(bottomPanel, new Insets(25, 0, 0, 0));

        scene = new Scene(root, 900, 700);
    }

    public void setFilmJudul(String judul) {
        filmJudulLabel.setText("Film: " + judul + " - " + controller.getModel().getJamTerpilih());
    }

    public void updateKursiGrid() {
        kursiGridPane.getChildren().clear();

        controller.getModel().getAllKursiNames();
        BioskopModel.Film currentFilm = controller.getModel().getFilmTerpilih();
        String currentJam = controller.getModel().getJamTerpilih();
        Set<String> selectedKursi = controller.getModel().getKursiTerpilih();

        char[] rows = {'A', 'B', 'C', 'D'};
        int colCount = 10;

        for (int rowIdx = 0; rowIdx < rows.length; rowIdx++) {
            char rowChar = rows[rowIdx];
            for (int colIdx = 0; colIdx < colCount; colIdx++) {
                String kursiName = String.valueOf(rowChar) + (colIdx + 1);
                Button seatButton = new Button(kursiName);
                seatButton.setPrefSize(55, 45);
                seatButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

                String baseStyle = "-fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);";

                if (currentFilm != null && currentJam != null && controller.getModel().isKursiTerisi(currentFilm, currentJam, kursiName)) {
                    seatButton.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-border-color: #8D2A1F;" + baseStyle);
                    seatButton.setDisable(true);
                } else if (selectedKursi.contains(kursiName)) {
                    seatButton.setStyle("-fx-background-color: #FFEB3B; -fx-text-fill: #2C3E50; -fx-border-color: #FFC107; -fx-border-width: 2px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" + baseStyle.replace("-fx-border-width: 1px;", ""));
                } else {
                    seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #BDC3C7;" + baseStyle);
                    seatButton.setOnMouseEntered(_ -> seatButton.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"));
                    seatButton.setOnMouseExited(_ -> seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #BDC3C7;" + baseStyle));
                }

                seatButton.setOnAction(_ -> controller.toggleKursiTerpilih(kursiName));

                kursiGridPane.add(seatButton, colIdx, rowIdx);
            }
        }
    }

    public void updateKursiTerpilihDisplay(Set<String> kursiTerpilih, String totalHargaSebelumDiskonFormatted) {
        if (kursiTerpilih.isEmpty()) {
            kursiTerpilihLabel.setText("Kursi Terpilih: Tidak Ada");
        } else {
            kursiTerpilihLabel.setText("Kursi Terpilih: " + String.join(", ", kursiTerpilih));
        }
        totalHargaLabel.setText("Total Harga: " + totalHargaSebelumDiskonFormatted);
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Kursi");
        updateKursiGrid();
        stage.setScene(scene);
        stage.show();
    }
}