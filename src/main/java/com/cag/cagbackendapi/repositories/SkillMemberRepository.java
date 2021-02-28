package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.SkillMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillMemberRepository extends CrudRepository<SkillMemberEntity, UUID> { }
