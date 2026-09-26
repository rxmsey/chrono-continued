package com.chrono;

import java.time.LocalDate;

public final class HistoricalCutoff
{
    public static final LocalDate OSRS_BACKUP = LocalDate.of(2007, 8, 10);

    private HistoricalCutoff() {}

    public static boolean isSupported(LocalDate date)
    {
        return date != null && !date.isAfter(OSRS_BACKUP);
    }
}
