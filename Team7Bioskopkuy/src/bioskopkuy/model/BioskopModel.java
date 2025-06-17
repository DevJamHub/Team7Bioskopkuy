package bioskopkuy.model;

import bioskopkuy.service.BioskopException;
import javafx.scene.image.Image; // Import ini untuk kelas Image
import java.text.NumberFormat;
import java.util.*;
// import java.io.File; // Tidak diperlukan untuk fungsionalitas ini, bisa dihapus

public class BioskopModel {

    // Kelas internal untuk merepresentasikan Film
    public static class Film {
        private final String judul;
        private final double hargaDasar;
        private final List<String> jamTayang; // Jam tayang dalam format HH:mm
        private final String imagePath; // Properti untuk path gambar

        // --- Variabel statis untuk jalur default absolut ---
        // Ganti ini dengan jalur absolut yang benar di komputer Anda
        // Pastikan ekstensi file sesuai (misalnya .jpeg atau .png)
        private static final String ABSOLUTE_DEFAULT_IMAGE_PATH = "file:///Users/sigitnovriyy/Documents/MATAKULIAH/SEMESTER 2/PBO/TRY/Team7Bioskopkuy/src/bioskopkuy/view/login/images/default_poster.jpeg";

        public Film(String judul, double hargaDasar, String imagePath) { // Sesuaikan konstruktor
            this.judul = judul;
            this.hargaDasar = hargaDasar;
            this.jamTayang = new ArrayList<>();
            // Jika imagePath yang diberikan null atau kosong, gunakan jalur absolut default
            if (imagePath == null || imagePath.isEmpty()) {
                this.imagePath = ABSOLUTE_DEFAULT_IMAGE_PATH;
            } else {
                this.imagePath = imagePath;
            }
        }

        public String getJudul() {
            return judul;
        }

        public double getHargaDasar() {
            return hargaDasar;
        }

        public List<String> getJamTayang() {
            return Collections.unmodifiableList(jamTayang); // Mengembalikan unmodifiable list
        }

        public void addJamTayang(String jam) {
            this.jamTayang.add(jam);
            Collections.sort(this.jamTayang); // Opsional: urutkan jam tayang
        }

        public String getImagePath() {
            return imagePath;
        }

        /**
         * Mengembalikan objek Image untuk poster film ini.
         * Akan mencoba memuat dari imagePath yang tersimpan.
         * Jika gagal, akan mencoba memuat dari jalur default absolut.
         *
         * @return Objek Image untuk poster, atau null jika bahkan gambar default pun gagal dimuat.
         */
        public javafx.scene.image.Image getPosterImage() { // Perubahan tipe kembalian ke javafx.scene.image.Image
            try {
                // Memuat gambar menggunakan jalur yang tersimpan (bisa absolut)
                return new Image(this.imagePath);
            } catch (Exception e) {
                System.err.println("Gagal memuat gambar untuk film '" + judul + "' dari jalur: " + this.imagePath + ". Error: " + e.getMessage());
                // Fallback: Jika gambar spesifik atau yang telah diset gagal dimuat,
                // coba memuat gambar default absolut (sekali lagi)
                try {
                    return new Image(ABSOLUTE_DEFAULT_IMAGE_PATH);
                } catch (Exception ex) {
                    System.err.println("Gagal memuat gambar fallback default absolut. Error: " + ex.getMessage());
                    return null; // Jika bahkan gambar default gagal, kembalikan null
                }
            }
        }

        @Override
        public String toString() {
            return judul + " (Rp" + String.format("%,.0f", hargaDasar) + ")";
        }
    }

    // --- Awal dari BioskopModel (seperti yang Anda berikan) ---
    private final BioskopDataStore dataStore;
    private Film filmTerpilih;
    private String jamTerpilih;
    private Set<String> kursiTerpilih; // Menggunakan Set untuk menghindari duplikasi kursi
    private double totalHargaSebelumDiskon;
    private double totalHargaSetelahDiskon;

    private BioskopDataStore.PaymentMethod metodePembayaranTerpilih;
    private double uangDibayar;

