package com.springboot.eventlink.locationApi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CITIES")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CITY_ID") // ✅ 명시적 매핑
    private Long cityId;

    @Column(name = "CITY_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID") // ✅ 정확한 외래키 연결
    private Country country;
}
