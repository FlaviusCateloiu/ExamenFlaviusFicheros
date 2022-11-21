import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "drivers")
public class Pilotos {
    @XmlElement(name = "driver")
    private ArrayList<Piloto> pilotos = new ArrayList<>();

    public Pilotos() {
    }

    public ArrayList<Piloto> getPilotos() {
        return pilotos;
    }

    public void setPilotos(ArrayList<Piloto> pilotos) {
        this.pilotos = pilotos;
    }
}
