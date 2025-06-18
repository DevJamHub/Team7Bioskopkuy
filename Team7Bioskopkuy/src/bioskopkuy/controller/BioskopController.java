package bioskopkuy.controller;

import bioskopkuy.model.BioskopDataStore;
import bioskopkuy.model.BioskopModel;
import bioskopkuy.service.BioskopException;
import bioskopkuy.view.login.MainView;
import bioskopkuy.view.login.admin.AdminDashboardView;
import bioskopkuy.view.login.admin.FilmManagementView;
import bioskopkuy.view.login.admin.PaymentMethodManagementView;
import bioskopkuy.view.login.penonton.FilmSelectionView;
import bioskopkuy.view.login.penonton.PaymentInputView;
import bioskopkuy.view.login.penonton.PaymentSelectionView;
import bioskopkuy.view.login.penonton.SeatSelectionView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService; // Corrected typo
import java.util.concurrent.TimeUnit;

public class BioskopController {
    private final BioskopModel model;
    private final MainView mainView;
    private final FilmSelectionView filmSelectionView;
    private final SeatSelectionView seatSelectionView;
    private final PaymentSelectionView paymentSelectionView;
    private final PaymentInputView paymentInputView;

    private final AdminDashboardView adminDashboardView;
    private final FilmManagementView filmManagementView;
    private final PaymentMethodManagementView paymentMethodManagementView;

    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "Admin123";
    private boolean adminLoggedIn = false;

    public BioskopController(BioskopModel model, Stage primaryStage) {
        this.model = model;

        this.mainView = new MainView(this, primaryStage);
        this.filmSelectionView = new FilmSelectionView(this, primaryStage);
        this.seatSelectionView = new SeatSelectionView(this, primaryStage);
        this.paymentSelectionView = new PaymentSelectionView(this, primaryStage);
        this.paymentInputView = new PaymentInputView(this, primaryStage);

        this.adminDashboardView = new AdminDashboardView(this, primaryStage);
        this.filmManagementView = new FilmManagementView(this, primaryStage);
        this.paymentMethodManagementView = new PaymentMethodManagementView(this, primaryStage);
    }

    public BioskopModel getModel() {
        return model;
    }

    public void mulaiAplikasi() {
        mainView.showView();
    }

    public boolean isAdminLoggedIn() {
        return adminLoggedIn;
    }

