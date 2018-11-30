package com.github.mhdirkse.timewriter;

import java.time.Instant;
import java.util.List;

import com.github.mhdirkse.timewriter.model.TimeNote;

public interface TimeNoteRepositoryCustom {
    List<TimeNote> findByPeriod(Long userId, Instant startTime, Instant endTime);
}
