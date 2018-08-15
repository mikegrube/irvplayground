package com.camas.irv.candidate;

import com.camas.irv.race.Race;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {

	List<Candidate> findByRace(Race race);

	List<Candidate> findByOrderByName();
}

