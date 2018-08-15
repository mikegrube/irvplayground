package com.camas.irv.race;

import com.camas.irv.candidate.Candidate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Race {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private int voterCount;

	@Transient
	private int candidateCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVoterCount() {
		return voterCount;
	}

	public void setVoterCount(int voterCount) {
		this.voterCount = voterCount;
	}

	public int getCandidateCount() {
		return candidateCount;
	}

	public void setCandidateCount(int candidateCount) {
		this.candidateCount = candidateCount;
	}

	public String toString() {
		return name;
	}
}
