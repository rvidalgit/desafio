package com.elo7.desafio.spaceProbe.request

import javax.validation.constraints.NotEmpty

class CommandRequest {

    @field:NotEmpty
    lateinit var command: List<Command>
}