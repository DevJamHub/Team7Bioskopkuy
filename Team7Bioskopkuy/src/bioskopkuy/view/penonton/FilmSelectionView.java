package bioskopkuy.view.penonton;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class FilmSelectionView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private VBox filmListPanel;

    public FilmSelectionView(BioskopController controller, Stage stage) {
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
        Button backButton = getBackButtonStyled();
        Label titleLabel = new Label("Pilih Film dan Jam Tayang");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, titleLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0));

        filmListPanel = new VBox(20);
        filmListPanel.setAlignment(Pos.TOP_CENTER);
        filmListPanel.setPadding(new Insets(25));
        filmListPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        ScrollPane scrollPane = new ScrollPane(filmListPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setCenter(scrollPane);

        scene = new Scene(root, 850, 650);
    }

    private Button getBackButtonStyled() {
        Button backButton = new Button("Kembali");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> {
            if (controller.isAdminLoggedIn()) {
                controller.kembaliKeAdminDashboard();
            } else {
                controller.kembaliKeMainView();
            }
        });
        return backButton;
    }

    public void refreshFilmList() {
        filmListPanel.getChildren().clear();

        List<BioskopModel.Film> films = controller.getModel().getAll(); // Menggunakan getAll() dari IManagementService di model
        if (films.isEmpty()) {
            Label noFilmLabel = new Label("Belum ada film yang tersedia saat ini.");
            noFilmLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
            noFilmLabel.setTextFill(Color.DARKRED);
            filmListPanel.getChildren().add(noFilmLabel);
            return;
        }

        for (BioskopModel.Film film : films) {
            HBox filmCard = getHBox();

            ImageView posterImageView = new ImageView();
            posterImageView.setFitWidth(100);
            posterImageView.setFitHeight(150);
            posterImageView.setPreserveRatio(true);
            posterImageView.setStyle("-fx-border-color: #B2D8D3; -fx-border-width: 1px;");

            Image poster = film.getPosterImage();
            if (poster != null) {
                posterImageView.setImage(poster);
            } else {
                System.err.println("Poster untuk film '" + film.getJudul() + "' tidak dapat dimuat (getPosterImage() mengembalikan null).");
            }

            VBox filmInfo = new VBox(8);
            filmInfo.setAlignment(Pos.CENTER_LEFT);

            // Menggunakan getDisplayInfo() dari Film (polymorphism)
            Label filmTitleLabel = new Label(film.getJudul()); // Tetap gunakan getJudul untuk judul utama
            filmTitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            filmTitleLabel.setTextFill(Color.web("#3A6D65"));

            Label filmPriceLabel = new Label("Harga: Rp" + String.format("%,.0f", film.getHargaDasar()) + "/kursi");
            filmPriceLabel.setFont(Font.font("Verdana", 16));
            filmPriceLabel.setTextFill(Color.web("#5C6F7E"));

            HBox jamTayangButtons = new HBox(15);
            jamTayangButtons.setAlignment(Pos.CENTER_LEFT);

            if (film.getJamTayang().isEmpty()) {
                Label noJamLabel = new Label("Tidak ada jam tayang tersedia.");
                noJamLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 15));
                noJamLabel.setTextFill(Color.GRAY);
                jamTayangButtons.getChildren().add(noJamLabel);
            } else {
                for (String jam : film.getJamTayang()) {
                    Button jamButton = new Button(jam);
                    jamButton.setFont(Font.font("Verdana", 17));
                    jamButton.setPrefWidth(90);
                    jamButton.setPrefHeight(40);
                    jamButton.setStyle("-fx-background-color: #FFFFFF;" +
                            "-fx-text-fill: #2C3E50;" +
                            "-fx-border-color: #5AAAA0;" +
                            "-fx-border-width: 1px;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
                    jamButton.setOnMouseEntered(_ -> jamButton.setStyle(jamButton.getStyle() + "-fx-background-color: #A3D8D0; -fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
                    jamButton.setOnMouseExited(_ -> jamButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"));

                    jamButton.setOnAction(_ -> controller.pilihFilmDanJam(film, jam));
                    jamTayangButtons.getChildren().add(jamButton);
                }
            }
            filmInfo.getChildren().addAll(filmTitleLabel, filmPriceLabel, jamTayangButtons);
            filmCard.getChildren().addAll(posterImageView, filmInfo);
            filmListPanel.getChildren().add(filmCard);
        }
    }

    private static HBox getHBox() {
        HBox filmCard = new HBox(15);
        filmCard.setAlignment(Pos.CENTER_LEFT);
        filmCard.setPadding(new Insets(15));
        filmCard.setStyle("-fx-background-color: #E0F2F1;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);");
        return filmCard;
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Pilih Film");
        refreshFilmList();
        stage.setScene(scene);
        stage.show();
    }
}