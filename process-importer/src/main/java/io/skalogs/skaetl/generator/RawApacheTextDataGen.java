package io.skalogs.skaetl.generator;

import lombok.Builder;

@Builder
public class RawApacheTextDataGen {
    public String type;
    public String timestamp;
    public String project;
    public String message;
}