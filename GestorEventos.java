package Main.Modelos;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class GestorEventos {
    private ArrayList<Evento> eventos;

    public GestorEventos() {
        this.eventos = new ArrayList<>();
    }

    public void agregarEvento(Evento evento) {
        this.eventos.add(evento);
        guardarEventos();
    }

    public void cargarEventos() {
        try (BufferedReader reader = new BufferedReader(new FileReader("eventos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Suponemos que el formato es: nombre|fecha|ubicacion|descripcion
                String[] partes = line.split("\\|");
                Evento evento = new Evento(partes[0], new Date(Long.parseLong(partes[1])), partes[2], partes[3]);
                eventos.add(evento);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar eventos: " + e.getMessage());
        }
    }

    private void guardarEventos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("eventos.txt"))) {
            for (Evento evento : eventos) {
                writer.println(evento.getNombre() + "|" + evento.getFecha().getTime() + "|" + evento.getUbicacion() + "|" + evento.getDescripcion());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar eventos: " + e.getMessage());
        }
    }

    public void mostrarEventos() {
        for (Evento evento : eventos) {
            System.out.println(evento);
        }
    }

    public void registrarAsistenteEnEvento(Evento evento, Asistente asistente) {
        evento.registrarAsistente(asistente);
        enviarNotificacion(asistente, evento);
        guardarEventos();
    }

    public void asignarRecursoAEvento(Evento evento, Recurso recurso) {
        evento.agregarRecurso(recurso);
        guardarEventos();
    }

    // Método para enviar una notificación a un asistente
    private void enviarNotificacion(Asistente asistente, Evento evento) {
        System.out.println("Enviando notificación a " + asistente.getCorreo() + " para el evento: " + evento.getNombre());
    }

    // Método para generar un informe básico de asistencia a los eventos
    public void generarInformeAsistencia() {
        System.out.println("Informe de Asistencia:");
        for (Evento evento : eventos) {
            System.out.println(evento.getNombre() + " - Asistentes: " + evento.getAsistentes().size());
        }
    }

	public ArrayList<Evento> getEventos() {
		return eventos;
	}
}
