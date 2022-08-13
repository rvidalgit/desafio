package com.elo7.desafio.util

import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.nio.file.Files

object TestUtil {
    @Throws(IOException::class)
    fun readJsonFile(file: String): String {
        val path = "__files/$file"
        return String(Files.readAllBytes(ClassPathResource(path).file.toPath()))
    }
}
