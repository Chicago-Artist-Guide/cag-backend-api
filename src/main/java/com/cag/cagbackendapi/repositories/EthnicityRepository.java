package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.EthnicityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EthnicityRepository extends CrudRepository<EthnicityEntity, UUID> { }
