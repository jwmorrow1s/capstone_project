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
        if(activeUser != null){
            List<Contribution> contributions = contributionDao.findAll();
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
            us1.setTurn(1);
            /*
            Contribution cont1 = new Contribution("");
            cont1.setCardinality(1);
            checkStory.addContribution(cont1);
            us1.addContribution(cont1);
            cont1.setUser(us1);
            cont1.setStory(checkStory);
            contributionDao.save(cont1);
            */
            userDao.save(us1);
        }
        if(email2 != null){
            User us2 = userDao.findByEmail(email2);
            us2.setTurn(2);
            userDao.save(us2);
        }
        if(email3 != null){
            User us3 = userDao.findByEmail(email3);
            us3.setTurn(3);
            userDao.save(us3);
        }
        if(email4 != null){
            User us4 = userDao.findByEmail(email4);
            us4.setTurn(4);
            userDao.save(us4);
        }
        if(email5 != null){
            User us5 = userDao.findByEmail(email5);
            us5.setTurn(5);
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
        if(activeUser == null || activeStory == null) return "redirect:/index";
        if(!(activeUser.getTurn() == activeStory.getTurn())){
            return "redirect:/index";
        }
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        model.addAttribute(new Contribution());
        return "index/contribution";
    }

    @RequestMapping(value = "/contribution", method = RequestMethod.POST)
    public String processContribForm(@ModelAttribute  @Valid Contribution checkContribution,
                                     Errors errors, Model model) {
        if(activeStory.getTurn() == activeStory.getTurns() - 1) activeStory.setActive(false);
        activeStory.setTurn(activeStory.getTurn() + 1);
        checkContribution.setCardinality(activeUser.getTurn());
        activeStory.addContribution(checkContribution);
        activeUser.addContribution(checkContribution);
        checkContribution.setStory(activeStory);
        checkContribution.setUser(activeUser);
        contributionDao.save(checkContribution);
        userDao.save(activeUser);
        storyDao.save(activeStory);
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        List<Contribution> currentStory = contributionDao.findAllByStoryIdOrderByCardinality(activeStory.getId());
        return "redirect:/story";
    }

}



/*
TODO
 ensure /contribution get and post render and process correctly
 clean up all the views
*/
