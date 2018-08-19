package com.camas.irv.rank;

import com.camas.irv.voter.Voter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface RankRepository extends CrudRepository<Rank, Long> {

	List<Rank> findByVoterOrderByCandidateName(Voter voter);

	List<Rank> findByVoterOrderByRankValue(Voter voter);

}

