package team.sideex.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterVariables;

public class SideexRunnerConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {

    private static final String RUNNER_EXE_PATH = "RUNNER_EXE_PATH";
    private static final String SELENIUM_PORT = "SELENIUM_PORT";
    private static final String DEV_SHM_USAGE = "DEV_SHM_USAGE";

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

    public boolean getDevShmUsage(){
        return getPropertyAsBoolean(DEV_SHM_USAGE);
    }

    public void setDevShmUsage(boolean enabled){
        setProperty(DEV_SHM_USAGE,enabled);
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

    }

    @Override
    public void threadStarted() {

//        getThreadContext().getVariables().putObject("RUNNER_EXE_PATH", getRunnerExePath());
//        getThreadContext().getVariables().putObject("SELENIUM_PORT", getSeleniumPort());

        JMeterVariables variables = new JMeterVariables();
        variables.put("RUNNER_EXE_PATH_FOR_SIDEEX_USE", getRunnerExePath());
        variables.put("SELENIUM_PORT_FOR_SIDEEX_USE", getSeleniumPort());
        variables.put("STATUS_DEV_SHM_USAGE_FOR_SIDEEX_USE", String.valueOf(getDevShmUsage()));

//        System.out.println("variables.get RUNNER_EXE_PATH:" + variables.get("RUNNER_EXE_PATH_FOR_SIDEEX_USE"));
//        System.out.println(("variables.get SELENIUM_PORT:" +variables.get("SELENIUM_PORT_FOR_SIDEEX_USE")));
        getThreadContext().setVariables(variables);

    }

    @Override
    public void threadFinished() {

    }

}
