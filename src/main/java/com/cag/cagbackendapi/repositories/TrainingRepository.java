package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.TrainingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainingRepository extends CrudRepository<TrainingEntity, UUID> { }
