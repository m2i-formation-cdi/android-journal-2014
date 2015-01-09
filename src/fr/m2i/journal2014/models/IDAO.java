package fr.m2i.journal2014.models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author seb
 * @param <T>
 */
public interface IDAO<T> {
    public int insert(T object) throws SQLException;
    public List<Map<String, String>> selectAll();
    public T selectOne(T oject);
    public int delete(T object);
    public int update(T object);
}