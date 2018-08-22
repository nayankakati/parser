package com.qualys.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HtmlController {

	@GetMapping("/")
	public String scan() {
		return "welcome";
	}

	@GetMapping("/scan")
	public String scanDetails(@RequestParam String id, Model model) {
		model.addAttribute("id", id);
		return "scanDetails";
	}
}
