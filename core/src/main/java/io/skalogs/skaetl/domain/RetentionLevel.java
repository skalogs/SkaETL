package io.skalogs.skaetl.domain;

public enum RetentionLevel {
    day(1),
    week(7),
    month(30),
    quarter(90),
    year(365),
    two_years(730),
    five_years(1825);

    public final int nbDays;

    RetentionLevel(int nbDay) {
        this.nbDays = nbDay;
    }
}
