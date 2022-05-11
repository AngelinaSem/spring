package com.example.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.spring.osnova.Shag;
import com.example.spring.osnova.Pol;
import com.example.spring.rep.Puljparep;
import com.example.spring.rep.Shagjparep;
import com.example.spring.rep.Poljparep;
import com.example.spring.rest.dto.Poldto;
import com.example.spring.rest.dto.Pollogdto;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Polservice {
    
    private final Poljparep polrep;
    private final Shagjparep shagrep;
    private final Puljparep pulrep;

    public Pol addpol(Poldto poldto) {
        Pol pol = new Pol();
        pol.setLogin(poldto.getUsername());
        pol.setFirstName(poldto.getFirstName());
        pol.setLastName(poldto.getLastName());
        pol.setAge(poldto.getAge());
        pol.setWeight(poldto.getWeight());
        pol.setHashPassword(poldto.getPassword().hashCode());
        return polrep.save(pol);
    }

    public String authorize(Pollogdto pollogdto) {
        Pol pol = polrep.findByLogin(pollogdto.getLogin());
        if (pol != null && pol.getHashPassword() == pollogdto.getPassword().hashCode()) {
            return String.format("%s.%d", pol.getLogin(), pol.getHashPassword());
        } else {
            throw new IllegalArgumentException("Wrong Pass or Login!");
        }
    }

    public Integer getTodayshag(String token) {
        Pol pol = checkToken(token);
        Optional<Shag> shagData = pol.getShagData().stream().filter(shag -> shag.getDate().equals(LocalDate.now())).findFirst();
        return shagData.isPresent() ? shagData.get().getShagCount() : 0;
    }

    @Transactional
    public void updateshagCount(String token, Integer shagAmount) {
        Pol pol = checkToken(token);
        Optional<Shag> shagData = pol.getShagData().stream().filter(shag -> shag.getDate().equals(LocalDate.now())).findFirst();
        if (shagData.isEmpty()) {
            Shag shag = new Shag();
            shag.setDate(LocalDate.now());
            shag.setShagCount(shagAmount);
            pol.getShagData().add(shag);
            shag.setPol(pol);
            shagrep.save(shag);
        } else {
            Integer currentAmount = shagData.get().getShagCount();
            shagData.get().setShagCount(currentAmount + shagAmount);
        }
        polrep.save(pol);
    }

    @Transactional
    public void deletepol(String token) {
        Pol pol = checkToken(token);
        polrep.deleteById(pol.getId());
    }

    private Pol checkToken(String token) {
        String[] tokenData = token.split("\\.");
        assert tokenData.length == 2 : "Invalid Token";
        String login = tokenData[0];
        Pol pol = polrep.findByLogin(login);
        assert pol != null : "No such pol";
        assert pol.getHashPassword() == Integer.parseInt(tokenData[1]) : "Wrong password";
        return pol;
    }

    public void deletePol(String token) {
    }

    public void updateShagCount(String token, Integer shagCount) {
    }

}