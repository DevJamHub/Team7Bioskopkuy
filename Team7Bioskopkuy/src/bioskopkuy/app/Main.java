package bioskopkuy.app; // Menyatakan bahwa kelas ini berada di package 'bioskopkuy.app'

import bioskopkuy.controller.BioskopController; // Mengimpor kelas controller utama aplikasi
import bioskopkuy.model.BioskopModel;           // Mengimpor model utama yang menyimpan data aplikasi
import javafx.application.Application;          // Kelas utama dari JavaFX untuk membuat aplikasi GUI
import javafx.stage.Stage;                      // Representasi dari jendela utama aplikasi JavaFX

// Kelas utama yang menjalankan aplikasi JavaFX
public class Main extends Application {

    // Metode start() akan dipanggil otomatis saat aplikasi JavaFX dijalankan
    @Override
    public void start(Stage primaryStage) {
        // Membuat objek model yang menyimpan data dan logika aplikasi
        BioskopModel model = new BioskopModel();

        // Membuat controller yang akan mengatur interaksi antara model dan tampilan
        BioskopController controller = new BioskopController(model, primaryStage);

        // Memulai aplikasi melalui metode utama yang ada di controller
        controller.mulaiAplikasi();
    }

    // Metode main sebagai titik awal program Java
    public static void main(String[] args) {
        // Memulai aplikasi JavaFX, akan memicu pemanggilan metode start()
        launch(args);
    }
}
