package fr.univtln.projet.planning.repository;

import java.sql.SQLException;
import java.util.List;

public interface GenericRepository<T, ID> {
    void create(T entity) throws SQLException;
    T read(ID id) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
}
