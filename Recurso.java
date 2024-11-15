package Main.Modelos;

public class Recurso {
    private String tipo;
    private String descripcion;

    public Recurso(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return tipo + " - " + descripcion;
    }

	public String getDescripcion() {
		return this.descripcion;
	}

	public String getTipo() {
		return this.tipo;
	}
}