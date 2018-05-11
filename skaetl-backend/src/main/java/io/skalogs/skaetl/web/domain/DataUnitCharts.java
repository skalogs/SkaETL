package io.skalogs.skaetl.web.domain;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataUnitCharts {
    private String borderColor;
    private String label;
    private List<DataCapture> data;
}