    public BioskopModel() {
        // Ini akan memanggil BioskopDataStore dan inisialisasi film di sana
        this.dataStore = new BioskopDataStore();
        resetTransaksi(); // Inisialisasi awal
    }

    // Mengatur ulang semua data transaksi
    public void resetTransaksi() {
        this.filmTerpilih = null;
        this.jamTerpilih = null;
        this.kursiTerpilih = new HashSet<>();
        this.totalHargaSebelumDiskon = 0.0;
        this.totalHargaSetelahDiskon = 0.0;
        this.metodePembayaranTerpilih = null;
        this.uangDibayar = 0.0;
    }

    // Perhatikan: getDaftarFilm() akan memanggil dataStore.getDaftarFilm()
    // yang harus mengembalikan List<BioskopModel.Film>
    public List<Film> getDaftarFilm() {
        return dataStore.getDaftarFilm();
    }

    public Film getFilmTerpilih() {
        return filmTerpilih;
    }

    public void setFilmTerpilih(Film filmTerpilih) {
        this.filmTerpilih = filmTerpilih;
    }

    public String getJamTerpilih() {
        return jamTerpilih;
    }

    public void setJamTerpilih(String jamTerpilih) {
        this.jamTerpilih = jamTerpilih;
    }

    public Set<String> getKursiTerpilih() {
        return Collections.unmodifiableSet(kursiTerpilih);
    }

    // Menambahkan kursi ke pilihan
    public void addKursiTerpilih(String kursi) throws BioskopException {
        if (filmTerpilih == null || jamTerpilih == null) {
            throw new BioskopException("Pilih film dan jam tayang terlebih dahulu.");
        }
        if (dataStore.isKursiTerisi(filmTerpilih, jamTerpilih, kursi)) {
            throw new BioskopException("Kursi " + kursi + " sudah terisi. Silakan pilih kursi lain.");
        }
        kursiTerpilih.add(kursi);
        hitungTotalHarga();
    }

    // Menghapus kursi dari pilihan
    public void removeKursiTerpilih(String kursi) {
        kursiTerpilih.remove(kursi);
        hitungTotalHarga();
    }

    // Mengosongkan semua kursi yang terpilih
    public void clearKursiTerpilih() {
        kursiTerpilih.clear();
        hitungTotalHarga();
    }

    // Menghitung total harga sebelum dan sesudah diskon
    public void hitungTotalHarga() {
        if (filmTerpilih != null) {
            this.totalHargaSebelumDiskon = filmTerpilih.getHargaDasar() * kursiTerpilih.size();
            int diskonPersen = (metodePembayaranTerpilih != null) ? metodePembayaranTerpilih.getDiscountPercent() : 0;
            this.totalHargaSetelahDiskon = totalHargaSebelumDiskon * (1 - (double)diskonPersen / 100);
        } else {
            this.totalHargaSebelumDiskon = 0.0;
            this.totalHargaSetelahDiskon = 0.0;
        }
    }

