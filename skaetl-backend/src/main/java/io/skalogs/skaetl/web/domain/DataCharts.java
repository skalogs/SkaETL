package io.skalogs.skaetl.web.domain;


import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataCharts {

    private List<DataUnitCharts> datasets;

}
