package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterVariables;

public class RapiRunnerConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {

    private static final String RUNNER_EXE_PATH = "RUNNER_EXE_PATH";
    private static final String SELENIUM_PORT = "SELENIUM_PORT";

    public String getRunnerExePath() {
        return getPropertyAsString(RUNNER_EXE_PATH);
    }

    public void setRunnerExePath(String path) {
        setProperty(RUNNER_EXE_PATH, path);
    }

    public String getSeleniumPort() {
        return getPropertyAsString(SELENIUM_PORT);
    }

    public void setSeleniumPort(String port) {
        setProperty(SELENIUM_PORT, port);
    }


    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

    }

    @Override
    public void threadStarted() {

//        getThreadContext().getVariables().putObject("RUNNER_EXE_PATH", getRunnerExePath());
//        getThreadContext().getVariables().putObject("SELENIUM_PORT", getSeleniumPort());

        JMeterVariables variables = new JMeterVariables();
        variables.put("RUNNER_EXE_PATH_FOR_RAPI_USE", getRunnerExePath());
        variables.put("SELENIUM_URL_FOR_RAPI_USE", getSeleniumPort());

        getThreadContext().setVariables(variables);

    }

    @Override
    public void threadFinished() {

    }

}
