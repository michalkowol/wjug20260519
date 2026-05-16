package eu.codeloop.ai.wjug.domain.common

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class IdGenerator {

    private val random = SecureRandom()

    fun generate(): String {
        return buildString(LENGTH) {
            repeat(LENGTH) { append(ALPHABET[random.nextInt(ALPHABET.length)]) }
        }
    }

    companion object {
        private const val ALPHABET = "2346789abcdefghijkmnpqrtwxyz"
        private const val LENGTH = 6
    }
}
