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


@Controller
@RequestMapping(value = "")
public class IndexController {

    private User activeUser = null;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContributionDao contributionDao;

    @Autowired
    private StoryDao storyDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        //model.addAttribute("users", userDao.findAll());
        model.addAttribute("activeUser", activeUser);
        model.addAttribute(new User());
        model.addAttribute(new Story());
        model.addAttribute("contribution", new Contribution());
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
        model.addAttribute(new Story());
        model.addAttribute("activeUser", activeUser);
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
        return "index/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser(Model model) {
        User dataUser = userDao.findOne(activeUser.getId());
        activeUser.setActive(false);
        dataUser.setActive(false);
        model.addAttribute(new User());
        return "redirect:/login";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public String displayStoriesForm(Model model){
        model.addAttribute("activeUser", activeUser);
        Story story = new Story();
        storyDao.save(story);
        model.addAttribute(new User());
        model.addAttribute("story", storyDao.findOne((int)storyDao.count()));
        model.addAttribute(new Contribution());
        return "index/stories";
    }

    @RequestMapping(value = "/stories", method = RequestMethod.POST)
    public String processStoriesForm(@ModelAttribute @Valid Story checkStory, @ModelAttribute @Valid Contribution checkContribution, Errors errors, Model model){
        User currentUser = userDao.findOne(activeUser.getId());
        currentUser.addContribution(checkContribution);
        activeUser = currentUser;
        contributionDao.save(checkContribution);
        //storyDao.save(currentStory);
        userDao.save(currentUser);
        //try creating a new Story object, populate with checkStory fields. Then addcontribution
//TODO: figure out how to process this tangled fucking mess


        return "index/stories";
    }
    /*TODO: Instantiate a new Story() and a new User() and try their respective addContributions()
            methods to see what the fuck is going on
    *
    * */

}

