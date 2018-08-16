package com.camas.irv.voter;

import com.camas.irv.race.Race;
import com.camas.irv.rank.Rank;

import javax.persistence.*;
import java.util.List;

@Entity
public class Voter {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Race race;

	@Transient
	private List<Rank> ranks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public String toString() {
		return "Race " + race.getName() + ": Voter " + id;
	}

	public List<Rank> getRanks() {
		return ranks;
	}

	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}
}
