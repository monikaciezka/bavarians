package com.bavarians.controller;

import com.bavarians.graphql.model.Oferta;
import com.bavarians.graphql.repository.ElementRepository;
import com.bavarians.graphql.repository.KlientRepository;
import com.bavarians.graphql.repository.PojazdRepository;
import com.bavarians.service.OfertaService;
import com.bavarians.service.impl.DefaultUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

import static com.bavarians.controller.PagesConstants.WELCOME_PAGE;

@Controller
public class PageController {
    @Autowired
    private DefaultUserDetailsService userService;
    @Autowired
    private OfertaService ofertaService;
    @Autowired
    private PojazdRepository pojazdRepository;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private KlientRepository klientRepository;

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return WELCOME_PAGE;
    }

    @GetMapping({"/klienci"})
    public String klienci(Model model) {
        model.addAttribute("users", userService.findAll());
        return PagesConstants.KLIENCI_PAGE;
    }

    @GetMapping({"/pojazdy"})
    public String pojazdy(Model model) {
        model.addAttribute("pojazdy", pojazdRepository.findAll());
        return PagesConstants.POJAZDY_PAGE;
    }

    @GetMapping("/pojazdy/usun/{id}")
    private String usunPojazd(@PathVariable("id") Long id) {
        pojazdRepository.findById(id).ifPresent(pojazd -> {
            Set<Oferta> oferty = pojazd.getOferty();
            oferty.forEach(o -> {
                elementRepository.deleteAll(o.getElementySerwisowe());
            });
        });
        pojazdRepository.findById(id).ifPresent(it -> {
            ofertaService.deleteAll(it.getOferty());
        });
        pojazdRepository.deleteById(id);
        return "redirect:/pojazdy/";
    }

    @GetMapping({"/elementy-serwisowe"})
    public String elementySerwisowe(Model model) {
        model.addAttribute("elementy", elementRepository.findAll());
        return PagesConstants.ELEMENTY_PAGE;
    }

    @GetMapping("/klienci/usun/{id}")
    private String usunKlienta(@PathVariable("id") Long id) {
        klientRepository.deleteById(id);
        return "redirect:/klienci/";
    }
}