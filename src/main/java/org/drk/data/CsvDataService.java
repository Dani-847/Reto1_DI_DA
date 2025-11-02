package org.drk.data;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CsvDataService implements DataService {

    private final String archivo;
    private static Integer lastId = -1;
    private static final Logger logger = Logger.getLogger(CsvDataService.class.getName());

    public CsvDataService(String csvFile) {
        archivo = csvFile;
    }

    @Override
    public List<Pelicula> findAll() {
        var salida = new ArrayList<Pelicula>();

        logger.info("Abriendo archivo");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            var contenido = br.lines();

            contenido.forEach(line -> {
                String[] lineArray = line.split(",");
                if (lineArray.length < 8) {
                    logger.severe("Linea mal formada");
                } else {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(Integer.parseInt(lineArray[0]));
                    pelicula.setTitulo(lineArray[1]);
                    pelicula.setAño(Integer.parseInt(lineArray[2]));
                    pelicula.setDirector(lineArray[3]);
                    pelicula.setDescripcion(lineArray[4]);
                    pelicula.setGenero(lineArray[5]);
                    pelicula.setImagenUrl(lineArray[6]);
                    pelicula.setUsuarioId(Integer.parseInt(lineArray[7]));
                    salida.add(pelicula);
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
    public Optional<Pelicula> save(Pelicula pelicula) {
        logger.info("Abriendo el archivo para escribir");
        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), true))) {
            lastId++;
            pelicula.setId(lastId);
            logger.info("Actualizando id: " + lastId);

            String salida = new StringBuilder()
                    .append(pelicula.getId()).append(",")
                    .append(pelicula.getTitulo()).append(",")
                    .append(pelicula.getAño()).append(",")
                    .append(pelicula.getDirector()).append(",")
                    .append(pelicula.getDescripcion()).append(",")
                    .append(pelicula.getGenero()).append(",")
                    .append(pelicula.getImagenUrl()).append(",")
                    .append(pelicula.getUsuarioId()).toString();

            logger.info("Nueva pelicula creada");
            bfw.write(salida);
            bfw.newLine();
            logger.info("Pelicula guardada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(pelicula);
    }
}
