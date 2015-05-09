package next.autogrid

import org.springframework.boot.test.SpringApplicationConfiguration
import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.beans.factory.annotation.Autowired

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application)
class ApplicationSpec extends Specification {
    void contextLoads() {
        expect:
        assert true
    }
}
