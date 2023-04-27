package team.sideex.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class EdgeConfig extends ConfigTestElement implements LoopIterationListener, ThreadListener {
    private static final String BROWSER_ADDITIONAL_ARGS = "BROWSER_ADDITIONAL_ARGS";

    public String getBrowserAdditionalArgs() {
        return getPropertyAsString(BROWSER_ADDITIONAL_ARGS);
    }

    public void setBrowserAdditionalArgs(String args) {
        setProperty(BROWSER_ADDITIONAL_ARGS, args);
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
//        System.out.println("EDGE iterationStart getBrowserAdditionalArgs(): " + getBrowserAdditionalArgs());
        JMeterContextService.getContext().getVariables().put("BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_EDGE_CONFIG", getBrowserAdditionalArgs());
    }

    @Override
    public void threadStarted() {

    }

    @Override
    public void threadFinished() {

    }
}
