package bioskopkuy.model; // Package untuk kelas-kelas yang menangani logika dan data aplikasi

import bioskopkuy.service.BioskopException; // Import exception untuk menangani kesalahan
import bioskopkuy.service.IManagementService; // Import interface untuk manajemen data
import javafx.scene.image.Image; // Import untuk menangani gambar
import java.text.NumberFormat; // Import untuk format angka
import java.util.*; // Import untuk koleksi
import java.io.FileInputStream; // Import untuk membaca file
import java.io.File; // Import untuk file
import java.util.UUID; // MODIFIKASI: Tambahkan import untuk ID unik

// Kelas utama yang bertugas mengelola logika aplikasi bioskop
// Mengimplementasikan interface IManagementService untuk manajemen data Film
public class BioskopModel implements IManagementService<BioskopModel.Film> {

    // Inner class untuk merepresentasikan entitas Film
    public static class Film extends AbstractEntity {
        private final double hargaDasar; // Harga tiket dasar per kursi
        private final List<String> jamTayang; // Daftar jam tayang film
        private final String imagePath; // Path gambar poster film

        // Path default gambar poster jika tidak ada poster yang disediakan
        private static final String ABSOLUTE_DEFAULT_IMAGE_PATH =
                "file:///Users/sigitnovriyy/Documents/MATAKULIAH/SEMESTER 2/PBO/TRY/Team7Bioskopkuy/src/bioskopkuy/view/images/default_poster.jpeg";

        // Konstruktor Film
        public Film(String judul, double hargaDasar, String imagePath) {
            super(judul, judul); // ID dan nama sama, yaitu judul film
            this.hargaDasar = hargaDasar; // Inisialisasi harga dasar
            this.jamTayang = new ArrayList<>(); // Inisialisasi daftar jam tayang
            // Gunakan gambar default jika path kosong
            this.imagePath = (imagePath == null || imagePath.isEmpty()) ? ABSOLUTE_DEFAULT_IMAGE_PATH : imagePath;
        }

        // Mengembalikan judul film
        public String getJudul() {
            return name; // Mengembalikan nama film (sama dengan judul)
        }

        // Mengembalikan harga dasar tiket
        public double getHargaDasar() {
            return hargaDasar; // Mengembalikan harga dasar
        }

        // Mengembalikan daftar jam tayang yang tidak bisa dimodifikasi
        public List<String> getJamTayang() {
            return Collections.unmodifiableList(jamTayang); // Mengembalikan list jam tayang yang tidak bisa dimodifikasi
        }

        // Menambahkan jam tayang baru dan mengurutkannya
        public void addJamTayang(String jam) {
            jamTayang.add(jam); // Menambahkan jam tayang
            Collections.sort(jamTayang); // Urutkan jam tayang secara alfabetis
        }

        // Mengembalikan path gambar poster
        public String getImagePath() {
            return imagePath; // Mengembalikan path gambar
        }

        // Mengembalikan objek Image dari path yang disediakan
        public Image getPosterImage() {
            Image loadedImage = null; // Inisialisasi gambar yang dimuat
            try {
                String filePath = imagePath; // Ambil path gambar
                if (filePath.startsWith("file:///")) {
                    filePath = filePath.substring(7); // Hilangkan prefix "file:///"
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        filePath = filePath.replaceFirst("^/", ""); // Untuk Windows, hilangkan slash awal
                    }
                }

                File file = new File(filePath); // Buat objek file
                if (file.exists() && file.isFile()) { // Cek apakah file ada
                    try (FileInputStream fis = new FileInputStream(file)) {
                        loadedImage = new Image(fis); // Muat gambar
                    }
                } else {
                    System.err.println("Poster tidak ditemukan: " + filePath); // Pesan error jika poster tidak ditemukan
                }
            } catch (Exception e) {
                System.err.println("Gagal memuat poster: " + e.getMessage()); // Pesan error jika gagal memuat poster
            }

            // Jika gagal, fallback ke gambar default
            if (loadedImage == null) {
                try {
                    String defaultPath = ABSOLUTE_DEFAULT_IMAGE_PATH.substring(7); // Ambil path default
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        defaultPath = defaultPath.replaceFirst("^/", ""); // Untuk Windows, hilangkan slash awal
                    }
                    File defaultFile = new File(defaultPath); // Buat objek file untuk gambar default
                    if (defaultFile.exists() && defaultFile.isFile()) { // Cek apakah file default ada
                        try (FileInputStream fis = new FileInputStream(defaultFile)) {
                            loadedImage = new Image(fis); // Muat gambar default
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Gagal memuat poster default: " + e.getMessage()); // Pesan error jika gagal memuat gambar default
                }
            }

            return loadedImage; // Kembalikan gambar yang dimuat
        }

