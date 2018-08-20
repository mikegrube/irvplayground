package com.camas.irv.race;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.voter.Voter;

import java.util.List;

public interface RaceService {

	List<Race> list();

	Race get(Long id);

	Race save(Race race);

	void delete(Long id);

	List<Candidate> candidatesForRace(Race race);

	int candidateCountForRace(Race race);

	List<Voter> votersForRace(Race race);

	RaceResult tabulate(Race race);

	void revote(Race race);

	void removeCandidateFromRace(Candidate candidate);
}
