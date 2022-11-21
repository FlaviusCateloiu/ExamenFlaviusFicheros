import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.xml.bind.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Mundial {

    public static List<Piloto> leerPilotos() {
        List<Piloto> pilotos = new ArrayList<>();
        Path nombreFichero = Path.of("data","pilotos.xml");
        JAXBContext context = null;

        try {
            context = JAXBContext.newInstance(Pilotos.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            Pilotos pil = (Pilotos) jaxbUnmarshaller.unmarshal(nombreFichero.toFile());

            pilotos = pil.getPilotos();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return pilotos;
    }
    
    public static List<Circuito> leerCircuitos() throws ParseException {
        List<Circuito> circuitos = new ArrayList<>();
        List<List<String>> circuitosString = new ArrayList<>();
        Circuito circuito;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        LocalDate fecha;

        try {
            circuitosString = Files.lines(Paths.get("data/circuitos.csv")).map(linea -> Arrays.asList(linea.split(","))).skip(1).toList();
        } catch (IOException e) {
            System.err.println("Error no se puede encuantrar el archivo");
        }

        for (List<String> linea : circuitosString) {
            circuito = new Circuito();
            circuito.setRonda(Integer.parseInt(linea.get(0)));
            circuito.setPais(linea.get(1));
            fecha = LocalDate.parse(linea.get(2), formatter);
            circuito.setFechaCarrera(fecha);

            circuitos.add(circuito);
        }

        return circuitos;
    }

    public static List<Resultado> leerResultados(List<Circuito> circuitos, List<Piloto> pilotos) {
        Path nombreFichero = Path.of("data/resultados.json");
        List<Resultado> resultados = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
            SimpleModule module = new SimpleModule("CustomPiloto", new Version(1, 0, 0, null, null, null));
            module.addDeserializer(Resultado.class, new CustomPiloto(Resultado.class, pilotos, circuitos));
            objectMapper.registerModule(module);
            resultados = objectMapper.readValue(nombreFichero.toFile(), new TypeReference<List<Resultado>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultados;
    }

    public static void imprimirClasificacionFinal(List<Resultado> resultados) {

    }

    public static void imprimirMayoresde30(List<Piloto> pilotos) {

    }

    public static void main(String[] args) throws ParseException {
        List<Piloto> pilotos = leerPilotos();
		// System.out.println(pilotos);

        //List<Circuito> circuitos = leerCircuitos();
		//System.out.println(circuitos);

        //List<Resultado> resultados = leerResultados(circuitos, pilotos);

        //imprimirClasificacionFinal(resultados);

        //imprimirMayoresde30(pilotos);
    }
}