    public void handleLogin(String role, String username, String password) {
        if ("Penonton".equals(role)) {
            adminLoggedIn = false;
            tampilFilmSelection();
        } else if ("Admin Bioskop".equals(role)) {
            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                adminLoggedIn = true;
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, Admin!");
                tampilAdminDashboard();
            } else {
                adminLoggedIn = false;
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah.");
            }
        }
    }

    public void tampilFilmSelection() {
        filmSelectionView.showView();
    }

    public void kembaliKeMainView() {
        model.resetTransaksi();
        adminLoggedIn = false;
        mainView.showView();
    }

    public void kembaliKeFilmSelectionView() {
        model.clearKursiTerpilih();
        filmSelectionView.showView();
    }

    public void kembaliKeSeatSelectionView() {
        seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted());
        seatSelectionView.showView();
    }

    public void kembaliKePaymentSelectionView() {
        paymentSelectionView.showView();
    }

    public void tampilAdminDashboard() {
        adminDashboardView.showView();
    }

    public void tampilFilmManagement() {
        filmManagementView.showView();
    }

    public void tampilPaymentMethodManagement() {
        paymentMethodManagementView.showView();
    }

    public void kembaliKeAdminDashboard() {
        adminDashboardView.showView();
    }

    public void pilihFilmDanJam(BioskopModel.Film film, String jam) {
        model.setFilmTerpilih(film);
        model.setJamTerpilih(jam);
        model.clearKursiTerpilih();

        seatSelectionView.setFilmJudul(film.getJudul());
        seatSelectionView.updateKursiGrid();
        seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted());
        seatSelectionView.showView();
    }

    public void toggleKursiTerpilih(String kursiName) {
        try {
            if (model.getKursiTerpilih().contains(kursiName)) {
                model.removeKursiTerpilih(kursiName);
            } else {
                model.addKursiTerpilih(kursiName);
            }
            seatSelectionView.updateKursiGrid();
            seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted());
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", e.getMessage());
        }
    }

    public void lanjutKePembayaran() {
        if (model.getKursiTerpilih().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon pilih setidaknya satu kursi.");
            return;
        }
        paymentSelectionView.showView();
    }

    public void pilihMetodePembayaran(BioskopDataStore.PaymentMethod metode) {
        model.setMetodePembayaranTerpilih(metode);
        paymentInputView.setTotalHargaDisplay(model.getTotalHargaSetelahDiskonFormatted());
        paymentInputView.updateDiskonDisplay(model.getDiskonPersenDariMetodeTerpilih(), model.getDiskonKeteranganDariMetodeTerpilih());
        paymentInputView.showView();
    }

    public void prosesPembayaran(double uangDibayar) {
        model.setUangDibayar(uangDibayar);

        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
        loadingAlert.setTitle("Memproses");
        loadingAlert.setHeaderText(null);
        loadingAlert.setContentText("Memproses pembayaran, harap tunggu...");
        loadingAlert.getDialogPane().lookupButton(loadingAlert.getButtonTypes().getFirst()).setVisible(false);
        loadingAlert.show();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            Platform.runLater(() -> {
                loadingAlert.close();
                try {
                    boolean success = model.prosesPembayaran();
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses",
                                "Pembayaran Berhasil!\n" +
                                        "Film: " + model.getFilmTerpilih().getJudul() + "\n" +
                                        "Jam: " + model.getJamTerpilih() + "\n" +
                                        "Kursi: " + String.join(", ", model.getKursiTerpilih()) + "\n" +
                                        "Metode Pembayaran: " + model.getMetodePembayaranTerpilih().getName() + "\n" +
                                        "Diskon (" + model.getDiskonKeteranganDariMetodeTerpilih() + "): " + model.getDiskonPersenDariMetodeTerpilih() + "%\n" +
                                        "Total Sebelum Diskon: " + model.getTotalHargaSebelumDiskonFormatted() + "\n" +
                                        "Total Setelah Diskon: " + model.getTotalHargaSetelahDiskonFormatted() + "\n" +
                                        "Dibayar: Rp" + String.format("%,.0f", model.getUangDibayar()) + "\n" +
                                        "Kembalian: Rp" + String.format("%,.0f", (model.getUangDibayar() - model.getTotalHargaAfterDiskon())));

                        model.resetTransaksi();
                        mainView.showView();
                    }
                } catch (BioskopException e) {
                    showAlert(Alert.AlertType.ERROR, "Error Pembayaran", e.getMessage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat memproses pembayaran: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            executor.shutdown();
        }, 1500, TimeUnit.MILLISECONDS);
    }

    public void tambahFilm(String judul, double harga, String jamTayang, String imagePath) {
        try {
            BioskopModel.Film newFilm = new BioskopModel.Film(judul, harga, imagePath);
            String[] jams = jamTayang.split(",");
            if (jams.length == 0 || (jams.length == 1 && jams[0].trim().isEmpty())) {
                throw new BioskopException("Jam tayang tidak boleh kosong.");
            }
            for (String jam : jams) {
                String trimmedJam = jam.trim();
                if (!trimmedJam.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    throw new BioskopException("Format jam tayang tidak valid: " + trimmedJam + ". Gunakan HH:mm (misal: 12:00, 14:30).");
                }
                newFilm.addJamTayang(trimmedJam);
            }
            model.add(newFilm); // Memanggil method add dari BioskopModel (implementasi IManagementService<Film>)
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + judul + "' berhasil ditambahkan.");
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void hapusFilm(BioskopModel.Film film) {
        try {
            model.remove(film); // Memanggil method remove dari BioskopModel (implementasi IManagementService<Film>)
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + film.getJudul() + "' berhasil dihapus.");
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        // Memanggil metode reguler di BioskopModel, bukan implementasi interface getAll()
        return model.getDaftarMetodePembayaran();
    }

    public void tambahMetodePembayaran(String name, int discountPercent, String discountDescription) {
        try {
            model.addMetodePembayaran(name, discountPercent, discountDescription); // Memanggil metode reguler di BioskopModel
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + name + "' berhasil ditambahkan.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void hapusMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) {
        try {
            model.removeMetodePembayaran(methodToRemove); // Memanggil metode reguler di BioskopModel
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + methodToRemove.getName() + "' berhasil dihapus.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void editMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) {
        try {
            model.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription); // Memanggil metode reguler di BioskopModel
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + originalMethod.getName() + "' berhasil diperbarui.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}