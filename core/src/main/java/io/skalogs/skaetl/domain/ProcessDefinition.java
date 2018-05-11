package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class ProcessDefinition {

    public abstract String getIdProcess();
    public abstract String getName();
}
