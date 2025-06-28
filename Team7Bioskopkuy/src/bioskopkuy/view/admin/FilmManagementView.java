package bioskopkuy.view.admin; // Package untuk tampilan manajemen film admin

import bioskopkuy.controller.BioskopController; // Import controller untuk mengelola film
import bioskopkuy.model.BioskopModel; // Import model untuk data film
import javafx.geometry.Insets; // Import untuk pengaturan margin dan padding
import javafx.geometry.Pos; // Import untuk pengaturan posisi
import javafx.scene.Scene; // Import untuk membuat scene
import javafx.scene.control.*; // Import untuk kontrol UI seperti Button, Label, TextField, dll.
import javafx.scene.image.Image; // Import untuk menangani gambar
import javafx.scene.image.ImageView; // Import untuk menampilkan gambar
import javafx.scene.layout.BorderPane; // Import untuk layout BorderPane
import javafx.scene.layout.GridPane; // Import untuk layout GridPane
import javafx.scene.layout.HBox; // Import untuk layout HBox
import javafx.scene.layout.VBox; // Import untuk layout VBox
import javafx.scene.paint.Color; // Import untuk warna
import javafx.scene.text.Font; // Import untuk font
import javafx.scene.text.FontWeight; // Import untuk pengaturan ketebalan font
import javafx.stage.FileChooser; // Import untuk dialog pemilihan file
import javafx.stage.Stage; // Import untuk stage aplikasi
import java.io.File; // Import untuk file
import java.util.List; // Import untuk list
import java.util.Optional; // Import untuk Optional

public class FilmManagementView { // Kelas untuk tampilan manajemen film
    private final BioskopController controller; // Controller untuk mengelola film
    private final Stage stage; // Stage untuk tampilan
    private Scene scene; // Scene untuk tampilan

    private TextField judulFilmField; // Field untuk judul film
    private TextField hargaFilmField; // Field untuk harga film
    private TextField jamTayangField; // Field untuk jam tayang film
    private ListView<BioskopModel.Film> filmListView; // ListView untuk menampilkan daftar film
    private Button hapusFilmButton; // Button untuk menghapus film
    private Button tambahFilmButton; // Button untuk menambah film

    private ImageView filmImageView; // ImageView untuk menampilkan poster film
    private Label imagePathLabel; // Label untuk menampilkan path gambar
    private String selectedImagePath; // Path gambar yang dipilih

    public FilmManagementView(BioskopController controller, Stage stage) { // Constructor
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Panggil metode inisialisasi
    }

    private void initialize() { // Metode untuk inisialisasi tampilan
        BorderPane root = new BorderPane(); // Buat layout BorderPane
        root.setPadding(new Insets(25)); // Set padding untuk root
        root.setStyle("-fx-background-color: linear-gradient(to bottom left, #5AAAA0, #7BD4C6);"); // Set style background

        HBox topPanel = new HBox(20); // Buat HBox untuk panel atas
        topPanel.setAlignment(Pos.CENTER_LEFT); // Set alignment
        Button backButton = new Button("Kembali"); // Button untuk kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Set font
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Set style
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Set efek hover
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Set efek hover
        backButton.setOnAction(_ -> controller.kembaliKeAdminDashboard()); // Aksi saat button diklik

        Label titleLabel = new Label("Manajemen Film Bioskop"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Set font
        titleLabel.setTextFill(Color.web("#2C3E50")); // Set warna teks
        topPanel.getChildren().addAll(backButton, titleLabel); // Tambahkan button dan label ke panel atas
        root.setTop(topPanel); // Set panel atas ke root
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0)); // Set margin untuk panel atas

        VBox centerContent = getVBox(); // Dapatkan VBox untuk konten tengah

        Label tambahFilmTitle = new Label("Tambah Film Baru"); // Label untuk menambah film
        tambahFilmTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Set font
        tambahFilmTitle.setTextFill(Color.web("#3A6D65")); // Set warna teks
        VBox.setMargin(tambahFilmTitle, new Insets(0, 0, 10, 0)); // Set margin untuk label

        GridPane formGrid = new GridPane(); // Buat GridPane untuk form
        formGrid.setHgap(15); // Set jarak horizontal
        formGrid.setVgap(15); // Set jarak vertikal
        formGrid.setAlignment(Pos.CENTER); // Set alignment

        Label judulLabel = new Label("Judul Film:"); // Label untuk judul film
        judulLabel.setFont(Font.font("Verdana", 17)); // Set font
        judulFilmField = new TextField(); // Inisialisasi field judul film
        judulFilmField.setPromptText("Masukkan judul film (misal: Oppenheimer)"); // Set placeholder
        judulFilmField.setPrefWidth(300); // Set lebar preferensi
        judulFilmField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Set style

