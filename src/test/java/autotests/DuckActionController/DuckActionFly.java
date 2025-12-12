package autotests.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckActionResponse;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Duck Action Controller")
@Feature("Fly")
public class DuckActionFly extends DuckActionsClient {

    @Test(description = "Проверка полета утки с Active крыльями")
    @CitrusTest
    public void activeWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("ACTIVE");

        runner.variable("duckId", "9911919");
        createDuckByDB(runner, duck);
        duckFly(runner, "${duckId}");
        validateWithString(runner, "{\n \"message\": \"I am flying :)\"\n}");
        deleteDuckByDB(runner, "${duckId}");
    }

    @Test(description = "Проверка полета утки с Fixed крыльями")
    @CitrusTest
    public void fixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("FIXED");

        runner.variable("duckId", "1243");
        createDuckByDB(runner, duck);
        duckFly(runner, "${duckId}");
        validateWithResource(runner, "duck_fly_fixed.json");
        deleteDuckByDB(runner, "${duckId}");
    }

    @Test(description = "Проверка полета утки с Undefined крыльями")
    @CitrusTest
    public void undefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("UNDEFINED");

        runner.variable("duckId", "167");
        createDuckByDB(runner, duck);
        duckFly(runner, "${duckId}");
        DuckActionResponse expectedResponse = new DuckActionResponse()
                .setMessage("Wings are not detected :(");
        validateWithPayload(runner, expectedResponse);
        deleteDuckByDB(runner, "${duckId}");
    }
}