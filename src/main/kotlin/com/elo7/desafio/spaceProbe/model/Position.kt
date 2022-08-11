package com.elo7.desafio.spaceProbe.model

import org.springframework.validation.annotation.Validated
import javax.persistence.Embeddable
import javax.validation.constraints.Positive

@Validated
@Embeddable
data class Position(
    @field:Positive
    var x: Int,

    @field:Positive
    var y: Int
)