package autotests.clients;

import autotests.payload.DuckPropertiesResponse;
import autotests.tests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;

public class DuckActionsClient extends BaseTest {

    @Step("Создать утку и извлечь ID")
    public void createDuckAndExtractId(@Optional @CitrusResource TestCaseRunner runner,
                                       Object duckProperties) {
        sendPostRequest(runner, "/api/duck/create", duckProperties);
        receiveResponseWithExtract(runner, HttpStatus.OK, "$.id", "duckId");
    }

    @Step("Создать утку через БД")
    public void createDuckByDB(@Optional @CitrusResource TestCaseRunner runner,
                               DuckPropertiesResponse duck) {
        String sqlStmt = "INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE) VALUES (${duckId}, '" +
                duck.getColor() + "', " + duck.getHeight() + ", '" +
                duck.getMaterial() + "', '" + duck.getSound() + "', '" +
                duck.getWingsState() + "')";
        executeSql(runner, sqlStmt);
    }

    @Step("Удалить утку через БД")
    public void deleteDuckByDB(@Optional @CitrusResource TestCaseRunner runner,
                               String id) {
        executeSql(runner, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Step("Проверить данные утки в БД")
    public void validateDuckByDB(@Optional @CitrusResource TestCaseRunner runner,
                                 String id,
                                 DuckPropertiesResponse expected) {
        String sql = "SELECT COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE FROM DUCK WHERE ID = " + id;
        validateDuckProperties(runner, sql, expected);
    }

    @Step("Проверить данные утки в БД")
    public void validateDuckInDb(@Optional @CitrusResource TestCaseRunner runner,
                                 String id,
                                 DuckPropertiesResponse expected) {
        String sql = "SELECT COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE FROM DUCK WHERE ID = " + id;
        validateDuckProperties(runner, sql, expected);
    }

    @Step("Проверить, что утка не существует в БД")
    public void validateDuckNotInDb(@Optional @CitrusResource TestCaseRunner runner,
                                    String id) {
        String sql = "SELECT COUNT(*) AS count FROM DUCK WHERE ID = " + id;
        validateDuckCount(runner, sql, "0");
    }

    @Step("Проверить, что ID четный")
    public void validateIdEven(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 != 0) {
                throw new RuntimeException("Ожидался ЧЁТНЫЙ ID, получен НЕЧЁТНЫЙ: " + id);
            }
        });
    }

    @Step("Проверить, что ID нечетный")
    public void validateIdOdd(@Optional @CitrusResource TestCaseRunner runner) {
        runner.run(context -> {
            long id = Long.parseLong(context.getVariable("duckId"));
            if (id % 2 == 0) {
                throw new RuntimeException("Ожидался НЕЧЁТНЫЙ ID, получен ЧЁТНЫЙ: " + id);
            }
        });
    }

    @Step("Валидировать ответ на обновление")
    public void validateUpdateResponse(@Optional @CitrusResource TestCaseRunner runner,
                                       String id) {
        String body = "{\n \"message\": \"Duck with id = " + id + " is updated\"\n}";
        validateWithString(runner, body);
    }

    @Step("Валидировать пустой ответ для wood")
    public void validateWoodResponseEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        validateWithString(runner, "{}");
    }

    @Step("Валидировать ответ для rubber")
    public void validateRubberResponse(@Optional @CitrusResource TestCaseRunner runner,
                                       String color,
                                       String material,
                                       String sound,
                                       String wingsState) {
        String body = "{\n" +
                "  \"color\": \"" + color + "\",\n" +
                "  \"height\": \"@ignore@\",\n" +
                "  \"material\": \"" + material + "\",\n" +
                "  \"sound\": \"" + sound + "\",\n" +
                "  \"wingsState\": \"" + wingsState + "\"\n" +
                "}";
        validateWithString(runner, body);
    }

    // Добавленные методы действий из обрезанного кода
    @Step("Плавание утки")
    public void duckSwim(@Optional @CitrusResource TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/swim?id=" + id);
    }

    @Step("Получение свойств утки")
    public void getDuckActionProperties(@Optional @CitrusResource TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/properties?id=" + id);
    }

    @Step("Полет утки")
    public void duckFly(@Optional @CitrusResource TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/fly?id=" + id);
    }

    @Step("Кряканье утки")
    public void duckQuack(@Optional @CitrusResource TestCaseRunner runner, String id, String repeat, String quackLength) {
        sendGetRequest(runner, "/api/duck/action/quack?id=" + id + "&repeat=" + repeat + "&quackLength=" + quackLength);
    }

    @Step("Удаление утки")
    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, "/api/duck/delete?id=" + id);
    }

    @Step("Обновление утки")
    public void updateDuck(@Optional @CitrusResource TestCaseRunner runner, String id, String color, double height, String material, String sound, String wingsState) {
        DuckPropertiesResponse payload = new DuckPropertiesResponse()
                .setColor(color)
                .setHeight(height)
                .setMaterial(material)
                .setSound(sound)
                .setWingsState(wingsState);
        sendPutRequest(runner, "/api/duck/update?id=" + id, payload);
    }
}