package repository;

import repository.dto.Dto;

import java.util.List;

/**
 *
 * @param <K> the key of the repository
 * @param <T> the type of the object we want
 */
public interface Repository<K,T extends Dto<K>> {

    /**
     * get an object T from his key
     * @param key
     * @return the object
     */
    public T get(K key);

    /**
     * get all objects of the type T
     * @return a list with the objects
     */
    public List<T> getAll();

    /**
     * @param key
     * @return if the object T with the key exists or not
     */
    public boolean contains(K key);
}
