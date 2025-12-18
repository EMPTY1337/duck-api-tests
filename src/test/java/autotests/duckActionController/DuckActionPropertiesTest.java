package autotests.duckActionController;

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

public class DuckActionPropertiesTest extends TestNGCitrusSpringSupport {

    private static final String URL = "http://localhost:2222";

    // TODO: SHIFT-AQA-1
    @Test(description = "Получение свойств утки с четным ID и материалом wood ")
    @CitrusTest
    public void testPropertiesEvenIdWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "wood", "quack", "FIXED");
        extractId(runner);
        validateIdEven(runner);
        getDuckProperties(runner, "${duckId}");
        validateWoodResponseEmpty(runner);
    }

    @Test(description = "Получение свойств утки с нечетным ID и материалом rubber")
    @CitrusTest
    public void testPropertiesOddIdRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");
        extractId(runner);
        validateIdOdd(runner);
        getDuckProperties(runner, "${duckId}");
        validateRubberResponse(runner);
    }

    private void createDuck(TestCaseRunner runner, String color, double height, String material,
                            String sound, String wingsState) {
        runner.$(http()
                .client(URL)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"color\":\"" + color + "\",\"height\":" + height +
                        ",\"material\":\"" + material + "\",\"sound\":\"" + sound +
                        "\",\"wingsState\":\"" + wingsState + "\"}"));
    }

    private void extractId(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    private void getDuckProperties(TestCaseRunner runner, String id) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    // БАГ сервиса: для wood всегда пустой объект
    private void validateWoodResponseEmpty(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{}"));
    }

    // Для rubber всё работает как ожидается
    private void validateRubberResponse(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"color\": \"yellow\",\n" +
                        "  \"height\": 802.9999999999999,\n" +
                        "  \"material\": \"rubber\",\n" +
                        "  \"sound\": \"quack\",\n" +
                        "  \"wingsState\": \"FIXED\"\n" +
                        "}"));
    }

    private void validateIdEven(TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 != 0) {
                throw new RuntimeException("Для material=wood ожидался ЧЁТНЫЙ ID, получен НЕЧЁТНЫЙ: " + id);
            }
        });
    }

    private void validateIdOdd(TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 == 0) {
                throw new RuntimeException("Для material=rubber ожидался НЕЧЁТНЫЙ ID, получен ЧЁТНЫЙ: " + id);
            }
        });
    }
}