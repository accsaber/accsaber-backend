package de.ixsen.accsaber.database.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID, RT> extends Repository<T, ID> {
    List<RT> findAll();
}