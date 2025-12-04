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

    @Test(description = "Проверка полета утки с Active крыльями и существующим id")
    @CitrusTest
    public void activeWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponseActiveWingsFly(runner, "{\n \"message\": \"I am flying :)\"\n}");

    }

    @Test(description = "Проверка полета утки с Fixed крыльями и существующим id")
    @CitrusTest
    public void fixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");

        //получение id
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        //
        duckFly(runner, "${duckId}");
        validateResponseFixedWingsFly(runner, "{\n \"message\": \"I can not fly :C\"\n}");

    }

    @Test(description = "Проверка полета утки с Undefined крыльями и существующим id")
    @CitrusTest
    public void undefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "UNDEFINED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponseUndefinedWingsFly(runner, "{\n \"message\": \"Wings are not detected :(\"\n}");

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

    public void  duckFly(TestCaseRunner runner, String id){
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));

    }

    public void validateResponseActiveWingsFly(TestCaseRunner runner, String responseMessage) {         //валидация при существующим ID и Active
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage) );

    }
    public void validateResponseFixedWingsFly(TestCaseRunner runner, String responseMessage) {         //валидация при существующим ID и Fixed
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage) );

    }
    public void validateResponseUndefinedWingsFly(TestCaseRunner runner, String responseMessage) {         //валидация при существующим ID и Undefined
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage) );

    }
}
