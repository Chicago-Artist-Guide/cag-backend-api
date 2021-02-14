package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.AgeIncrementEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgeIncrementRepository extends CrudRepository<AgeIncrementEntity, UUID> { }
