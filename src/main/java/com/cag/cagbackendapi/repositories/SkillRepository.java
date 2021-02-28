package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.SkillEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<SkillEntity, UUID> { }
