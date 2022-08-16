package com.elo7.desafio.spaceProbe.model

import org.springframework.validation.annotation.Validated
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Validated
@Embeddable
data class Position(
    var x: Int,
    var y: Int,

    @field:Column(nullable = false, length = 5)
    @field:Enumerated(EnumType.STRING)
    var direction: Direction,
)