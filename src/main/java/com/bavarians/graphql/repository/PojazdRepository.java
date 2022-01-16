package com.bavarians.graphql.repository;

import com.bavarians.graphql.model.Pojazd;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PojazdRepository extends PagingAndSortingRepository<Pojazd, Long> {

    //Klient findByEmail(String username);

}