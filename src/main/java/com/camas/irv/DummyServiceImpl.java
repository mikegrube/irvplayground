package com.camas.irv;

import com.camas.irv.candidate.Affiliation;
import com.camas.irv.candidate.Candidate;
import com.camas.irv.candidate.CandidateService;
import com.camas.irv.race.Race;
import com.camas.irv.race.RaceService;
import com.camas.irv.rank.Rank;
import com.camas.irv.rank.RankService;
import com.camas.irv.voter.Voter;
import com.camas.irv.voter.VoterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DummyServiceImpl implements DummyService {
	private static final Logger log = LoggerFactory.getLogger(DummyServiceImpl.class);

	private RaceService raceService;
	@Autowired
	public void setRaceService(RaceService raceService) {
		this.raceService = raceService;
	}

	private CandidateService candidateService;
	@Autowired
	public void setCandidateService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	private VoterService voterService;
	@Autowired
	public void setVoterService(VoterService voterService) {
		this.voterService = voterService;
	}

	private RankService rankService;
	@Autowired
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}

	@Override
	public Race makeDummyRace() {

		Race race = createRace();

		createCandidates(race);

		createVoters(race);

		return race;
	}

	private Race createRace() {
		//Create the race
		Race race = new Race();
		race.setName("Local Election");
		race.setVoterCount(10);
		race = raceService.save(race);

		return race;
	}

	private void createCandidates(Race race) {

		Candidate candidate = new Candidate();
		candidate.setName("John Blue");
		candidate.setAffiliation(Affiliation.BLUE);
		candidate.setRace(race);
		candidateService.save(candidate);

		candidate = new Candidate();
		candidate.setName("Mary Green");
		candidate.setAffiliation(Affiliation.GREEN);
		candidate.setRace(race);
		candidateService.save(candidate);

		candidate = new Candidate();
		candidate.setName("Paula Red");
		candidate.setAffiliation(Affiliation.RED);
		candidate.setRace(race);
		candidateService.save(candidate);

	}

	@Override
	public void createVoters(Race race) {

		//We need to deal with existing voters

		List<Voter> voters = voterService.votersForRaceWithRanks(race);
		int existingVoterCt = voters.size();
		int neededVoterCt = race.getVoterCount();

		if (neededVoterCt > existingVoterCt) {
			for (int i = existingVoterCt; i < neededVoterCt; i++) {
				Voter voter = new Voter();
				voter.setRace(race);
				voter = voterService.save(voter);
				voters.add(voter);
			}
		} else if (neededVoterCt < existingVoterCt) {
			for (int i = existingVoterCt -1; i >= neededVoterCt; i--) {
				Voter voter = voters.get(i);
				voters.remove(voter);
				voterService.delete(voter.getId());
			}

		}

		for (Voter voter : voters) {

			voterService.dropRanks(voter);
			createRanks(voter);
		}

	}

	@Override
	public void createRanks(Voter voter) {

		//We need to deal with existing ranks; drop? or reuse
		voterService.dropRanks(voter);

		Iterable<Candidate> candidates = candidateService.candidatesforRace(voter.getRace());

		int candidateCt = raceService.candidateCountForRace(voter.getRace());

		int[] ranks = generateRanks(candidateCt);

		int ct = 0;
		for (Candidate candidate : candidates) {
			Rank rank = new Rank();
			rank.setVoter(voter);
			rank.setCandidate(candidate);
			rank.setRankValue(ranks[ct]);
			rankService.save(rank);
			ct++;
		}
	}

	private int[] generateRanks(int count) {

		int[] ranks = new int[count];

		//First set up sequential numbers in the array
		for (int i = 0; i < count; i++) {
			ranks[i] = i + 1;
		}

		//Next, use the Fisher-Yates shuffle (Durstenfeld version) (https://en.wikipedia.org/wiki/Fisher–Yates_shuffle)
		for (int i = 0; i < (count - 1); i++) {
			int j = ThreadLocalRandom.current().nextInt(i, count);
			int hold = ranks[i];
			ranks[i] = ranks[j];
			ranks[j] = hold;
		}

		return ranks;
	}

}
