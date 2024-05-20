## LiterAlura

LiterAlura es una aplicación de backend desarrollada como solución al challenge propuesto por Alura Latam. Esta aplicación proporciona funcionalidades para gestionar un catálogo de libros, permitiendo a los usuarios realizar diversas operaciones como búsqueda por título, listar libros registrados, listar autores, buscar autores vivos en un año específico y filtrar libros por idioma. Además, maneja la posibilidad de errores al buscar libros inexistentes y evita la inserción duplicada de libros en la base de datos.

### Tecnologías utilizadas

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL

### Instrucciones de instalación

1. Clona este repositorio en tu máquina local.
2. Abre el proyecto en tu IDE preferido.
3. Configura tu base de datos PostgreSQL y asegúrate de que la configuración en el archivo `application.properties` sea correcta.
4. Ejecuta la aplicación.

### Uso

Una vez que la aplicación esté en funcionamiento, puedes acceder a las siguientes funcionalidades desde la consola:

- **Buscar libro por título:** Ingresa el nombre del libro que deseas buscar. Si el libro existe en la API, será registrado en la base de datos.
- **Listar libros registrados:** Muestra todos los libros registrados en la base de datos.
- **Listar autores registrados:** Muestra todos los autores registrados en la base de datos.
- **Listar autores vivos en un año específico:** Ingresa el año y muestra los autores que estaban vivos en ese año.
- **Listar libros por idioma:** Ingresa el código de idioma (ES, EN, FR o PT) y muestra los libros en ese idioma registrados en la base de datos.
- **Funcionalidades Extra:** Generar Estadisticas, Top 10 Libros Mas Descargados de La BD, Busqueda de autores por nombre y otras mas.
### Contribuciones

¡Las contribuciones son bienvenidas! Si tienes alguna idea de mejora o alguna funcionalidad adicional que te gustaría implementar, no dudes en abrir un pull request.

### Contacto

Si tienes alguna pregunta o sugerencia, no dudes en ponerte en contacto conmigo.
