/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
    private Input input = new Input();
    private Report report = new Report();
    private WebDriver webdriver = new WebDriver();
    private Play play = new Play();

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public WebDriver getWebdriver() {
        return webdriver;
    }

    public void setWebdriver(WebDriver webdriver) {
        this.webdriver = webdriver;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
