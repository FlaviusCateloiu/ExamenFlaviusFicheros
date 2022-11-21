import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.xml.bind.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        LocalDate fechaNz;
        JAXBContext context = null;

        /*try {
            context = JAXBContext.newInstance(Pilotos.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            Pilotos pil = (Pilotos) jaxbUnmarshaller.unmarshal(nombreFichero.toFile());

            pilotos = pil.getPilotos();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }*/

        try {
            // DocumentBuilderFactory es una clase especial para crear parsers
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // DocumentBuilder define el parser DOM que se va a utilizar
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // Document es el objeto que contiene la lectura completa del XML
            Document doc = dBuilder.parse(nombreFichero.toFile());
            // Ahora doc tiene el documento en memoria
            doc.getDocumentElement().normalize();

            NodeList listaDrivers = doc.getElementsByTagName("driver");
            for (int i = 0; i < listaDrivers.getLength(); i++) {
                // Node representa a cualquier nodo de un árbol
                Node nodoPiloto = listaDrivers.item(i);
                if (nodoPiloto.getNodeType() == Node.ELEMENT_NODE) {
                    // Element es un tipo de nodo que representa un elemento del XML
                    Element elemPiloto = (Element) nodoPiloto;
                    Piloto l = new Piloto();
                    l.setNumero(Integer.parseInt(elemPiloto.getAttribute("number")));
                    l.setNombre(elemPiloto.getElementsByTagName("name").item(0).getTextContent());
                    l.setEquipo(elemPiloto.getElementsByTagName("team").item(0).getTextContent());
                    String fecha = elemPiloto.getElementsByTagName("dob").item(0).getTextContent();
                    l.setFechaNacimiento(LocalDate.parse(fecha, formatter));

                    pilotos.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println("Pilotos mayores de 30 años:");
        int fechaHoy = LocalDate.now().getYear();
        for (Piloto pil : pilotos) {
            int fechaNaz = pil.getFechaNacimiento().getYear();
            fechaNaz = fechaHoy - fechaNaz;
            if (fechaNaz >= 30) {
                System.out.println(pil.getNombre() + " tiene " + fechaNaz + " años.");
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        List<Piloto> pilotos = leerPilotos();
		 System.out.println(pilotos);

        List<Circuito> circuitos = leerCircuitos();
		System.out.println(circuitos);

        List<Resultado> resultados = leerResultados(circuitos, pilotos);
        System.out.println(resultados);

        imprimirClasificacionFinal(resultados);

        imprimirMayoresde30(pilotos);
    }
}
