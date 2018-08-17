package com.camas.irv.voter;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.candidate.CandidateService;
import com.camas.irv.race.Race;
import com.camas.irv.race.RaceService;
import com.camas.irv.rank.Rank;
import com.camas.irv.rank.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class VoterServiceImpl implements VoterService {
	private static final Logger log = LoggerFactory.getLogger(VoterServiceImpl.class);

	private VoterRepository repository;
	@Autowired
	public void setRepository(VoterRepository repository) {
		this.repository = repository;
	}

	private RaceService raceService;
	@Autowired
	public void setRaceService(RaceService raceService) {
		this.raceService = raceService;
	}

	private RankService rankService;
	@Autowired
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}

	private CandidateService candidateService;
	@Autowired
	public void setCandidateService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	@Override
	public Iterable<Voter> list() {
		return repository.findByOrderById();
	}

	@Override
	public Voter get(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Voter save(Voter voter) {
		return repository.save(voter);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Iterable<Race> availableRaces() {
		return raceService.list();
	}

	@Override
	public List<Voter> votersForRaceWithRanks(Race race) {

		List<Voter> voters = repository.findByRaceOrderById(race);

		//Add ranks lists
		for (Voter voter : voters) {
			voter.setRanks(rankService.ranksForVoter(voter));
		}

		return voters;
	}

	@Override
	public void makeDummyVoters(Race race) {

		for (int i = 0; i < race.getVoterCount(); i++) {
			Voter voter = new Voter();
			voter.setRace(race);
			voter = save(voter);

			//Add ranks
			rankService.makeDummyRanks(voter);
		}

	}

	@Override
	public int raceCandidateCt(Race race) {
		return raceService.candidateCountForRace(race);
	}

	@Override
	public List<Rank> ranksForVoter(Voter voter) {
		return rankService.ranksForVoter(voter);
	}

	@Override
	public List<Candidate> candidatesForRace(Race race) {
		return raceService.candidatesForRace(race);
	}

	@Override
	public void applyRanks(Voter voter) {

		List<Rank> trueRanks = ranksForVoter(voter);

		for (int i = 0; i < voter.getRanks().size(); i++) {
			trueRanks.get(i).setRankValue(voter.getRanks().get(i).getRankValue());
			rankService.save(trueRanks.get(i));
		}
	}

	@Override
	public boolean validate(Voter voter) {

		boolean ok = true;

		List<Rank> ranks = voter.getRanks();
		List<Integer> usedValue = new ArrayList<>();

		//Check for duplicate rankValues
		for (Rank rank : ranks) {
			if (rank.getRankValue() != 0) {
				if (!usedValue.contains(rank.getRankValue())) {
					usedValue.add(rank.getRankValue());
				} else {
					ok = false;
				}
			}
		}

		//Rankings must be sequential starting with 1
		Collections.sort(usedValue);
		int last = 0;
		for (Integer i : usedValue) {
			if (i != last + 1) {
				ok = false;
			}
			last = i;
		}

		return ok;
	}

	@Override
	public Rank rankForOrder(Voter voter, int order) {

		List<Rank> ranks = voter.getRanks();
		for (Rank rank : ranks) {
			if (rank.getRankValue() == order) {
				return rank;
			}
		}

		return null;
	}

}
