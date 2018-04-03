package com.example.Cooperwrite.controllers;

import com.example.Cooperwrite.models.Contribution;
import com.example.Cooperwrite.models.Story;
import com.example.Cooperwrite.models.User;
import com.example.Cooperwrite.models.data.ContributionDao;
import com.example.Cooperwrite.models.data.StoryDao;
import com.example.Cooperwrite.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping(value = "")
public class IndexController {

    private User activeUser = null;
    private Story activeStory = null;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private ContributionDao contributionDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        //TEMPORARY: locate the active story and set it as the global var
        if(activeUser != null && !(activeUser.getContributions().isEmpty())){
            List<Contribution> contributions = activeUser.getContributions();
            for(Contribution c : contributions){
                Story story = c.getStory();
                if(story.isActive()){
                    activeStory = story;
                    break;
                }
            }
        }

        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        model.addAttribute(new User());
        model.addAttribute(new Story());
        model.addAttribute("contribution", new Contribution());
        return "index/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String processUserForm(@ModelAttribute  @Valid User newUser,
                                       Errors errors, Model model) {
        User byName = userDao.findByName(newUser.getName());
        User byEmail = userDao.findByEmail(newUser.getEmail());
        if(byName != null) errors.rejectValue("name", null, "Name already exists");
        if(byEmail != null) errors.rejectValue("email", null, "Email already registered");

        if (errors.hasErrors()) {
            return "index/index";
        }

        activeUser = newUser;
        newUser.setActive(true);
        userDao.save(newUser);

        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String displayLoginForm(Model model) {
        model.addAttribute(new User());
        model.addAttribute(new Story());
        model.addAttribute("activeUser", activeUser);
        return "index/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLoginForm(@ModelAttribute  @Valid User checkUser,
                                  Errors errors, Model model) {
        /*log out logged in folks --TEMPORARY--*/
        List<User> loggedIns = userDao.findByActive(true);
        for(User u : loggedIns) u.setActive(false);

        User byName = userDao.findByName(checkUser.getName());
        String byPassword = byName.getPassword();

        if(byName != null) {
            if(byPassword != null) {
                activeUser = userDao.findByName(checkUser.getName());
                activeUser.setActive(true);
                userDao.save(activeUser);
                return "redirect:/";
            }
            errors.rejectValue("password", null, "Incorrect password");
            return "index/login";
        }

        else errors.rejectValue("name", null, "Username doesn't exist");

        return "index/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser(Model model) {
        activeUser.setActive(false);
        userDao.save(activeUser);
        activeUser = null;
        //new User required for login checkUser
        model.addAttribute(new User());
        return "redirect:/login";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public String displayStoriesForm(Model model){
        model.addAttribute("activeUser", activeUser);
        Story newStory = new Story("Untitled", 3);
        model.addAttribute("story", newStory);
        model.addAttribute(new Contribution());

        return "index/stories";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.POST)
    public String processStoriesForm(@ModelAttribute @Valid Story checkStory, @ModelAttribute @Valid Contribution checkContribution,
                                     Errors errors,
                                     @RequestParam String email1,
                                     @RequestParam String email2,
                                     @RequestParam(required = false) String email3,
                                     @RequestParam(required = false) String email4,
                                     @RequestParam(required = false) String email5,
                                     Model model){
        activeUser.addContribution(checkContribution);
        checkStory.addContribution(checkContribution);
        checkStory.setTurn(1);
        storyDao.save(checkStory);
        activeUser.setTurn(0);
        userDao.save(activeUser);
        checkContribution.setStory(checkStory);
        checkContribution.setUser(activeUser);
        contributionDao.save(checkContribution);
        activeStory = checkStory;
        checkStory.setActive(true);
        model.addAttribute(new User());

        if(email1 != null){
            User us1 = userDao.findByEmail(email1);
            Contribution cont1 = new Contribution("");
            cont1.setCardinality(1);
            checkStory.addContribution(cont1);
            us1.addContribution(cont1);
            us1.setTurn(1);
            cont1.setUser(us1);
            cont1.setStory(checkStory);
            contributionDao.save(cont1);
            userDao.save(us1);
        }
        if(email2 != null){
            User us2 = userDao.findByEmail(email2);
            Contribution cont2 = new Contribution("");
            cont2.setCardinality(2);
            checkStory.addContribution(cont2);
            us2.addContribution(cont2);
            us2.setTurn(2);
            cont2.setUser(us2);
            cont2.setStory(checkStory);
            contributionDao.save(cont2);
            userDao.save(us2);
        }
        if(email3 != null){
            User us3 = userDao.findByEmail(email3);
            Contribution cont3 = new Contribution("");
            cont3.setCardinality(3);
            checkStory.addContribution(cont3);
            us3.addContribution(cont3);
            us3.setTurn(3);
            cont3.setUser(us3);
            cont3.setStory(checkStory);
            contributionDao.save(cont3);
            userDao.save(us3);
        }
        if(email4 != null){
            User us4 = userDao.findByEmail(email4);
            Contribution cont4 = new Contribution("");
            cont4.setCardinality(4);
            checkStory.addContribution(cont4);
            us4.addContribution(cont4);
            us4.setTurn(4);
            cont4.setUser(us4);
            cont4.setStory(checkStory);
            contributionDao.save(cont4);
            userDao.save(us4);
        }
        if(email5 != null){
            User us5 = userDao.findByEmail(email5);
            Contribution cont5 = new Contribution("");
            cont5.setCardinality(5);
            checkStory.addContribution(cont5);
            us5.addContribution(cont5);
            us5.setTurn(5);
            cont5.setUser(us5);
            cont5.setStory(checkStory);
            contributionDao.save(cont5);
            userDao.save(us5);
        }
        return "redirect:/";
    }
    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public String displayStory(Model model){
        if(activeUser == null || activeStory == null) return "redirect:/index";
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        List<Contribution> currentStory = contributionDao.findAllByStoryIdOrderByCardinality(activeStory.getId());
        model.addAttribute("currentStory", currentStory);
        return "index/story";
    }

    @RequestMapping(value = "/contribution", method = RequestMethod.GET)
    public String displayContributionForm(Model model){
        if(!(activeUser.getTurn() == activeStory.getTurn())) return "redirect:/index";
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        Contribution contribution = contributionDao.findByStoryIdAndUserId(activeStory.getId(), activeUser.getId());
        model.addAttribute("contribution", contribution);
        return "index/contribution";
    }

    @RequestMapping(value = "/contribution", method = RequestMethod.POST)
    public String processContribForm(@ModelAttribute  @Valid Contribution checkContribution,
                                     Errors errors, Model model) {
        activeStory.setTurn(activeStory.getTurn() + 1);
        contributionDao.save(checkContribution);
        storyDao.save(activeStory);
        return "index/contribution";
    }

}



/*
TODO
 ensure /contribution get and post render and process correctly
 clean up all the views
*/
