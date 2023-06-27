package ncku.selab.rapi.api.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ncku.selab.rapi.api.config.utils.BrowsersSessionsPropertySerializer;

@SuperBuilder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class WebDriverSessionConfig extends WebDriverCommonConfig {
    @JsonSerialize(using = BrowsersSessionsPropertySerializer.class)
    private Session sessions;
}
