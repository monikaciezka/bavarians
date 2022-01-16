package com.bavarians.graphql.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "pojazd")
@Table(name = "pojazd")
public class Pojazd {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numerRejestracyjny")
    private String numerRejestracyjny;

    @Column(name = "marka")
    private String marka;

    @Column(name = "model")
    private String model;

    @Column(name = "vin")
    private String vin;

    @Column(name = "przebieg")
    private String przebieg;

    @Column(name = "edytowano")
    private Date edytowano = new Date();

    @ManyToOne
    @JoinColumn(name = "klient_id")
    private Klient wlasciciel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pojazd")
    private Set<Oferta> oferty = new HashSet<>();

    public Pojazd() {
    }

    public Pojazd(String numerRejestracyjny, String marka, String model, String vin, String przebieg, Klient wlasciciel, Set<Oferta> oferty) {
        this.numerRejestracyjny = numerRejestracyjny;
        this.marka = marka;
        this.model = model;
        this.vin = vin;
        this.przebieg = przebieg;
        this.wlasciciel = wlasciciel;
        this.oferty = oferty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumerRejestracyjny() {
        return numerRejestracyjny;
    }

    public void setNumerRejestracyjny(String numerRejestracyjny) {
        this.numerRejestracyjny = numerRejestracyjny;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPrzebieg() {
        return przebieg;
    }

    public void setPrzebieg(String przebieg) {
        this.przebieg = przebieg;
    }

    public Klient getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(Klient wlasciciel) {
        this.wlasciciel = wlasciciel;
    }

    public Set<Oferta> getOferty() {
        return oferty;
    }

    public void setOferty(Set<Oferta> oferty) {
        this.oferty = oferty;
    }

    public Date getEdytowano() {
        return edytowano;
    }

    public void setEdytowano(Date edytowano) {
        this.edytowano = edytowano;
    }

    @Override
    public String toString() {
        return "Pojazd{" +
                "numerRejestracyjny='" + numerRejestracyjny + '\'' +
                ", marka='" + marka + '\'' +
                ", model='" + model + '\'' +
                ", vin='" + vin + '\'' +
                ", przebieg='" + przebieg + '\'' +
                ", dataModyfikacji=" + edytowano +
                ", wlasciciel=" + wlasciciel +
                '}';
    }
}
