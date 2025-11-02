package org.drk.user;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CsvUserService implements UserService {

    private final String archivo;
    private static Integer lastId = -1;
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

        logger.info("Abriendo archivo");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            var contenido = br.lines();

            contenido.forEach(line -> {
                String[] lineArray = line.split(",");
                if (lineArray.length < 3) {
                    logger.severe("Linea mal formada");
                } else {
                    User user = new User();
                    user.setId(Integer.parseInt(lineArray[0]));
                    user.setEmail(lineArray[1]);
                    user.setPassword(lineArray[2]);
                    salida.add(user);
                }
            });
            lastId = salida.size();
            logger.info("Actualizo tamaño: " + lastId);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    @Override
    public Optional<User> save(User user) {
        logger.info("Abriendo el archivo para escribir");
        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), true))) {
            lastId++;
            user.setId(lastId);
            logger.info("Actualizando id: " + lastId);

            String salida = new StringBuilder()
                    .append(user.getId()).append(",")
                    .append(user.getEmail()).append(",")
                    .append(user.getPassword()).toString();

            logger.info("Nuevo usuario creado");
            bfw.write(salida);
            bfw.newLine();
            logger.info("Usuario guardado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(user);
    }
}
