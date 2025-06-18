package bioskopkuy.service;

public class BioskopException extends Exception {
    public BioskopException(String message) {
        super(message); // Memanggil konstruktor kelas induk (Exception) dengan pesan yang diberikan.
    }
}