package com.camas.irv.race;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.rank.Rank;
import com.camas.irv.voter.Voter;
import com.camas.irv.voter.VoterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class RaceResult {
	private static final Logger log = LoggerFactory.getLogger(RaceResult.class);

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
			pluralityResult.add(pvr);
		}

		//Sum the votes for the plurality results
		applyVotes(voters, pluralityResult);

		//Create rounds for irv until only two candidates are left

		//Create the first round with all candidates (same as plurality)
		irvRounds.add(pluralityResult);

		log.info("Starting irv rounds");

		List<VoteResult> lastRound = pluralityResult;

		//While there are more than two candidates still in play, create another round
		if (lastRound.size() > 2) {

			//Find the in-play candidate with the fewest votes and remove
			VoteResult least = null;
			for (VoteResult voteResult : lastRound) {
				if (least == null || voteResult.getVotes() < least.getVotes()) {
					least = voteResult;
				}
			}

			List<VoteResult> newRound = makeNewRound(lastRound, least.getCandidate());

			//Redistribute that candidate's highest-ranked votes among other in-play candidates
			applyVotes(voters, newRound);

			irvRounds.add(newRound);

			lastRound = newRound;

		}

	}

	private List<VoteResult> makeNewRound(List<VoteResult> previousRound, Candidate minusCandidate) {

		List<VoteResult> voteResults = new ArrayList<>();

		for (VoteResult voteResult : previousRound) {
			if (!voteResult.getCandidate().equals(minusCandidate)) {
				voteResults.add(new VoteResult(voteResult.getCandidate()));
			}
		}

		return voteResults;
	}

	private void applyVotes(List<Voter> voters, List<VoteResult> voteResults) {

		for (Voter voter : voters) {
			List<Rank> ranks = voterService.ranksForVoterByRankValue(voter);
			log.info("Voter: " + voter.getId());
			boolean found = false;
			for (Rank rank : ranks) {
				if (rank.getRankValue() != 0) {
					for (VoteResult voteResult : voteResults) {
						if (!found && rank.getCandidate().equals(voteResult.getCandidate())) {
							voteResult.addVote();
							found = true;
							break;
						}
					}
				}
			}
		}

	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public List<VoteResult> getPluralityResult() {
		return pluralityResult;
	}

	public void setPluralityResult(List<VoteResult> pluralityResult) {
		this.pluralityResult = pluralityResult;
	}

	public List<List<VoteResult>> getIrvRounds() {
		return irvRounds;
	}

	public void setIrvRounds(List<List<VoteResult>> irvRounds) {
		this.irvRounds = irvRounds;
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
