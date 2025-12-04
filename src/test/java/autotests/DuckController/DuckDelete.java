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
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckDelete extends TestNGCitrusSpringSupport {

    @Test(description = "Успешное удаление уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        extractDuckId(runner);
        deleteDuck(runner, "${duckId}");
        validateResponseSuccessDelete(runner);
    }

    @Test(description = "Удаление уточки с несуществующим ID")
    @CitrusTest
    public void deleteNonExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        deleteDuck(runner, "999999");
        validateResponseNotFound(runner);
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
                                "\"wingsState\": \"" + wingsState + "\"}")
        );
    }

    //Извлечение id созданной уточки в отдельном методе
    public void extractDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
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
                        .client("http://localhost:2222")
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", idDuck)
        );
    }

    public void validateResponseSuccessDelete(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\"message\": \"Duck is deleted\"}")
        );
    }

    public void validateResponseNotFound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\"message\": \"Duck not found\"}")
        );
    }
}