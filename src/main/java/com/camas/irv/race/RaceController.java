package com.camas.irv.race;

import com.camas.irv.DummyService;
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
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/race")
public class RaceController {
	private static final Logger log = LoggerFactory.getLogger(RaceController.class);

	private RaceService service;
	@Autowired
	public void setService(RaceService service) {
		this.service = service;
	}

	private DummyService dummyService;
	@Autowired
	public void setDummyService(DummyService dummyService) {
		this.dummyService = dummyService;
	}

	//List

	@GetMapping("/list")
	public String list(Model model) {

		List<Race> races = service.list();

		boolean found = false;
		for (Race race : races) {
			race.setCandidateCount(service.candidateCountForRace(race));
			found = true;
		}

		//Build a race if there aren't any
		if (!found) {
			races.add(dummyService.makeDummyRace());
		}

		model.addAttribute("races", races);

		return "raceList";
	}

	//Show

	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {

		Race race = service.get(id);
		model.addAttribute("race", race);
		model.addAttribute("candidates", service.candidatesForRace(race));
		model.addAttribute("voters", service.votersForRace(race));

		return "raceShow";
	}

	//Create

	@GetMapping("/new")
	public String create(Model model) {

		model.addAttribute("race", new Race());

		return "raceEdit";
	}

	@PostMapping("/save")
	public String save(@Valid Race race, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "raceEdit";
		}

		race = service.save(race);

		return "redirect:/race/" + race.getId();
	}

	//Update

	@GetMapping("/edit/{id}")
	public String update(@PathVariable Long id, Model model) {

		model.addAttribute("race", service.get(id));

		return "raceEdit";
	}

	//Uses the same POST as create

	//Delete

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {

		service.delete(id);

		return "redirect:/race/list";
	}

	//Tabulate the results

	@GetMapping("/tabulate/{id}")
	public String tabulate(@PathVariable Long id, Model model) {

		Race race = service.get(id);

		RaceResult raceResult = service.tabulate(race);

		model.addAttribute("raceResult", raceResult);

		return "raceTabulation";
	}

	@GetMapping("/revote/{id}")
	public String revote(@PathVariable Long id, Model model) {

		Race race = service.get(id);

		service.revote(race);

		return "redirect:/race/" + race.getId();
	}

}
