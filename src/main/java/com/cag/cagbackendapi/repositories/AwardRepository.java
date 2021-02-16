package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.AwardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AwardRepository extends CrudRepository<AwardEntity, UUID> { }
