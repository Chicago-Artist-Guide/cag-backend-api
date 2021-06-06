package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.UnionStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UnionStatusRepository extends CrudRepository<UnionStatusEntity, UUID> {
    List<UnionStatusEntity> getByName(String demographicUnionStatus);
}
