package com.elo7.desafio.spaceProbe.service

import com.elo7.desafio.exception.InvalidProbePositionException
import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.planet.repository.PlanetRepository
import com.elo7.desafio.spaceProbe.component.CommandInterpreterComponent
import com.elo7.desafio.spaceProbe.extension.checkCause
import com.elo7.desafio.spaceProbe.extension.isInvalidPosition
import com.elo7.desafio.spaceProbe.extension.move
import com.elo7.desafio.spaceProbe.extension.specificCause
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.repository.SpaceProbeRepository
import com.elo7.desafio.spaceProbe.response.Message
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class SpaceProbeService(
    val spaceProbeRepository: SpaceProbeRepository,
    val commandInterpreterComponent: CommandInterpreterComponent,
    val planetRepository: PlanetRepository
) {

    fun create(spaceProbe: SpaceProbe): SpaceProbe {
        return try {
            val planet = planetRepository.findById(spaceProbe.planet.id!!)
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

    fun list(page: Int, size: Int, idPlaneta: Long): Page<SpaceProbe> {
        return spaceProbeRepository.findAllByPlanet_Id(idPlaneta, PageRequest.of(page, size))
    }

    fun delete(id: Long) {
        return spaceProbeRepository.deleteById(id)
    }

    fun executeCommand(actions: String, idProbe: Long): Message {
        val actionList = commandInterpreterComponent.splittedCommand(actions)
        val spaceProbe = spaceProbeRepository.findById(idProbe).orElseThrow {
            NotFoundException("Sonda não encontrada")
        }

        actionList.forEach {
            spaceProbe.position.move(it)
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