package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Period {
    @Builder.Default
    private int time = -1;
    @Builder.Default
    private int maxNum = -1;
}
