package autotests.tests;

import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.DelegatingPayloadVariableExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public abstract class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void sendGetRequest(TestCaseRunner runner, String path) {
        runner.$(http()
                .client(duckService)
                .send()
                .get(path));
    }

    protected void sendPostRequest(TestCaseRunner runner, String path, Object payload) {
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .type(MessageType.JSON)
                .contentType("application/json")
                .body(new ObjectMappingPayloadBuilder(payload, objectMapper)));
    }

    protected void sendPutRequest(TestCaseRunner runner, String path, Object payload) {
        runner.$(http()
                .client(duckService)
                .send()
                .put(path)
                .message()
                .type(MessageType.JSON)
                .contentType("application/json")
                .body(new ObjectMappingPayloadBuilder(payload, objectMapper)));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, String path) {
        runner.$(http()
                .client(duckService)
                .send()
                .delete(path));
    }

    protected void receiveResponseWithExtract(TestCaseRunner runner, HttpStatus status, String expression, String variable) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression(expression, variable))
                .timeout(10000));
    }

    protected void validateWithString(TestCaseRunner runner, String body) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    protected void validateWithResource(TestCaseRunner runner, String resourcePath) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(resourcePath)));
    }

    protected void validateWithPayload(TestCaseRunner runner, Object payload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(payload, objectMapper)));
    }

    protected void validateResponseStatusOk(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK));
    }

    protected void validateResponseStatusNotFound(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    protected void executeSql(TestCaseRunner runner, String sqlStmt) {
        runner.$(sql(testDb).statement(sqlStmt));
    }

    protected void validateDuckProperties(TestCaseRunner runner, String sql, DuckPropertiesResponse expected) {
        runner.$(query(testDb)
                .statement(sql)
                .validate("COLOR", expected.getColor())
                .validate("HEIGHT", String.valueOf(expected.getHeight()))
                .validate("MATERIAL", expected.getMaterial())
                .validate("SOUND", expected.getSound())
                .validate("WINGS_STATE", expected.getWingsState()));
    }

    protected void validateDuckCount(TestCaseRunner runner, String sql, String count) {
        runner.$(query(testDb)
                .statement(sql)
                .validate("count", count));
    }
}