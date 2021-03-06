package com.camas.irv.rank;


import com.camas.irv.candidate.Candidate;
import com.camas.irv.voter.Voter;

import java.util.List;

public interface RankService {

	Iterable<Rank> list();

	Rank get(Long id);

	Rank save(Rank rank);

	void delete(Long id);

	Iterable<Candidate> availableCandidates();

	Iterable<Voter> availableVoters();

	List<Rank> ranksForVoter(Voter voter);

	List<Rank> ranksForVoterByRank(Voter voter);

	void deleteByVoter(Voter voter);
}
