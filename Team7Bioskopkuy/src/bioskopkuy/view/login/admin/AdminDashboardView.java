package bioskopkuy.view.login.admin;

import bioskopkuy.controller.BioskopController;
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
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboardView {
    private final BioskopController controller;
    private final Stage stage;
    private Scene scene;

    public AdminDashboardView(BioskopController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to top right, #3A6D65, #5AAAA0);");

        HBox topLeftPanel = new HBox();
        topLeftPanel.setAlignment(Pos.TOP_LEFT);

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        logoutButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        logoutButton.setOnMouseEntered(_ -> logoutButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        logoutButton.setOnMouseExited(_ -> logoutButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-color: #D32F2F; -fx-border-width: 1.5px; -fx-background-radius: 8px; -fx-border-radius: 8px;"));
        logoutButton.setOnAction(_ -> controller.kembaliKeMainView());

        topLeftPanel.getChildren().add(logoutButton);
        root.setTop(topLeftPanel);
        BorderPane.setMargin(topLeftPanel, new Insets(0, 0, 30, 0));

        VBox optionsPanel = new VBox(25);
        optionsPanel.setAlignment(Pos.CENTER);
        optionsPanel.setPadding(new Insets(40));
        optionsPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-border-color: #5AAAA0;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 34));
        titleLabel.setTextFill(Color.web("#3A6D65"));
        VBox.setMargin(titleLabel, new Insets(0, 0, 25, 0));

        Button filmManagementButton = new Button("Manajemen Film");
        filmManagementButton.setPrefSize(300, 70);
        filmManagementButton.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        filmManagementButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        filmManagementButton.setOnMouseEntered(_ -> filmManagementButton.setStyle(filmManagementButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        filmManagementButton.setOnMouseExited(_ -> filmManagementButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        filmManagementButton.setOnAction(_ -> controller.tampilFilmManagement());

        Button paymentMethodManagementButton = new Button("Manajemen Pembayaran");
        paymentMethodManagementButton.setPrefSize(300, 70);
        paymentMethodManagementButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        paymentMethodManagementButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        paymentMethodManagementButton.setOnMouseEntered(_ -> paymentMethodManagementButton.setStyle(paymentMethodManagementButton.getStyle() + "-fx-scale-y: 1.05; -fx-scale-x: 1.05;"));
        paymentMethodManagementButton.setOnMouseExited(_ -> paymentMethodManagementButton.setStyle("-fx-background-color: #5AAAA0; -fx-text-fill: white; -fx-border-color: #3A6D65; -fx-border-width: 1.5px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        paymentMethodManagementButton.setOnAction(_ -> controller.tampilPaymentMethodManagement());

        optionsPanel.getChildren().addAll(
                titleLabel,
                filmManagementButton,
                paymentMethodManagementButton
        );
        root.setCenter(optionsPanel);

        BorderPane.setAlignment(optionsPanel, Pos.TOP_CENTER);

        scene = new Scene(root, 850, 650);
    }

    public void showView() {
        stage.setTitle("BioskopKuy! - Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}