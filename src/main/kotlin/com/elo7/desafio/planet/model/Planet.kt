package com.elo7.desafio.planet.model

import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Entity
@Table(name = "planet")
@EntityListeners(AuditingEntityListener::class)
class Planet(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_planet")
    @SequenceGenerator(name = "seq_planet", sequenceName = "planet_seq", allocationSize = 1)
    var id: Long? = null,

    @field:NotNull
    @field:Positive
    val width: Int,

    @field:NotNull
    @field:Positive
    val height: Int,

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: LocalDateTime?,

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var updatedAt: LocalDateTime?,

    @Transient
    @JsonManagedReference
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "planet",
        orphanRemoval = true
    )
    val spaceProbes: Set<SpaceProbe>?
)
