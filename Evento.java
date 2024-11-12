package Main.Modelos;
import java.util.ArrayList;
import java.util.Date;

public class Evento {
    private String nombre;
    private Date fecha;
    private String ubicacion;
    private String descripcion;
    private ArrayList<Asistente> asistentes;
    private ArrayList<Recurso> recursos;

    public Evento(String nombre, Date fecha, String ubicacion, String descripcion) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.asistentes = new ArrayList<>();
        this.recursos = new ArrayList<>();
    }

    public void registrarAsistente(Asistente asistente) {
        this.asistentes.add(asistente);
    }

    public void agregarRecurso(Recurso recurso) {
        this.recursos.add(recurso);
    }

    public ArrayList<Asistente> getAsistentes() {
        return asistentes;
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    @Override
    public String toString() {
        return "Evento: " + nombre + " | Fecha: " + fecha + " | Ubicación: " + ubicacion + " | Cantidad de asistentes: " + asistentes.size();
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

	public void setUbicacion(String nuevaUbicacion) {
		this.ubicacion = nuevaUbicacion;
	}

	public void setDescripcion(String nuevaDescripcion) {
		this.descripcion = nuevaDescripcion;
		
	}

	public void setNombre(String nuevoNombre) {
		this.nombre = nuevoNombre;
		
	}

	public void setFecha(Date nuevaFecha) {
		this.fecha = nuevaFecha;
		
	}
}
