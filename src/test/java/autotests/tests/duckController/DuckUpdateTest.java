package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckActionResponse;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends DuckActionsClient {

    @Test(description = "Изменить цвет и высоту уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(6)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);

        updateDuck(runner, "${duckId}", "red", 15.5, "rubber", "quack", "ACTIVE");
        validateWithString(runner, "{\"message\": \"Duck with id = ${duckId} is updated\"}");
    }

    @Test(description = "Изменить цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("red")
                .setHeight(7)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);

        updateDuck(runner, "${duckId}", "yellow", 0.03, "rubber", "quack-quack-quack", "ACTIVE");
        DuckActionResponse expectedResponse = new DuckActionResponse()
                .setMessage("Duck with id = ${duckId} is updated");
        validateWithPayload(runner, expectedResponse);
    }
}