package com.github.mhdirkse.timewriter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.mhdirkse.timewriter.model.TimeNote;
import com.github.mhdirkse.timewriter.model.UserInfo;

@RestController
@RequestMapping("/api/timenotes")
@Transactional(
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = Throwable.class)
public class TimeNoteController {
    private UserInfoRepository userInfoRepository;
    private TimeNoteRepository timeNoteRepository;

    TimeNoteController(
            UserInfoRepository userInfoRepository,
            TimeNoteRepository timeNoteRepository) {
        this.userInfoRepository = userInfoRepository;
        this.timeNoteRepository = timeNoteRepository;
    }

    @GetMapping("/{startTime}/{endTime}")
    public List<TimeNote> list(
            @PathVariable Instant startTime,
            @PathVariable Instant endTime,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        UserInfo user = userInfoRepository.getOne(loggedUser.getId());
        return timeNoteRepository.findByPeriod(user.getId(), startTime, endTime); 
    }

    @PostMapping
    public ResponseEntity<TimeNote> add(
            @RequestBody TimeNote timeNote,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        if(!timeNote.getUserId().equals(loggedUser.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(timeNote.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(timeNoteRepository.save(timeNote), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeNote> modify(
            @PathVariable Long id,
            @RequestBody TimeNote timeNote,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        if((timeNote.getId() == null) || (!timeNote.getId().equals(id))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!timeNote.getUserId().equals(loggedUser.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(timeNoteRepository.save(timeNote), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeNote> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        Optional<TimeNote> toDelete = timeNoteRepository.findById(id);
        if(!toDelete.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!toDelete.get().getUserId().equals(loggedUser.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        timeNoteRepository.delete(toDelete.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
