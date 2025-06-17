package bioskopkuy.view.login.admin;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopDataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

public class PaymentMethodManagementView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    private ListView<BioskopDataStore.PaymentMethod> paymentMethodListView;
    private TextField methodNameField;
    private TextField discountPercentField;
    private TextField discountDescriptionField;
    private Button editButton;
    private Button deleteButton;
    private Button addButton;

    private BioskopDataStore.PaymentMethod selectedMethodForEdit;

    public PaymentMethodManagementView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to top right, #5AAAA0, #7BD4C6);");

        HBox topPanel = new HBox(20);
        topPanel.setAlignment(Pos.CENTER_LEFT);
        Button backButton = new Button("Kembali");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #D3E0E1; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #F8F8F8; -fx-text-fill: #2C3E50; -fx-border-color: #5AAAA0; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        backButton.setOnAction(_ -> controller.kembaliKeAdminDashboard());

        Label titleLabel = new Label("Manajemen Metode Pembayaran");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        topPanel.getChildren().addAll(backButton, titleLabel);
        root.setTop(topPanel);
        BorderPane.setMargin(topPanel, new Insets(0, 0, 25, 0));

        VBox centerContent = new VBox(25);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(30));
        centerContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        Label listLabel = new Label("Metode Pembayaran Saat Ini:");
        listLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        listLabel.setTextFill(Color.web("#3A6D65"));

        paymentMethodListView = new ListView<>();
        paymentMethodListView.setPrefHeight(250);
        paymentMethodListView.setPlaceholder(new Label("Tidak ada metode pembayaran tersedia."));
        paymentMethodListView.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        paymentMethodListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(BioskopDataStore.PaymentMethod method, boolean empty) {
                super.updateItem(method, empty);
                if (empty || method == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(method.toString());
                    setStyle("-fx-padding: 8px;");
                }
            }
        });

        paymentMethodListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                selectedMethodForEdit = newVal;
                methodNameField.setText(newVal.getName());
                discountPercentField.setText(String.valueOf(newVal.getDiscountPercent()));
                discountDescriptionField.setText(newVal.getDiscountDescription());
                editButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setText("Update Metode");
                addButton.setOnAction(_ -> handleEditMethod());
            } else {
                clearFormFields();
                addButton.setText("Tambah Metode");
                addButton.setOnAction(_ -> handleAddMethod());
            }
        });

        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle("-fx-border-color: #5AAAA0; -fx-border-width: 1px; -fx-padding: 15px; -fx-background-color: #E0F2F1; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Label formTitle = new Label("Formulir Metode Pembayaran");
        formTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#3A6D65"));

        Label nameLabel = new Label("Nama Metode:");
        nameLabel.setFont(Font.font("Verdana", 16));
        methodNameField = new TextField();
        methodNameField.setPromptText("Nama Metode (misal: GoPay, Dana)");
        methodNameField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        Label percentLabel = new Label("Diskon (%):");
        percentLabel.setFont(Font.font("Verdana", 16));
        discountPercentField = new TextField();
        discountPercentField.setPromptText("Persentase Diskon (0-100)");
        discountPercentField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");
        discountPercentField.textProperty().addListener((_, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                discountPercentField.setText(oldVal);
            }
        });

        Label descLabel = new Label("Keterangan Diskon:");
        descLabel.setFont(Font.font("Verdana", 16));
        discountDescriptionField = new TextField();
        discountDescriptionField.setPromptText("Keterangan (misal: Promo Akhir Tahun)");
        discountDescriptionField.setStyle("-fx-font-size: 15px; -fx-padding: 8px; -fx-background-color: #F8F8F8; -fx-border-color: #B2D8D3; -fx-border-width: 1px; -fx-border-radius: 5px;");


        HBox buttonPanel = new HBox(15);
        buttonPanel.setAlignment(Pos.CENTER);
        addButton = new Button("Tambah Metode");
        addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        addButton.setPrefSize(180, 45);
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        addButton.setOnMouseEntered(e -> addButton.setStyle(addButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #388E3C; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        addButton.setOnAction(_ -> handleAddMethod());

        editButton = new Button("Edit Terpilih");
        editButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        editButton.setPrefSize(180, 45);
        editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #2C3E50; -fx-border-color: #FFA000; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        editButton.setOnMouseEntered(e -> editButton.setStyle(editButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #2C3E50; -fx-border-color: #FFA000; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        editButton.setDisable(true);
        editButton.setOnAction(_ -> handleEditMethod());

        deleteButton = new Button("Hapus Terpilih");
        deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        deleteButton.setPrefSize(180, 45);
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(deleteButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        deleteButton.setDisable(true);
        deleteButton.setOnAction(_ -> handleDeleteMethod());

        Button clearFormButton = new Button("Clear Form");
        clearFormButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        clearFormButton.setPrefSize(180, 45);
        clearFormButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-border-color: #757575; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        clearFormButton.setOnMouseEntered(e -> clearFormButton.setStyle(clearFormButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        clearFormButton.setOnMouseExited(e -> clearFormButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-border-color: #757575; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        clearFormButton.setOnAction(_ -> clearFormFields());

        buttonPanel.getChildren().addAll(addButton, editButton, deleteButton, clearFormButton);

        formPanel.getChildren().addAll(
                formTitle,
                nameLabel, methodNameField,
                percentLabel, discountPercentField,
                descLabel, discountDescriptionField,
                buttonPanel
        );

        centerContent.getChildren().addAll(listLabel, paymentMethodListView, formPanel);
        root.setCenter(centerContent);

        scene = new Scene(root, 950, 850);
    }

    public void refreshMetodePembayaranList() {
        paymentMethodListView.getItems().clear();
        List<BioskopDataStore.PaymentMethod> methods = controller.getDaftarMetodePembayaran();
        paymentMethodListView.getItems().addAll(methods);
        paymentMethodListView.getSelectionModel().clearSelection();
        clearFormFields();
    }

    private void clearFormFields() {
        methodNameField.clear();
        discountPercentField.clear();
        discountDescriptionField.clear();
        selectedMethodForEdit = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setText("Tambah Metode");
        addButton.setOnAction(_ -> handleAddMethod());
    }

    private void handleAddMethod() {
        String name = methodNameField.getText().trim();
        String percentText = discountPercentField.getText().trim();
        String description = discountDescriptionField.getText().trim();

        if (name.isEmpty() || percentText.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field harus diisi.");
            return;
        }

        try {
            int percent = Integer.parseInt(percentText);
            controller.tambahMetodePembayaran(name, percent, description);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Persentase diskon harus berupa angka.");
        }
    }

    private void handleEditMethod() {
        if (selectedMethodForEdit == null) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih metode pembayaran yang ingin diedit.");
            return;
        }

        String newName = methodNameField.getText().trim();
        String newPercentText = discountPercentField.getText().trim();
        String newDescription = discountDescriptionField.getText().trim();

        if (newName.isEmpty() || newPercentText.isEmpty() || newDescription.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field harus diisi.");
            return;
        }

        try {
            int newPercent = Integer.parseInt(newPercentText);
            controller.editMetodePembayaran(selectedMethodForEdit, newName, newPercent, newDescription);
            clearFormFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Persentase diskon harus berupa angka.");
        }
    }

    private void handleDeleteMethod() {
        BioskopDataStore.PaymentMethod selectedMethod = paymentMethodListView.getSelectionModel().getSelectedItem();
        if (selectedMethod != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Penghapusan");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Anda yakin ingin menghapus metode pembayaran '" + selectedMethod.getName() + "'?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.hapusMetodePembayaran(selectedMethod);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih metode pembayaran yang ingin dihapus.");
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
        stage.setTitle("BioskopKuy! - Manajemen Metode Pembayaran");
        refreshMetodePembayaranList();
        stage.setScene(scene);
        stage.show();
    }
}