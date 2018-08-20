package com.camas.irv.candidate;

import com.camas.irv.race.Race;
import com.camas.irv.race.RaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {
	private static final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

	private CandidateRepository repository;
	@Autowired
	public void setRepository(CandidateRepository repository) {
		this.repository = repository;
	}

	private RaceService raceService;
	@Autowired
	public void setRaceService(RaceService raceService) {
		this.raceService = raceService;
	}

	@Override
	public Iterable<Candidate> list() {
		return repository.findByOrderByName();
	}

	@Override
	public Candidate get(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Candidate save(Candidate candidate) {
		return repository.save(candidate);
	}

	@Override
	public void delete(Long id) {

		raceService.removeCandidateFromRace(get(id));

		repository.deleteById(id);
	}

	@Override
	public Iterable<Affiliation> availableAffiliations() {
		return Arrays.asList(Affiliation.values());
	}

	@Override
	public Iterable<Race> availableRaces() {
		return raceService.list();
	}

	@Override
	public List<Candidate> candidatesforRace(Race race) {
		return repository.findByRace(race);
	}

	@Override
	public Race findRace(Long raceId) {
		return raceService.get(raceId);
	}

}
