package bioskopkuy.controller; // Package controller yang berfungsi sebagai penghubung antara model dan view

// Import semua komponen yang diperlukan dari model, service, view, dan JavaFX
import bioskopkuy.model.BioskopDataStore;
import bioskopkuy.model.BioskopModel;
import bioskopkuy.service.BioskopException;
import bioskopkuy.view.MainView;
import bioskopkuy.view.admin.AdminDashboardView;
import bioskopkuy.view.admin.FilmManagementView;
import bioskopkuy.view.admin.PaymentMethodManagementView;
import bioskopkuy.view.penonton.FilmSelectionView;
import bioskopkuy.view.penonton.PaymentInputView;
import bioskopkuy.view.penonton.PaymentSelectionView;
import bioskopkuy.view.penonton.SeatSelectionView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Controller utama untuk aplikasi bioskop
public class BioskopController {
    // === Properti utama untuk model dan view ===
    private final BioskopModel model;

    private final MainView mainView;
    private final FilmSelectionView filmSelectionView;
    private final SeatSelectionView seatSelectionView;
    private final PaymentSelectionView paymentSelectionView;
    private final PaymentInputView paymentInputView;

    private final AdminDashboardView adminDashboardView;
    private final FilmManagementView filmManagementView;
    private final PaymentMethodManagementView paymentMethodManagementView;

    // === Konstanta kredensial admin ===
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "Admin123";
    private boolean adminLoggedIn = false;

    // === Konstruktor controller ===
    public BioskopController(BioskopModel model, Stage primaryStage) {
        this.model = model;

        // Inisialisasi semua view dan mengirimkan controller serta stage ke masing-masing view
        this.mainView = new MainView(this, primaryStage);
        this.filmSelectionView = new FilmSelectionView(this, primaryStage);
        this.seatSelectionView = new SeatSelectionView(this, primaryStage);
        this.paymentSelectionView = new PaymentSelectionView(this, primaryStage);
        this.paymentInputView = new PaymentInputView(this, primaryStage);
        this.adminDashboardView = new AdminDashboardView(this, primaryStage);
        this.filmManagementView = new FilmManagementView(this, primaryStage);
        this.paymentMethodManagementView = new PaymentMethodManagementView(this, primaryStage);
    }

    // Mengembalikan instance model
    public BioskopModel getModel() {
        return model;
    }

    // Memulai aplikasi dengan menampilkan tampilan utama
    public void mulaiAplikasi() {
        mainView.showView();
    }

    public boolean isAdminLoggedIn() {
        return adminLoggedIn;
    }

    // === Handler login untuk penonton dan admin ===
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

    // === Navigasi antar tampilan ===
    public void tampilFilmSelection() {
        filmSelectionView.showView();
    }

    public void kembaliKeMainView() {
        model.resetTransaksi(); // Reset semua data transaksi saat kembali ke menu utama
        adminLoggedIn = false;
        mainView.showView();
    }

    public void kembaliKeFilmSelectionView() {
        model.clearKursiTerpilih(); // Kosongkan kursi terpilih
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

    // === Pemilihan film dan jam tayang ===
    public void pilihFilmDanJam(BioskopModel.Film film, String jam) {
        model.setFilmTerpilih(film);
        model.setJamTerpilih(jam);
        model.clearKursiTerpilih();

        seatSelectionView.setFilmJudul(film.getJudul());
        seatSelectionView.updateKursiGrid();
        seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted());
        seatSelectionView.showView();
    }

    // === Memilih atau membatalkan pilihan kursi ===
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

    // === Lanjut ke pemilihan metode pembayaran ===
    public void lanjutKePembayaran() {
        if (model.getKursiTerpilih().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon pilih setidaknya satu kursi.");
            return;
        }
        paymentSelectionView.showView();
    }

    // === Memilih metode pembayaran ===
    public void pilihMetodePembayaran(BioskopDataStore.PaymentMethod metode) {
        model.setMetodePembayaranTerpilih(metode);
        paymentInputView.setTotalHargaDisplay(model.getTotalHargaSetelahDiskonFormatted());
        paymentInputView.updateDiskonDisplay(model.getDiskonPersenDariMetodeTerpilih(), model.getDiskonKeteranganDariMetodeTerpilih());
        paymentInputView.showView();
    }

    // === Proses pembayaran dengan animasi loading dan validasi ===
    public void prosesPembayaran(double uangDibayar) {
        model.setUangDibayar(uangDibayar);

        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
        loadingAlert.setTitle("Memproses");
        loadingAlert.setHeaderText(null);
        loadingAlert.setContentText("Memproses pembayaran, harap tunggu...");
        loadingAlert.getDialogPane().lookupButton(loadingAlert.getButtonTypes().getFirst()).setVisible(false);
        loadingAlert.show();

        // Delay simulasi proses pembayaran
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

    // === Manajemen data Film ===
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
            model.add(newFilm);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + judul + "' berhasil ditambahkan.");
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void hapusFilm(BioskopModel.Film film) {
        try {
            model.remove(film);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + film.getJudul() + "' berhasil dihapus.");
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // === Mendapatkan daftar metode pembayaran ===
    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return model.getDaftarMetodePembayaran();
    }

    // === Manajemen metode pembayaran ===
    public void tambahMetodePembayaran(String name, int discountPercent, String discountDescription) {
        try {
            model.addMetodePembayaran(name, discountPercent, discountDescription);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + name + "' berhasil ditambahkan.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void hapusMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) {
        try {
            model.removeMetodePembayaran(methodToRemove);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + methodToRemove.getName() + "' berhasil dihapus.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void editMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) {
        try {
            model.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + originalMethod.getName() + "' berhasil diperbarui.");
            paymentMethodManagementView.refreshMetodePembayaranList();
            paymentSelectionView.updateMetodePembayaranButtons();
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // === Utility untuk menampilkan alert pop-up ===
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
