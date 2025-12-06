package autotests.DuckActionController;

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

public class DuckActionFly extends TestNGCitrusSpringSupport {

    private static final String URL = "http://localhost:2222";

    @Test(description = "Проверка полета утки с Active крыльями")
    @CitrusTest
    public void activeWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "ACTIVE");
        extractId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n \"message\": \"I am flying :)\"\n}");
    }

    @Test(description = "Проверка полета утки с Fixed крыльями")
    @CitrusTest
    public void fixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");
        extractId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n \"message\": \"I can not fly :C\"\n}");
    }

    @Test(description = "Проверка полета утки с Undefined крыльями")
    @CitrusTest
    public void undefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "UNDEFINED");
        extractId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n \"message\": \"Wings are not detected :(\"\n}");
    }

    private void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http()
                .client(URL)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"color\":\"" + color + "\",\"height\":" + height + ",\"material\":\"" + material + "\",\"sound\":\"" + sound + "\",\"wingsState\":\"" + wingsState + "\"}"));
    }

    private void extractId(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    private void duckFly(TestCaseRunner runner, String id) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    private void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }
}
