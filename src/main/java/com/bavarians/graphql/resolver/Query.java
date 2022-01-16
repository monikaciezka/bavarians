package com.bavarians.graphql.resolver;

import com.bavarians.graphql.model.Pojazd;
import com.bavarians.graphql.repository.PojazdRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {
    private final PojazdRepository pojazdRepository;

    @Autowired
    public Query(PojazdRepository pojazdRepository) {
        this.pojazdRepository = pojazdRepository;
    }

    public Iterable<Pojazd> findAllPojazd() {
        return pojazdRepository.findAll();
    }


}
