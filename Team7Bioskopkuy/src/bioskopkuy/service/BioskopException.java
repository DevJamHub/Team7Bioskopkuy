package bioskopkuy.service;

/**
 * Kelas pengecualian (exception) kustom untuk aplikasi BioskopKuy.
 * Kelas ini digunakan untuk menandakan kesalahan atau kondisi khusus yang terjadi
 * selama operasi aplikasi BioskopKuy, seperti validasi input yang gagal,
 * data tidak ditemukan, atau masalah bisnis lainnya.
 * Dengan membuat pengecualian kustom, kita dapat memberikan pesan kesalahan yang lebih spesifik
 * dan menanganinya secara terpusat di dalam aplikasi.
 */
public class BioskopException extends Exception {

    /**
     * Konstruktor untuk membuat objek BioskopException baru.
     *
     * @param message Pesan deskriptif yang menjelaskan penyebab pengecualian.
     * Pesan ini akan menjadi detail dari pengecualian yang dilempar.
     */
    public BioskopException(String message) {
        super(message); // Memanggil konstruktor kelas induk (Exception) dengan pesan yang diberikan.
    }
}