package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessFilter {
    //usecase de base
    //TODO EL ou truc basic (><!=&|) ?
    private String criteria;
    private String name;
    private String idFilter;
}
