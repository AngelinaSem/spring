package com.example.spring.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.spring.osnova.Shag;

@Repository
public interface Shagjparep extends JpaRepository<Shag, Long> {
}