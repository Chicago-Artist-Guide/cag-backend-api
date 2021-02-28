package com.cag.cagbackendapi.repositories;

import com.cag.cagbackendapi.entities.UnionStatusMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UnionStatusMemberRepository extends CrudRepository<UnionStatusMemberEntity, UUID> { }
