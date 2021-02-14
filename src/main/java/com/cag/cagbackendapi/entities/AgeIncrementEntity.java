package com.cag.cagbackendapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "age_increment")
public class AgeIncrementEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID age_increment_id;
    private int youngest_age;
    private int oldest_age;
}
