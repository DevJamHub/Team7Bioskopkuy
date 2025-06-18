package bioskopkuy.model;

import bioskopkuy.service.BioskopException;
import bioskopkuy.service.IManagementService;
import javafx.scene.image.Image;
import java.text.NumberFormat;
import java.util.*;
import java.io.FileInputStream;
import java.io.File;

// Kita akan mengimplementasikan IManagementService untuk Film secara eksplisit
// dan menggunakan metode yang sudah ada untuk PaymentMethod.
// Ini untuk menghindari ambiguitas getAll() dari dua implementasi interface yang sama.
public class BioskopModel implements IManagementService<BioskopModel.Film> { // Hanya implementasikan untuk Film

    // Inner class Film sekarang extend AbstractEntity
    public static class Film extends AbstractEntity {
        private final double hargaDasar;
        private final List<String> jamTayang;
        private final String imagePath;

        private static final String ABSOLUTE_DEFAULT_IMAGE_PATH = "file:///Users/sigitnovriyy/Documents/MATAKULIAH/SEMESTER 2/PBO/TRY/Team7Bioskopkuy/src/bioskopkuy/view/login/images/default_poster.jpeg";

        public Film(String judul, double hargaDasar, String imagePath) {
            super(judul, judul); // ID dan name sama-sama judul untuk Film
            this.hargaDasar = hargaDasar;
            this.jamTayang = new ArrayList<>();
            if (imagePath == null || imagePath.isEmpty()) {
                this.imagePath = ABSOLUTE_DEFAULT_IMAGE_PATH;
            } else {
                this.imagePath = imagePath;
            }
        }

        public String getJudul() {
            return name; // Judul sekarang diambil dari AbstractEntity.name
        }

        public double getHargaDasar() {
            return hargaDasar;
        }

        public List<String> getJamTayang() {
            return Collections.unmodifiableList(jamTayang);
        }

        public void addJamTayang(String jam) {
            this.jamTayang.add(jam);
            Collections.sort(this.jamTayang);
        }

        public String getImagePath() {
            return imagePath;
        }

