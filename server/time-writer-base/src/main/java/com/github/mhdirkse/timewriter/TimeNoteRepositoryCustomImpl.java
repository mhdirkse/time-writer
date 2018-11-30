package com.github.mhdirkse.timewriter;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.github.mhdirkse.timewriter.model.TimeNote;

public class TimeNoteRepositoryCustomImpl implements TimeNoteRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TimeNote> findByPeriod(Long userId, Instant startTime, Instant endTime) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TimeNote> cq = cb.createQuery(TimeNote.class);
        Root<TimeNote> timeNoteList = cq.from(TimeNote.class);
        cq.where(cb.and(
                cb.equal(timeNoteList.get("userId"), userId),
                cb.between(timeNoteList.get("timestamp"), startTime, endTime)));
        TypedQuery<TimeNote> q = entityManager.createQuery(cq);
        return q.getResultList();
    }
}
