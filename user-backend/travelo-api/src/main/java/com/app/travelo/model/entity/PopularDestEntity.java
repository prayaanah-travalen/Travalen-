package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "popularDestination")

public class PopularDestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popular_dest_id")
    private Long popularDestId;

    @Column(name = "location")
    private String location;

    @Column(name = "image")
    private String image;

    @Column(name = "url")
    private String url;


}