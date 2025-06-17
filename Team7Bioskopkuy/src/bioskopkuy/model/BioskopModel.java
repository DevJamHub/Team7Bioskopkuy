package bioskopkuy.model;

import bioskopkuy.service.BioskopException;
import java.text.NumberFormat;
import java.util.*;

public class BioskopModel {

    public static class Film {
        private final String judul;
        private final double hargaDasar;
        private final List<String> jamTayang;

        public Film(String judul, double hargaDasar) {
            this.judul = judul;
            this.hargaDasar = hargaDasar;
            this.jamTayang = new ArrayList<>();
        }

        public String getJudul() {
            return judul;
        }

        public double getHargaDasar() {
            return hargaDasar;
        }

        public List<String> getJamTayang() {
            return Collections.unmodifiableList(jamTayang);
        }

        public void addJamTayang(String jam) {
            this.jamTayang.add(jam);
        }

        @Override
        public String toString() {
            return judul + " (Rp" + String.format("%,.0f", hargaDasar) + ")";
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

    public List<String> getAllKursiNames() {
        return dataStore.getAllKursiNames();
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

    public void addFilm(String judul, double harga, String jamTayang) throws BioskopException {
        Film newFilm = new Film(judul, harga);
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
        dataStore.addFilm(newFilm);
    }

    public void removeFilm(Film film) throws BioskopException {
        dataStore.removeFilm(film);
    }

    public List<BioskopDataStore.PaymentMethod> getDaftarMetodePembayaran() {
        return dataStore.getDaftarMetodePembayaran();
    }

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
}