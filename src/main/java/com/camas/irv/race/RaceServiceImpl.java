package com.camas.irv.race;

import com.camas.irv.DummyService;
import com.camas.irv.candidate.Affiliation;
import com.camas.irv.candidate.Candidate;
import com.camas.irv.candidate.CandidateRepository;
import com.camas.irv.candidate.CandidateService;
import com.camas.irv.voter.Voter;
import com.camas.irv.voter.VoterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceServiceImpl implements RaceService {
	private static final Logger log = LoggerFactory.getLogger(RaceServiceImpl.class);

	private RaceRepository repository;
	@Autowired
	public void setRaceRepository(RaceRepository raceRepository) {
		this.repository = raceRepository;
	}

	private CandidateService candidateService;
	@Autowired
	public void setRaceService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	private VoterService voterService;
	@Autowired
	public void setVoterService(VoterService voterService) {
		this.voterService = voterService;
	}

	private DummyService dummyService;
	@Autowired
	public void setDummyService(DummyService dummyService) {
		this.dummyService = dummyService;
	}

	@Override
	public List<Race> list() {
		return repository.findByOrderByName();
	}

	@Override
	public Race get(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Race save(Race race) {

		race = repository.save(race);

		//Voting needs to be redone; voter count could have changed
		dummyService.createVoters(race);
		revote(race);

		return race;
	}

	@Override
	public void delete(Long id) {

		repository.deleteById(id);

	}

	@Override
	public List<Candidate> candidatesForRace(Race race) {
		return candidateService.candidatesforRace(race);
	}

	@Override
	public int candidateCountForRace(Race race) {

		Iterable<Candidate> candidates = candidatesForRace(race);
		int ct = 0;
		for (Candidate candidate : candidates) {
			ct++;
		}

		return ct;
	}

	@Override
	public List<Voter> votersForRace(Race race) {

		return voterService.votersForRaceWithRanks(race);
	}

	@Override
	public RaceResult tabulate(Race race) {

		return new RaceResult(this, voterService, race);

	}

	@Override
	public void revote(Race race) {

		voterService.revote(race);

	}

}
