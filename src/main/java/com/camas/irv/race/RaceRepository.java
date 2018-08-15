package com.camas.irv.race;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RaceRepository extends CrudRepository<Race, Long> {

	List<Race> findByOrderByName();
}

