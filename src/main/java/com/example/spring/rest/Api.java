package com.example.spring.rest;

import com.example.spring.osnova.Pol;
import com.example.spring.rest.dto.Poldto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.spring.rest.dto.Pollogdto;
import com.example.spring.service.Polservice;

@RestController
@RequestMapping(("/api/v1"))
@RequiredArgsConstructor
public class Api {
    private static final String TOKEN_HEADER = "AUTH-TOKEN-X";

    private final Polservice polservice;

    @PostMapping("/pol")
    public ResponseEntity<String> addPol(@RequestBody Poldto poldto) {
        Pol pol = polservice.addpol(poldto);
        return ResponseEntity.ok().body(String.format("token: %s.%s", pol.getLogin(), pol.getHashPassword()));
    }

    @GetMapping("/pol/authorize")
    public ResponseEntity<String> authorize(@RequestBody Pollogdto loginDto) {
        String token = polservice.authorize(loginDto);
        return ResponseEntity.ok().body(String.format("Successful authorization! Token: %s", token));
    }

    @GetMapping("/shag")
    public ResponseEntity<String> getTodayShag(@RequestHeader(TOKEN_HEADER) String token) {
        Integer shag = polservice.getTodayshag(token);
        return ResponseEntity.ok().body(String.format("Your shag amount for today is %s", shag));
    }

    @PutMapping("/shag")
    public ResponseEntity<Void> updateShagCount(@RequestHeader(TOKEN_HEADER) String token, @RequestParam Integer shagCount) {
        polservice.updateShagCount(token, shagCount);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/pol")
    public ResponseEntity<Void> deletePol(@RequestHeader(TOKEN_HEADER) String token) {
        polservice.deletePol(token);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AssertionError.class)
    public ResponseEntity<String> handleAssertionError(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}