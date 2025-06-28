package bioskopkuy.view.admin;

import bioskopkuy.controller.BioskopController; // Controller untuk mengelola logika bisnis
import bioskopkuy.model.BioskopDataStore; // Data store untuk informasi bioskop
import javafx.geometry.Insets; // Untuk mengatur padding di sekitar elemen UI
import javafx.geometry.Pos; // Untuk mengatur posisi elemen UI
import javafx.scene.Scene; // Kontainer untuk semua elemen UI JavaFX
import javafx.scene.control.*; // Mengandung semua kontrol UI dasar
import javafx.scene.layout.BorderPane; // Kontainer layout yang membagi menjadi 5 wilayah
import javafx.scene.layout.HBox; // Kontainer layout horizontal
import javafx.scene.layout.VBox; // Kontainer layout vertikal
import javafx.scene.paint.Color; // Untuk mengatur warna elemen UI
import javafx.scene.text.Font; // Untuk mengatur font teks
import javafx.scene.text.FontWeight; // Untuk mengatur berat font (tebal, dll.)
import javafx.stage.Stage; // Jendela utama aplikasi
import java.util.List; // Untuk bekerja dengan daftar data
import java.util.Optional; // Untuk nilai opsional yang mungkin null

public class PaymentMethodManagementView {
    private final BioskopController controller; // Controller untuk mengelola metode pembayaran
    private final Stage stage; // Stage untuk menampilkan tampilan
    private Scene scene; // Scene yang akan ditampilkan

    private ListView<BioskopDataStore.PaymentMethod> paymentMethodListView; // ListView untuk menampilkan metode pembayaran
    private TextField methodNameField; // TextField untuk nama metode pembayaran
    private TextField discountPercentField; // TextField untuk persentase diskon
    private TextField discountDescriptionField; // TextField untuk keterangan diskon
    private Button editButton; // Tombol untuk mengedit metode pembayaran
    private Button deleteButton; // Tombol untuk menghapus metode pembayaran
    private Button addButton; // Tombol untuk menambah metode pembayaran

    private BioskopDataStore.PaymentMethod selectedMethodForEdit; // Metode pembayaran yang dipilih untuk diedit

    public PaymentMethodManagementView(BioskopController controller, Stage stage) {
        this.controller = controller; // Inisialisasi controller
        this.stage = stage; // Inisialisasi stage
        initialize(); // Memanggil metode inisialisasi
    }

