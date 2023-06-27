package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Builder(toBuilder = true, setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WebDriver {
    @Builder.Default
    private ArrayList<WebDriverCommonConfig> configs = new ArrayList<WebDriverCommonConfig>();
    @Builder.Default
    private Map<String, String> i18n = Collections.emptyMap();
}
