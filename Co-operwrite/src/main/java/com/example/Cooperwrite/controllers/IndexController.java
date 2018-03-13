package com.example.Cooperwrite.controllers;

import com.example.Cooperwrite.models.User;
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

    private User activeUser;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        //model.addAttribute("users", userDao.findAll());
        model.addAttribute("activeUser", activeUser);
        model.addAttribute(new User());
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
        activeUser = newUser;
        newUser.setActive(true);
        userDao.save(newUser);
        return "redirect:/index/user/" + newUser.getId();
    }

    @RequestMapping(value = "index/user/{userId}", method = RequestMethod.GET)
    public String processUser(Model model){
        model.addAttribute("activeUser", activeUser);
        return "index/user";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String displayLoginForm(Model model) {
        model.addAttribute(new User());
        return "index/login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLoginForm(@ModelAttribute  @Valid User checkUser,
                                  Errors errors, Model model) {
        for(User u : userDao.findAll()){
            if(checkUser.getName().equals(u.getName())){
                if(checkUser.getPassword().equals(u.getPassword())){
                    u.setActive(true);
                    return "redirect:/";
                }
                else errors.rejectValue("password", null, "Incorrect password.");
            }
            else errors.rejectValue("name", null, "Username doesn't exist.");
        }
        return "redirect:/index/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser(Model model) {
        User dataUser = userDao.findOne(activeUser.getId());
        activeUser.setActive(false);
        dataUser.setActive(false);
        model.addAttribute(new User());
        return "index/login";
    }
}

