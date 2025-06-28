package bioskopkuy.controller; // Package untuk kelas controller yang menghubungkan model dan view

// Import semua komponen yang diperlukan dari model, service, view, dan JavaFX
import bioskopkuy.model.BioskopDataStore; // Model untuk menyimpan data bioskop
import bioskopkuy.model.BioskopModel; // Model untuk logika aplikasi bioskop
import bioskopkuy.service.BioskopException; // Exception untuk menangani kesalahan
import bioskopkuy.view.MainView; // Tampilan utama aplikasi
import bioskopkuy.view.admin.AdminDashboardView; // Tampilan dashboard admin
import bioskopkuy.view.admin.FilmManagementView; // Tampilan manajemen film
import bioskopkuy.view.admin.PaymentMethodManagementView; // Tampilan manajemen metode pembayaran
import bioskopkuy.view.penonton.FilmSelectionView; // Tampilan pemilihan film untuk penonton
import bioskopkuy.view.penonton.PaymentInputView; // Tampilan input pembayaran
import bioskopkuy.view.penonton.PaymentSelectionView; // Tampilan pemilihan metode pembayaran
import bioskopkuy.view.penonton.SeatSelectionView; // Tampilan pemilihan kursi
import javafx.application.Platform; // Platform untuk menjalankan kode di thread JavaFX
import javafx.scene.control.Alert; // Alert untuk menampilkan pesan
import javafx.stage.Stage; // Stage untuk jendela aplikasi
import java.util.List; // Import untuk daftar
import java.util.concurrent.Executors; // Executor untuk menjalankan tugas di thread terpisah
import java.util.concurrent.ScheduledExecutorService; // Executor untuk tugas terjadwal
import java.util.concurrent.TimeUnit; // Unit waktu untuk penjadwalan

// Kelas utama controller yang menjadi penghubung antara logika bisnis (model) dan tampilan (view)
public class BioskopController {

    // === Properti untuk menyimpan objek model dan semua view yang akan digunakan ===
    private final BioskopModel model; // Model data bioskop

    // View untuk penonton
    private final MainView mainView; // Tampilan utama
    private final FilmSelectionView filmSelectionView; // Tampilan pemilihan film
    private final SeatSelectionView seatSelectionView; // Tampilan pemilihan kursi
    private final PaymentSelectionView paymentSelectionView; // Tampilan pemilihan metode pembayaran
    private final PaymentInputView paymentInputView; // Tampilan input pembayaran

    // View untuk admin
    private final AdminDashboardView adminDashboardView; // Dashboard admin
    private final FilmManagementView filmManagementView; // Manajemen film
    private final PaymentMethodManagementView paymentMethodManagementView; // Manajemen metode pembayaran

    // === Kredensial untuk login sebagai admin ===
    private static final String ADMIN_USERNAME = "Admin"; // Username admin
    private static final String ADMIN_PASSWORD = "Admin123"; // Password admin
    private boolean adminLoggedIn = false; // Status login admin

    // Konstruktor controller yang menerima model dan stage utama
    public BioskopController(BioskopModel model, Stage primaryStage) {
        this.model = model; // Inisialisasi model

        // Inisialisasi seluruh view dan mengirimkan referensi controller dan stage
        this.mainView = new MainView(this, primaryStage); // Inisialisasi tampilan utama
        this.filmSelectionView = new FilmSelectionView(this, primaryStage); // Inisialisasi tampilan pemilihan film
        this.seatSelectionView = new SeatSelectionView(this, primaryStage); // Inisialisasi tampilan pemilihan kursi
        this.paymentSelectionView = new PaymentSelectionView(this, primaryStage); // Inisialisasi tampilan pemilihan metode pembayaran
        this.paymentInputView = new PaymentInputView(this, primaryStage); // Inisialisasi tampilan input pembayaran
        this.adminDashboardView = new AdminDashboardView(this, primaryStage); // Inisialisasi dashboard admin
        this.filmManagementView = new FilmManagementView(this, primaryStage); // Inisialisasi tampilan manajemen film
        this.paymentMethodManagementView = new PaymentMethodManagementView(this, primaryStage); // Inisialisasi tampilan manajemen metode pembayaran
    }

