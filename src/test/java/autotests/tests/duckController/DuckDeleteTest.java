package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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

        createDuckAndExtractId(runner, duck);

        deleteDuck(runner, "${duckId}");
        validateResponseStatusOk(runner);
    }

    @Test(description = "Удаление уточки с несуществующим ID")
    @CitrusTest
    public void deleteNonExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        deleteDuck(runner, "999999");
        validateResponseStatusNotFound(runner);
    }
}