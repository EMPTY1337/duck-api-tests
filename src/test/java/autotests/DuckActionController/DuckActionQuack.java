package autotests.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payload.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckActionQuack extends DuckActionsClient {

    @Test(description = "Проверка кряканья утки с корректным нечетным ID и корректным звуком")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("rubber")
                .setSound("quack-quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);
        validateIdOdd(runner);

        duckQuack(runner, "${duckId}", "1", "2");
        validateWithString(runner, "{\n \"sound\": \"quack-quack, quack-quack\"\n}");
    }

    // TODO: SHIFT-AQA-2
    @Test(description = "Проверка кряканья утки с корректным четным ID и корректным звуком")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        DuckPropertiesResponse duck = new DuckPropertiesResponse()
                .setColor("yellow")
                .setHeight(8.03)
                .setMaterial("wood")
                .setSound("quack-quack")
                .setWingsState("ACTIVE");

        createDuckAndExtractId(runner, duck);
        validateIdEven(runner);

        duckQuack(runner, "${duckId}", "1", "1");
        validateWithResource(runner, "duck_quack_response.json");
    }
}