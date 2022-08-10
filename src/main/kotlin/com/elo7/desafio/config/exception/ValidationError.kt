package com.elo7.desafio.config.exception

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class ValidationError @JsonCreator constructor(
    @param:JsonProperty("field") val field: String?,
    @param:JsonProperty("message") val message: String?
) {}