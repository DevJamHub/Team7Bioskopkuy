package bioskopkuy.model; // Package model, berisi logika data utama aplikasi

import bioskopkuy.service.BioskopException; // Import exception untuk menangani kesalahan
import java.util.*; // Import untuk koleksi

// Kelas ini bertanggung jawab menyimpan semua data aplikasi bioskop, termasuk film, kursi terisi, dan metode pembayaran
public class BioskopDataStore {

    // ==============================
    // === INNER CLASS: PaymentMethod ===
    // ==============================

    // Inner class yang merepresentasikan metode pembayaran (nama, diskon, deskripsi)
    public static class PaymentMethod {
        private String name;               // Nama metode pembayaran (misal: Cash, Debit Card)
        private int discountPercent;       // Persentase diskon (misal: 5 berarti diskon 5%)
        private String discountDescription; // Deskripsi dari diskon

        // Konstruktor untuk menginisialisasi semua atribut
        public PaymentMethod(String name, int discountPercent, String discountDescription) {
            this.name = name; // Inisialisasi nama metode pembayaran
            this.discountPercent = discountPercent; // Inisialisasi persentase diskon
            this.discountDescription = discountDescription; // Inisialisasi deskripsi diskon
        }

        // Getter untuk masing-masing atribut
        public String getName() {
            return name; // Mengembalikan nama metode pembayaran
        }

        public int getDiscountPercent() {
            return discountPercent; // Mengembalikan persentase diskon
        }

        public String getDiscountDescription() {
            return discountDescription; // Mengembalikan deskripsi diskon
        }

        // Setter untuk memperbarui nilai atribut jika diperlukan
        public void setDiscountPercent(int discountPercent) {
            this.discountPercent = discountPercent; // Set persentase diskon
        }

        public void setDiscountDescription(String discountDescription) {
            this.discountDescription = discountDescription; // Set deskripsi diskon
        }

        public void setName(String name) {
            this.name = name; // Set nama metode pembayaran
        }

        // Mengembalikan representasi string yang ditampilkan di antarmuka pengguna
        @Override
        public String toString() {
            return name + (discountPercent > 0 ? " (" + discountPercent + "% Diskon - " + discountDescription + ")" : ""); // Format string untuk ditampilkan
        }
    }

    // ========================
    // === PROPERTI DATA ===
    // ========================

    private final List<BioskopModel.Film> daftarFilm; // List yang menyimpan seluruh film yang tersedia
    private final Map<String, Set<String>> kursiTerisiMap; // Map berisi informasi kursi yang sudah dipesan, dengan key "JudulFilm-Jam"
    private List<PaymentMethod> daftarMetodePembayaran; // List semua metode pembayaran yang tersedia

    // ========================
    // === KONSTRUKTOR ===
    // ========================

    // Konstruktor untuk menginisialisasi data kosong dan data default
    public BioskopDataStore() {
        this.daftarFilm = new ArrayList<>(); // Inisialisasi daftar film
        this.kursiTerisiMap = new HashMap<>(); // Inisialisasi map kursi terisi
        initializeDefaultData(); // Menambahkan metode pembayaran default saat aplikasi pertama kali dijalankan
    }

    // Inisialisasi daftar metode pembayaran default (Cash, Debit Card, GoodPay)
    private void initializeDefaultData() {
        this.daftarMetodePembayaran = new ArrayList<>(); // Inisialisasi daftar metode pembayaran
        this.daftarMetodePembayaran.add(new PaymentMethod("Cash", 0, "Tidak Ada Diskon")); // Tambahkan metode pembayaran Cash
        this.daftarMetodePembayaran.add(new PaymentMethod("Debit Card", 5, "Promo Debit BNI")); // Tambahkan metode pembayaran Debit Card
        this.daftarMetodePembayaran.add(new PaymentMethod("GoodPay", 15, "Cashback 15% Max Rp15.000")); // Tambahkan metode pembayaran GoodPay
    }

    // ========================
    // === OPERASI FILM ===
    // ========================

    // Mengembalikan daftar film (dalam bentuk unmodifiable list agar tidak bisa dimodifikasi dari luar)
    public List<BioskopModel.Film> getDaftarFilm() {
        return Collections.unmodifiableList(daftarFilm); // Kembalikan daftar film yang tidak bisa dimodifikasi
    }

    // Menambahkan film baru ke daftar
    public void addFilm(BioskopModel.Film film) throws BioskopException {
        for (BioskopModel.Film existingFilm : daftarFilm) { // Cek apakah film sudah ada
            if (existingFilm.getJudul().equalsIgnoreCase(film.getJudul())) {
                throw new BioskopException("Film dengan judul '" + film.getJudul() + "' sudah ada."); // Pesan error jika film sudah ada
            }
        }
        daftarFilm.add(film); // Tambahkan film ke daftar
    }

    // Menghapus film dari daftar dan juga menghapus semua kursi yang pernah dipesan untuk film tersebut
    public void removeFilm(BioskopModel.Film film) throws BioskopException {
        boolean removed = daftarFilm.remove(film); // Hapus film dari daftar
        if (!removed) {
            throw new BioskopException("Film '" + film.getJudul() + "' tidak ditemukan."); // Pesan error jika film tidak ditemukan
        }

        // Hapus semua entri kursi yang dipesan berdasarkan key yang cocok dengan judul film
        List<String> keysToRemove = new ArrayList<>(); // List untuk menyimpan key yang akan dihapus
        for (String key : kursiTerisiMap.keySet()) { // Iterasi melalui semua key di map
            if (key.startsWith(film.getJudul() + "-")) { // Cek apakah key cocok dengan judul film
                keysToRemove.add(key); // Tambahkan key ke list untuk dihapus
            }
        }
        for (String key : keysToRemove) {
            kursiTerisiMap.remove(key); // Hapus key dari map
        }
    }

