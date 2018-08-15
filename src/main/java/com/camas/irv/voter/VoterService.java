package com.camas.irv.voter;


import com.camas.irv.race.Race;

import java.util.List;

public interface VoterService {

	Iterable<Voter> list();

	Voter get(Long id);

	Voter save(Voter voter);

	void delete(Long id);

	Iterable<Race> availableRaces();

	List<Voter> votersForRaceWithRanks(Race race);

	void makeDummyVoters(Race race);
}
