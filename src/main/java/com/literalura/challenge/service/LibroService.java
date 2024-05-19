package com.literalura.challenge.service;

import com.literalura.challenge.Repository.AutorRepository;
import com.literalura.challenge.Repository.LibroRepository;
import com.literalura.challenge.model.Autor;
import com.literalura.challenge.model.DatosAutor;
import com.literalura.challenge.model.DatosLibro;
import com.literalura.challenge.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;
    public Libro guardarLibroConAutor(DatosLibro datosLibro) {
        // Guardar el autor y devolver la entidad persistida
        Autor autor = guardarAutor(datosLibro);

        // Crear el libro y asignar el autor
        Libro libro = new Libro(datosLibro);
        libro.setAutor(autor);

        // Guardar el libro y devolver la entidad persistida
        return libroRepository.save(libro);
    }


    public Autor guardarAutor(DatosLibro datosLibro) {
        // Asumimos que solo hay un autor
        DatosAutor datosAutor = datosLibro.autor().get(0);

        // Verificar si el autor ya existe en la base de datos
        Optional<Autor> autorExistente = verificarAutorExistenteEnBD(datosAutor.nombre());
        if (autorExistente.isPresent()) {
            // Si el autor ya está en la base de datos, simplemente lo obtenemos
            return autorExistente.get();
        } else {
            // Si el autor no está en la base de datos, lo creamos
            Autor autor = new Autor(datosAutor);
            return autorRepository.save(autor);
        }
    }

    public Optional<Libro> verificarLibroExistenteEnBD(String titulo){
        return libroRepository.findByTitulo(titulo);
    }

    public Optional<Autor> verificarAutorExistenteEnBD(String nombre){
        return autorRepository.findByNombre(nombre);
    }
}
