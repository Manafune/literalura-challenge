package com.literalura.challenge.repository;

import com.literalura.challenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <=:añoIngresado AND a.fechaDeFallecimiento > :añoIngresado")
    List<Autor> listarAutoresPorAño(Integer añoIngresado);

}
