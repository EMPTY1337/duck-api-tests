package autotests.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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

        createDuckAndExtractId(runner, duck);
        duckFly(runner, "${duckId}");
        validateResponseWithMessage(runner, "{\n \"message\": \"I am flying :)\"\n}");
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

        createDuckAndExtractId(runner, duck);
        duckFly(runner, "${duckId}");
        validateResponseWithMessage(runner, "{\n \"message\": \"I can not fly :C\"\n}");
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

        createDuckAndExtractId(runner, duck);
        duckFly(runner, "${duckId}");
        validateResponseWithMessage(runner, "{\n \"message\": \"Wings are not detected :(\"\n}");
    }
}
