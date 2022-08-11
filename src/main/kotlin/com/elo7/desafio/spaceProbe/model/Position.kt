package com.elo7.desafio.spaceProbe.model

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotNull

@Embeddable
data class Position(
    @NotNull
    @Column(nullable = false)
    var x: Int,

    @NotNull
    @Column(nullable = false)
    var y: Int
)