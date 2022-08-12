package com.elo7.desafio.spaceProbe.service

import com.elo7.desafio.exception.InvalidProbePositionException
import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.exception.SpaceProbeCollidedException
import com.elo7.desafio.spaceProbe.component.CommandInterpreterComponent
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.repository.SpaceProbeRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*
import kotlin.math.abs

@Service
class SpaceProbeService(
    val spaceProbeRepository: SpaceProbeRepository,
    val commandInterpreterComponent: CommandInterpreterComponent
) {

    fun create(spaceProbe: SpaceProbe): SpaceProbe {
        return try {
            spaceProbeRepository.save(spaceProbe)
        } catch (ex: DataIntegrityViolationException) {
            val cause = ex.rootCause
            val specificCause = cause as SQLIntegrityConstraintViolationException
            if (specificCause.sqlState.equals("23505")) {
                throw SpaceProbeCollidedException("Está operação causará uma colisão entre as sondas")
            } else if (specificCause.sqlState.equals("23506")) {
                throw NotFoundException("Planeta não encontrado")
            }
            throw specificCause
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

    fun executeCommand(actions: String, idProbe: Long) {
        val actionList = commandInterpreterComponent.splittedCommand(actions)
        val spaceProbe = spaceProbeRepository.findById(idProbe).orElseThrow {
            NotFoundException("Sonda não encontrada")
        }

        actionList.forEach {
            spaceProbe.position.move(it)
        }

        val planet = spaceProbe.planet

        if (planet.height < abs(spaceProbe.position.y) && planet.width < abs(spaceProbe.position.x)) {
            throw InvalidProbePositionException("Essas instruções iriam levar a sonda para uma posição inválida")
        }

        spaceProbeRepository.save(spaceProbe)

        // TODO: adicionar retorno da api
    }
}