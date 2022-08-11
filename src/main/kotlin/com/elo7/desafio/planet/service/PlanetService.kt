package com.elo7.desafio.planet.service

import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.repository.PlanetRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlanetService(
    val planetRepository: PlanetRepository
) {

    fun create(planet: Planet): Planet {
        return planetRepository.save(planet)
    }

    fun get(id: Long): Optional<Planet> {
        return planetRepository.findById(id)
    }

    fun list(page: Int, size: Int): Iterable<Planet> {
        return planetRepository.findAll(PageRequest.of(page, size))
    }

    fun delete(id: Long){
        return planetRepository.deleteById(id)
    }
}