package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.PastPerformanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PastPerformanceRepository extends CrudRepository<PastPerformanceEntity, UUID> { }
