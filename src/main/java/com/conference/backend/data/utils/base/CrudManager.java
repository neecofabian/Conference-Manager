package com.conference.backend.data.utils.base;

import java.util.List;

public interface CrudManager<T extends AbstractEntity> {
    /**
     * Deletes the given {@code T} from the repository.
     * @param t The type of AbstractEntity to delete
     */
    void delete(T t);

    /**
     * Persists the given {@code T} into the repository.
     * @param t The type of AbstractEntity to save
     */
    void save(T t);

    /**
     * A list of names of all the Objects in this repository
     * @return The list of strings of names
     */
    List<String> getNames();

}