        // Tampilkan informasi sederhana tentang film
        @Override
        public String toString() {
            return name + " (Rp" + String.format("%,.0f", hargaDasar) + ")"; // Format string untuk menampilkan informasi film
        }

        // Informasi lengkap untuk ditampilkan di UI
        @Override
        public String getDisplayInfo() {
            return getJudul() + " (Rp" + String.format("%,.0f", hargaDasar) + "/kursi) - Jam: " + String.join(", ", getJamTayang());
        }
    }

    // MODIFIKASI: Inner class untuk merepresentasikan resi transaksi yang sudah selesai
    public static class TransactionReceipt {
        private final String id; // ID unik untuk resi
        private final String filmTitle; // Judul film yang dibeli
        private final String showtime; // Jam tayang film
        private final List<String> seats; // Daftar kursi yang dibeli
        private final double totalPrice; // Total harga transaksi

        // Konstruktor untuk TransactionReceipt
        public TransactionReceipt(String filmTitle, String showtime, List<String> seats, double totalPrice) {
            // Generate ID unik menggunakan UUID
            this.id = UUID.randomUUID().toString(); // Membuat ID unik
            this.filmTitle = filmTitle; // Menyimpan judul film
            this.showtime = showtime; // Menyimpan jam tayang
            this.seats = new ArrayList<>(seats); // Salin list kursi
            this.totalPrice = totalPrice; // Menyimpan total harga
        }

        // Getter untuk ID resi
        public String getId() {
            return id; // Mengembalikan ID resi
        }

        // Getter untuk judul film
        public String getFilmTitle() {
            return filmTitle; // Mengembalikan judul film
        }

        // Getter untuk jam tayang
        public String getShowtime() {
            return showtime; // Mengembalikan jam tayang
        }

        // Getter untuk daftar kursi
        public List<String> getSeats() {
            return Collections.unmodifiableList(seats); // Mengembalikan daftar kursi yang tidak bisa dimodifikasi
        }

        // Getter untuk total harga
        public double getTotalPrice() {
            return totalPrice; // Mengembalikan total harga
        }
    }

    // Instance dari data store yang menyimpan data film, kursi, dan metode pembayaran
    private final BioskopDataStore dataStore; // Data store untuk menyimpan data

    // MODIFIKASI: Tambahkan list untuk menyimpan riwayat transaksi yang berhasil
    private final List<TransactionReceipt> riwayatTransaksi; // Riwayat transaksi

    // Variabel untuk menyimpan status transaksi saat ini
    private Film filmTerpilih; // Film yang dipilih
    private String jamTerpilih; // Jam yang dipilih
    private Set<String> kursiTerpilih; // Kursi yang dipilih

    // Total harga sebelum dan sesudah diskon
    private double totalHargaSebelumDiskon; // Total harga sebelum diskon
    private double totalHargaSetelahDiskon; // Total harga setelah diskon

    // Informasi pembayaran
    private BioskopDataStore.PaymentMethod metodePembayaranTerpilih; // Metode pembayaran yang dipilih
    private double uangDibayar; // Uang yang dibayar

    // Konstruktor: inisialisasi datastore dan tambahkan film default jika belum ada
    public BioskopModel() {
        this.dataStore = new BioskopDataStore(); // Inisialisasi data store
        this.riwayatTransaksi = new ArrayList<>(); // MODIFIKASI: Inisialisasi riwayat transaksi

        // Tambahkan 1 film default jika kosong
        if (dataStore.getDaftarFilm().isEmpty()) {
            Film film1 = new Film("The Jungle of Basori", 50000.0, null); // Buat film default
            film1.addJamTayang("10:00"); // Tambahkan jam tayang
            film1.addJamTayang("13:00");
            film1.addJamTayang("16:00");
            film1.addJamTayang("19:00");
            try {
                add(film1); // Tambahkan film ke data store
            } catch (BioskopException e) {
                System.err.println("Gagal menambah film default: " + e.getMessage()); // Pesan error jika gagal menambah film
            }
        }

        resetTransaksi(); // Atur ulang transaksi
    }

    // Reset semua variabel transaksi
    public void resetTransaksi() {
        filmTerpilih = null; // Reset film terpilih
        jamTerpilih = null; // Reset jam terpilih
        kursiTerpilih = new HashSet<>(); // Reset kursi terpilih
        totalHargaSebelumDiskon = 0.0; // Reset total harga sebelum diskon
        totalHargaSetelahDiskon = 0.0; // Reset total harga setelah diskon
        metodePembayaranTerpilih = null; // Reset metode pembayaran
        uangDibayar = 0.0; // Reset uang dibayar
    }

