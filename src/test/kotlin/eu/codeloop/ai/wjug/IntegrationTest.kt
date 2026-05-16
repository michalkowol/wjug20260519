package eu.codeloop.ai.wjug

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegrationTest {

    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    @Qualifier("toolSpecs")
    protected lateinit var toolSpecifications: List<SyncToolSpecification>
}
