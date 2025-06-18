package bioskopkuy.service;

import java.util.List;

public interface IManagementService<T> {
    void add(T item) throws BioskopException;
    void remove(T item) throws BioskopException;

    List<T> getAll();
}