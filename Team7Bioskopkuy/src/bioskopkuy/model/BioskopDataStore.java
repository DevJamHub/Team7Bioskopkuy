package bioskopkuy.model; // Pastikan package ini sama dengan BioskopModel.java

import bioskopkuy.service.BioskopException;
// import java.io.File; // Tidak diperlukan di sini
import java.util.*;

public class BioskopDataStore {

    public static class PaymentMethod {
        private final String name;
        private int discountPercent;
        private String discountDescription;

        public PaymentMethod(String name, int discountPercent, String discountDescription) {
            this.name = name;
            this.discountPercent = discountPercent;
            this.discountDescription = discountDescription;
        }

        public String getName() {
            return name;
        }

        public int getDiscountPercent() {
            return discountPercent;
        }

        public String getDiscountDescription() {
            return discountDescription;
        }

        public void setDiscountPercent(int discountPercent) {
            this.discountPercent = discountPercent;
        }

        public void setDiscountDescription(String discountDescription) {
            this.discountDescription = discountDescription;
        }

        @Override
        public String toString() {
            return name + (discountPercent > 0 ? " (" + discountPercent + "% Diskon - " + discountDescription + ")" : "");
        }
    }

    // Menggunakan BioskopModel.Film (karena definisi Film sekarang ada di sana)
    private final List<BioskopModel.Film> daftarFilm;
    private final Map<String, Set<String>> kursiTerisiMap; // Key: FilmJudul-JamTayang, Value: Set<KursiName>

    private List<PaymentMethod> daftarMetodePembayaran;

    public BioskopDataStore() {
        this.daftarFilm = new ArrayList<>();
        this.kursiTerisiMap = new HashMap<>();
        initializeDefaultData();
    }

    // Menginisialisasi data film dan metode pembayaran default
    private void initializeDefaultData() {

        BioskopModel.Film film1 = new BioskopModel.Film("The Jungle of Basori", 50000.0, null);
        film1.addJamTayang("10:00");
        film1.addJamTayang("13:00");
        film1.addJamTayang("16:00");
        film1.addJamTayang("19:00");
        daftarFilm.add(film1);


        this.daftarMetodePembayaran = new ArrayList<>();
        this.daftarMetodePembayaran.add(new PaymentMethod("Cash", 0, "Tidak Ada Diskon"));
        this.daftarMetodePembayaran.add(new PaymentMethod("Debit Card", 5, "Promo Debit BNI"));
        this.daftarMetodePembayaran.add(new PaymentMethod("GoodPay", 15, "Cashback 15% Max Rp15.000"));
    }

    public List<BioskopModel.Film> getDaftarFilm() {
        return Collections.unmodifiableList(daftarFilm);
    }

    // Menambah film baru - sekarang menerima BioskopModel.Film
    public void addFilm(BioskopModel.Film film) throws BioskopException {
        for (BioskopModel.Film existingFilm : daftarFilm) {
            if (existingFilm.getJudul().equalsIgnoreCase(film.getJudul())) {
                throw new BioskopException("Film dengan judul '" + film.getJudul() + "' sudah ada.");
            }
        }
        daftarFilm.add(film);
    }

    // Menghapus film - sekarang menerima BioskopModel.Film
    public void removeFilm(BioskopModel.Film film) throws BioskopException {
        boolean removed = daftarFilm.remove(film);
        if (!removed) {
            throw new BioskopException("Film '" + film.getJudul() + "' tidak ditemukan.");
        }
        // Hapus juga data kursi terisi untuk film yang dihapus
        List<String> keysToRemove = new ArrayList<>();
        for (String key : kursiTerisiMap.keySet()) {
            if (key.startsWith(film.getJudul() + "-")) {
                keysToRemove.add(key);
            }
        }
        for (String key : keysToRemove) {
            kursiTerisiMap.remove(key);
        }
    }

    // Membuat kunci unik untuk kombinasi film dan jam tayang - menerima BioskopModel.Film
    private String generateKursiKey(BioskopModel.Film film, String jam) {
        return film.getJudul() + "-" + jam;
    }

