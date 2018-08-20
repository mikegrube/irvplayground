package com.camas.irv.candidate;

import com.camas.irv.race.Race;

import java.util.List;

public interface CandidateService {

	Iterable<Candidate> list();

	Candidate get(Long id);

	Candidate save(Candidate candidate);

	void delete(Long id);

	Iterable<Affiliation> availableAffiliations();

	Iterable<Race> availableRaces();

	List<Candidate> candidatesforRace(Race race);

	Race findRace(Long raceId);

}
