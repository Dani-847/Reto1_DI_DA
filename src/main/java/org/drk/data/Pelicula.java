package org.drk.data;

/**
 * Clase de modelo que representa una película en el sistema.
 */
public class Pelicula {

    private int id;
    private String titulo;
    private int anio;
    private String director;
    private String descripcion;
    private String genero;
    private String imagenUrl;
    private int usuarioId; // referencia al usuario propietario

    public Pelicula() {}

    public Pelicula(int id, String titulo, int anio, String director,
                    String descripcion, String genero, String imagenUrl, int usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.director = director;
        this.descripcion = descripcion;
        this.genero = genero;
        this.imagenUrl = imagenUrl;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
}