    // Implementasi method dari IManagementService
    @Override
    public void add(Film item) throws BioskopException {
        dataStore.addFilm(item); // Tambahkan film ke data store
    }

    @Override
    public void remove(Film item) throws BioskopException {
        dataStore.removeFilm(item); // Hapus film dari data store
    }

    @Override
    public List<Film> getAll() {
        return dataStore.getDaftarFilm(); // Kembalikan daftar film
    }

    // Metode untuk menambahkan metode pembayaran baru
    public void addMetodePembayaran(String name, int discountPercent, String discountDescription) throws BioskopException {
        dataStore.addMetodePembayaran(name, discountPercent, discountDescription); // Tambahkan metode pembayaran
    }

    // Hapus metode pembayaran yang sudah ada
    public void removeMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) throws BioskopException {
        dataStore.removeMetodePembayaran(methodToRemove); // Hapus metode pembayaran
    }

    // Update metode pembayaran dan sesuaikan transaksi jika sedang digunakan
    public void updateMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) throws BioskopException {
        BioskopDataStore.PaymentMethod updated = dataStore.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription); // Update metode pembayaran
        if (metodePembayaranTerpilih == originalMethod) {
            setMetodePembayaranTerpilih(updated); // Perbarui referensi ke metode baru
        }
        hitungTotalHarga(); // Hitung total harga
    }

    // Getter untuk daftar metode pembayaran
    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return dataStore.getDaftarMetodePembayaran(); // Kembalikan daftar metode pembayaran
    }

    // Getter dan setter untuk transaksi
    public Film getFilmTerpilih() {
        return filmTerpilih; // Kembalikan film terpilih
    }

    public void setFilmTerpilih(Film filmTerpilih) {
        this.filmTerpilih = filmTerpilih; // Set film terpilih
    }

    public String getJamTerpilih() {
        return jamTerpilih; // Kembalikan jam terpilih
    }

    public void setJamTerpilih(String jamTerpilih) {
        this.jamTerpilih = jamTerpilih; // Set jam terpilih
    }

    public Set<String> getKursiTerpilih() {
        return Collections.unmodifiableSet(kursiTerpilih); // Kembalikan set kursi terpilih yang tidak bisa dimodifikasi
    }

    // Menambahkan kursi terpilih dan menghitung total harga
    public void addKursiTerpilih(String kursi) throws BioskopException {
        if (filmTerpilih == null || jamTerpilih == null) {
            throw new BioskopException("Pilih film dan jam terlebih dahulu."); // Pesan error jika film dan jam belum dipilih
        }
        if (dataStore.isKursiTerisi(filmTerpilih, jamTerpilih, kursi)) {
            throw new BioskopException("Kursi sudah terisi."); // Pesan error jika kursi sudah terisi
        }
        kursiTerpilih.add(kursi); // Tambahkan kursi terpilih
        hitungTotalHarga(); // Hitung total harga
    }

    // Menghapus kursi terpilih dan menghitung total harga
    public void removeKursiTerpilih(String kursi) {
        kursiTerpilih.remove(kursi); // Hapus kursi terpilih
        hitungTotalHarga(); // Hitung total harga
    }

    // Menghapus semua kursi terpilih
    public void clearKursiTerpilih() {
        kursiTerpilih.clear(); // Hapus semua kursi terpilih
        hitungTotalHarga(); // Hitung total harga
    }

    // Hitung ulang total harga transaksi berdasarkan diskon
    public void hitungTotalHarga() {
        if (filmTerpilih != null) {
            totalHargaSebelumDiskon = filmTerpilih.getHargaDasar() * kursiTerpilih.size(); // Hitung total harga sebelum diskon
            int diskon = metodePembayaranTerpilih != null ? metodePembayaranTerpilih.getDiscountPercent() : 0; // Ambil diskon
            totalHargaSetelahDiskon = totalHargaSebelumDiskon * (1 - diskon / 100.0); // Hitung total harga setelah diskon
        } else {
            totalHargaSebelumDiskon = 0.0; // Reset total harga sebelum diskon
            totalHargaSetelahDiskon = 0.0; // Reset total harga setelah diskon
        }
    }

    // Mengembalikan total harga sebelum diskon dalam format yang sesuai
    public String getTotalHargaSebelumDiskonFormatted() {
        return "Rp" + NumberFormat.getInstance(new Locale("id", "ID")).format(totalHargaSebelumDiskon); // Format total harga sebelum diskon
    }

    // Mengembalikan total harga setelah diskon
    public double getTotalHargaAfterDiskon() {
        return totalHargaSetelahDiskon; // Kembalikan total harga setelah diskon
    }

    // Mengembalikan total harga setelah diskon dalam format yang sesuai
    public String getTotalHargaSetelahDiskonFormatted() {
        return "Rp" + NumberFormat.getInstance(new Locale("id", "ID")).format(totalHargaSetelahDiskon); // Format total harga setelah diskon
    }

    // Mengembalikan metode pembayaran terpilih
    public BioskopDataStore.PaymentMethod getMetodePembayaranTerpilih() {
        return metodePembayaranTerpilih; // Kembalikan metode pembayaran terpilih
    }

    // Mengatur metode pembayaran terpilih dan menghitung total harga
    public void setMetodePembayaranTerpilih(BioskopDataStore.PaymentMethod metode) {
        this.metodePembayaranTerpilih = metode; // Set metode pembayaran terpilih
        hitungTotalHarga(); // Update harga jika metode pembayaran berubah
    }

    // Mengembalikan uang yang dibayar
    public double getUangDibayar() {
        return uangDibayar; // Kembalikan uang yang dibayar
    }

    // Mengatur uang yang dibayar
    public void setUangDibayar(double uangDibayar) {
        this.uangDibayar = uangDibayar; // Set uang yang dibayar
    }

    // Mengembalikan diskon persen dari metode terpilih
    public int getDiskonPersenDariMetodeTerpilih() {
        return metodePembayaranTerpilih != null ? metodePembayaranTerpilih.getDiscountPercent() : 0; // Kembalikan diskon persen dari metode terpilih
    }

    // Mengembalikan keterangan diskon dari metode terpilih
    public String getDiskonKeteranganDariMetodeTerpilih() {
        return metodePembayaranTerpilih != null ? metodePembayaranTerpilih.getDiscountDescription() : "Tidak Ada Diskon"; // Kembalikan keterangan diskon dari metode terpilih
    }

    // Cek apakah kursi terisi
    public boolean isKursiTerisi(Film film, String jam, String kursiName) {
        return dataStore.isKursiTerisi(film, jam, kursiName); // Cek apakah kursi terisi
    }

    // Mengambil semua nama kursi
    public void getAllKursiNames() {
        dataStore.getAllKursiNames(); // Ambil semua nama kursi
    }

    // MODIFIKASI: Proses finalisasi transaksi: validasi, tandai kursi, dan simpan resi
    public boolean prosesPembayaran() throws BioskopException {
        // Validasi transaksi
        if (kursiTerpilih.isEmpty() || filmTerpilih == null || jamTerpilih == null || metodePembayaranTerpilih == null) {
            throw new BioskopException("Transaksi belum lengkap. Pastikan film, jam, kursi, dan metode pembayaran sudah dipilih."); // Pesan error jika transaksi tidak lengkap
        }
        if (uangDibayar < totalHargaSetelahDiskon) {
            throw new BioskopException("Uang kurang: Rp" + String.format("%,.0f", totalHargaSetelahDiskon - uangDibayar)); // Pesan error jika uang yang dibayar kurang
        }

        // Tandai kursi sebagai terisi di data store
        dataStore.tandaiKursiTerisi(filmTerpilih, jamTerpilih, kursiTerpilih); // Tandai kursi yang dipilih sebagai terisi

        // MODIFIKASI: Buat objek resi baru dan tambahkan ke riwayat
        TransactionReceipt newReceipt = new TransactionReceipt(
                filmTerpilih.getJudul(), // Judul film
                jamTerpilih, // Jam tayang
                new ArrayList<>(kursiTerpilih), // Salin set kursi ke list
                totalHargaSetelahDiskon // Total harga setelah diskon
        );
        riwayatTransaksi.add(newReceipt); // Tambahkan resi ke riwayat transaksi

        return true; // Kembalikan true jika pembayaran berhasil
    }

    // MODIFIKASI: Metode getter untuk riwayat transaksi
    public List<TransactionReceipt> getTransactionHistory() {
        // Mengembalikan list yang tidak bisa dimodifikasi untuk mencegah perubahan dari luar
        return Collections.unmodifiableList(riwayatTransaksi); // Kembalikan riwayat transaksi
    }

    // MODIFIKASI: Metode untuk menghapus resi dari riwayat berdasarkan ID
    public void removeTransactionReceipt(String receiptId) {
        riwayatTransaksi.removeIf(receipt -> receipt.getId().equals(receiptId)); // Hapus resi berdasarkan ID
    }

    // Overloading untuk menampilkan info entitas
    public String getDisplayInfo(Film film) {
        return film.getDisplayInfo(); // Kembalikan info film
    }

    public String getDisplayInfo(BioskopDataStore.PaymentMethod method) {
        return method.getName() + " (" + method.getDiscountPercent() + "% Off)"; // Kembalikan info metode pembayaran
    }
}
