package bioskopkuy.model; // Menandakan bahwa kelas ini berada dalam package 'model'

// Kelas abstrak yang menjadi induk (superclass) dari entitas-entitas lain seperti Film, PaymentMethod, dll.
// Tujuan penggunaan kelas abstrak adalah untuk menyatukan atribut dan perilaku umum dari entitas-entitas tersebut.
public abstract class AbstractEntity {
    protected String id;   // Atribut ID yang bersifat unik untuk membedakan setiap entitas
    protected String name; // Atribut nama entitas (misalnya nama film atau nama metode pembayaran)

    // Konstruktor dari AbstractEntity
    // Konstruktor ini digunakan oleh kelas turunan untuk mengisi nilai id dan name saat objek dibuat
    public AbstractEntity(String id, String name) {
        this.id = id;       // Menginisialisasi atribut id
        this.name = name;   // Menginisialisasi atribut name
    }

    // Metode abstrak yang harus diimplementasikan oleh semua kelas turunan
    // Fungsinya untuk mengembalikan informasi yang akan ditampilkan ke pengguna (biasanya berupa String ringkas)
    public abstract String getDisplayInfo(); // Deklarasi metode abstrak
}
