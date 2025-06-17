package bioskopkuy.view.login.admin;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class FilmManagementView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private TextField judulFilmField;
    private TextField hargaFilmField;
    private TextField jamTayangField;
    private ListView<BioskopModel.Film> filmListView;
    private Button hapusFilmButton;
    private Button tambahFilmButton;

    // Untuk upload gambar
    private ImageView filmImageView;
    private Label imagePathLabel;
    private String selectedImagePath; // Menyimpan path gambar yang dipilih

    public FilmManagementView(BioskopController controller, Stage stage) {
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
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> controller.kembaliKeAdminDashboard());

        Label titleLabel = new Label("Manajemen Film Bioskop");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, titleLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0));

        VBox centerContent = getVBox();

        Label tambahFilmTitle = new Label("Tambah Film Baru");
        tambahFilmTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        tambahFilmTitle.setTextFill(Color.web("#3A6D65"));
        VBox.setMargin(tambahFilmTitle, new Insets(0, 0, 10, 0));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        Label judulLabel = new Label("Judul Film:");
        judulLabel.setFont(Font.font("Verdana", 17));
        judulFilmField = new TextField();
        judulFilmField.setPromptText("Masukkan judul film (misal: Oppenheimer)");
        judulFilmField.setPrefWidth(300);
        judulFilmField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        Label hargaLabel = new Label("Harga (Rp):");
        hargaLabel.setFont(Font.font("Verdana", 17));
        hargaFilmField = new TextField();
        hargaFilmField.setPromptText("Harga per kursi (misal: 50000)");
        hargaFilmField.setPrefWidth(300);
        hargaFilmField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");
        hargaFilmField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([.]?\\d{0,2})?")) { // Allow only numbers and one decimal point
                hargaFilmField.setText(oldValue);
            }
        });

        Label jamTayangLabel = new Label("Jam Tayang (HH:mm,HH:mm,...):");
        jamTayangLabel.setFont(Font.font("Verdana", 17));
        jamTayangField = new TextField();
        jamTayangField.setPromptText("Contoh: 12:00,14:30,17:00");
        jamTayangField.setPrefWidth(300);
        jamTayangField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // --- Penambahan untuk Upload Gambar ---
        Label imageLabel = new Label("Poster Film:");
        imageLabel.setFont(Font.font("Verdana", 17));

        filmImageView = new ImageView();
        filmImageView.setFitWidth(100);
        filmImageView.setFitHeight(150);
        filmImageView.setStyle("-fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-color: #F8F8F8;");
        filmImageView.setPreserveRatio(true);

        imagePathLabel = new Label("Belum ada gambar dipilih.");
        imagePathLabel.setFont(Font.font("Verdana", 12));
        imagePathLabel.setTextFill(Color.GRAY);
        imagePathLabel.setWrapText(true); // Pastikan label wrap jika path terlalu panjang

        Button uploadImageButton = new Button("Upload Gambar");
        uploadImageButton.setFont(Font.font("Verdana", 16));
        uploadImageButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px;");
        uploadImageButton.setOnMouseEntered(_ -> uploadImageButton.setStyle(uploadImageButton.getStyle() + "-fx-background-color: #7BD4C6;"));
        uploadImageButton.setOnMouseExited(_ -> uploadImageButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px;"));
        uploadImageButton.setOnAction(_ -> handleUploadImage());

        VBox imageInputContainer = new VBox(5, filmImageView, imagePathLabel, uploadImageButton);
        imageInputContainer.setAlignment(Pos.CENTER_LEFT);
        // --- Akhir Penambahan untuk Upload Gambar ---

        formGrid.add(judulLabel, 0, 0);
        formGrid.add(judulFilmField, 1, 0);
        formGrid.add(hargaLabel, 0, 1);
        formGrid.add(hargaFilmField, 1, 1);
        formGrid.add(jamTayangLabel, 0, 2);
        formGrid.add(jamTayangField, 1, 2);
        formGrid.add(imageLabel, 0, 3);
        formGrid.add(imageInputContainer, 1, 3); // Tambahkan container gambar ke grid

        tambahFilmButton = new Button("Tambah Film");
        tambahFilmButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        tambahFilmButton.setPrefSize(200, 50);
        tambahFilmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        tambahFilmButton.setOnMouseEntered(_ -> tambahFilmButton.setStyle(tambahFilmButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        tambahFilmButton.setOnMouseExited(_ -> tambahFilmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        tambahFilmButton.setOnAction(_ -> handleTambahFilm());

        Label daftarFilmTitle = new Label("Daftar Film Saat Ini");
        daftarFilmTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        daftarFilmTitle.setTextFill(Color.web("#3A6D65"));
        daftarFilmTitle.setPadding(new Insets(30, 0, 10, 0));

        filmListView = new ListView<>();
        filmListView.setPrefHeight(250);
        filmListView.setPlaceholder(new Label("Tidak ada film yang ditambahkan."));
        filmListView.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        filmListView.setCellFactory(_ -> new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final ImageView cellImageView = new ImageView();
            private final Label filmInfoLabel = new Label();
            {
                cellImageView.setFitWidth(50);
                cellImageView.setFitHeight(75);
                cellImageView.setPreserveRatio(true);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(cellImageView, filmInfoLabel);
                hbox.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(BioskopModel.Film film, boolean empty) {
                super.updateItem(film, empty);
                if (empty || film == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    filmInfoLabel.setText(film.getJudul() + " (Rp" + String.format("%,.0f", film.getHargaDasar()) + "/kursi) - Jam: " + String.join(", ", film.getJamTayang()));
                    if (film.getImagePath() != null && !film.getImagePath().isEmpty()) {
                        try {
                            // Gunakan File URL untuk gambar lokal
                            Image image = new Image(new File(film.getImagePath()).toURI().toString(), true);
                            cellImageView.setImage(image);
                        } catch (Exception e) {
                            System.err.println("Error loading image for " + film.getJudul() + ": " + e.getMessage());
                            cellImageView.setImage(null); // Clear image on error
                        }
                    } else {
                        cellImageView.setImage(null);
                    }
                    setGraphic(hbox);
                }
            }
        });

        hapusFilmButton = new Button("Hapus Film Terpilih");
        hapusFilmButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        hapusFilmButton.setPrefSize(250, 50);
        hapusFilmButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        hapusFilmButton.setOnMouseEntered(_ -> hapusFilmButton.setStyle(hapusFilmButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        hapusFilmButton.setOnMouseExited(_ -> hapusFilmButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        hapusFilmButton.setDisable(true); // Disable by default
        hapusFilmButton.setOnAction(_ -> handleHapusFilm());

        filmListView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> hapusFilmButton.setDisable(newSelection == null));

        centerContent.getChildren().addAll(
                tambahFilmTitle, formGrid, tambahFilmButton,
                daftarFilmTitle, filmListView, hapusFilmButton
        );
        root.setCenter(centerContent);

        scene = new Scene(root, 950, 850); // Ukuran scene lebih besar untuk mengakomodasi gambar
    }

    private static VBox getVBox() {
        VBox centerContent = new VBox(25); // Spasi antar bagian
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(30));
        centerContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");
        return centerContent;
    }

    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Poster Film");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            imagePathLabel.setText("File: " + selectedFile.getName());
            try {
                Image image = new Image(selectedFile.toURI().toString());
                filmImageView.setImage(image);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat gambar: " + e.getMessage());
                filmImageView.setImage(null);
                selectedImagePath = null;
                imagePathLabel.setText("Gagal memuat gambar.");
            }
        } else {
            selectedImagePath = null;
            imagePathLabel.setText("Belum ada gambar dipilih.");
            filmImageView.setImage(null);
        }
    }

    private void handleTambahFilm() {
        String judul = judulFilmField.getText().trim();
        String hargaText = hargaFilmField.getText().trim().replace(',', '.'); // Handle comma as decimal separator
        String jamTayang = jamTayangField.getText().trim();

        if (judul.isEmpty() || hargaText.isEmpty() || jamTayang.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field (judul, harga, jam tayang) harus diisi.");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaText);
            if (harga <= 0) {
                showAlert(Alert.AlertType.WARNING, "Harga Invalid", "Harga harus lebih dari nol.");
                return;
            }
            // Panggil controller dengan imagePath
            controller.tambahFilm(judul, harga, jamTayang, selectedImagePath);
            judulFilmField.clear();
            hargaFilmField.clear();
            jamTayangField.clear();
            selectedImagePath = null; // Reset path gambar
            filmImageView.setImage(null); // Hapus preview gambar
            imagePathLabel.setText("Belum ada gambar dipilih.");
            refreshFilmList(controller.getModel().getDaftarFilm()); // Refresh list setelah menambah
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Harga Tidak Valid", "Harga harus berupa angka yang valid.");
        }
    }

    private void handleHapusFilm() {
        BioskopModel.Film selectedFilm = filmListView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Konfirmasi Penghapusan");
            confirmationAlert.setHeaderText("Hapus Film: " + selectedFilm.getJudul() + "?");
            confirmationAlert.setContentText("Menghapus film akan juga menghapus semua data kursi yang sudah terisi untuk film ini. Lanjutkan?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.hapusFilm(selectedFilm);
                refreshFilmList(controller.getModel().getDaftarFilm()); // Refresh list setelah menghapus
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih film yang ingin dihapus terlebih dahulu.");
        }
    }

    public void refreshFilmList(List<BioskopModel.Film> films) {
        filmListView.getItems().setAll(films);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Manajemen Film");
        refreshFilmList(controller.getModel().getDaftarFilm()); // Pastikan list diperbarui saat view ditampilkan
        selectedImagePath = null; // Reset image path saat view ditampilkan
        filmImageView.setImage(null); // Reset preview gambar
        imagePathLabel.setText("Belum ada gambar dipilih.");
        stage.setScene(scene);
        stage.show();
    }
}