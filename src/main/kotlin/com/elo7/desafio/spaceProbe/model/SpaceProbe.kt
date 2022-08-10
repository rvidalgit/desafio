package com.elo7.desafio.spaceProbe.model

import com.elo7.desafio.planet.model.Planet
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "space_probe")
@EntityListeners(AuditingEntityListener::class)
class SpaceProbe(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotNull
    @Column(nullable = false)
    var x: Int,

    @NotNull
    @Column(nullable = false)
    var y: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var direction: DirectionEnum,

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: LocalDateTime?,

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var updatedAt: LocalDateTime?,

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "planet_id", referencedColumnName = "id")
    var planet: Planet
)
