package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionSwimTest extends DuckActionsClient {

    // TODO: SHIFT-AQA-3
    @Test(description = "Проверка того, что уточка плывет с существующим id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("FIXED");

        createDuckAndExtractId(runner, duck);
        duckSwim(runner, "${duckId}");
        validateWithString(runner, "{\n \"message\": \"Paws are not found ((((\"\n}");
    }

    @Test(description = "Проверка того, что уточка плывет с НЕсуществующим id")
    @CitrusTest
    public void invalidSwim(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack")
                .setWingsState("FIXED");

        createDuckAndExtractId(runner, duck);
        duckSwim(runner, "54");
        validateWithResource(runner, "duck_swim_response.json");
    }
}
