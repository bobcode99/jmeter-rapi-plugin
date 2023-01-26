package team.sideex.reporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import team.sideex.sampler.TestSamplerResult;
import team.sideex.sampler.TestSamplerResult;

public class TestSamplerResultCollector extends AbstractListenerElement implements SampleListener, Clearable, Serializable,
        TestStateListener, Remoteable, NoThreadClone {

    public ArrayList<String> allResponseMessage = new ArrayList<String>(); // Create an ArrayList object

    public boolean isSampleWanted(SampleResult result){
        if(result instanceof TestSamplerResult){
            System.out.println("isSampleWanted true");
            return true;
        }
        System.out.println("isSampleWanted false");
        return false;
    }

    @Override
    public void testEnded() {
        System.out.println("testEnded 1");
        System.out.println("Here's allResponseMessage: " + allResponseMessage);
    }

    @Override
    public void testEnded(String host) {
        System.out.println("testEnded 2");

    }

    @Override
    public void testStarted() {
        System.out.println("testStarted 1");

    }

    @Override
    public void testStarted(String host) {
        System.out.println("testStarted 2");

    }

    @Override
    public void clearData() {
        System.out.println("clearData");
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        SampleResult result = event.getResult();
        System.out.println();
        if(isSampleWanted(result)){
//            long during = result.getEndTime() - result.getStartTime();
//            System.out.println(result.getSampleLabel() + ":" + during + "ms");
            System.out.println("sampleOccurred !!!");
            System.out.println("result: " + result);
            System.out.println("result getResponseData: " + Arrays.toString(result.getResponseData()));
            System.out.println("result getResponseDataAsString: " + result.getResponseDataAsString());
            allResponseMessage.add(result.getResponseDataAsString());


        }

    }

    @Override
    public void sampleStarted(SampleEvent event) {
        System.out.println("sampleStarted");
    }

    @Override
    public void sampleStopped(SampleEvent event) {
        System.out.println("sampleStopped");

    }

}
