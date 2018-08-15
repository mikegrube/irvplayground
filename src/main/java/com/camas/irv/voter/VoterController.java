package com.camas.irv.voter;

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
@RequestMapping("/voter")
public class VoterController {
	private static final Logger log = LoggerFactory.getLogger(VoterController.class);

	private VoterService service;

	@Autowired
	public void setService(VoterService service) {
		this.service = service;
	}

	//List

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("voters", service.list());

		return "voterList";
	}

	//Show

	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {

		Voter voter = service.get(id);
		model.addAttribute("voter", voter);

		return "voterShow";
	}

	//Create

	@GetMapping("/new")
	public String create(Model model) {

		model.addAttribute("voter", new Voter());
		model.addAttribute("races", service.availableRaces());

		return "voterEdit";
	}

	@PostMapping("/save")
	public String save(@Valid Voter voter, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "voterEdit";
		}

		voter = service.save(voter);

		return "redirect:/voter/" + voter.getId();
	}

	//Update

	@GetMapping("/edit/{id}")
	public String update(@PathVariable Long id, Model model) {

		model.addAttribute("voter", service.get(id));
		model.addAttribute("races", service.availableRaces());

		return "voterEdit";
	}

	//Uses the same POST as create

	//Delete

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {

		service.delete(id);

		return "redirect:/voter/list";
	}

}
