package autotests.DuckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreate extends TestNGCitrusSpringSupport {

    @Test(description = "Создать утку с material = rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack-quack", "ACTIVE");
        validateCreateResponse(runner, "rubber");
    }

    @Test(description = "Создать утку с material = wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "brown", 10.5, "wood", "quack", "ACTIVE");
        validateCreateResponse(runner, "wood");
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material,
                           String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{" +
                                "\"color\": \"" + color + "\"," +
                                "\"height\": " + height + "," +
                                "\"material\": \"" + material + "\"," +
                                "\"sound\": \"" + sound + "\"," +
                                "\"wingsState\": \"" + wingsState + "\"" +
                                "}")
        );
    }

    public void validateCreateResponse(@Optional @CitrusResource TestCaseRunner runner, String expectedMaterial) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(expectedMaterial)
        );
    }
}
