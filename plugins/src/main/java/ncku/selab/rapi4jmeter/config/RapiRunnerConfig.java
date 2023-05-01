package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterVariables;

public class RapiRunnerConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {

    private static final String RUNNER_EXE_PATH = "RUNNER_EXE_PATH";
    private static final String SELENIUM_URL = "SELENIUM_URL";

    public String getRunnerExePath() {
        return getPropertyAsString(RUNNER_EXE_PATH);
    }

    public void setRunnerExePath(String path) {
        setProperty(RUNNER_EXE_PATH, path);
    }

    public String getSeleniumURL() {
        return getPropertyAsString(SELENIUM_URL);
    }

    public void setSeleniumPort(String port) {
        setProperty(SELENIUM_URL, port);
    }


    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

    }

    @Override
    public void threadStarted() {

        JMeterVariables variables = new JMeterVariables();
        variables.put("RUNNER_EXE_PATH_FOR_RAPI_USE", getRunnerExePath());
        variables.put("SELENIUM_URL_FOR_RAPI_USE", getSeleniumURL());

        getThreadContext().setVariables(variables);

    }

    @Override
    public void threadFinished() {

    }

}
