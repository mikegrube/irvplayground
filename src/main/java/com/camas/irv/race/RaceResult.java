package com.camas.irv.race;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.rank.Rank;
import com.camas.irv.voter.Voter;
import com.camas.irv.voter.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class RaceResult {

	private RaceService raceService;

	private VoterService voterService;

	private Race race;

	private List<VoteResult> pluralityResult = new ArrayList<>();

	private List<List<VoteResult>> irvRounds = new ArrayList<>();

	public RaceResult(RaceService raceService, VoterService voterService, Race race) {
		this.race = race;
		this.raceService = raceService;
		this.voterService = voterService;

		List<Voter> voters = voterService.votersForRaceWithRanks(race);

		//Make a VoteResult for each candidate
		List<Candidate> candidates = raceService.candidatesForRace(race);
		for (Candidate candidate : candidates) {
			VoteResult pvr = new VoteResult(candidate);
		}

		//Sum the votes for the plurality results
		for (Voter voter : voters) {
			Rank rank = voterService.rankForOrder(voter, 1);
			if (rank != null) {
				for (VoteResult voteResult : pluralityResult) {
					if (rank.getCandidate().equals(voteResult.getCandidate())) {
						voteResult.addVote();
					}
				}
			}
		}

		//TODO: Create rounds for irv until only two candidates are left


	}

}

class VoteResult {

	private Candidate candidate;
	private int votes;

	public VoteResult(Candidate candidate) {
		this.candidate = candidate;
		this.votes = 0;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public void addVote() {
		this.votes++;
	}
}