    // Getter untuk model, agar dapat diakses oleh view jika diperlukan
    public BioskopModel getModel() {
        return model; // Mengembalikan model
    }

    // Memulai aplikasi dengan menampilkan tampilan utama (main menu)
    public void mulaiAplikasi() {
        mainView.showView(); // Menampilkan tampilan utama
    }

    // Mengembalikan status login admin
    public boolean isAdminLoggedIn() {
        return adminLoggedIn; // Mengembalikan status login admin
    }

    // === Logika login berdasarkan role (Penonton atau Admin) ===
    public void handleLogin(String role, String username, String password) {
        if ("Penonton".equals(role)) {
            // Jika login sebagai penonton, tidak perlu validasi username/password
            adminLoggedIn = false; // Set status admin tidak login
            tampilFilmSelection(); // Tampilkan pemilihan film
        } else if ("Admin Bioskop".equals(role)) {
            // Jika login sebagai admin, validasi username dan password
            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                adminLoggedIn = true; // Set status admin login
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, Admin!"); // Tampilkan alert sukses
                tampilAdminDashboard(); // Tampilkan dashboard admin
            } else {
                adminLoggedIn = false; // Set status admin tidak login
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah."); // Tampilkan alert gagal
            }
        }
    }

    // === Navigasi antar view (halaman) ===

    // Tampilkan halaman pemilihan film
    public void tampilFilmSelection() {
        filmSelectionView.showView(); // Menampilkan tampilan pemilihan film
    }

    // Kembali ke halaman utama dan reset transaksi
    public void kembaliKeMainView() {
        model.resetTransaksi(); // Reset transaksi
        adminLoggedIn = false; // Set status admin tidak login
        mainView.showView(); // Tampilkan tampilan utama
    }

    // Kembali ke halaman pemilihan film dan hapus kursi yang telah dipilih
    public void kembaliKeFilmSelectionView() {
        model.clearKursiTerpilih(); // Hapus kursi yang dipilih
        filmSelectionView.showView(); // Tampilkan tampilan pemilihan film
    }

    // Kembali ke halaman pemilihan kursi dan perbarui tampilan kursi yang dipilih
    public void kembaliKeSeatSelectionView() {
        seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted()); // Perbarui tampilan kursi
        seatSelectionView.showView(); // Tampilkan tampilan pemilihan kursi
    }

    // Kembali ke halaman pemilihan metode pembayaran
    public void kembaliKePaymentSelectionView() {
        paymentSelectionView.showView(); // Tampilkan tampilan pemilihan metode pembayaran
    }

    // Tampilkan dashboard admin
    public void tampilAdminDashboard() {
        adminDashboardView.showView(); // Menampilkan dashboard admin
    }

    // Tampilkan halaman manajemen film
    public void tampilFilmManagement() {
        filmManagementView.showView(); // Menampilkan tampilan manajemen film
    }

    // Tampilkan halaman manajemen metode pembayaran
    public void tampilPaymentMethodManagement() {
        paymentMethodManagementView.showView(); // Menampilkan tampilan manajemen metode pembayaran
    }

    // Kembali ke dashboard admin dari halaman lain
    public void kembaliKeAdminDashboard() {
        adminDashboardView.showView(); // Menampilkan dashboard admin
    }

    // === Logika pemilihan film dan jam tayang ===
    public void pilihFilmDanJam(BioskopModel.Film film, String jam) {
        model.setFilmTerpilih(film); // Set film yang dipilih
        model.setJamTerpilih(jam); // Set jam tayang yang dipilih
        model.clearKursiTerpilih(); // Hapus kursi yang dipilih

        seatSelectionView.setFilmJudul(film.getJudul()); // Set judul film di tampilan pemilihan kursi
        seatSelectionView.updateKursiGrid(); // Perbarui grid kursi
        seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted()); // Perbarui tampilan kursi yang dipilih
        seatSelectionView.showView(); // Tampilkan tampilan pemilihan kursi
    }

    // === Logika pemilihan dan pembatalan kursi ===
    public void toggleKursiTerpilih(String kursiName) {
        try {
            // Jika kursi sudah dipilih, batalkan
            if (model.getKursiTerpilih().contains(kursiName)) {
                model.removeKursiTerpilih(kursiName); // Hapus kursi dari yang dipilih
            } else {
                model.addKursiTerpilih(kursiName); // Tambahkan kursi ke yang dipilih
            }
            // Update tampilan grid dan informasi kursi yang terpilih
            seatSelectionView.updateKursiGrid(); // Perbarui grid kursi
            seatSelectionView.updateKursiTerpilihDisplay(model.getKursiTerpilih(), model.getTotalHargaSebelumDiskonFormatted()); // Perbarui tampilan kursi yang dipilih
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    // Lanjut ke pemilihan metode pembayaran jika kursi sudah dipilih
    public void lanjutKePembayaran() {
        if (model.getKursiTerpilih().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon pilih setidaknya satu kursi."); // Tampilkan alert jika tidak ada kursi yang dipilih
            return; // Keluar dari metode
        }
        paymentSelectionView.showView(); // Tampilkan tampilan pemilihan metode pembayaran
    }

    // === Pemilihan metode pembayaran ===
    public void pilihMetodePembayaran(BioskopDataStore.PaymentMethod metode) {
        model.setMetodePembayaranTerpilih(metode); // Set metode pembayaran yang dipilih
        paymentInputView.setTotalHargaDisplay(model.getTotalHargaSetelahDiskonFormatted()); // Tampilkan total harga setelah diskon
        paymentInputView.updateDiskonDisplay(model.getDiskonPersenDariMetodeTerpilih(), model.getDiskonKeteranganDariMetodeTerpilih()); // Perbarui tampilan diskon
        paymentInputView.showView(); // Tampilkan tampilan input pembayaran
    }

    // === Proses pembayaran dengan simulasi delay ===
    public void prosesPembayaran(double uangDibayar) {
        model.setUangDibayar(uangDibayar); // Set uang yang dibayar

        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION); // Alert untuk loading
        loadingAlert.setTitle("Memproses"); // Judul alert
        loadingAlert.setHeaderText(null); // Header alert
        loadingAlert.setContentText("Memproses pembayaran, harap tunggu..."); // Konten alert
        loadingAlert.getDialogPane().lookupButton(loadingAlert.getButtonTypes().getFirst()).setVisible(false); // Sembunyikan tombol di alert
        loadingAlert.show(); // Tampilkan alert loading

        // Eksekusi proses pembayaran dengan delay 1.5 detik
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); // Executor untuk menjalankan proses di thread terpisah
        executor.schedule(() -> {
            Platform.runLater(() -> {
                loadingAlert.close(); // Tutup alert loading
                try {
                    boolean success = model.prosesPembayaran(); // Proses pembayaran
                    if (success) {
                        // Tampilkan alert sukses jika pembayaran berhasil
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
                        model.resetTransaksi(); // Reset transaksi setelah pembayaran
                        mainView.showView(); // Tampilkan tampilan utama
                    }
                } catch (BioskopException e) {
                    showAlert(Alert.AlertType.ERROR, "Error Pembayaran", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat memproses pembayaran: " + e.getMessage()); // Tampilkan alert jika terjadi kesalahan
                    e.printStackTrace(); // Cetak stack trace untuk debugging
                }
            });
            executor.shutdown(); // Shutdown executor
        }, 1500, TimeUnit.MILLISECONDS); // Delay 1.5 detik
    }

    // === Manajemen film oleh admin ===
    public void tambahFilm(String judul, double harga, String jamTayang, String imagePath) {
        try {
            BioskopModel.Film newFilm = new BioskopModel.Film(judul, harga, imagePath); // Buat objek film baru
            String[] jams = jamTayang.split(","); // Pisahkan jam tayang
            if (jams.length == 0 || (jams.length == 1 && jams[0].trim().isEmpty())) {
                throw new BioskopException("Jam tayang tidak boleh kosong."); // Validasi jam tayang
            }
            for (String jam : jams) {
                String trimmedJam = jam.trim(); // Trim jam tayang
                // Validasi format jam tayang menggunakan pola HH:mm
                if (!trimmedJam.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    throw new BioskopException("Format jam tayang tidak valid: " + trimmedJam + ". Gunakan HH:mm (misal: 12:00, 14:30)."); // Validasi format jam tayang
                }
                newFilm.addJamTayang(trimmedJam); // Tambahkan jam tayang ke film
            }
            model.add(newFilm); // Tambahkan film ke model
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + judul + "' berhasil ditambahkan."); // Tampilkan alert sukses
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    // Menghapus film dari daftar
    public void hapusFilm(BioskopModel.Film film) {
        try {
            model.remove(film); // Hapus film dari model
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Film '" + film.getJudul() + "' berhasil dihapus."); // Tampilkan alert sukses
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    // === Manajemen metode pembayaran oleh admin ===
    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return model.getDaftarMetodePembayaran(); // Mengembalikan daftar metode pembayaran
    }

    public void tambahMetodePembayaran(String name, int discountPercent, String discountDescription) {
        try {
            model.addMetodePembayaran(name, discountPercent, discountDescription); // Tambahkan metode pembayaran ke model
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + name + "' berhasil ditambahkan."); // Tampilkan alert sukses
            paymentMethodManagementView.refreshMetodePembayaranList(); // Refresh daftar metode pembayaran di tampilan manajemen
            paymentSelectionView.updateMetodePembayaranButtons(); // Perbarui tombol metode pembayaran di tampilan pemilihan
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    public void hapusMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) {
        try {
            model.removeMetodePembayaran(methodToRemove); // Hapus metode pembayaran dari model
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + methodToRemove.getName() + "' berhasil dihapus."); // Tampilkan alert sukses
            paymentMethodManagementView.refreshMetodePembayaranList(); // Refresh daftar metode pembayaran di tampilan manajemen
            paymentSelectionView.updateMetodePembayaranButtons(); // Perbarui tombol metode pembayaran di tampilan pemilihan
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    public void editMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) {
        try {
            model.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription); // Perbarui metode pembayaran di model
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode pembayaran '" + originalMethod.getName() + "' berhasil diperbarui."); // Tampilkan alert sukses
            paymentMethodManagementView.refreshMetodePembayaranList(); // Refresh daftar metode pembayaran di tampilan manajemen
            paymentSelectionView.updateMetodePembayaranButtons(); // Perbarui tombol metode pembayaran di tampilan pemilihan
        } catch (BioskopException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    // === MODIFIKASI: Metode-metode baru untuk mengelola riwayat transaksi di troli ===
    // Dipanggil oleh FilmSelectionView untuk mendapatkan daftar resi
    public List<BioskopModel.TransactionReceipt> getTransactionHistory() {
        return model.getTransactionHistory(); // Mengembalikan daftar riwayat transaksi
    }

    // Dipanggil oleh FilmSelectionView untuk mencetak resi
    public void cetakResi(BioskopModel.TransactionReceipt receipt) {
        // Implementasi logika pencetakan tiket di sini, misalnya menampilkan pop-up.
        // Ini adalah contoh sederhana. Anda bisa mengembangkannya untuk mencetak ke file, dll.
        showAlert(Alert.AlertType.INFORMATION, "Cetak Resi",
                "Tiket berhasil dicetak!\n\n" +
                        "ID Resi: " + receipt.getId() + "\n" +
                        "Film: " + receipt.getFilmTitle() + "\n" +
                        "Jam: " + receipt.getShowtime() + "\n" +
                        "Kursi: " + String.join(", ", receipt.getSeats()) + "\n" +
                        "Total Pembayaran: Rp" + String.format("%,.0f", receipt.getTotalPrice())); // Menampilkan informasi resi
    }

    // Dipanggil oleh FilmSelectionView untuk menghapus resi dari riwayat
    public void hapusResi(String receiptId) {
        try {
            model.removeTransactionReceipt(receiptId); // Menghapus resi berdasarkan ID
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pesanan berhasil dihapus dari riwayat."); // Tampilkan alert sukses
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus pesanan: " + e.getMessage()); // Tampilkan alert jika terjadi kesalahan
        }
    }

    // === Utility method untuk menampilkan alert pop-up ===
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type); // Buat alert baru
        alert.setTitle(title); // Set judul alert
        alert.setHeaderText(null); // Set header alert
        alert.setContentText(message); // Set konten alert
        alert.showAndWait(); // Tampilkan alert dan tunggu hingga ditutup
    }
}
