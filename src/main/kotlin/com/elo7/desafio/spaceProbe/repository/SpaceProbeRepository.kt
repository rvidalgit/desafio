package com.elo7.desafio.spaceProbe.repository

import com.elo7.desafio.spaceProbe.model.SpaceProbe
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SpaceProbeRepository : PagingAndSortingRepository<SpaceProbe, Long>