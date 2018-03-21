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
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "")
public class IndexController {
    /*TODO: use sessions and redis and accomplish this vvv without global variables. It's spaghetti*/
    private User activeUser = null;
    private Story activeStory = null;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContributionDao contributionDao;

    @Autowired
    private StoryDao storyDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        if(activeUser != null){
            List<Contribution> contributions = new ArrayList<>();
            contributions = contributionDao.findByUserId(activeUser.getId());
            for(Contribution c : contributions){
                if(storyDao.findOne(c.getStoryId()).getActive() == true){
                    activeStory = storyDao.findOne(c.getStoryId());
                    break;
                }
            }
        }
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        model.addAttribute(new User());
        if(activeStory != null) System.out.printf("\n\n\n%d\n\n\n", activeStory.getTurn());
        return "index/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String processUserForm(@ModelAttribute  @Valid User newUser,
                                       Errors errors, Model model) {
        for(User u : userDao.findAll()){
            if(newUser.getName().equals(u.getName())){
                errors.rejectValue("name", null, "Name already exists");
                if(newUser.getEmail().equals(u.getEmail())){
                    errors.rejectValue("email", null, "Email already registered");
                }
                return "index/index";
            }
            else if(newUser.getEmail().equals(u.getEmail())){
                errors.rejectValue("email", null, "Email already registered");
                return "index/index";
            }
        }

        if (errors.hasErrors()) {
            return "index/index";
        }
        if(activeUser != null) {
            User oldUser = userDao.findOne(activeUser.getId());
            activeUser.setActive(false);
            oldUser.setActive(false);
        }
        activeUser = newUser;
        newUser.setActive(true);
        userDao.save(newUser);
        return "redirect:/";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String displayLoginForm(Model model) {
        model.addAttribute(new User());
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        return "index/login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLoginForm(@ModelAttribute  @Valid User checkUser,
                                  Errors errors, Model model) {
        for(User u : userDao.findAll()){
            if(checkUser.getName().equals(u.getName())){
                if(checkUser.getPassword().equals(u.getPassword())){
                    activeUser = u;
                    u.setActive(true);
                    return "redirect:/";
                }
                else errors.rejectValue("password", null, "Incorrect password.");
            }
            else errors.rejectValue("name", null, "Username doesn't exist.");
        }
        return "index/index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser(Model model) {
        User dataUser = userDao.findOne(activeUser.getId());
        activeUser.setActive(false);
        dataUser.setActive(false);
        activeUser = null;
        model.addAttribute(new User());
        return "redirect:/";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public String displayStoriesForm(Model model){
        if(activeStory != null) return "redirect:/story";
        model.addAttribute("activeUser", activeUser);
        model.addAttribute(new Story("",3, 1, true));
        model.addAttribute(new Contribution());
        model.addAttribute(new User());
        return "index/stories";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.POST)
    public String processStoriesForm(@ModelAttribute @Valid Story checkStory,
                                     @ModelAttribute @Valid Contribution checkContribution,
                                     Errors errors,
                                     @RequestParam String email1,
                                     @RequestParam String email2,
                                     @RequestParam String email3,
                                     @RequestParam String email4,
                                     @RequestParam String email5,
                                     Model model){
        /*TODO: (1) Doesn't validate  (2) Will need to check for an extant email before saving or it will error out*/
        checkStory.setActive(true);
        checkStory.setTurn(1);
        storyDao.save(checkStory);
        Story currentStory = storyDao.findTopByOrderByIdDesc();
        activeStory = currentStory;
        if(!(email1.equals(""))){
            User us1 = userDao.findByEmail(email1);
            Contribution cont1 = new Contribution("", currentStory.getId(), 1);
            us1.addContribution(cont1);
            us1.setTurn(1);
            contributionDao.save(cont1);
            userDao.save(us1);
        }
        if(!(email2.equals(""))){
            User us2 = userDao.findByEmail(email2);
            Contribution cont2 = new Contribution("", currentStory.getId(), 2);
            us2.addContribution(cont2);
            us2.setTurn(2);
            contributionDao.save(cont2);
            userDao.save(us2);
        }
        if(!(email3.equals(""))){
            User us3 = userDao.findByEmail(email3);
            Contribution cont3 = new Contribution("", currentStory.getId(), 3);
            us3.addContribution(cont3);
            us3.setTurn(3);
            contributionDao.save(cont3);
            userDao.save(us3);
        }
        if(!(email4.equals(""))){
            User us4 = userDao.findByEmail(email4);
            Contribution cont4 = new Contribution("", currentStory.getId(), 4);
            us4.addContribution(cont4);
            us4.setTurn(4);
            contributionDao.save(cont4);
            userDao.save(us4);
        }
        if(!(email5.equals(""))){
            User us5 = userDao.findByEmail(email5);
            Contribution cont5 = new Contribution("", currentStory.getId(), 5);
            us5.addContribution(cont5);
            us5.setTurn(5);
            contributionDao.save(cont5);
            userDao.save(us5);
        }
        User currentUser = userDao.findOne(activeUser.getId());
        checkContribution.setStoryId(currentStory.getId());
        checkContribution.setCardinality(0);
        currentUser.addContribution(checkContribution);
        currentUser.setTurn(0);
        contributionDao.save(checkContribution);
        userDao.save(currentUser);
        return "redirect:/stories";
    }
    //TODO: /story get and post
    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public String displayStoryForm(Model model){
        if(activeUser == null || activeStory == null) return "redirect:/index";
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        List<Contribution> contributions = contributionDao.findByStoryIdOrderByCardinalityAsc(activeStory.getId());
        ArrayList<Contribution> currentStory = new ArrayList<Contribution>();
        for(Contribution c : contributions){
            if(!c.getText().equals("")) currentStory.add(c);
        }
        model.addAttribute("currentStory", currentStory);
        return "index/story";
    }

    @RequestMapping(value = "/contribution", method = RequestMethod.GET)
    public String displayContributionForm(Model model){
        if(!(activeUser.getTurn() == activeStory.getTurn())) return "redirect:/index";
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("activeStory", activeStory);
        Contribution contribution = contributionDao.findByStoryId(activeStory.getId()).get(0);
        model.addAttribute("contribution", contribution);
        List<Contribution> contributions = contributionDao.findByStoryIdOrderByCardinalityAsc(activeStory.getId());
        ArrayList<Contribution> currentStory = new ArrayList<Contribution>();
        for(Contribution c : contributions){
            if(!c.getText().equals("")) currentStory.add(c);
        }
        model.addAttribute("currentStory", currentStory);
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

