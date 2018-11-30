package com.github.mhdirkse.timewriter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mhdirkse.timewriter.model.TimeNote;

public interface TimeNoteRepository
    extends JpaRepository<TimeNote, Long>, TimeNoteRepositoryCustom {
}
