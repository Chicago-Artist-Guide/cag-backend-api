package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.AgeIncrementEntity;
import com.cag.cagbackendapi.entities.UnionStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface AgeIncrementRepository extends CrudRepository<AgeIncrementEntity, UUID> {
    AgeIncrementEntity getByName(List<String> ageIncrement);
}
