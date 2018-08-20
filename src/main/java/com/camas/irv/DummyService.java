package com.camas.irv;


import com.camas.irv.race.Race;
import com.camas.irv.voter.Voter;

public interface DummyService {

	Race makeDummyRace();

	void createVoters(Race race);

	void createRanks(Voter voter);

}
