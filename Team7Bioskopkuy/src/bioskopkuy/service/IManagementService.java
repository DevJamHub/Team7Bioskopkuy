package bioskopkuy.service;

import java.util.List; // Mengimpor List untuk digunakan dalam antarmuka

// Antarmuka ini mendefinisikan layanan manajemen umum untuk entitas yang dapat ditentukan.
// @param <T> Tipe entitas yang akan dikelola oleh layanan ini.
public interface IManagementService<T> {

    // Menambahkan item baru ke dalam sistem.
    // @param item Item yang akan ditambahkan.
    // @throws BioskopException Jika terjadi kesalahan saat menambahkan item.
    void add(T item) throws BioskopException;

    // Menghapus item dari sistem.
    // @param item Item yang akan dihapus.
    // @throws BioskopException Jika terjadi kesalahan saat menghapus item.
    void remove(T item) throws BioskopException;

    // Mengambil semua item yang ada dalam sistem.
    // @return Daftar semua item.
    List<T> getAll();
}
