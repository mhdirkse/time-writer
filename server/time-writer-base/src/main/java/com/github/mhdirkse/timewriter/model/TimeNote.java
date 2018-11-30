package com.github.mhdirkse.timewriter.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TimeNotes")
@Getter
@Setter
public class TimeNote implements Serializable {
    private static final long serialVersionUID = -6137166475760436163L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Instant timestamp;

    private String message;
}
