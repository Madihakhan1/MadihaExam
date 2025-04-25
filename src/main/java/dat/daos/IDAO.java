package dat.daos;

import java.util.List;

public interface IDAO<T, Integer> {
        T create(T t);
        List<T> getAll();
        T getById(int id);
        T update(int id, T t);
        boolean delete(int id);
    }


