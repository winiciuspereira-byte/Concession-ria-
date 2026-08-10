package com.marcelogomes.concessionaria.repository;

import com.marcelogomes.concessionaria.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
}