    // Mendapatkan total harga sebelum diskon dalam format mata uang
    public String getTotalHargaSebelumDiskonFormatted() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + formatter.format(totalHargaSebelumDiskon);
    }

    // Mendapatkan total harga setelah diskon (nilai double)
    public double getTotalHargaAfterDiskon() {
        return totalHargaSetelahDiskon;
    }

    // Mendapatkan total harga setelah diskon dalam format mata uang
    public String getTotalHargaSetelahDiskonFormatted() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + formatter.format(totalHargaSetelahDiskon);
    }

    public BioskopDataStore.PaymentMethod getMetodePembayaranTerpilih() {
        return metodePembayaranTerpilih;
    }

    public void setMetodePembayaranTerpilih(BioskopDataStore.PaymentMethod metodePembayaranTerpilih) {
        this.metodePembayaranTerpilih = metodePembayaranTerpilih;
        hitungTotalHarga(); // Hitung ulang harga setelah metode pembayaran dipilih
    }

    public double getUangDibayar() {
        return uangDibayar;
    }

    public void setUangDibayar(double uangDibayar) {
        this.uangDibayar = uangDibayar;
    }

    public int getDiskonPersenDariMetodeTerpilih() {
        return (metodePembayaranTerpilih != null) ? metodePembayaranTerpilih.getDiscountPercent() : 0;
    }

    public String getDiskonKeteranganDariMetodeTerpilih() {
        return (metodePembayaranTerpilih != null) ? metodePembayaranTerpilih.getDiscountDescription() : "Tidak Ada Diskon";
    }

    // Memeriksa status kursi dari data store
    public boolean isKursiTerisi(Film film, String jam, String kursiName) {
        return dataStore.isKursiTerisi(film, jam, kursiName);
    }

    // Mendapatkan semua nama kursi dari data store (untuk visualisasi)
    public void getAllKursiNames() {
        dataStore.getAllKursiNames();
    }

    // Memproses transaksi pembayaran
    public boolean prosesPembayaran() throws BioskopException {
        if (totalHargaSetelahDiskon <= 0 && kursiTerpilih.isEmpty()) {
            throw new BioskopException("Total harga belum dihitung atau tidak valid. Silakan pilih film dan kursi.");
        }
        if (uangDibayar < totalHargaSetelahDiskon) {
            throw new BioskopException("Jumlah uang yang dibayarkan kurang dari total harga. Kurang Rp" + String.format("%,.0f", (totalHargaSetelahDiskon - uangDibayar)) + ".");
        }
        if (filmTerpilih == null || jamTerpilih == null || kursiTerpilih.isEmpty() || metodePembayaranTerpilih == null) {
            throw new BioskopException("Data transaksi tidak lengkap. Silakan ulangi proses pemilihan.");
        }

        // Tandai kursi sebagai terisi di data store
        // Penting: pastikan BioskopDataStore.tandaiKursiTerisi menerima BioskopModel.Film
        dataStore.tandaiKursiTerisi(filmTerpilih, jamTerpilih, kursiTerpilih);
        return true;
    }

    // --- Metode untuk manajemen admin ---

    // Menambah film (delegasi ke dataStore) - sesuaikan dengan imagePath
    public void addFilm(String judul, double harga, String jamTayang, String imagePath) throws BioskopException {
        Film newFilm = new Film(judul, harga, imagePath); // Lewatkan imagePath
        String[] jams = jamTayang.split(",");
        if (jams.length == 0 || (jams.length == 1 && jams[0].trim().isEmpty())) {
            throw new BioskopException("Jam tayang tidak boleh kosong.");
        }
        for (String jam : jams) {
            String trimmedJam = jam.trim();
            if (!trimmedJam.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) { // Validasi format HH:mm
                throw new BioskopException("Format jam tayang tidak valid: " + trimmedJam + ". Gunakan HH:mm (misal: 12:00, 14:30).");
            }
            newFilm.addJamTayang(trimmedJam);
        }
        dataStore.addFilm(newFilm); // Pastikan BioskopDataStore.addFilm menerima BioskopModel.Film
    }

    // Menghapus film (delegasi ke dataStore)
    public void removeFilm(Film film) throws BioskopException {
        dataStore.removeFilm(film); // Pastikan BioskopDataStore.removeFilm menerima BioskopModel.Film
    }

    // Mendapatkan daftar metode pembayaran (delegasi ke dataStore)
    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return dataStore.getDaftarMetodePembayaran();
    }

    // Menambah metode pembayaran (delegasi ke dataStore)
    public void addMetodePembayaran(String name, int discountPercent, String discountDescription) throws BioskopException {
        dataStore.addMetodePembayaran(name, discountPercent, discountDescription);
    }

    // Menghapus metode pembayaran (delegasi ke dataStore)
    public void removeMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) throws BioskopException {
        dataStore.removeMetodePembayaran(methodToRemove);
    }

    // Memperbarui metode pembayaran (delegasi ke dataStore)
    public void updateMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) throws BioskopException {
        BioskopDataStore.PaymentMethod updatedMethod = dataStore.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription);

        // Jika metode pembayaran yang sedang aktif di model diupdate, pastikan model memegang referensi yang benar
        if (metodePembayaranTerpilih == originalMethod) {
            setMetodePembayaranTerpilih(updatedMethod); // Mengupdate referensi
        }
        hitungTotalHarga(); // Hitung ulang harga jika ada perubahan diskon
    }
}