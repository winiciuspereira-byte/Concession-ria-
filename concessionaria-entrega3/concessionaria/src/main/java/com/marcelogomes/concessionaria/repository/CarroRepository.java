package com.marcelogomes.concessionaria.repository;

import com.marcelogomes.concessionaria.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {

    boolean existsByChassi(String chassi);
    boolean existsByPlaca(String placa);

    @Query("SELECT c FROM Carro c WHERE " +
           "(:cor IS NULL OR LOWER(c.cor) = LOWER(:cor)) AND " +
           "(:ano IS NULL OR c.anoModelo = :ano OR c.anoFabricacao = :ano)")
    List<Carro> filtrar(@Param("cor") String cor, @Param("ano") Integer ano);
}