        Label hargaLabel = new Label("Harga (Rp):"); // Label untuk harga film
        hargaLabel.setFont(Font.font("Verdana", 17)); // Set font
        hargaFilmField = new TextField(); // Inisialisasi field harga film
        hargaFilmField.setPromptText("Harga per kursi (misal: 50000)"); // Set placeholder
        hargaFilmField.setPrefWidth(300); // Set lebar preferensi
        hargaFilmField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Set style
        hargaFilmField.textProperty().addListener((_, oldValue, newValue) -> { // Listener untuk validasi input harga
            if (!newValue.matches("\\d*([.]?\\d{0,2})?")) { // Cek format input
                hargaFilmField.setText(oldValue); // Kembalikan ke nilai lama jika tidak valid
            }
        });

        Label jamTayangLabel = new Label("Jam Tayang (HH:mm,HH:mm,...):"); // Label untuk jam tayang
        jamTayangLabel.setFont(Font.font("Verdana", 17)); // Set font
        jamTayangField = new TextField(); // Inisialisasi field jam tayang
        jamTayangField.setPromptText("Contoh: 12:00,14:30,17:00"); // Set placeholder
        jamTayangField.setPrefWidth(300); // Set lebar preferensi
        jamTayangField.setStyle("-fx-font-size: 16px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Set style

        Label imageLabel = new Label("Poster Film:"); // Label untuk poster film
        imageLabel.setFont(Font.font("Verdana", 17)); // Set font

        filmImageView = new ImageView(); // Inisialisasi ImageView untuk poster film
        filmImageView.setFitWidth(100); // Set lebar ImageView
        filmImageView.setFitHeight(150); // Set tinggi ImageView
        filmImageView.setStyle("-fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-color: #F8F8F8;"); // Set style
        filmImageView.setPreserveRatio(true); // Pertahankan rasio gambar

        imagePathLabel = new Label("Belum ada gambar dipilih."); // Label untuk menampilkan path gambar
        imagePathLabel.setFont(Font.font("Verdana", 12)); // Set font
        imagePathLabel.setTextFill(Color.GRAY); // Set warna teks
        imagePathLabel.setWrapText(true); // Izinkan teks membungkus

        Button uploadImageButton = new Button("Upload Gambar"); // Button untuk upload gambar
        uploadImageButton.setFont(Font.font("Verdana", 16)); // Set font
        uploadImageButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px;"); // Set style
        uploadImageButton.setOnMouseEntered(_ -> uploadImageButton.setStyle(uploadImageButton.getStyle() + "-fx-background-color: #7BD4C6;")); // Set efek hover
        uploadImageButton.setOnMouseExited(_ -> uploadImageButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px;")); // Set efek hover
        uploadImageButton.setOnAction(_ -> handleUploadImage()); // Aksi saat button diklik

        VBox imageInputContainer = new VBox(5, filmImageView, imagePathLabel, uploadImageButton); // Kontainer untuk input gambar
        imageInputContainer.setAlignment(Pos.CENTER_LEFT); // Set alignment

        formGrid.add(judulLabel, 0, 0); // Tambahkan label judul ke grid
        formGrid.add(judulFilmField, 1, 0); // Tambahkan field judul ke grid
        formGrid.add(hargaLabel, 0, 1); // Tambahkan label harga ke grid
        formGrid.add(hargaFilmField, 1, 1); // Tambahkan field harga ke grid
        formGrid.add(jamTayangLabel, 0, 2); // Tambahkan label jam tayang ke grid
        formGrid.add(jamTayangField, 1, 2); // Tambahkan field jam tayang ke grid
        formGrid.add(imageLabel, 0, 3); // Tambahkan label poster ke grid
        formGrid.add(imageInputContainer, 1, 3); // Tambahkan kontainer input gambar ke grid

        tambahFilmButton = new Button("Tambah Film"); // Button untuk menambah film
        tambahFilmButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Set font
        tambahFilmButton.setPrefSize(200, 50); // Set ukuran preferensi
        tambahFilmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"); // Set style
        tambahFilmButton.setOnMouseEntered(_ -> tambahFilmButton.setStyle(tambahFilmButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Set efek hover
        tambahFilmButton.setOnMouseExited(_ -> tambahFilmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);")); // Set efek hover
        tambahFilmButton.setOnAction(_ -> handleTambahFilm()); // Aksi saat button diklik

        Label daftarFilmTitle = new Label("Daftar Film Saat Ini"); // Label untuk daftar film
        daftarFilmTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 22)); // Set font
        daftarFilmTitle.setTextFill(Color.web("#3A6D65")); // Set warna teks
        daftarFilmTitle.setPadding(new Insets(30, 0, 10, 0)); // Set padding

        filmListView = new ListView<>(); // Inisialisasi ListView untuk daftar film
        filmListView.setMaxHeight(Double.MAX_VALUE); // Set tinggi maksimum
        filmListView.setPlaceholder(new Label("Tidak ada film yang ditambahkan.")); // Set placeholder

        // Bungkus ListView dengan ScrollPane
        ScrollPane scrollableList = new ScrollPane(filmListView); // Buat ScrollPane untuk ListView
        scrollableList.setFitToWidth(true); // Set agar lebar sesuai
        scrollableList.setPrefHeight(300); // Set tinggi preferensi
        scrollableList.setStyle("-fx-background-color:transparent;"); // Set style

        filmListView.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Set style untuk ListView
        filmListView.setCellFactory(_ -> new ListCell<>() { // Set factory untuk cell ListView
            private final HBox hbox = new HBox(10); // Buat HBox untuk cell
            private final ImageView cellImageView = new ImageView(); // ImageView untuk gambar film
            private final Label filmInfoLabel = new Label(); // Label untuk informasi film
            {
                cellImageView.setFitWidth(50); // Set lebar ImageView
                cellImageView.setFitHeight(75); // Set tinggi ImageView
                cellImageView.setPreserveRatio(true); // Pertahankan rasio gambar
                hbox.setAlignment(Pos.CENTER_LEFT); // Set alignment
                hbox.getChildren().addAll(cellImageView, filmInfoLabel); // Tambahkan ImageView dan label ke HBox
                hbox.setPadding(new Insets(5)); // Set padding untuk HBox
            }

            @Override
            protected void updateItem(BioskopModel.Film film, boolean empty) { // Override untuk update item
                super.updateItem(film, empty); // Panggil super
                if (empty || film == null) { // Cek jika item kosong
                    setText(null); // Set teks null
                    setGraphic(null); // Set graphic null
                } else {
                    // Menggunakan getDisplayInfo() dari Film (yang sekarang extends AbstractEntity)
                    filmInfoLabel.setText(controller.getModel().getDisplayInfo(film)); // Set informasi film
                    if (film.getImagePath() != null && !film.getImagePath().isEmpty()) { // Cek jika ada path gambar
                        try {
                            Image image = new Image(new File(film.getImagePath()).toURI().toString(), true); // Load gambar
                            cellImageView.setImage(image); // Set gambar ke ImageView
                        } catch (Exception e) { // Tangani exception
                            System.err.println("Error loading image for " + film.getJudul() + ": " + e.getMessage()); // Tampilkan error
                            cellImageView.setImage(null); // Set gambar null
                        }
                    } else {
                        cellImageView.setImage(null); // Set gambar null jika tidak ada
                    }
                    setGraphic(hbox); // Set graphic ke HBox
                }
            }
        });

        hapusFilmButton = new Button("Hapus Film Terpilih"); // Button untuk menghapus film terpilih
        hapusFilmButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Set font
        hapusFilmButton.setPrefSize(250, 50); // Set ukuran preferensi
        hapusFilmButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"); // Set style
        hapusFilmButton.setOnMouseEntered(_ -> hapusFilmButton.setStyle(hapusFilmButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Set efek hover
        hapusFilmButton.setOnMouseExited(_ -> hapusFilmButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);")); // Set efek hover
        hapusFilmButton.setDisable(true); // Disable button saat tidak ada film terpilih
        hapusFilmButton.setOnAction(_ -> handleHapusFilm()); // Aksi saat button diklik


        filmListView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) ->
                hapusFilmButton.setDisable(newSelection == null) // Listener untuk enable/disable delete button berdasarkan selection
        );

        centerContent.getChildren().addAll(
                tambahFilmTitle, formGrid, tambahFilmButton,
                daftarFilmTitle, scrollableList, hapusFilmButton
        ); // Tambahkan semua komponen ke centerContent
        root.setCenter(centerContent); // Set centerContent ke root

        scene = new Scene(root, 950, 850); // Buat scene dengan ukuran 950x850
    }

    private static VBox getVBox() { // Helper method untuk membuat VBox dengan style tertentu
        VBox centerContent = new VBox(25); // Buat VBox dengan spacing 25
        centerContent.setAlignment(Pos.TOP_CENTER); // Set alignment
        centerContent.setPadding(new Insets(30)); // Set padding
        centerContent.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" +
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        ); // Set style gradient dan efek bayangan
        return centerContent; // Kembalikan VBox
    }

