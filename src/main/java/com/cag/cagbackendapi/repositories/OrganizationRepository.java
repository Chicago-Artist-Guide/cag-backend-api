package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, UUID> {
    Organization getById(UUID id);
}
