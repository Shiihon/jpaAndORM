package org.example.JPATorsdag2908.daos;

import java.util.Set;

public interface IDAO<T> {
    T getById(Integer id);
    Set<T> getAll();
    void create (T t);
    void update (T t);
    void delete (T t);

}
