package com.bavarians.service;

import com.bavarians.graphql.model.Element;
import com.bavarians.graphql.model.Oferta;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OfertaService {
    Oferta recalculateAndSave(Oferta o);

    Oferta recalculateAndSave(Oferta o, List<Element> elementySerwisowe);

    Oferta findByIdWithElements(Long id);

    Optional<Oferta> findById(Long id);

    void deleteById(Long id);

    List<Oferta> findAll();

    void deleteAll(Set<Oferta> oferty);
}
