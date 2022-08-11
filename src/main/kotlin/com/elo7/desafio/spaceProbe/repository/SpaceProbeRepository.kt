package com.elo7.desafio.spaceProbe.repository

import com.elo7.desafio.spaceProbe.model.SpaceProbe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SpaceProbeRepository : PagingAndSortingRepository<SpaceProbe, Long> {

    fun findAllByPlanet_Id(planet_id: Long, pageable: Pageable): Page<SpaceProbe>
}