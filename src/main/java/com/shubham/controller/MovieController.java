package com.shubham.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.shubham.entity.Movie;
import com.shubham.service.DatabaseService;

import jakarta.validation.Valid;

@RestController
public class MovieController {

	@Autowired
	DatabaseService databaseService;

	@GetMapping("/index")
	public ModelAndView homePage() {
		String viewName = "index";
		Map<String, Object> model = new HashMap<>();
		List<Movie> moviesList = databaseService.getAllMovies();
		model.put("moviesList", moviesList);
		return new ModelAndView(viewName, model);
	}

	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchlistForm(@RequestParam(required = false) Integer id) {
		String viewName = "watchlistItemForm";
		Map<String, Object> model = new HashMap<>();
		if (id == null)
			model.put("newMovie", new Movie());
		else
			model.put("newMovie", databaseService.getMovieById(id));
		return new ModelAndView(viewName, model);
	}

	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchlistForm(@Valid @ModelAttribute("newMovie") Movie newMovie,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, Object> model = new HashMap<>();
			model.put("newMovie", newMovie);
			model.put("bindingResult", bindingResult);
			return new ModelAndView("watchlistItemForm", model);
		}

		Integer id = newMovie.getId();
		if (id == null)
			databaseService.create(newMovie);
		else
			databaseService.update(id, newMovie);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/watchlist");

		return new ModelAndView(redirectView);
	}

	@GetMapping("/watchlist")
	public ModelAndView getWatchlist() {
		String viewName = "watchlist";
		Map<String, Object> model = new HashMap<>();
		List<Movie> moviesList = databaseService.getAllMovies();
		model.put("moviesList", moviesList);
		model.put("noOfMovies", moviesList.size());
		return new ModelAndView(viewName, model);
	}

	@GetMapping("/deleteMovie")
	public ModelAndView removeMovie(@RequestParam Integer id) {
		databaseService.deleteMovieById(id);

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/watchlist");

		return new ModelAndView(redirectView);
	}
}