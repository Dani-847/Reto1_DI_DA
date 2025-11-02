package org.drk.user;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CsvUserService implements UserService {

    private final String archivo;
    private static int lastId = -1;
    private static final Logger logger = Logger.getLogger(CsvUserService.class.getName());

    public CsvUserService(String csvFile) {
        archivo = csvFile;
    }

    @Override
    public Optional<User> validate(ArrayList<User> usuarios, String correo, String contraseña) {
        for (User u : usuarios) {
            if (correo.equals(u.getEmail()) && contraseña.equals(u.getPassword())) {
                User user = new User();
                user.setId(u.getId());
                user.setEmail(u.getEmail());
                user.setPassword(u.getPassword());
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        var salida = new ArrayList<User>();
        int maxId = -1;

        logger.info("Abriendo archivo");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            br.lines().forEach(line -> {
                String[] lineArray = line.split(",");
                if (lineArray.length < 3) {
                    logger.severe("Linea mal formada");
                } else {
                    User user = new User();
                    user.setId(Integer.parseInt(lineArray[0].trim()));
                    user.setEmail(lineArray[1].trim());
                    user.setPassword(lineArray[2].trim());
                    salida.add(user);
                }
            });
            for (User u : salida) {
                if (u.getId() > maxId) maxId = u.getId();
            }
            lastId = Math.max(lastId, maxId);
            logger.info("lastId actualizado a: " + lastId);

        } catch (FileNotFoundException e) {
            logger.warning("Archivo no encontrado: " + archivo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    @Override
    public Optional<User> save(User user) {
        logger.info("Abriendo el archivo para escribir");
        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), true))) {
            lastId = lastId < 0 ? 0 : lastId + 1;
            user.setId(lastId);

            String salida = new StringBuilder()
                    .append(user.getId()).append(",")
                    .append(user.getEmail()).append(",")
                    .append(user.getPassword())
                    .toString();

            bfw.write(salida);
            bfw.newLine();
            logger.info("Usuario guardado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(user);
    }
}
