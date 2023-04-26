package team.sideex.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterVariables;

public class ChromeConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {
    private static final String BROWSER_ADDITIONAL_ARGS = "BROWSER_ADDITIONAL_ARGS";

    public String getBrowserAdditionalArgs() {
        return getPropertyAsString(BROWSER_ADDITIONAL_ARGS);
    }

    public void setBrowserAdditionalArgs(String args) {
        setProperty(BROWSER_ADDITIONAL_ARGS, args);
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {

    }

    @Override
    public void threadStarted() {
        JMeterVariables variables = new JMeterVariables();

        variables.put("BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_GOOGLE_CHROME", getBrowserAdditionalArgs());

    }

    @Override
    public void threadFinished() {

    }
}
