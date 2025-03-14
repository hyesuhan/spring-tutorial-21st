package com.ceos21.spring_boot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Test {
    @Id
    private Long id;
    private String name;
}
