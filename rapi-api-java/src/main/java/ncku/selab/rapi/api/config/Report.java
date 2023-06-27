package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true, setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Report {
    @Builder.Default
    private String type = "json";
    @Builder.Default
    private int snapshot = 0;
    @Builder.Default
    private int snapshotQuality = 20;
}
