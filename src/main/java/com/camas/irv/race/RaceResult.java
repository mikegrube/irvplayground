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

	private VotingRound pluralityResult = new VotingRound(0, "A single round can be won by plurality.");

	private List<VotingRound> irvRounds = new ArrayList<>();

	public RaceResult(RaceService raceService, VoterService voterService, Race race) {
		this.race = race;
		this.raceService = raceService;
		this.voterService = voterService;

		List<Voter> voters = voterService.votersForRaceWithRanks(race);
		int voterCt = voters.size();

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
		irvRounds.add(duplicateRound(pluralityResult));

		VotingRound lastRound = pluralityResult;

		//While there are more than two candidates still in play, create another round
		//Stop if one candidate has > 50% of all voters' votes
		while (lastRound.size() > 2) {

			if (lastRound.anyMajority(voterCt)) {
				lastRound.setComment("One candidate has a majority.");
				break;
			}

			//Find the in-play candidate with the fewest votes and remove
			//If there is a tie for least, all tied are removed and votes redistributed
			List<VoteResult> leasts = new ArrayList<>();
			for (VoteResult voteResult : lastRound.getVoteResults()) {
				if (leasts.size() == 0 || voteResult.getVotes() == leasts.get(0).getVotes()) {
					leasts.add(voteResult);
				} else if (voteResult.getVotes() < leasts.get(0).getVotes()) {
					leasts = new ArrayList<>();
					leasts.add(voteResult);
				}
			}

			VotingRound newRound = lastRound.makeNewRound(leasts);

			//Redistribute that candidate's highest-ranked votes among other in-play candidates
			applyVotes(voters, newRound);

			irvRounds.add(newRound);

			lastRound = newRound;

		}

		if (lastRound.size() == 2) {
			//If tied, break tie on greater number of #1 votes (pluralityResult has first round)
			VoteResult winner1 = lastRound.getVoteResults().get(0);
			VoteResult winner2 = lastRound.getVoteResults().get(1);
			int winner1Ct = 0;
			int winner2Ct = 0;
			if (winner1.getVotes() == winner2.getVotes()) {
				for (VoteResult voteResult : pluralityResult.getVoteResults()) {
					if (voteResult.getCandidate().equals(winner1.getCandidate())) {
						winner1Ct = voteResult.getVotes();
					}
					if (voteResult.getCandidate().equals(winner2.getCandidate())) {
						winner2Ct = voteResult.getVotes();
					}
					if (winner1Ct > winner2Ct) {
						lastRound.setComment(winner1.getCandidate().getName() + " wins based on having more 1st place votes.");
					} else if (winner2Ct > winner1Ct) {
						lastRound.setComment(winner2.getCandidate().getName() + " wins based on having more 1st place votes.");
					} else {
						lastRound.setComment("Tied winners have equal 1st place votes.");
					}
				}
			}

		}
	}

	private VotingRound duplicateRound(VotingRound previousVotingRound) {

		VotingRound votingRound = new VotingRound(0, "First IRV round is the same as the only plurality round.");

		for (VoteResult voteResult : previousVotingRound.getVoteResults()) {

			VoteResult newVoteResult = new VoteResult(voteResult.getCandidate());
			newVoteResult.setVotes(voteResult.getVotes());
			votingRound.add(newVoteResult);
		}

		return votingRound;
	}

	private void applyVotes(List<Voter> voters, VotingRound votingRound) {

		for (Voter voter : voters) {
			List<Rank> ranks = voterService.ranksForVoterByRankValue(voter);
			boolean found = false;
			for (Rank rank : ranks) {
				if (rank.getRankValue() != 0) {
					for (VoteResult voteResult : votingRound.getVoteResults()) {
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

	public VotingRound getPluralityResult() {
		return pluralityResult;
	}

	public void setPluralityResult(VotingRound votingRound) {
		this.pluralityResult = pluralityResult;
	}

	public List<VotingRound> getIrvRounds() {
		return irvRounds;
	}

	public void setIrvRounds(List<VotingRound> irvRounds) {
		this.irvRounds = irvRounds;
	}
}

class VotingRound {

	private int roundNo;
	private List<VoteResult> voteResults = new ArrayList<>();
	private String comment;

	public VotingRound(int roundNo, String comment) {
		this.roundNo = roundNo;
		this.comment = comment;
	}

	public int getRoundNo() {
		return roundNo;
	}

	public void setRoundNo(int roundNo) {
		this.roundNo = roundNo;
	}

	public List<VoteResult> getVoteResults() {
		return voteResults;
	}

	public void setVoteResults(List<VoteResult> voteResults) {
		this.voteResults = voteResults;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void add(VoteResult voteResult) {
		voteResults.add(voteResult);
	}

	public int size() {
		return voteResults.size();
	}

	public boolean anyMajority(int voterCt) {

		int majority = (voterCt / 2) + 1;

		for (VoteResult voteResult : voteResults) {
			if (voteResult.getVotes() >= (majority)) {
				return true;
			}
		}

		return false;

	}

	public VotingRound makeNewRound(List<VoteResult> leasts) {

		VotingRound newVotingRound = new VotingRound(roundNo+1, "Removing the candidate with the least votes.");
		if (leasts.size() > 1) {
			newVotingRound.setComment("Removing multiple candidates tied for the least votes.");
		}

		for (VoteResult voteResult : voteResults) {

			boolean ok = true;
			for (VoteResult leastResult : leasts) {
				if (voteResult.getCandidate().equals(leastResult.getCandidate())) {
					ok = false;
				}
			}
			if (ok) {
				newVotingRound.add(new VoteResult(voteResult.getCandidate()));
			}
		}

		return newVotingRound;
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
