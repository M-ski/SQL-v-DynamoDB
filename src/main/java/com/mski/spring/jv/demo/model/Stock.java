package com.mski.spring.jv.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Enumerated
    private Set<Restrictions> restrictions;

    public enum Restrictions {
        UNRESTRICTED, ALCOHOL, TOBACCO
    }
}
