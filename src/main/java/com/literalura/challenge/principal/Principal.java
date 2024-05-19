package com.literalura.challenge.principal;

import com.literalura.challenge.Repository.AutorRepository;
import com.literalura.challenge.Repository.LibroRepository;
import com.literalura.challenge.model.Autor;
import com.literalura.challenge.model.Datos;
import com.literalura.challenge.model.DatosLibro;
import com.literalura.challenge.model.Libro;
import com.literalura.challenge.service.ConsumoAPI;
import com.literalura.challenge.service.ConvierteDatos;
import com.literalura.challenge.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConvierteDatos conversor = new ConvierteDatos();
    Scanner teclado = new Scanner(System.in);

    @Autowired
    private LibroService service;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void muestraElMenu(){
        var opcion = -1;
        while(opcion!=0) {
            var menu = """
                -------------
                Elija la opcion a través de su numero:
                1 - Buscar libro por titulo
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                6 - Generar estadisticas
                7 - Top 10 libros
                8 - Buscar autor por nombre
                9 - Listar autores con otras consultas
                0 - Salir
                """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:

                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    private void buscarLibroPorTitulo() {
        Datos datosEncontrados = getDatosLibro();
        Optional<DatosLibro> libroBuscado = datosEncontrados.resultados().stream()
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println(
                    "\n----- LIBRO -----" +
                            "\nTitulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autor().stream()
                            .map(a -> a.nombre()).limit(1).collect(Collectors.joining())+
                            "\nIdioma: " + libroBuscado.get().idiomas() +
                            "\nNumero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                            "\n-----------------\n"
            );
            try{
                DatosLibro libroEncontrado = libroBuscado.get();
                Optional<Libro> libroExistente = service.verificarLibroExistenteEnBD(libroEncontrado.titulo());


                if (libroExistente.isPresent()) {
                    System.out.println("El libro '" + libroEncontrado.titulo() + "' ya se encuentra en la base de datos.");
                }else{
                    Optional<Autor> autorExistente = service.verificarAutorExistenteEnBD(libroEncontrado.autor().stream()
                            .map(a -> a.nombre())
                            .collect(Collectors.joining()));
                    Autor autor= null;
                    if (autorExistente.isPresent()) {
                        System.out.println("El autor '" + autorExistente.get().getNombre() + "' ya se encuentra en la base de datos.");
                }else {
                        autor = service.guardarAutor(libroEncontrado);
                    }
                    Libro libro = new Libro(libroEncontrado);
                    libro.setAutor(autor);
                    libro = service.guardarLibroConAutor(libroEncontrado);

                    System.out.println("Libro guardado: " + libro.getTitulo());
                }


            }catch (Exception e){
                System.out.println("Ocurrio un error:" +e.getMessage());
            }

        }else {
            System.out.println("No se encontro ningun libro");
        }

    }


//        //top 10 libros
//        System.out.println("Top 10 libros mas descargados");
//        datos.resultados().stream()
//                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
//                .limit(10)
//                .map(l -> l.titulo().toUpperCase())
//                .forEach(System.out::println);
//
//        // Busqueda de libro por nombre
//        System.out.println("Ingrese el nombre del libro que desea buscar");
//        var tituloLibro = teclado.nextLine();
//        json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
//        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
//        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
//                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
//                .findFirst();
//        if(libroBuscado.isPresent()){
//            System.out.println("Libro Encontrado: ");
//            System.out.println(libroBuscado.get());
//        }else {
//            System.out.println("Libro no encontrado");
//        }
//
//        //trabajando con estadistica
//        DoubleSummaryStatistics est = datos.resultados().stream()
//                .filter(d -> d.numeroDeDescargas()>0.0)
//                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
//
//        System.out.println("Cantidad Media de Descargas: " + est.getAverage());
//        System.out.println("Cantidad Maxima de Descargas: " + est.getMax());
//        System.out.println("Cantidad Minima de Descargas: " + est.getMin());
//        System.out.println("Cantidad de registros evaluados para calculos estadisticos: " + est.getCount());


}
