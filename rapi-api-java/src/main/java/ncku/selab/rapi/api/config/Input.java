package ncku.selab.rapi.api.config;

import lombok.*;

import java.util.ArrayList;

@Builder(toBuilder = true, setterPrefix = "with")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class Input {
    @NonNull
    private ArrayList<String> testSuites;
    @Builder.Default
    private ArrayList<String> variables = new ArrayList<String>();
    @Builder.Default
    private ArrayList<String> dataDriven = new ArrayList<String>();
}
