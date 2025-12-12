package autotests.DuckActionController;

import autotests.clients.DuckActionsClient;
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
@Feature("Quack")
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

        runner.variable("duckId", "7");
        createDuckByDB(runner, duck);
        duckQuack(runner, "${duckId}", "1", "2");
        validateWithString(runner, "{\n \"sound\": \"quack-quack, quack-quack\"\n}");
        deleteDuckByDB(runner, "${duckId}");
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

        runner.variable("duckId", "26");
        createDuckByDB(runner, duck);
        duckQuack(runner, "${duckId}", "1", "1");
        validateWithResource(runner, "duck_quack_response.json");
        deleteDuckByDB(runner, "${duckId}");
    }
}