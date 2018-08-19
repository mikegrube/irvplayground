package com.camas.irv.voter;


import com.camas.irv.candidate.Candidate;
import com.camas.irv.race.Race;
import com.camas.irv.rank.Rank;

import java.util.List;

public interface VoterService {

	Iterable<Voter> list();

	Voter get(Long id);

	Voter save(Voter voter);

	void delete(Long id);

	Iterable<Race> availableRaces();

	List<Voter> votersForRaceWithRanks(Race race);

	void makeDummyVoters(Race race);

	int raceCandidateCt(Race race);

	List<Rank> ranksForVoter(Voter voter);

	List<Candidate> candidatesForRace(Race race);

	void applyRanks(Voter voter);

	boolean validate(Voter voter);

	Rank rankForOrder(Voter voter, int order);

	List<Rank> ranksForVoterByRankValue(Voter voter);

	void revote(Race race);
}