    // ==============================
    // === MANAJEMEN KURSI TERISI ===
    // ==============================

    // Membuat key gabungan dari judul film dan jam tayang (digunakan untuk menyimpan dan membaca data kursi terisi)
    private String generateKursiKey(BioskopModel.Film film, String jam) {
        return film.getJudul() + "-" + jam; // Mengembalikan key gabungan
    }

    // Mengecek apakah kursi tertentu sudah terisi pada film dan jam tertentu
    public boolean isKursiTerisi(BioskopModel.Film film, String jam, String kursiName) {
        String key = generateKursiKey(film, jam); // Dapatkan key untuk film dan jam
        Set<String> terisi = kursiTerisiMap.getOrDefault(key, Collections.emptySet()); // Dapatkan set kursi terisi
        return terisi.contains(kursiName); // Kembalikan true jika kursi terisi
    }

    // Menandai kursi sebagai terisi untuk film dan jam tertentu
    public void tandaiKursiTerisi(BioskopModel.Film film, String jam, Set<String> kursiNames) {
        String key = generateKursiKey(film, jam); // Dapatkan key untuk film dan jam
        kursiTerisiMap.computeIfAbsent(key, _ -> new HashSet<>()).addAll(kursiNames); // Tambahkan kursi ke set terisi
    }

    // Metode dummy untuk membangkitkan semua nama kursi (tidak digunakan saat ini)
    public void getAllKursiNames() {
        List<String> allKursi = new ArrayList<>(); // List untuk menyimpan semua nama kursi
        char[] rows = {'A', 'B', 'C', 'D'}; // Baris kursi
        int colCount = 10; // Jumlah kolom per baris
        for (char rowChar : rows) { // Iterasi melalui setiap baris
            for (int colIdx = 0; colIdx < colCount; colIdx++) { // Iterasi melalui setiap kolom
                allKursi.add(String.valueOf(rowChar) + (colIdx + 1)); // Tambahkan nama kursi ke list
            }
        }
        // Tidak mengembalikan apapun — bisa dikembangkan jika diperlukan
    }

    // ===============================
    // === METODE PEMBAYARAN ===
    // ===============================

    // Mengembalikan daftar semua metode pembayaran
    public List<PaymentMethod> getDaftarMetodePembayaran() {
        return Collections.unmodifiableList(daftarMetodePembayaran); // Kembalikan daftar metode pembayaran yang tidak bisa dimodifikasi
    }

    // Menambahkan metode pembayaran baru setelah validasi
    public void addMetodePembayaran(String name, int discountPercent, String discountDescription) throws BioskopException {
        if (name.trim().isEmpty()) {
            throw new BioskopException("Nama metode pembayaran tidak boleh kosong."); // Pesan error jika nama kosong
        }

        for (PaymentMethod method : daftarMetodePembayaran) { // Cek apakah metode pembayaran sudah ada
            if (method.getName().equalsIgnoreCase(name.trim())) {
                throw new BioskopException("Metode pembayaran '" + name + "' sudah ada."); // Pesan error jika metode sudah ada
            }
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new BioskopException("Persentase diskon harus antara 0-100."); // Pesan error jika diskon tidak valid
        }

        daftarMetodePembayaran.add(new PaymentMethod(name.trim(), discountPercent, discountDescription.trim())); // Tambahkan metode pembayaran baru
    }

    // Menghapus metode pembayaran dari daftar
    public void removeMetodePembayaran(PaymentMethod methodToRemove) throws BioskopException {
        if (!daftarMetodePembayaran.remove(methodToRemove)) {
            throw new BioskopException("Metode pembayaran '" + methodToRemove.getName() + "' tidak ditemukan."); // Pesan error jika metode tidak ditemukan
        }
    }

    // Memperbarui data dari metode pembayaran yang sudah ada
    public PaymentMethod updateMetodePembayaran(
            PaymentMethod originalMethod,
            String newName,
            int newDiscountPercent,
            String newDiscountDescription
    ) throws BioskopException {
        if (newName.trim().isEmpty()) {
            throw new BioskopException("Nama metode pembayaran tidak boleh kosong."); // Pesan error jika nama kosong
        }

        if (newDiscountPercent < 0 || newDiscountPercent > 100) {
            throw new BioskopException("Persentase diskon harus antara 0-100."); // Pesan error jika diskon tidak valid
        }

        // Cek apakah nama baru sudah dipakai oleh metode pembayaran lain
        for (PaymentMethod method : daftarMetodePembayaran) {
            if (method != originalMethod && method.getName().equalsIgnoreCase(newName.trim())) {
                throw new BioskopException("Nama metode pembayaran '" + newName + "' sudah digunakan."); // Pesan error jika nama sudah ada
            }
        }

        // Update nilai atribut metode pembayaran
        originalMethod.setName(newName.trim()); // Set nama baru
        originalMethod.setDiscountPercent(newDiscountPercent); // Set persentase diskon baru
        originalMethod.setDiscountDescription(newDiscountDescription.trim()); // Set deskripsi diskon baru

        return originalMethod; // Mengembalikan metode yang telah diperbarui
    }
}
