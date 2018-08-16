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
import java.util.ArrayList;
import java.util.List;


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

	@PostMapping("/save")
	public String save(@Valid Voter voter, Model model, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			model = prepModelForEdit(model, voter);
			return "voterEdit";
		} else {
			if (!service.validate(voter)) {
				bindingResult.rejectValue("race", "incorrect.ranking", "Non-zero ranking cannot have duplicates");

				model = prepModelForEdit(model, voter);
				return "voterEdit";
			}

		}

		service.applyRanks(voter);
		voter = service.save(voter);

		return "redirect:/race/" + voter.getRace().getId();
	}

	private Model prepModelForEdit(Model model, Voter voter) {

		model.addAttribute("voter", voter);

		int candidateCt = service.raceCandidateCt(voter.getRace());
		List<Integer> nums = new ArrayList<>();
		nums.add(0);
		for (int i = 1; i <= candidateCt; i++) {
			nums.add(i);
		}
		model.addAttribute("values", nums);

		model.addAttribute("candidates", service.candidatesForRace(voter.getRace()));

		return model;
	}

	//Update

	@GetMapping("/edit/{id}")
	public String update(@PathVariable Long id, Model model) {

		Voter voter = service.get(id);
		voter.setRanks(service.ranksForVoter(voter));

		model = prepModelForEdit(model, voter);

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
