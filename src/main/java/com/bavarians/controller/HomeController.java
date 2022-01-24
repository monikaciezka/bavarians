package com.bavarians.controller;

import com.bavarians.graphql.model.Klient;
import com.bavarians.graphql.repository.ElementRepository;
import com.bavarians.graphql.repository.KlientRepository;
import com.bavarians.service.impl.KlientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.bavarians.controller.PagesConstants.WELCOME_PAGE;

@Controller
public class HomeController {
    @Autowired
    private KlientService userService;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private KlientRepository klientRepository;

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return WELCOME_PAGE;
    }


    @GetMapping({"/elementy-serwisowe"})
    public String elementySerwisowe(Model model) {
        model.addAttribute("elementy", elementRepository.findAll());
        return PagesConstants.ELEMENTY_PAGE;
    }


}