    private void handleUploadImage() { // Method untuk menangani upload gambar
        FileChooser fileChooser = new FileChooser(); // Buat FileChooser
        fileChooser.setTitle("Pilih Poster Film"); // Set judul dialog
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        ); // Set filter ekstensi file

        File selectedFile = fileChooser.showOpenDialog(stage); // Tampilkan dialog pilih file
        if (selectedFile != null) { // Jika file dipilih
            selectedImagePath = selectedFile.getAbsolutePath(); // Simpan path file
            imagePathLabel.setText("File: " + selectedFile.getName()); // Update label path
            try {
                Image image = new Image(selectedFile.toURI().toString()); // Load gambar
                filmImageView.setImage(image); // Tampilkan gambar
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat gambar: " + e.getMessage()); // Tampilkan error
                filmImageView.setImage(null); // Reser image view
                selectedImagePath = null; // Reset path
                imagePathLabel.setText("Gagal memuat gambar."); // Update label
            }
        } else { // Jika tidak ada file dipilih
            selectedImagePath = null; // Reset path
            filmImageView.setImage(null); // Reset image view
            imagePathLabel.setText("Belum ada gambar dipilih."); // Update label
        }
    }

    private void handleTambahFilm() { // Method untuk menangani penambahan film
        String judul = judulFilmField.getText().trim(); // Ambil input judul
        String hargaText = hargaFilmField.getText().trim().replace(',', '.'); // Ambil input harga
        String jamTayang = jamTayangField.getText().trim(); // Ambil input jam tayang

        if (judul.isEmpty() || hargaText.isEmpty() || jamTayang.isEmpty()) { // Validasi input kosong
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field (judul, harga, jam tayang) harus diisi.");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaText); // Parse harga ke double
            if (harga <= 0) { // Validasi harga positif
                showAlert(Alert.AlertType.WARNING, "Harga Invalid", "Harga harus lebih dari nol.");
                return;
            }
            controller.tambahFilm(judul, harga, jamTayang, selectedImagePath); // Panggil controller untuk tambah film
            // Reset form
            judulFilmField.clear();
            hargaFilmField.clear();
            jamTayangField.clear();
            selectedImagePath = null;
            filmImageView.setImage(null);
            imagePathLabel.setText("Belum ada gambar dipilih.");
            refreshFilmList(controller.getModel().getAll()); // Refresh daftar film
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Harga Tidak Valid", "Harga harus berupa angka yang valid.");
        }
    }

    private void handleHapusFilm() { // Method untuk menangani penghapusan film
        BioskopModel.Film selectedFilm = filmListView.getSelectionModel().getSelectedItem(); // Ambil film terpilih
        if (selectedFilm != null) {
            Optional<ButtonType> result = getButtonType(selectedFilm);
            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.hapusFilm(selectedFilm); // Panggil controller untuk hapus film
                refreshFilmList(controller.getModel().getAll()); // Refresh daftar film
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih film yang ingin dihapus terlebih dahulu.");
        }
    }

    private static Optional<ButtonType> getButtonType(BioskopModel.Film selectedFilm) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION); // Buat alert konfirmasi
        confirmationAlert.setTitle("Konfirmasi Penghapusan");
        confirmationAlert.setHeaderText("Hapus Film: " + selectedFilm.getJudul() + "?");
        confirmationAlert.setContentText("Menghapus film akan juga menghapus semua data kursi yang sudah terisi untuk film ini. Lanjutkan?");

        // Tampilkan dialog konfirmasi
        return confirmationAlert.showAndWait();
    }

    public void refreshFilmList(List<BioskopModel.Film> films) { // Method untuk refresh daftar film
        filmListView.getItems().setAll(films); // Update ListView
    }

    private void showAlert(Alert.AlertType type, String title, String message) { // Helper method untuk menampilkan alert
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showView() { // Method untuk menampilkan view
        stage.setTitle("BioskopKuy! - Manajemen Film"); // Set judul stage
        refreshFilmList(controller.getModel().getAll()); // Refresh daftar film
        selectedImagePath = null; // Reset path gambar
        filmImageView.setImage(null); // Reset image view
        imagePathLabel.setText("Belum ada gambar dipilih."); // Reset label path
        stage.setScene(scene); // Set scene ke stage
        stage.show(); // Tampilkan stage
    }
}

