package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    User getById(UUID id);
}
