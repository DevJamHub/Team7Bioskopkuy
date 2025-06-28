package bioskopkuy.service;

// Kelas ini merepresentasikan pengecualian khusus untuk aplikasi BioskopKuy.
// Kelas ini memperluas kelas Exception untuk menangani kesalahan yang spesifik dalam konteks bioskop.
public class BioskopException extends Exception {

    // Konstruktor untuk BioskopException yang menerima pesan kesalahan.
    // @param message Pesan yang menjelaskan kesalahan.
    public BioskopException(String message) {
        super(message); // Memanggil konstruktor kelas induk (Exception) dengan pesan yang diberikan.
    }
}
