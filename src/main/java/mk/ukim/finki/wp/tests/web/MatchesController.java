package mk.ukim.finki.wp.tests.web;

import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.service.MatchLocationService;
import mk.ukim.finki.wp.tests.service.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MatchesController {

    private final MatchService matchService;
    private final MatchLocationService locationService;

    public MatchesController(MatchService service, MatchLocationService locationService) {
        this.matchService = service;
        this.locationService = locationService;
    }

    /**
     * This method should use the "list.html" template to display all matches.
     * The method should be mapped on paths '/' and '/matches'.
     * The arguments that this method takes are optional and can be 'null'.
     * In the case when the arguments are not passed (both are 'null') all matches should be displayed.
     * If one, or both of the arguments are not 'null', the matches that are the result of the call
     * to the method 'listMatchesWithPriceLessThanAndType' from the MatchService should be displayed.
     *
     * @param price
     * @param type
     * @return The view "list.html".
     */
    @GetMapping({"/", "/matches"})
    public String showMatches(@RequestParam(required = false) Double price, @RequestParam(required = false) MatchType type, Model model) {
        List<Match> matches;
        List<MatchType> types = List.of(MatchType.values());
        if (price == null && type == null) {
            matches = this.matchService.listAllMatches();
        } else {
            matches = this.matchService.listMatchesWithPriceLessThanAndType(price, type);
        }

        int totalFollows = 0;
        for (Match match : matches) {
            totalFollows += match.getFollows();
        }

        model.addAttribute("totalFollows", totalFollows);
        model.addAttribute("matches", matches);
        model.addAttribute("types", types);

        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/matches/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/matches/add")
    public String showAdd(Model model) {
        List<MatchType> types = List.of(MatchType.values());
        List<MatchLocation> locations = locationService.listAll();
        model.addAttribute("locations", locations);
        model.addAttribute("types", types);
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/matches/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/matches/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Match match = this.matchService.findById(id);
        List<MatchType> types = List.of(MatchType.values());
        List<MatchLocation> locations = locationService.listAll();
        model.addAttribute("locations", locations);
        model.addAttribute("match", match);
        model.addAttribute("types", types);
        return "form";
    }

    /**
     * This method should create a match given the arguments it takes.
     * The method should be mapped on path '/matches'.
     * After the entity is created, all matches should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/matches")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam MatchType type,
                         @RequestParam Long location) {
        this.matchService.create(name, description, price, type, location);
        return "redirect:/matches";
    }

    /**
     * This method should update a match given the arguments it takes.
     * The method should be mapped on path '/matches/[id]'.
     * After the entity is updated, all matches should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/matches/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam MatchType type,
                         @RequestParam Long location) {
        this.matchService.update(id, name, description, price, type, location);
        return "redirect:/matches";
    }

    /**
     * This method should delete the match that has the appropriate identifier.
     * The method should be mapped on path '/matches/[id]/delete'.
     * After the entity is deleted, all matches should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/matches/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.matchService.delete(id);
        return "redirect:/matches";
    }

    /**
     * This method should increase the number of followers of the appropriate match by 1.
     * The method should be mapped on path '/matches/[id]/follow'.
     * After the operation, all matches should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/matches/{id}/follow")
    public String follow(@PathVariable Long id) {
        this.matchService.follow(id);
        return "redirect:/matches";
    }
}
