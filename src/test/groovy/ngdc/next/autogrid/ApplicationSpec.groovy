package ngdc.next.autogrid

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.amqp.rabbit.core.RabbitTemplate


@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application)
class ApplicationSpec extends Specification {
    @Autowired
    RabbitTemplate rabbitTemplate

    void contextLoads() {
        expect:
        rabbitTemplate != null
    }
}
