package com.cag.cagbackendapi.entities;

import org.hibernate.annotations.Type;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public class UuidEntity {
    @Id
    @Type(type = "pg-uuid")
    private final UUID id;

    public UuidEntity() {
        this.id = UUID.randomUUID();
    }
}
