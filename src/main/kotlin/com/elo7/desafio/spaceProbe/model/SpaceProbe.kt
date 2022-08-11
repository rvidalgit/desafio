package com.elo7.desafio.spaceProbe.model

import com.elo7.desafio.planet.model.Planet
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "space_probe",
    uniqueConstraints = [UniqueConstraint(name = "uk_space_probe", columnNames = ["x", "y", "planet_id"])]
)
@EntityListeners(AuditingEntityListener::class)
class SpaceProbe(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_space_probe")
    @SequenceGenerator(name = "seq_space_probe", sequenceName = "space_probe_seq", allocationSize = 1)
    var id: Long? = null,

    @field:Valid
    @field:NotNull
    @Embedded
    var position: Position,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var direction: DirectionEnum,

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: LocalDateTime?,

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var updatedAt: LocalDateTime?,

    @field:NotNull
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "planet_id", referencedColumnName = "id")
    var planet: Planet
)
