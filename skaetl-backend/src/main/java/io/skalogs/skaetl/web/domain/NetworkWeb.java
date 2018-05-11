package io.skalogs.skaetl.web.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NetworkWeb {
    @Builder.Default
    private List<NetworkNodeWeb> nodeList = new ArrayList<>();
    @Builder.Default
    private List<NetworkLinksWeb> linksList = new ArrayList<>();
}