        public javafx.scene.image.Image getPosterImage() {
            Image loadedImage = null;
            if (this.imagePath != null && !this.imagePath.isEmpty()) {
                try {
                    String filePath = this.imagePath;
                    if (filePath.startsWith("file:///")) {
                        filePath = filePath.substring(7);
                        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                            filePath = filePath.replaceFirst("^/", "");
                        }
                    } else if (filePath.startsWith("file:/")) {
                        filePath = filePath.substring(5);
                        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                            filePath = filePath.replaceFirst("^/", "");
                        }
                    }

                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            loadedImage = new Image(fis);
                        }
                    } else {
                        System.err.println("File poster tidak ditemukan atau bukan file valid: " + filePath + " untuk film: " + name); // Menggunakan name dari AbstractEntity
                    }
                } catch (Exception e) {
                    System.err.println("Gagal memuat gambar untuk film '" + name + "' dari jalur: " + this.imagePath + ". Error: " + e.getMessage()); // Menggunakan name dari AbstractEntity
                }
            }

            if (loadedImage == null) {
                System.out.println("Mencoba memuat gambar default untuk film '" + name + "'..."); // Menggunakan name dari AbstractEntity
                try {
                    String defaultFilePath = ABSOLUTE_DEFAULT_IMAGE_PATH;
                    defaultFilePath = defaultFilePath.substring(7);
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        defaultFilePath = defaultFilePath.replaceFirst("^/", "");
                    }

                    File defaultFile = new File(defaultFilePath);
                    if (defaultFile.exists() && defaultFile.isFile()) {
                        try (FileInputStream fis = new FileInputStream(defaultFile)) {
                            loadedImage = new Image(fis);
                        }
                    } else {
                        System.err.println("File default poster tidak ditemukan atau bukan file valid: " + defaultFilePath);
                    }
                } catch (Exception ex) {
                    System.err.println("Gagal memuat gambar fallback default absolut. Error: " + ex.getMessage());
                }
            }

            return loadedImage;
        }

        @Override
        public String toString() {
            return name + " (Rp" + String.format("%,.0f", hargaDasar) + ")"; // Menggunakan name dari AbstractEntity
        }

        @Override
        public String getDisplayInfo() { // Implementasi abstract method dari AbstractEntity
            return getJudul() + " (Rp" + String.format("%,.0f", hargaDasar) + "/kursi) - Jam: " + String.join(", ", getJamTayang());
        }
    }

    private final BioskopDataStore dataStore;
    private Film filmTerpilih;
    private String jamTerpilih;
    private Set<String> kursiTerpilih;
    private double totalHargaSebelumDiskon;
    private double totalHargaSetelahDiskon;

    private BioskopDataStore.PaymentMethod metodePembayaranTerpilih;
    private double uangDibayar;

    public BioskopModel() {
        this.dataStore = new BioskopDataStore();
        BioskopModel.Film film1 = new BioskopModel.Film("The Jungle of Basori", 50000.0, null);
        film1.addJamTayang("10:00");
        film1.addJamTayang("13:00");
        film1.addJamTayang("16:00");
        film1.addJamTayang("19:00");
        // Tambahkan film default hanya jika belum ada film
        if (dataStore.getDaftarFilm().isEmpty()) {
            try {
                // Memanggil method 'add' dari IManagementService<Film> yang diimplementasikan oleh BioskopModel
                add(film1);
            } catch (BioskopException e) {
                System.err.println("Error adding default film: " + e.getMessage());
            }
        }
        resetTransaksi();
    }

    public void resetTransaksi() {
        this.filmTerpilih = null;
        this.jamTerpilih = null;
        this.kursiTerpilih = new HashSet<>();
        this.totalHargaSebelumDiskon = 0.0;
        this.totalHargaSetelahDiskon = 0.0;
        this.metodePembayaranTerpilih = null;
        this.uangDibayar = 0.0;
    }

    // Implementasi IManagementService<Film>
    @Override
    public void add(Film item) throws BioskopException {
        dataStore.addFilm(item);
    }

    @Override
    public void remove(Film item) throws BioskopException {
        dataStore.removeFilm(item);
    }

    @Override
    public List<Film> getAll() {
        return dataStore.getDaftarFilm();
    }

    // Metode manajemen PaymentMethod (metode reguler, tidak dari IManagementService)
    public void addMetodePembayaran(String name, int discountPercent, String discountDescription) throws BioskopException {
        dataStore.addMetodePembayaran(name, discountPercent, discountDescription);
    }

    public void removeMetodePembayaran(BioskopDataStore.PaymentMethod methodToRemove) throws BioskopException {
        dataStore.removeMetodePembayaran(methodToRemove);
    }

    public void updateMetodePembayaran(BioskopDataStore.PaymentMethod originalMethod, String newName, int newDiscountPercent, String newDiscountDescription) throws BioskopException {
        BioskopDataStore.PaymentMethod updatedMethod = dataStore.updateMetodePembayaran(originalMethod, newName, newDiscountPercent, newDiscountDescription);
        if (metodePembayaranTerpilih == originalMethod) {
            setMetodePembayaranTerpilih(updatedMethod);
        }
        hitungTotalHarga();
    }

    // Metode getter/setter transaksi dan lainnya
    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return dataStore.getDaftarMetodePembayaran();
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

    public void removeKursiTerpilih(String kursi) {
        kursiTerpilih.remove(kursi);
        hitungTotalHarga();
    }

    public void clearKursiTerpilih() {
        kursiTerpilih.clear();
        hitungTotalHarga();
    }

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

    public String getTotalHargaSebelumDiskonFormatted() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + formatter.format(totalHargaSebelumDiskon);
    }

    public double getTotalHargaAfterDiskon() {
        return totalHargaSetelahDiskon;
    }

    public String getTotalHargaSetelahDiskonFormatted() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + formatter.format(totalHargaSetelahDiskon);
    }

    public BioskopDataStore.PaymentMethod getMetodePembayaranTerpilih() {
        return metodePembayaranTerpilih;
    }

    public void setMetodePembayaranTerpilih(BioskopDataStore.PaymentMethod metodePembayaranTerpilih) {
        this.metodePembayaranTerpilih = metodePembayaranTerpilih;
        hitungTotalHarga();
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

    public boolean isKursiTerisi(Film film, String jam, String kursiName) {
        return dataStore.isKursiTerisi(film, jam, kursiName);
    }

    public void getAllKursiNames() {
        dataStore.getAllKursiNames();
    }

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

        dataStore.tandaiKursiTerisi(filmTerpilih, jamTerpilih, kursiTerpilih);
        return true;
    }

    // Overloading method contoh
    public String getDisplayInfo(Film film) {
        return film.getDisplayInfo();
    }

    public String getDisplayInfo(BioskopDataStore.PaymentMethod method) {
        return method.getName() + " (" + method.getDiscountPercent() + "% Off)";
    }
}