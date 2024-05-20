package com.literalura.challenge.principal;

import com.literalura.challenge.model.*;
import com.literalura.challenge.repository.AutorRepository;
import com.literalura.challenge.repository.LibroRepository;
import com.literalura.challenge.service.ConsumoAPI;
import com.literalura.challenge.service.ConvierteDatos;
import com.literalura.challenge.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConvierteDatos conversor = new ConvierteDatos();
    Scanner teclado = new Scanner(System.in);

    @Autowired
    private LibroService service;


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
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresPorAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    generarEstadisticasDeLibrosBD();
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

    private void listarLibrosRegistrados() {
        List<Libro> librosRegistrados = service.listarLibrosRegistrados();
        librosRegistrados.forEach(l -> System.out.println(
                "----- LIBRO -----" +
                        "\nTitulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getIdioma().getIdiomaOmdb() +
                        "\nNumero de descargas: " + l.getNumeroDeDescargas() +
                        "\n-----------------\n"
        ));
    }

    private void listarAutoresRegistrados(){
        List<Autor> autoresRegistrados = service.listarAutoresRegistrados();
        if (autoresRegistrados.isEmpty()){
            System.out.println("Auno no hay autores registrados en la BD");
        }else {
            autoresRegistrados.forEach(a -> System.out.println(
                    "----- AUTORES -----" +
                            "\nNombre: " + a.getNombre() +
                            "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                            "\nFecha de fallecimiento: " + a.getFechaDeFallecimiento() +
                            "\nLibros: " + a.getLibros() +
                            "\n-----------------\n"
            ));
        }
    }

    private void listarAutoresPorAño() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar Ejemplo:1619");
        try {
            String entrada = teclado.nextLine();
            var añoIngresado = Integer.valueOf(entrada);
            List<Autor> autoresPorAño = service.listarAutoresPorAño(añoIngresado);
            if(!autoresPorAño.isEmpty()){
                autoresPorAño.forEach(a -> System.out.println(
                        "----- AUTORES -----" +
                                "\nNombre: " + a.getNombre() +
                                "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                                "\nFecha de fallecimiento: " + a.getFechaDeFallecimiento() +
                                "\nLibros: " + a.getLibros() +
                                "\n-----------------\n"
                ));
            }else {
                System.out.println("No hay autores vivos en el año ingresado que se encuentren registrados en la BD");
            }
        }catch (NumberFormatException e){
            System.out.println("Introduce un año valido");
        }catch (InputMismatchException e){
            System.out.println("Favor de ingresar numeros de 4 cifras");
        }catch (Exception e){
            System.out.println("Ocurrio un error Inesperado"+e.getMessage() );
        }
    }

    private void listarLibrosPorIdioma(){
        var menuIdiomas = """
                Ingrese el idioma para buscar los libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;
        System.out.println(menuIdiomas);
        var idiomaIngresado = teclado.nextLine();
        if(idiomaIngresado.equalsIgnoreCase("es") || idiomaIngresado.equalsIgnoreCase("en")
                || idiomaIngresado.equalsIgnoreCase("fr") || idiomaIngresado.equalsIgnoreCase("pt")){
            Idioma idiomaSeleccionado = Idioma.fromString(idiomaIngresado);
            List<Libro> librosPorIdioma = service.listarLibrosPorIdioma(idiomaSeleccionado);
            if(librosPorIdioma.isEmpty()){
                System.out.println("No hay libros registrados en ese idioma en la BD");
            }else {
                librosPorIdioma.forEach(l -> System.out.println(
                        "----- LIBRO -----" +
                                "\nTitulo: " + l.getTitulo() +
                                "\nAutor: " + l.getAutor().getNombre() +
                                "\nIdioma: " + l.getIdioma().getIdiomaOmdb() +
                                "\nNumero de descargas: " + l.getNumeroDeDescargas() +
                                "\n-----------------\n"
                ));
            }
        } else {
            System.out.println("Por favor selecciona un idioma valido");
        }
    }

    public void generarEstadisticasDeLibrosBD() {

        DoubleSummaryStatistics est = service.obtenerEstadisticasDeDescargas();
        System.out.println("Estadísticas de descargas de los libros de la BD");
        System.out.println("Cantidad Media de Descargas: " + est.getAverage());
        System.out.println("Cantidad Máxima de Descargas: " + est.getMax());
        System.out.println("Cantidad Mínima de Descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para cálculos estadísticos: " + est.getCount());
    }





//        //top 10 libros
//        System.out.println("Top 10 libros mas descargados");
//        datos.resultados().stream()
//                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
//                .limit(10)
//                .map(l -> l.titulo().toUpperCase())
//                .forEach(System.out::println);
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
