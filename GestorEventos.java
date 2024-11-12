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
        eventos.clear();
        
        // Implementacion de flujo de E/S para guardar la info en el archivo eventos.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("eventos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split("\\|");
                
                // Carga de datos básicos del evento
                String nombre = partes[0];
                Date fecha = new Date(Long.parseLong(partes[1]));
                String ubicacion = partes[2];
                String descripcion = partes[3];
                Evento evento = new Evento(nombre, fecha, ubicacion, descripcion);

                // Carga de asistentes
                if (partes.length > 4 && partes[4].startsWith("Asistentes:")) {
                    String[] asistentesData = partes[4].substring(11).split(";");
                    for (String asistenteData : asistentesData) {
                        if (!asistenteData.isEmpty()) {
                            String[] asistenteParts = asistenteData.split(",");
                            Asistente asistente = new Asistente(asistenteParts[0], asistenteParts[1]);
                            evento.registrarAsistente(asistente);
                        }
                    }
                }

                // Carga de recursos
                if (partes.length > 5 && partes[5].startsWith("Recursos:")) {
                    String[] recursosData = partes[5].substring(9).split(";");
                    for (String recursoData : recursosData) {
                        if (!recursoData.isEmpty()) {
                            String[] recursoParts = recursoData.split(",");
                            Recurso recurso = new Recurso(recursoParts[0], recursoParts[1]);
                            evento.agregarRecurso(recurso);
                        }
                    }
                }

                eventos.add(evento);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar eventos: " + e.getMessage());
        }
    }

    private void guardarEventos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("eventos.txt"))) {
            for (Evento evento : eventos) {
                // Guardamos el evento con sus atributos
                writer.print(evento.getNombre() + "|" + evento.getFecha().getTime() + "|" + evento.getUbicacion() + "|" + evento.getDescripcion());
                
                // Guardamos los asistentes
                writer.print("|Asistentes:");
                for (Asistente asistente : evento.getAsistentes()) {
                    writer.print(asistente.getNombre() + "," + asistente.getCorreo() + ";");
                }
                
                // Guardamos los recursos
                writer.print("|Recursos:");
                for (Recurso recurso : evento.getRecursos()) {
                    writer.print(recurso.getTipo() + "," + recurso.getDescripcion() + ";");
                }
                
                writer.println();
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

    // Enviar una notificación por mail al nuevo participante
    private void enviarNotificacion(Asistente asistente, Evento evento) {
        System.out.println("Enviando notificación a " + asistente.getCorreo() + " para el evento: " + evento.getNombre());
    }

	public ArrayList<Evento> getEventos() {
		return eventos;
	}
}
