package ncku.selab.rapi.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true, setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Play {
    @Builder.Default
    private int mode = 2;
    @Builder.Default
    private String entry = "";
    @Builder.Default
    private int autoWaitTimeout = 30;
    @Builder.Default
    private int speed = 5;
    @Builder.Default
    private Period period = new Period();
}
