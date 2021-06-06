package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.ProfileEntity;
import com.cag.cagbackendapi.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, UUID> {
    ProfileEntity getByUserEntity_userId(UUID userId);
}
