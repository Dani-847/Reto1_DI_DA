package org.drk.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación de DataService que usa ficheros CSV para la persistencia.
 * Estructura del CSV:
 * id,titulo,anio,director,descripcion,genero,imagenUrl,usuarioId
 */
public class CsvDataService implements DataService {

    private final Path peliculasPath;

    public CsvDataService(String csvPath) {
        this.peliculasPath = Paths.get(csvPath);
    }

    @Override
    public List<Pelicula> leerPeliculas() throws IOException {
        if (!Files.exists(peliculasPath)) {
            return new ArrayList<>();
        }

        try (BufferedReader br = Files.newBufferedReader(peliculasPath)) {
            return br.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parsePelicula)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Pelicula> leerPeliculasPorUsuario(int usuarioId) throws IOException {
        List<Pelicula> todas = leerPeliculas();
        return todas.stream()
                .filter(p -> p.getUsuarioId() == usuarioId)
                .collect(Collectors.toList());
    }

    @Override
    public void agregarPelicula(Pelicula pelicula) throws IOException {
        // genera un ID único si no está definido
        if (pelicula.getId() == 0) {
            pelicula.setId(generarIdUnico());
        }

        String linea = String.join(",",
                String.valueOf(pelicula.getId()),
                escape(pelicula.getTitulo()),
                String.valueOf(pelicula.getAnio()),
                escape(pelicula.getDirector()),
                escape(pelicula.getDescripcion()),
                escape(pelicula.getGenero()),
                escape(pelicula.getImagenUrl()),
                String.valueOf(pelicula.getUsuarioId())
        );

        Files.write(peliculasPath,
                (linea + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public void eliminarPelicula(int id) throws IOException {
        if (!Files.exists(peliculasPath)) return;

        List<String> todas = Files.readAllLines(peliculasPath);
        List<String> filtradas = todas.stream()
                .filter(line -> {
                    Pelicula p = parsePelicula(line);
                    return p != null && p.getId() != id;
                })
                .map(this::lineaPelicula)
                .collect(Collectors.toList());

        Files.write(peliculasPath, filtradas,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Convierte una línea CSV en un objeto Pelicula.
     */
    private Pelicula parsePelicula(String line) {
        try {
            String[] parts = line.split(",", -1);
            if (parts.length < 8) return null;

            int id = Integer.parseInt(parts[0].trim());
            String titulo = parts[1].trim();
            int anio = Integer.parseInt(parts[2].trim());
            String director = parts[3].trim();
            String descripcion = parts[4].trim();
            String genero = parts[5].trim();
            String imagenUrl = parts[6].trim();
            int usuarioId = Integer.parseInt(parts[7].trim());

            return new Pelicula(id, titulo, anio, director, descripcion, genero, imagenUrl, usuarioId);

        } catch (Exception e) {
            System.err.println("Error al parsear línea CSV: " + line);
            return null;
        }
    }

    /**
     * Genera un ID único incremental.
     */
    private int generarIdUnico() throws IOException {
        List<Pelicula> peliculas = leerPeliculas();
        return peliculas.stream()
                .mapToInt(Pelicula::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Serializa una película a línea CSV.
     */
    private String lineaPelicula(Pelicula p) {
        return String.join(",",
                String.valueOf(p.getId()),
                escape(p.getTitulo()),
                String.valueOf(p.getAnio()),
                escape(p.getDirector()),
                escape(p.getDescripcion()),
                escape(p.getGenero()),
                escape(p.getImagenUrl()),
                String.valueOf(p.getUsuarioId())
        );
    }

    /**
     * Escapa las comas o saltos de línea para mantener la integridad del CSV.
     */
    private String escape(String text) {
        if (text == null) return "";
        return text.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }
}
