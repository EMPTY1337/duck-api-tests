package autotests.tests;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {EndpointConfig.class})
public abstract class BaseTest extends TestNGCitrusSpringSupport {

    @CitrusTest
    @Test
    public void testBaseEndpoint(@Optional @CitrusResource TestCaseRunner runner) {

    }
}
