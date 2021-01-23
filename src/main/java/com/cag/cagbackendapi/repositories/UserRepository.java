package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    UserEntity getByUserId(UUID user_id);
}
