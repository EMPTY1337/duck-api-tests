package autotests.duckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckUpdateTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    @Test(description = "Изменить цвет и высоту уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 6, "rubber", "quack", "ACTIVE");
        extractId(runner);
        updateDuck(runner, "${duckId}", "red", 15.5, "rubber", "quack", "ACTIVE");
        validateResponseUpdate(runner);
    }

    @Test(description = "Изменить цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "red", 7, "rubber", "quack", "ACTIVE");
        extractId(runner);
        updateDuck(runner, "${duckId}", "yellow", 0.03, "rubber", "quack-quack-quack", "ACTIVE");
        validateResponseUpdate(runner);
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material,
                           String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"}")
        );
    }

    public void extractId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    public void updateDuck(TestCaseRunner runner, String id, String color, double height,
                           String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .put("/api/duck/update")
                        .queryParam("id", id)
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState)
        );
    }

    public void validateResponseUpdate(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n \"message\": \"Duck with id = ${duckId} is updated\"\n}")
        );
    }
}