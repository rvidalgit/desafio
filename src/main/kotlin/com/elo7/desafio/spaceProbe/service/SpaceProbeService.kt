package com.elo7.desafio.spaceProbe.service

import com.elo7.desafio.exception.InvalidProbePositionException
import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.planet.service.PlanetService
import com.elo7.desafio.spaceProbe.extension.checkCause
import com.elo7.desafio.spaceProbe.extension.isInvalidPosition
import com.elo7.desafio.spaceProbe.extension.move
import com.elo7.desafio.spaceProbe.extension.specificCause
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.repository.SpaceProbeRepository
import com.elo7.desafio.spaceProbe.request.Command
import com.elo7.desafio.spaceProbe.response.Message
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class SpaceProbeService(
    val spaceProbeRepository: SpaceProbeRepository,
    val planetService: PlanetService
) {

    fun create(spaceProbe: SpaceProbe): SpaceProbe {
        return try {
            val planet = planetService.get(spaceProbe.planet.id!!)
            if (planet.isPresent) {
                spaceProbe.planet = planet.get()
                checkPosition(spaceProbe)
            }
            spaceProbeRepository.save(spaceProbe)
        } catch (ex: DataIntegrityViolationException) {
            val specificCause = ex.specificCause()
            throw specificCause.checkCause()
        }
    }

    fun get(id: Long): Optional<SpaceProbe> {
        return spaceProbeRepository.findById(id)
    }

    fun list(page: Int, size: Int): Page<SpaceProbe> {
        return spaceProbeRepository.findAll(PageRequest.of(page, size))
    }

    fun list(page: Int, size: Int, idPlanet: Long?): Page<SpaceProbe> {
        return if (idPlanet == null) {
            list(page, size)
        } else {
            spaceProbeRepository.findAllByPlanet_Id(idPlanet, PageRequest.of(page, size))
        }
    }

    fun delete(id: Long) {
        return spaceProbeRepository.deleteById(id)
    }

    fun executeCommand(actions: List<Command>, idProbe: Long): Message {
        val spaceProbe = spaceProbeRepository.findById(idProbe).orElseThrow {
            NotFoundException("Sonda não encontrada")
        }

        actions.forEach {
            spaceProbe.position.move(it.name)
        }

        checkPosition(spaceProbe)

        try {
            spaceProbeRepository.save(spaceProbe)
        } catch (ex: DataIntegrityViolationException) {
            val specificCause = ex.specificCause()
            specificCause.checkCause()
        }

        return Message("Posição final da sonda: x=${spaceProbe.position.x} y=${spaceProbe.position.y} apontando para ${spaceProbe.position.direction.label}")
    }

    private fun checkPosition(spaceProbe: SpaceProbe) {
        if (spaceProbe.isInvalidPosition()) {
            throw InvalidProbePositionException("Essas instruções iriam levar a sonda para uma posição inválida")
        }
    }

}