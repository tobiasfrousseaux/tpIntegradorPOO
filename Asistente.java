package Main.Modelos;

public class Asistente {
    private String nombre;
    private String correo;

    public Asistente(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }

	public String getNombre() {
		return this.nombre;
	}
}
