package com.elo7.desafio.spaceProbe.extension

import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.exception.SpaceProbeCollidedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.sql.SQLIntegrityConstraintViolationException

@ExtendWith(MockitoExtension::class)
class SQLIntegrityConstraintViolationExceptionExtensionKtTest {

    @Mock
    private lateinit var sqlIntegrityConstraintViolationException: SQLIntegrityConstraintViolationException

    @Test
    fun `Testa lancar a excecao - 23505`() {
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("23505")
        assertThrows<SpaceProbeCollidedException> {
            sqlIntegrityConstraintViolationException.checkCause()
        }
    }

    @Test
    fun `Testa lancar a excecao - 23506`() {
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("23506")
        assertThrows<NotFoundException> {
            sqlIntegrityConstraintViolationException.checkCause()
        }
    }

    @Test
    fun `Testa lancar a excecao - default`() {
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("11111")
        assertThrows<SQLIntegrityConstraintViolationException> {
            sqlIntegrityConstraintViolationException.checkCause()
        }
    }
}