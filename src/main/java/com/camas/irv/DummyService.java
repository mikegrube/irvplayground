package com.camas.irv;


import com.camas.irv.race.Race;
import com.camas.irv.voter.Voter;

public interface DummyService {

	Race makeDummyRace();

	void createRanks(Voter voter);

}
