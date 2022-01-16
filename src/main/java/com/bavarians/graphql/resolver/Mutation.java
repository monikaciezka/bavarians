package com.bavarians.graphql.resolver;

import com.bavarians.graphql.model.Klient;
import com.bavarians.service.impl.DefaultUserDetailsService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {
    private DefaultUserDetailsService defaultUserDetailsService;

    @Autowired
    public Mutation(DefaultUserDetailsService defaultUserDetailsService) {
        this.defaultUserDetailsService = defaultUserDetailsService;
    }


    public Klient createKlient(String imie, String nazwisko, String haslo) {
        Klient klient = new Klient();
        klient.setImie(imie);
        klient.setNazwisko(nazwisko);
        klient.setHaslo(haslo);
        defaultUserDetailsService.save(klient);
        return klient;
    }
}
