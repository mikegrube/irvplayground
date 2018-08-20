package com.camas.irv.candidate;

import com.camas.irv.race.Race;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/candidate")
public class CandidateController {
	private static final Logger log = LoggerFactory.getLogger(CandidateController.class);

	private CandidateService service;

	@Autowired
	public void setService(CandidateService service) {
		this.service = service;
	}
/*
	//List

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("candidates", service.list());

		return "candidateList";
	}
*/
	//Show

	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {

		Candidate candidate = service.get(id);
		model.addAttribute("candidate", candidate);

		return "candidateShow";
	}
/*
	//Create

	@GetMapping("/new")
	public String create(Model model) {

		model.addAttribute("candidate", new Candidate());
		model.addAttribute("affiliations", service.availableAffiliations());
		model.addAttribute("races", service.availableRaces());

		return "candidateEdit";
	}
*/
	@GetMapping("/newWithRace/{raceId}")
	public String createWithRace(@PathVariable Long raceId, Model model) {

		Candidate candidate = new Candidate();
		Race race = service.findRace(raceId);
		candidate.setRace(race);
		model.addAttribute("candidate", candidate);
		model.addAttribute("affiliations", service.availableAffiliations());
		model.addAttribute("races", service.availableRaces());

		return "candidateEdit";
	}

	@PostMapping("/save")
	public String save(@Valid Candidate candidate, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("affiliations", service.availableAffiliations());
			model.addAttribute("races", service.availableRaces());

			return "candidateEdit";
		}

		candidate = service.save(candidate);

		return "redirect:/candidate/" + candidate.getId();
	}

	//Update

	@GetMapping("/edit/{id}")
	public String update(@PathVariable Long id, Model model) {

		model.addAttribute("candidate", service.get(id));
		model.addAttribute("affiliations", service.availableAffiliations());
		model.addAttribute("races", service.availableRaces());

		return "candidateEdit";
	}

	//Uses the same POST as create

	//Delete

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {

		service.delete(id);

		return "redirect:/candidate/list";
	}

}
