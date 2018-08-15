package com.camas.irv.rank;

import com.camas.irv.candidate.Candidate;
import com.camas.irv.voter.Voter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Rank {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Voter voter;

	@ManyToOne
	private Candidate candidate;

	private int rankValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Voter getVoter() {
		return voter;
	}

	public void setVoter(Voter voter) {
		this.voter = voter;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public int getRankValue() {
		return rankValue;
	}

	public void setRankValue(int rankValue) {
		this.rankValue = rankValue;
	}

	public String toString() {
		return candidate.getName() + " - " + rankValue;
	}
}
