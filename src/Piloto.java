import jakarta.xml.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "driver")
public class Piloto {

    @XmlAttribute(name = "number")
    private String numero;
    @XmlElement(name = "name")
    private String nombre;
    @XmlElement(name = "team")
    private String equipo;
    @XmlElement(name = "dob")
    private String fechaNacimiento;

    // Getters
    public String getNombre() { return nombre; }
    public String getNumero() { return numero;}
    public String getEquipo() { return equipo; }
    public String getFechaNacimiento() { return fechaNacimiento; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    @Override
    public String toString() {
        return "Piloto{" +
                "nombre='" + nombre + '\'' +
                ", numero=" + numero +
                ", equipo='" + equipo + '\'' +
                ", fechaNacimiento=" + fechaNacimiento/*.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))*/ +
                '}';
    }
}
