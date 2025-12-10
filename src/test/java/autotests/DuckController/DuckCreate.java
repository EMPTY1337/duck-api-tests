package autotests.DuckController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreate extends DuckActionsClient {

    @Test(description = "Создать утку с material = rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack-quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);
    }
    @Test(description = "Создать утку с material = wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("brown")
                .setHeight(10.5)
                .setMaterial("wood")
                .setSound("quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);
    }
}