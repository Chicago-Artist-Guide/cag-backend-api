package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.OrganizationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends CrudRepository<OrganizationEntity, UUID> {
    OrganizationEntity getById(UUID id);
}
