package autotests.DuckActionController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckActionQuack extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка кряканья утки с корректным нечетным ID и корректным звуком")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack-quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );

        duckQuack(runner, "${duckId}", "1", "2");
        validateResponseQuack(runner, "{\n \"sound\": \"quack\"\n}");
        // Проверяем, что ID нечетный
        validateOddId(runner);
    }

    @Test(description = "Проверка кряканья утки с корректным четным ID и корректным звуком")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "wood", "quack-quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckQuack(runner, "${duckId}", "1", "1");
        validateResponseQuack(runner, "{\n \"sound\": \"quack-quack\"\n}");
        // Проверяем, что ID четный
        validateEvenId(runner);
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
                                "\"wingsState\": \"" + wingsState + "\"}")
        );
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitionCount)
                        .queryParam("soundCount", soundCount)
        );
    }

    public void validateResponseQuack(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage)
        );
    }

    // Дополнительные методы для проверки четности нечетности ID
    public void validateOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext context) {
                String duckId = context.getVariable("duckId");
                int id = Integer.parseInt(duckId);

                // Проверяем, что ID нечетный
                if (id % 2 == 0) {
                    throw new RuntimeException("ID должен быть нечетным " + id);
                }
                System.out.println("ID " + id + " является нечетным");
            }
        });
    }

    public void validateEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext context) {
                String duckId = context.getVariable("duckId");
                int id = Integer.parseInt(duckId);

                // Проверяем, что ID четный
                if (id % 2 != 0) {
                    throw new RuntimeException("ID должен быть четным" + id);
                }
                System.out.println("ID " + id + " является четным");
            }
        });
    }}
