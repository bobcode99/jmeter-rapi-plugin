package ncku.selab.rapi.api.config;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder(setterPrefix = "with")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public abstract class WebDriverCommonConfig {
    @Builder.Default
    private String type = "selenium";
    @NonNull
    private String serverUrl;
}
