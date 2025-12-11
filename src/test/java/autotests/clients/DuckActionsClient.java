package autotests.clients;

import autotests.tests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;



import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;


public class DuckActionsClient extends BaseTest {

    @Autowired
    protected HttpClient duckService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // СОЗДАНИЕ УТКИ С ИЗВЛЕЧЕНИЕМ ID
    public void createDuckAndExtractId(@Optional @CitrusResource TestCaseRunner runner,
                                       Object duckProperties) {
        runner.$(http()
                .client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .type(MessageType.JSON)
                .contentType("application/json")
                .body(new ObjectMappingPayloadBuilder(duckProperties, objectMapper)));

        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "duckId"))
                .timeout(10000));
    }


    public void getDuckActionProperties(@Optional @CitrusResource TestCaseRunner runner,
                                        String id) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void duckSwim(@Optional @CitrusResource TestCaseRunner runner,
                         String id) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    public void duckFly(@Optional @CitrusResource TestCaseRunner runner,
                        String id) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    public void duckQuack(@Optional @CitrusResource TestCaseRunner runner,
                          String id,
                          String repetitionCount,
                          String soundCount) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public void updateDuck(@Optional @CitrusResource TestCaseRunner runner,
                                        String id,
                                        String color,
                                        double height,
                                        String material,
                                        String sound,
                                        String wingsState) {
        runner.$(http()
                .client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("id", id)
                .queryParam("color", color)
                .queryParam("height", String.valueOf(height))
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));
    }

    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner,
                           String id) {
        runner.$(http()
                .client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }
    // 1. ВАЛИДАЦИЯ С STRING ОТВЕТОМ
    public void validateWithString(@Optional @CitrusResource TestCaseRunner runner,
                                   String responseString) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseString));
    }

    // 2. ВАЛИДАЦИЯ ИЗ РЕСУРСОВ
    public void validateWithResource(@Optional @CitrusResource TestCaseRunner runner,
                                     String fileName) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(fileName)));
    }

    // 3. ВАЛИДАЦИЯ С PAYLOAD
    public void validateWithPayload(@Optional @CitrusResource TestCaseRunner runner,
                                    Object expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, objectMapper)));
    }


    public void validateResponseWithMessage(@Optional @CitrusResource TestCaseRunner runner,
                                            String responseMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    public void validateResponseWithMessageButNotFound(@Optional @CitrusResource TestCaseRunner runner,
                                            String responseMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    public void validateResponseStatusOk(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK));
    }

    public void validateResponseStatusNotFound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public void validateUpdateResponse(@Optional @CitrusResource TestCaseRunner runner,
                                       String id) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n \"message\": \"Duck with id = " + id + " is updated\"\n}"));
    }

    public void validateWoodResponseEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{}"));
    }

    public void validateRubberResponse(@Optional @CitrusResource TestCaseRunner runner,
                                           String color,
                                           String material,
                                           String sound,
                                           String wingsState) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"color\": \"" + color + "\",\n" +
                        "  \"height\": \"@ignore@\",\n" +
                        "  \"material\": \"" + material + "\",\n" +
                        "  \"sound\": \"" + sound + "\",\n" +
                        "  \"wingsState\": \"" + wingsState + "\"\n" +
                        "}"));
    }



    // Проверка четности ID
    public void validateIdEven(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 != 0) {
                throw new RuntimeException("Ожидался ЧЁТНЫЙ ID, получен НЕЧЁТНЫЙ: " + id);
            }
        });
    }

    // Проверка нечетности ID
    public void validateIdOdd(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 == 0) {
                throw new RuntimeException("Ожидался НЕЧЁТНЫЙ ID, получен ЧЁТНЫЙ: " + id);
            }
        });
    }
}