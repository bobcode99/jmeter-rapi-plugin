package team.sideex.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import team.sideex.JMeterPluginUtils;

import javax.swing.*;

public class SideexRunnerConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {

    private static final String RUNNER_EXE_PATH = "RUNNER_EXE_PATH";
    private static final String SELENIUM_PORT = "SELENIUM_PORT";

    public void setRunnerExePath(String path) {
        setProperty(RUNNER_EXE_PATH, path);
    }

    public void setSeleniumPort(String port) {
        setProperty(SELENIUM_PORT, port);
    }

    public String getRunnerExePath() {
        return getPropertyAsString(RUNNER_EXE_PATH);
    }
    public String getSeleniumPort() {
        return getPropertyAsString(SELENIUM_PORT);
    }
    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        System.out.println("iterationStart");
    }

    @Override
    public void threadStarted() {
        System.out.println("threadStarted");

        getThreadContext().getVariables().putObject("RUNNER_EXE_PATH", getRunnerExePath());

        getThreadContext().getVariables().putObject("SELENIUM_PORT", getSeleniumPort());

    }

    @Override
    public void threadFinished() {
        System.out.println("threadFinished");

    }

}
