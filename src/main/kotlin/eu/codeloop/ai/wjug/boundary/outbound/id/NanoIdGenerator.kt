package eu.codeloop.ai.wjug.boundary.outbound.id

import eu.codeloop.ai.wjug.domain.common.IdGenerator
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class NanoIdGenerator : IdGenerator {

    private val random = SecureRandom()

    override fun generate(): String =
        buildString(LENGTH) {
            repeat(LENGTH) { append(ALPHABET[random.nextInt(ALPHABET.length)]) }
        }

    companion object {
        private const val ALPHABET = "2346789abcdefghijkmnpqrtwxyz"
        private const val LENGTH = 6
    }
}
