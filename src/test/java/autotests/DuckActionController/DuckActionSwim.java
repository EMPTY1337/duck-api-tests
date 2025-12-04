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

public class DuckActionSwim extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка того, что уточка плывет с существующим id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckSwim(runner, "${duckId}");
        validateResponseSuccessfulSwim(runner, "{\n \"message\": \"I'm swimming\"\n}");

    }

    @Test(description = "Проверка того, что уточка плывет с НЕсуществующим id")
    @CitrusTest
    public void invalidSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckSwim(runner, "54");
        validateResponseInvalidSwim(runner, "{\n \"message\": \"Paws are not found ((((\"\n}");

    }
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body("{\n" +
                                                "\"color\": \"" + color + "\",\n" +
                                                "\"height\": " + height + ",\n" +
                                                "\"material\": \"" + material + "\",\n" +
                                                "\"sound\": \"" + sound + "\",\n" +
                                                "\"wingsState\": \"" + wingsState + "\"}"));
    }

    public void  duckSwim(TestCaseRunner runner, String id){
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));

    }

    public void validateResponseSuccessfulSwim(TestCaseRunner runner, String responseMessage) {         //валидация при существующим ID
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage) );

    }

    public void validateResponseInvalidSwim(TestCaseRunner runner, String responseMessage) {         //валидация при НЕсуществующим ID
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage) );

    }

}
