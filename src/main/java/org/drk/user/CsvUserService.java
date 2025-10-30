package org.drk.user;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación de UserService que utiliza un CSV plano.
 * Estructura del archivo:
 * id,email,password
 */
public class CsvUserService implements UserService {

    private final Path usuariosPath;

    public CsvUserService(String csvPath) {
        this.usuariosPath = Paths.get(csvPath);
    }

    @Override
    public List<User> leerUsuarios() throws IOException {
        if (!Files.exists(usuariosPath)) return new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(usuariosPath)) {
            return br.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseUsuario)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public User autenticar(String email, String password) throws IOException {
        return leerUsuarios().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User buscarPorId(int id) throws IOException {
        return leerUsuarios().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void registrarUsuario(User usuario) throws IOException {
        if (usuario.getId() == 0) {
            usuario.setId(generarIdUnico());
        }

        String linea = String.join(",",
                String.valueOf(usuario.getId()),
                escape(usuario.getEmail()),
                escape(usuario.getPassword())
        );

        Files.write(usuariosPath,
                (linea + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // =================== PRIVADOS ===================

    private User parseUsuario(String line) {
        try {
            String[] parts = line.split(",", -1);
            if (parts.length < 3) return null;

            int id = Integer.parseInt(parts[0].trim());
            String email = parts[1].trim();
            String password = parts[2].trim();

            return new User(id, email, password);
        } catch (Exception e) {
            System.err.println("Error al parsear usuario: " + line);
            return null;
        }
    }

    private int generarIdUnico() throws IOException {
        List<User> usuarios = leerUsuarios();
        return usuarios.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }
}