    // Memeriksa apakah kursi sudah terisi - menerima BioskopModel.Film
    public boolean isKursiTerisi(BioskopModel.Film film, String jam, String kursiName) {
        String key = generateKursiKey(film, jam);
        Set<String> terisi = kursiTerisiMap.getOrDefault(key, Collections.emptySet());
        return terisi.contains(kursiName);
    }

    // Menandai kursi sebagai terisi setelah pembayaran sukses - menerima BioskopModel.Film
    public void tandaiKursiTerisi(BioskopModel.Film film, String jam, Set<String> kursiNames) {
        String key = generateKursiKey(film, jam);
        kursiTerisiMap.computeIfAbsent(key, _ -> new HashSet<>()).addAll(kursiNames);
    }

    // Metode ini mungkin tidak perlu mengembalikan nilai, hanya sebagai referensi
    public void getAllKursiNames() {
        List<String> allKursi = new ArrayList<>();
        char[] rows = {'A', 'B', 'C', 'D'}; // Ubah sesuai kebutuhan layout kursi
        int colCount = 10;
        for (char rowChar : rows) {
            for (int colIdx = 0; colIdx < colCount; colIdx++) {
                allKursi.add(String.valueOf(rowChar) + (colIdx + 1));
            }
        }
    }

    public List<PaymentMethod> getDaftarMetodePembayaran() {
        return Collections.unmodifiableList(daftarMetodePembayaran);
    }

    // Menambah metode pembayaran baru
    public void addMetodePembayaran(String name, int discountPercent, String discountDescription) throws BioskopException {
        if (name.trim().isEmpty()) {
            throw new BioskopException("Nama metode pembayaran tidak boleh kosong.");
        }
        for (PaymentMethod method : daftarMetodePembayaran) {
            if (method.getName().equalsIgnoreCase(name.trim())) {
                throw new BioskopException("Metode pembayaran '" + name + "' sudah ada.");
            }
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new BioskopException("Persentase diskon harus antara 0-100.");
        }
        daftarMetodePembayaran.add(new PaymentMethod(name.trim(), discountPercent, discountDescription.trim()));
    }

    // Menghapus metode pembayaran
    public void removeMetodePembayaran(PaymentMethod methodToRemove) throws BioskopException {
        if (!daftarMetodePembayaran.remove(methodToRemove)) {
            throw new BioskopException("Metode pembayaran '" + methodToRemove.getName() + "' tidak ditemukan.");
        }
    }

    // Memperbarui metode pembayaran yang sudah ada
    public PaymentMethod updateMetodePembayaran(PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) throws BioskopException {
        if (newName.trim().isEmpty()) {
            throw new BioskopException("Nama metode pembayaran tidak boleh kosong.");
        }
        if (newDiscountPercent < 0 || newDiscountPercent > 100) {
            throw new BioskopException("Persentase diskon harus antara 0-100.");
        }

        // Cek jika nama baru sudah ada dan bukan metode yang sama
        if (!originalMethod.getName().equalsIgnoreCase(newName.trim())) {
            for (PaymentMethod method : daftarMetodePembayaran) {
                if (method != originalMethod && method.getName().equalsIgnoreCase(newName.trim())) {
                    throw new BioskopException("Nama metode pembayaran '" + newName + "' sudah digunakan.");
                }
            }
            // Jika nama berubah, hapus yang lama dan tambah yang baru
            removeMetodePembayaran(originalMethod);
            addMetodePembayaran(newName, newDiscountPercent, newDiscountDescription);
            return getPaymentMethodByName(newName); // Kembalikan objek yang baru ditambahkan
        } else {
            // Jika nama tidak berubah, hanya update properti yang ada
            originalMethod.setDiscountPercent(newDiscountPercent);
            originalMethod.setDiscountDescription(newDiscountDescription.trim());
            return originalMethod;
        }
    }

    // Mendapatkan objek PaymentMethod berdasarkan nama
    public PaymentMethod getPaymentMethodByName(String name) {
        for (PaymentMethod method : daftarMetodePembayaran) {
            if (method.getName().equalsIgnoreCase(name)) {
                return method;
            }
        }
        return null;
    }
}