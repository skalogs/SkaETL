package io.skalogs.skaetl.domain;

public enum ElasticsearchBackoffPolicy {
    NO_BACKOFF,
    CONSTANT_BACKOFF,
    EXPONENTIAL_BACKOFF
}
