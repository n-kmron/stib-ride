package repository.dao;

import java.util.List;

/**
 * Data access object
 * @param <K> the key of the object we want to access
 * @param <T> the type of the object we want to access
 */
public interface Dao<K,T> {

    public T get(K key);

    public List<T> getAll();
}
