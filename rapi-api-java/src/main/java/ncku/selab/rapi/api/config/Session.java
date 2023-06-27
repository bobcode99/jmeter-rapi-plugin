package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Session {
    @Builder.Default
    private String sessionId = "";
    @Builder.Default
    private boolean keepSessionAlive = false;
}
