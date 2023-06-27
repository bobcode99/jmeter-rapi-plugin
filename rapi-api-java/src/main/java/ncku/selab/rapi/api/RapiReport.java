package ncku.selab.rapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class RapiReport {
    private JsonNode json;
    private String html;

    RapiReport(String stdout, String reportType) throws JsonProcessingException {
        parseReport(stdout, reportType);
    }

    private void parseReport(String input, String reportType) throws JsonProcessingException {
        String jsonStartToken = "INFO Start to send json report to api";
        String jsonEndToken = "INFO End of sending json report to api";
        String htmlStartToken = "INFO Start to send html report to api";
        String htmlEndToken = "INFO End of sending html report to api";
        if (Objects.equals(reportType, "all") || Objects.equals(reportType, "json")) {
            int start = input.indexOf(jsonStartToken) + jsonStartToken.length();
            int end = input.indexOf(jsonEndToken, start);
            if (end != -1) {
                String jsonStr = input.substring(start, end);
                ObjectMapper objectMapper = new ObjectMapper();
                this.json = objectMapper.readTree(jsonStr);
            } else {
                this.json = null;
            }
        }
        if (Objects.equals(reportType, "all") || Objects.equals(reportType, "html")) {
            int start = input.indexOf(htmlStartToken) + htmlStartToken.length();
            int end = input.indexOf(htmlEndToken, start);
            if (end != -1) {
                this.html = input.substring(start, end);
            } else {
                this.html = null;
            }
        }
    }

    public JsonNode getJson() {
        return json;
    }

    public String getJsonString() {
        return json.toString();
    }

    public String getHtml() {
        return html;
    }
}
