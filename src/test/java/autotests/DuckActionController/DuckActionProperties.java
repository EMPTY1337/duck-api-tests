package autotests.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionProperties extends DuckActionsClient {

    // TODO: SHIFT-AQA-1
    @Test(description = "Получение свойств утки с четным ID и материалом wood")
    @CitrusTest
    public void testPropertiesEvenIdWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("wood")
                .setSound("quack")
                .setWingsState("FIXED");

        createDuckAndExtractId(runner, duck);
        validateIdEven(runner);

        getDuckActionProperties(runner, "${duckId}");
        validateWoodResponseEmpty(runner);
    }

    @Test(description = "Получение свойств утки с нечетным ID и материалом rubber")
    @CitrusTest
    public void testPropertiesOddIdRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("FIXED");

        createDuckAndExtractId(runner, duck);
        validateIdOdd(runner);

        getDuckActionProperties(runner, "${duckId}");
        validateRubberResponse(runner, "yellow", "rubber", "quack", "FIXED");
    }
}