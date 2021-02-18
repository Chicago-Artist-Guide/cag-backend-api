package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.AgeIncrementMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgeIncrementMemberRepository extends CrudRepository<AgeIncrementMemberEntity, UUID> { }
