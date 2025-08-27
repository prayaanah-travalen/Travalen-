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
@Table(name = "common_parameter")
public class CommonParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="parameter_id")
    private String parameterId;

    @Column(name="parameter_group")
    private String parameterGroup;

    @Column(name="parameter")
    private String parameter;

    @Column(name="description")
    private String description;

    @Column(name="available")
    private Boolean available;
}
