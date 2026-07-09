package com.enterprise.hotel.hotel;

import com.enterprise.hotel.common.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String city;

    private String address;

    @Column(name = "star_rating", nullable = false)
    private int starRating;

    @Column(name = "manager_id")
    private Long managerId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    @Builder.Default
    private Set<String> amenities = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hotel_images", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "url")
    @Builder.Default
    private Set<String> images = new HashSet<>();
}
