package com.camas.irv.rank;

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
@RequestMapping("/rank")
public class RankController {
	private static final Logger log = LoggerFactory.getLogger(RankController.class);

	private RankService service;

	@Autowired
	public void setService(RankService service) {
		this.service = service;
	}
/*
	//List

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("ranks", service.list());

		return "rankList";
	}

	//Show

	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {

		Rank rank = service.get(id);
		model.addAttribute("rank", rank);

		return "rankShow";
	}

	//Create

	@GetMapping("/new")
	public String create(Model model) {

		model.addAttribute("rank", new Rank());
		model.addAttribute("voters", service.availableVoters());
		model.addAttribute("candidates", service.availableCandidates());

		return "rankEdit";
	}

	@PostMapping("/save")
	public String save(@Valid Rank rank, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "rankEdit";
		}

		rank = service.save(rank);

		return "redirect:/rank/" + rank.getId();
	}

	//Update

	@GetMapping("/edit/{id}")
	public String update(@PathVariable Long id, Model model) {

		model.addAttribute("rank", service.get(id));
		model.addAttribute("voters", service.availableVoters());
		model.addAttribute("candidates", service.availableCandidates());

		return "rankEdit";
	}

	//Uses the same POST as create

	//Delete

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {

		service.delete(id);

		return "redirect:/rank/list";
	}
*/
}
