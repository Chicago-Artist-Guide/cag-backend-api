package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.SkillsManualEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillsManualRepository extends CrudRepository<SkillsManualEntity, UUID> {
}
