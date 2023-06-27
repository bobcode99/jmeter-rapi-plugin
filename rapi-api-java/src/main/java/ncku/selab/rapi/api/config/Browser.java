package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Browser {
    private final boolean active = true;
    @Builder.Default
    private boolean keepSessionAlive = false;
    @Builder.Default
    private Map<String, Object> capability = Collections.emptyMap();
}
