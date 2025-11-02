package org.drk.data;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CsvDataService implements DataService {

    private final String archivo;
    private static int lastId = -1;
    private static final Logger logger = Logger.getLogger(CsvDataService.class.getName());

    public CsvDataService(String csvFile) {
        archivo = csvFile;
    }

    @Override
    public List<Pelicula> findAll() {
        var salida = new ArrayList<Pelicula>();
        int maxId = -1;

        logger.info("Abriendo archivo");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            br.lines().forEach(line -> {
                String[] lineArray = line.split(",");
                if (lineArray.length < 8) {
                    logger.severe("Linea mal formada");
                } else {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(Integer.parseInt(lineArray[0].trim()));
                    pelicula.setTitulo(lineArray[1].trim());
                    pelicula.setAño(Integer.parseInt(lineArray[2].trim()));
                    pelicula.setDirector(lineArray[3].trim());
                    pelicula.setDescripcion(lineArray[4].trim());
                    pelicula.setGenero(lineArray[5].trim());
                    pelicula.setImagenUrl(lineArray[6].trim());
                    pelicula.setUsuarioId(Integer.parseInt(lineArray[7].trim()));
                    salida.add(pelicula);
                }
            });
            for (Pelicula p : salida) {
                if (p.getId() > maxId) maxId = p.getId();
            }
            lastId = Math.max(lastId, maxId);
            logger.info("lastId actualizado a: " + lastId);

        } catch (FileNotFoundException e) {
            // Si no existe el archivo, la lista queda vacía y lastId no cambia
            logger.warning("Archivo no encontrado: " + archivo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    @Override
    public Optional<Pelicula> save(Pelicula pelicula) {
        logger.info("Abriendo el archivo para escribir (append)");
        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), true))) {
            lastId = lastId < 0 ? 0 : lastId + 1;
            pelicula.setId(lastId);
            logger.info("Asignando nuevo id: " + lastId);

            String salida = new StringBuilder()
                    .append(pelicula.getId()).append(",")
                    .append(pelicula.getTitulo()).append(",")
                    .append(pelicula.getAño()).append(",")
                    .append(pelicula.getDirector()).append(",")
                    .append(pelicula.getDescripcion()).append(",")
                    .append(pelicula.getGenero()).append(",")
                    .append(pelicula.getImagenUrl()).append(",")
                    .append(pelicula.getUsuarioId())
                    .toString();

            bfw.write(salida);
            bfw.newLine();
            logger.info("Pelicula guardada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(pelicula);
    }

    @Override
    public boolean deleteById(int id) {
        logger.info("Eliminando pelicula id=" + id);
        List<Pelicula> todas = findAll();
        boolean removed = todas.removeIf(p -> p.getId() == id);
        if (!removed) return false;

        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), false))) {
            for (Pelicula p : todas) {
                String linea = new StringBuilder()
                        .append(p.getId()).append(",")
                        .append(p.getTitulo()).append(",")
                        .append(p.getAño()).append(",")
                        .append(p.getDirector()).append(",")
                        .append(p.getDescripcion()).append(",")
                        .append(p.getGenero()).append(",")
                        .append(p.getImagenUrl()).append(",")
                        .append(p.getUsuarioId())
                        .toString();
                bfw.write(linea);
                bfw.newLine();
            }
            logger.info("Archivo reescrito sin la pelicula eliminada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Recalcular lastId al máximo id actual del archivo
        findAll();
        return true;
    }
}
