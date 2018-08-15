package com.camas.irv.voter;

import com.camas.irv.race.Race;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface VoterRepository extends CrudRepository<Voter, Long> {

	Iterable<Voter> findByOrderById();

	List<Voter> findByRaceOrderById(Race race);
}

