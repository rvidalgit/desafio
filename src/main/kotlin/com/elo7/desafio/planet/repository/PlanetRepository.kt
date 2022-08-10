package com.elo7.desafio.planet.repository

import com.elo7.desafio.planet.model.Planet
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanetRepository : PagingAndSortingRepository<Planet, Long> {

}