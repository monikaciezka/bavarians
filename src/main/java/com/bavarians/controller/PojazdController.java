package com.bavarians.controller;

import com.bavarians.graphql.model.Oferta;
import com.bavarians.graphql.model.Pojazd;
import com.bavarians.graphql.repository.ElementRepository;
import com.bavarians.graphql.repository.KlientRepository;
import com.bavarians.graphql.repository.PojazdRepository;
import com.bavarians.service.OfertaService;
import com.bavarians.service.impl.KlientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("pojazdy")
public class PojazdController {
    public static final String REDIRECT_POJAZDY = "redirect:/pojazdy/";
    @Autowired
    private KlientService userService;
    @Autowired
    private OfertaService ofertaService;
    @Autowired
    private PojazdRepository pojazdRepository;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private KlientRepository klientRepository;

    @GetMapping
    public String pojazdy(Model model) {
        model.addAttribute("pojazdy", pojazdRepository.findAll());
        return PagesConstants.POJAZDY_PAGE;
    }

    @GetMapping("/usun/{id}")
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
        return REDIRECT_POJAZDY;
    }

    @GetMapping("/dodaj")
    private String dodajPojazd(Model model) {
        model.addAttribute("pojazdForm", new Pojazd());
        return PagesConstants.NOWY_POJAZD_PAGE;
    }

    @PostMapping("/dodaj")
    public String dodajPojazd(@ModelAttribute("pojazdForm") Pojazd pojazdForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return PagesConstants.NOWY_POJAZD_PAGE;
        }
        pojazdRepository.save(pojazdForm);
        return REDIRECT_POJAZDY;
    }


    @GetMapping("/edytuj/{id}")
    private String edytujPojazd(@PathVariable("id") Long id, Model model) {
        Optional<Pojazd> pojazdOptional = pojazdRepository.findById(id);
        pojazdOptional.ifPresent(o -> {
            model.addAttribute("pojazdForm", o);

        });
        return PagesConstants.EDYCJA_POJAZD_PAGE;
    }

    @PostMapping("/edytuj/{id}")
    public String edytujPojazd(@PathVariable("id") Long id, @ModelAttribute("pojazdForm") Pojazd pojazdForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return PagesConstants.EDYCJA_POJAZD_PAGE;
        }
        pojazdRepository.save(pojazdForm);
        return REDIRECT_POJAZDY;
    }

    @GetMapping("/nowa-oferta/{pojazdId}")
    private String utworzOferteDlaPojazdu(@PathVariable("pojazdId") Long id, Model model) {
        Optional<Oferta> optionalOferta = pojazdRepository.findById(id).map(pojazd -> {
            Oferta oferta = new Oferta();
            oferta.setEdytowano(new Date());
            oferta.setPojazd(pojazd);
            ofertaService.recalculateAndSave(oferta);
            model.addAttribute("ofertaForm", oferta);
            return oferta;
        });

        return PagesConstants.EDYCJA_OFERTA_PAGE;
    }

}