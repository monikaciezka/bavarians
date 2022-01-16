package com.bavarians.graphql.repository;

import com.bavarians.graphql.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    @Query("from oferta o left join pojazd p on o.pojazd=p.id where o.id =?1")
    Oferta findOneWithPojazd(Long id);

    @Query(value = "select o.id from oferta o left join pojazd p on o.pojazd_id=p.id left join element_serwisowy e on e.oferta_id = o.id where o.id =?1 GROUP BY o.id",nativeQuery = true)
    Oferta findOneWithPojazdAndElements(Long id);

    @Query("from oferta o left join pojazd p on o.pojazd = p.id left join element e on e.oferta = o.id where o.id =?1 GROUP BY o.id")
    Oferta findOneWithPojazdAndElements2(Long id);
}