package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.SkillEntity;
import com.cag.cagbackendapi.entities.UnionStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<SkillEntity, UUID> {
    SkillEntity getByName(String userSkill);
}
