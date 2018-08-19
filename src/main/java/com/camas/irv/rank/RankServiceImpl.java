package com.camas.irv.rank;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.candidate.CandidateService;
import com.camas.irv.voter.Voter;
import com.camas.irv.voter.VoterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class RankServiceImpl implements RankService {
	private static final Logger log = LoggerFactory.getLogger(RankServiceImpl.class);

	private RankRepository repository;
	@Autowired
	public void setRepository(RankRepository repository) {
		this.repository = repository;
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

	@Override
	public Iterable<Rank> list() {
		return repository.findAll();
	}

	@Override
	public Rank get(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Rank save(Rank rank) {
		return repository.save(rank);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Iterable<Candidate> availableCandidates() {
		return candidateService.list();
	}

	@Override
	public Iterable<Voter> availableVoters() {
		return voterService.list();
	}

	@Override
	public void makeDummyRanks(Voter voter) {

		int[][] initialRanks = new int[][] {
				{1, 2, 3},
				{1, 3, 2},
				{2, 3, 1},
				{2, 1, 3},
				{3, 1, 2},
				{3, 2, 1},
				{1, 0, 0},
				{0, 1, 0},
				{0, 0, 1}
		};

		Iterable<Candidate> candidates = candidateService.candidatesforRace(voter.getRace());

		int randNum = ThreadLocalRandom.current().nextInt(0, 9);

		int cct = 0;
		for (Candidate candidate : candidates) {
			Rank rank = new Rank();
			rank.setVoter(voter);
			rank.setCandidate(candidate);
			rank.setRankValue(initialRanks[randNum][cct]);
			save(rank);
			cct++;
		}
	}

	@Override
	public List<Rank> ranksForVoter(Voter voter) {

		List<Rank> ranks = repository.findByVoterOrderByCandidateName(voter);

		return ranks;
	}

	@Override
	public List<Rank> ranksForVoterByRank(Voter voter) {

		List<Rank> ranks = repository.findByVoterOrderByRankValue(voter);

		return ranks;
	}

	@Override
	public void deleteByVoter(Voter voter) {

		List<Rank> ranks = ranksForVoter(voter);
		for (Rank rank : ranks) {
			repository.delete(rank);
		}

	}

}
