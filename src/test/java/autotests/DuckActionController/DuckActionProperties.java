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

public class DuckActionProperties extends TestNGCitrusSpringSupport {

    @Test(description = "Получение свойств утки с четным ID и материалом wood")
    @CitrusTest
    public void testPropertiesEvenIdWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "wood", "quack", "FIXED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        getDuckProperties(runner, "${duckId}");

        validatePropertiesResponseWood(runner);

        validateEvenId(runner);
    }

    @Test(description = "Получение свойств утки с нечетным ID и материалом rubber")
    @CitrusTest
    public void testPropertiesOddIdRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.03, "rubber", "quack", "FIXED");

        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );

        getDuckProperties(runner, "${duckId}");

        validatePropertiesResponseRubber(runner);

        validateOddId(runner);
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

    public void getDuckProperties(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id)
        );
    }

    public void validatePropertiesResponseWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "  \"color\": \"yellow\",\n" +
                                "  \"height\": 802.9999999999999,\n" +
                                "  \"material\": \"wood\",\n" +
                                "  \"sound\": \"quack\",\n" +
                                "  \"wingsState\": \"FIXED\"\n" +
                                "}")
        );
    }

    public void validatePropertiesResponseRubber(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
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
                                "}")
        );
    }

    public void validateEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            String duckId = context.getVariable("duckId");
            long id = Long.parseLong(duckId);

            if (id % 2 != 0) {
                throw new RuntimeException("Для материала wood ожидался ЧЕТНЫЙ ID, но получен НЕЧЕТНЫЙ ID=" + id);
            }

            if (id <= 0 || id > 9223372036854775807L) {
                throw new RuntimeException("ID=" + id + " вне допустимого диапазона (0, 9223372036854775807)");
            }

            System.out.println(String.format("ID=%d четный, материал='wood' - корректно", id));
        });
    }

    public void validateOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            String duckId = context.getVariable("duckId");
            long id = Long.parseLong(duckId);

            if (id % 2 == 0) {
                throw new RuntimeException("Для материала rubber ожидался НЕЧЕТНЫЙ ID, но получен ЧЕТНЫЙ ID=" + id);
            }

            if (id <= 0 || id > 9223372036854775807L) {
                throw new RuntimeException("ID=" + id + " вне допустимого диапазона (0, 9223372036854775807)");
            }

            System.out.println(String.format("ID=%d нечетный, материал='rubber' - корректно", id));
        });
    }

}