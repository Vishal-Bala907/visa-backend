package com.visa.services.imple;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateService {
	
	public Long getPrevWeekTimestamp() {
		long prevWeek = LocalDate.now().minusDays(7).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
		return prevWeek;
	}
	public Long getPrevMonthTimestamp() {
		long prevWeek = LocalDate.now().minusDays(30).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
		return prevWeek;
	}
	public Long getPrevYearTimestamp() {
		long prevWeek = LocalDate.now().minusDays(365).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
		return prevWeek;
	}
	
}
