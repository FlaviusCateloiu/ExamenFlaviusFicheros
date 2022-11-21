import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

public class CustomPiloto extends StdDeserializer<Resultado> {
    private List<Piloto> pilotos;
    private List<Circuito> circuitos;

    public CustomPiloto(Class<?> vc, List<Piloto> pilotos, List<Circuito> circuitos) {
        super(vc);
        this.pilotos = pilotos;
        this.circuitos = circuitos;
    }

    @Override
    public Resultado deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Resultado resultado = new Resultado();
        String date;
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        JsonNode trackNode, positionNode, noNode, driverNode, pointsNode;

        trackNode = node.get("Track");
        String circuito = trackNode.asText();
        for (Circuito cir : circuitos) {
            if (cir.getPais().contains(circuito)) {
                resultado.setCircuito(cir);
            }
        }

        driverNode = node.get("Driver");
        String piloto = driverNode.asText();
        for (Piloto pil : pilotos) {
            if (pil.getNombre().contains(piloto)) {
                resultado.setPiloto(pil);
            }
        }

        positionNode = node.get("Position");
        if (!positionNode.asText().contains("NC")) {
            resultado.setPosicion(positionNode.asInt());
        } else {
            resultado.setPosicion(-1);
        }

        pointsNode = node.get("Points");
        resultado.setPuntos(pointsNode.asDouble());

        return resultado;
    }
}
