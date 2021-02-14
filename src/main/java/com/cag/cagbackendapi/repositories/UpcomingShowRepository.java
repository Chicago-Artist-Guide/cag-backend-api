package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.UpcomingShowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UpcomingShowRepository extends CrudRepository<UpcomingShowEntity, UUID> { }