    private void initialize() {
        BorderPane root = new BorderPane(); // Membuat layout utama
        root.setPadding(new Insets(25)); // Menambahkan padding
        root.setStyle("-fx-background-color: linear-gradient(to top right, #5AAAA0, #7BD4C6);"); // Mengatur warna latar belakang

        HBox topPanel = new HBox(20); // Panel atas untuk judul dan tombol kembali
        topPanel.setAlignment(Pos.CENTER_LEFT); // Mengatur posisi konten
        Button backButton = new Button("Kembali"); // Tombol kembali
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Mengatur font tombol
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya tombol
        backButton.setOnMouseEntered(_ -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse masuk
        backButton.setOnMouseExited(_ -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;")); // Gaya saat mouse keluar
        backButton.setOnAction(_ -> controller.kembaliKeAdminDashboard()); // Aksi saat tombol diklik

        Label titleLabel = new Label("Manajemen Metode Pembayaran"); // Label judul
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28)); // Mengatur font label
        titleLabel.setTextFill(Color.web("#2C3E50")); // Mengatur warna teks
        topPanel.getChildren().addAll(backButton, titleLabel); // Menambahkan tombol dan label ke panel atas
        root.setTop(topPanel); // Mengatur panel atas di layout
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0)); // Mengatur margin panel atas

        VBox centerContent = new VBox(25); // Konten tengah
        centerContent.setAlignment(Pos.TOP_CENTER); // Mengatur posisi konten
        centerContent.setPadding(new Insets(30)); // Menambahkan padding
        centerContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" + // Mengatur gaya konten tengah
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        Label listLabel = new Label("Metode Pembayaran Saat Ini:"); // Label untuk daftar metode pembayaran
        listLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font label
        listLabel.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks

        paymentMethodListView = new ListView<>(); // Inisialisasi ListView
        paymentMethodListView.setPrefHeight(250); // Mengatur tinggi ListView
        paymentMethodListView.setPlaceholder(new Label("Tidak ada metode pembayaran tersedia.")); // Placeholder jika tidak ada item
        paymentMethodListView.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;"); // Mengatur gaya ListView
        paymentMethodListView.setCellFactory(_ -> new ListCell<>() { // Mengatur tampilan item di ListView
            @Override
            protected void updateItem(BioskopDataStore.PaymentMethod method, boolean empty) {
                super.updateItem(method, empty);
                if (empty || method == null) {
                    setText(null); // Menghapus teks jika item kosong
                    setGraphic(null); // Menghapus grafik jika item kosong
                } else {
                    // Menggunakan method overloading getDisplayInfo dari controller
                    setText(controller.getModel().getDisplayInfo(method)); // Menampilkan informasi metode pembayaran
                    setStyle("-fx-padding: 8px;"); // Mengatur padding item
                }
            }
        });

        paymentMethodListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> { // Listener untuk perubahan pilihan
            if (newVal != null) {
                selectedMethodForEdit = newVal; // Menyimpan metode yang dipilih untuk diedit
                methodNameField.setText(newVal.getName()); // Mengisi field dengan data metode yang dipilih
                discountPercentField.setText(String.valueOf(newVal.getDiscountPercent())); // Mengisi field dengan persentase diskon
                discountDescriptionField.setText(newVal.getDiscountDescription()); // Mengisi field dengan keterangan diskon
                editButton.setDisable(false); // Mengaktifkan tombol edit
                deleteButton.setDisable(false); // Mengaktifkan tombol hapus
                addButton.setText("Update Metode"); // Mengubah teks tombol tambah
                addButton.setOnAction(_ -> handleEditMethod()); // Mengatur aksi tombol untuk edit
            } else {
                clearFormFields(); // Menghapus field jika tidak ada pilihan
                addButton.setText("Tambah Metode"); // Mengubah teks tombol kembali ke tambah
                addButton.setOnAction(_ -> handleAddMethod()); // Mengatur aksi tombol untuk tambah
            }
        });

        VBox formPanel = new VBox(15); // Panel untuk form input
        formPanel.setPadding(new Insets(20)); // Menambahkan padding
        formPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #F0FFFC, #A5F3EB, #5AAAA0);" + // Mengatur gaya panel form
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 6);" +
                "-fx-padding: 40px;"
        );
        Label formTitle = new Label("Formulir Metode Pembayaran"); // Judul form
        formTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Mengatur font judul
        formTitle.setTextFill(Color.web("#3A6D65")); // Mengatur warna teks

        Label nameLabel = new Label("Nama Metode:"); // Label untuk nama metode
        nameLabel.setFont(Font.font("Verdana", 16)); // Mengatur font label
        methodNameField = new TextField(); // Inisialisasi TextField untuk nama metode
        methodNameField.setPromptText("Nama Metode (misal: GoPay, Dana)"); // Placeholder untuk TextField
        methodNameField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya TextField

        Label percentLabel = new Label("Diskon (%):"); // Label untuk diskon
        percentLabel.setFont(Font.font("Verdana", 16)); // Mengatur font label
        discountPercentField = new TextField(); // Inisialisasi TextField untuk persentase diskon
        discountPercentField.setPromptText("Persentase Diskon (0-100)"); // Placeholder untuk TextField
        discountPercentField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya TextField
        discountPercentField.textProperty().addListener((_, oldVal, newVal) -> { // Listener untuk validasi input diskon
            if (!newVal.matches("\\d*")) { // Memastikan input hanya angka
                discountPercentField.setText(oldVal); // Mengembalikan nilai lama jika tidak valid
            }
        });

        Label descLabel = new Label("Keterangan Diskon:"); // Label untuk keterangan diskon
        descLabel.setFont(Font.font("Verdana", 16)); // Mengatur font label
        discountDescriptionField = new TextField(); // Inisialisasi TextField untuk keterangan diskon
        discountDescriptionField.setPromptText("Keterangan (misal: Promo Akhir Tahun)"); // Placeholder untuk TextField
        discountDescriptionField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Mengatur gaya TextField

        HBox buttonPanel = new HBox(15); // Panel untuk tombol
        buttonPanel.setAlignment(Pos.CENTER); // Mengatur posisi tombol
        addButton = new Button("Tambah Metode"); // Tombol untuk menambah metode
        addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        addButton.setPrefSize(180, 45); // Mengatur ukuran tombol
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"); // Mengatur gaya tombol
        addButton.setOnMouseEntered(_ -> addButton.setStyle(addButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        addButton.setOnMouseExited(_ -> addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;")); // Gaya saat mouse keluar
        addButton.setOnAction(_ -> handleAddMethod()); // Aksi saat tombol diklik

        editButton = new Button("Edit Terpilih"); // Tombol untuk mengedit metode terpilih
        editButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        editButton.setPrefSize(180, 45); // Mengatur ukuran tombol
        editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #2C3E50; -fx-border-color: #FFA000; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"); // Mengatur gaya tombol
        editButton.setOnMouseEntered(_ -> editButton.setStyle(editButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        editButton.setOnMouseExited(_ -> editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #2C3E50; -fx-border-color: #FFA000; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;")); // Gaya saat mouse keluar
        editButton.setDisable(true); // Menonaktifkan tombol edit jika tidak ada pilihan
        editButton.setOnAction(_ -> handleEditMethod()); // Aksi saat tombol diklik

        deleteButton = new Button("Hapus Terpilih"); // Tombol untuk menghapus metode terpilih
        deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        deleteButton.setPrefSize(180, 45); // Mengatur ukuran tombol
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"); // Mengatur gaya tombol
        deleteButton.setOnMouseEntered(_ -> deleteButton.setStyle(deleteButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        deleteButton.setOnMouseExited(_ -> deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;")); // Gaya saat mouse keluar
        deleteButton.setDisable(true); // Menonaktifkan tombol hapus jika tidak ada pilihan
        deleteButton.setOnAction(_ -> handleDeleteMethod()); // Aksi saat tombol diklik

        Button clearFormButton = new Button("Clear Form"); // Tombol untuk membersihkan form
        clearFormButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Mengatur font tombol
        clearFormButton.setPrefSize(180, 45); // Mengatur ukuran tombol
        clearFormButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-border-color: #757575; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"); // Mengatur gaya tombol
        clearFormButton.setOnMouseEntered(_ -> clearFormButton.setStyle(clearFormButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;")); // Gaya saat mouse masuk
        clearFormButton.setOnMouseExited(_ -> clearFormButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-border-color: #757575; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;")); // Gaya saat mouse keluar
        clearFormButton.setOnAction(_ -> clearFormFields()); // Aksi saat tombol diklik

        buttonPanel.getChildren().addAll(addButton, editButton, deleteButton, clearFormButton); // Menambahkan tombol ke panel tombol

        formPanel.getChildren().addAll( // Menambahkan elemen ke panel form
                formTitle,
                nameLabel, methodNameField,
                percentLabel, discountPercentField,
                descLabel, discountDescriptionField,
                buttonPanel
        );

        centerContent.getChildren().addAll(listLabel, paymentMethodListView, formPanel); // Menambahkan elemen ke konten tengah
        root.setCenter(centerContent); // Mengatur konten tengah di layout

        scene = new Scene(root, 950, 850); // Membuat scene dengan ukuran tertentu
    }

    public void refreshMetodePembayaranList() {
        paymentMethodListView.getItems().clear(); // Menghapus item di ListView
        List<BioskopDataStore.PaymentMethod> methods = controller.getDaftarMetodePembayaran(); // Mengambil daftar metode pembayaran
        paymentMethodListView.getItems().addAll(methods); // Menambahkan metode pembayaran ke ListView
        paymentMethodListView.getSelectionModel().clearSelection(); // Menghapus pilihan di ListView
        clearFormFields(); // Menghapus field form
    }

    private void clearFormFields() {
        methodNameField.clear(); // Menghapus field nama metode
        discountPercentField.clear(); // Menghapus field persentase diskon
        discountDescriptionField.clear(); // Menghapus field keterangan diskon
        selectedMethodForEdit = null; // Mengatur metode yang dipilih menjadi null
        editButton.setDisable(true); // Menonaktifkan tombol edit
        deleteButton.setDisable(true); // Menonaktifkan tombol hapus
        addButton.setText("Tambah Metode"); // Mengubah teks tombol kembali ke tambah
        addButton.setOnAction(_ -> handleAddMethod()); // Mengatur aksi tombol untuk tambah
    }

    private void handleAddMethod() {
        String name = methodNameField.getText().trim(); // Mengambil nama metode
        String percentText = discountPercentField.getText().trim(); // Mengambil persentase diskon
        String description = discountDescriptionField.getText().trim();
        discountDescriptionField.getText().trim(); // Mengambil keterangan diskon

        if (name.isEmpty() || percentText.isEmpty() || description.isEmpty()) { // Validasi input kosong
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field harus diisi."); // Menampilkan peringatan
            return;
        }

        try {
            int percent = Integer.parseInt(percentText); // Parsing persentase ke integer
            controller.tambahMetodePembayaran(name, percent, description); // Memanggil controller untuk menambah metode
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Persentase diskon harus berupa angka."); // Menampilkan error jika parsing gagal
        }
    }

    private void handleEditMethod() {
        if (selectedMethodForEdit == null) { // Validasi apakah ada metode yang dipilih
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih metode pembayaran yang ingin diedit."); // Menampilkan peringatan
            return;
        }

        String newName = methodNameField.getText().trim(); // Mengambil nama baru
        String newPercentText = discountPercentField.getText().trim(); // Mengambil persentase baru
        String newDescription = discountDescriptionField.getText().trim(); // Mengambil keterangan baru

        if (newName.isEmpty() || newPercentText.isEmpty() || newDescription.isEmpty()) { // Validasi input kosong
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field harus diisi."); // Menampilkan peringatan
            return;
        }

        try {
            int newPercent = Integer.parseInt(newPercentText); // Parsing persentase baru
            controller.editMetodePembayaran(selectedMethodForEdit, newName, newPercent, newDescription); // Memanggil controller untuk mengedit
            clearFormFields(); // Membersihkan form setelah edit
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Persentase diskon harus berupa angka."); // Menampilkan error jika parsing gagal
        }
    }

    private void handleDeleteMethod() {
        BioskopDataStore.PaymentMethod selectedMethod = paymentMethodListView.getSelectionModel().getSelectedItem(); // Mengambil metode yang dipilih
        if (selectedMethod != null) { // Validasi apakah ada metode yang dipilih
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION); // Membuat dialog konfirmasi
            confirmAlert.setTitle("Konfirmasi Penghapusan"); // Judul dialog
            confirmAlert.setHeaderText(null); // Tidak ada header
            confirmAlert.setContentText("Anda yakin ingin menghapus metode pembayaran '" + selectedMethod.getName() + "'?"); // Pesan konfirmasi

            Optional<ButtonType> result = confirmAlert.showAndWait(); // Menampilkan dialog dan menunggu respon
            if (result.isPresent() && result.get() == ButtonType.OK) { // Jika user klik OK
                controller.hapusMetodePembayaran(selectedMethod); // Memanggil controller untuk menghapus
            }
        } else { // Jika tidak ada metode yang dipilih
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih metode pembayaran yang ingin dihapus."); // Menampilkan peringatan
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
        stage.setTitle("BioskopKuy! - Manajemen Metode Pembayaran"); // Mengatur judul stage
        refreshMetodePembayaranList(); // Memperbarui daftar metode pembayaran
        stage.setScene(scene); // Mengatur scene untuk stage
        stage.show(); // Menampilkan stage
    }
}
