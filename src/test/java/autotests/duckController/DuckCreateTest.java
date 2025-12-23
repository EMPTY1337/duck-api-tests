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

public class DuckCreateTest extends TestNGCitrusSpringSupport {

    private static final String URL = "http://localhost:2222";

    @Test(description = "Создать утку с material = rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack-quack", "ACTIVE");
        validateCreateResponseRubber(runner, "rubber", "yellow", "quack-quack");
    }

    @Test(description = "Создать утку с material = wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "brown", 10.5, "wood", "quack", "ACTIVE");
        validateCreateResponseWood(runner, "wood", "brown", "quack");
    }

    private void createDuck(TestCaseRunner runner, String color, double height, String material,
                            String sound, String wingsState) {
        runner.$(http()
                .client(URL)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"color\":\"" + color + "\",\"height\":" + height + ",\"material\":\"" + material + "\",\"sound\":\"" + sound + "\",\"wingsState\":\"" + wingsState + "\"}"));
    }

    private void validateCreateResponseRubber(TestCaseRunner runner, String material, String color, String sound) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"id\": 9,\n" +
                        "  \"color\": \"" + color + "\",\n" +
                        "  \"height\": 8.03,\n" +
                        "  \"material\": \"" + material + "\",\n" +
                        "  \"sound\": \"" + sound + "\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}"));
    }

    private void validateCreateResponseWood(TestCaseRunner runner, String material, String color, String sound) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"id\": 10,\n" +
                        "  \"color\": \"" + color + "\",\n" +
                        "  \"height\": 10.5,\n" +
                        "  \"material\": \"" + material + "\",\n" +
                        "  \"sound\": \"" + sound + "\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}"));
    }
}