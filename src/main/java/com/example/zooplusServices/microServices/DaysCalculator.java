package com.example.zooplusServices.microServices;

import java.time.LocalDate;

public interface DaysCalculator {
    LocalDate subtractDaysSkippingWeekends(LocalDate date, int days);
}
