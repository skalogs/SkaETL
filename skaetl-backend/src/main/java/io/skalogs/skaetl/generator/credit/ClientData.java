package io.skalogs.skaetl.generator.credit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ClientData {
    private String firstName;
    private String lastName;
    private String email;
}
