package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.EthnicityMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EthnicityMemberRepository extends CrudRepository<EthnicityMemberEntity, UUID> { }
