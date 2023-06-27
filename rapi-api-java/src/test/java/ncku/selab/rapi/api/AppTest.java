package ncku.selab.rapi.api;

import ncku.selab.rapi.api.config.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
@Ignore // Remove ignore if you need run unit test.
public class AppTest
{
    /**
     * Rigorous Test :-)
     * @throws Exception
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException, InterruptedException, Exception
    {
        ArrayList<String> testSuites = new ArrayList<>();
        testSuites.add("src/test/resources/sample-test-suite.json");
        Input input = Input.builder().withTestSuites(testSuites).build();

        Map<String, Object> caps = new HashMap<>();
        caps.put("browserName", "chrome");

        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();

        ArrayList<String> browserArgsList = new ArrayList<String>(Arrays.asList("--disable-gpu", "--disable-dev-shm-usage", "disable-web-security", "disable-site-isolation-trials"));

        browserArgs.put("args", browserArgsList);
        caps.put("goog:chromeOptions", browserArgs);
        ArrayList<WebDriverCommonConfig> service = new ArrayList<WebDriverCommonConfig>();
        service.add(
                WebDriverBrowserConfig.builder()
                        .withBrowsers(Browser.builder().withCapability(caps).build())
                        .withServerUrl("http://127.0.0.1:4444").build());

        Config config = Config.builder().withInput(input)
                .withWebdriver(WebDriver.builder().withConfigs(service).build()).build();

        String configString = config.toString();
        System.out.println("Rapi config: " + configString);

        Rapi rapi = new Rapi("/path/to/rapi-runner", config);
        RapiReport report = rapi.run();
        System.out.println(report.getJson());
    }
}
