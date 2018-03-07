package com.example.Cooperwrite.controllers;

import com.example.Cooperwrite.models.User;
import com.example.Cooperwrite.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "")
public class IndexController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute(new User());
        return "index/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid User newUser,
                                       Errors errors, Model model) {
        for(User u : userDao.findAll()){
            if(newUser.getName().equals(u.getName())){
                errors.rejectValue("name", null, "Name already exists");
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

        userDao.save(newUser);
        return "redirect:/index";
    }

}
