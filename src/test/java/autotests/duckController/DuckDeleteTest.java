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

public class DuckDeleteTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    @Test(description = "Успешное удаление уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        extractId(runner);
        deleteDuck(runner, "${duckId}");
        validateResponseSuccessDelete(runner,"{\"message\": \"Duck is deleted\"}");
    }

    @Test(description = "Удаление уточки с несуществующим ID")
    @CitrusTest
    public void deleteNonExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        deleteDuck(runner, "999999");
        validateResponseNotFound(runner, "{\n" +
                "  \"timestamp\": \"@ignore@\",\n" +
                "  \"status\": 500,\n" +
                "  \"error\": \"Internal Server Error\",\n" +
                "  \"message\": \"No class ru.cft.shift.qa.duck.model.entity.Duck entity with id 999999 exists!\",\n" +
                "  \"path\": \"/api/duck/delete\"\n" +
                "}");
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{" +
                                "\"color\": \"" + color + "\"," +
                                "\"height\": " + height + "," +
                                "\"material\": \"" + material + "\"," +
                                "\"sound\": \"" + sound + "\"," +
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

    public void deleteDuck(TestCaseRunner runner, String idDuck) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", idDuck)
        );
    }

    public void validateResponseSuccessDelete(@Optional @CitrusResource TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage)
        );
    }

    public void validateResponseNotFound(@Optional @CitrusResource TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage)
        );

    }
}