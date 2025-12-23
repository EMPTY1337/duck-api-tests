package autotests.tests.DuckController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck Controller")
@Feature("Delete")
public class DuckDeleteTest extends DuckActionsClient {

    @Test(description = "Успешное удаление уточки")
    @CitrusTest
    public void successDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(0.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("ACTIVE");

        runner.variable("duckId", "223556");
        createDuckByDB(runner, duck);
        deleteDuck(runner, "${duckId}");
        validateResponseStatusOk(runner);
        validateDuckNotInDb(runner, "${duckId}");
    }

    @Test(description = "Удаление уточки с несуществующим ID")
    @CitrusTest
    public void deleteNonExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "8888811");
        deleteDuck(runner, "999999");
        validateResponseStatusNotFound(runner);
    }
}