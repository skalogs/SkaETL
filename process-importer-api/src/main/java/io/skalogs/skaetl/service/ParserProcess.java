package io.skalogs.skaetl.service;


import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;

public interface ParserProcess {

    ParserResult process(String value, ProcessParser processParser);
}
