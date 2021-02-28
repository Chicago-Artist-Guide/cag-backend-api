package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.ProfilePhotoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfilePhotoRepository extends CrudRepository<ProfilePhotoEntity, UUID> { }
