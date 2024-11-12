package Main;

import javax.swing.*;
import Main.Modelos.Asistente;
import Main.Modelos.Evento;
import Main.Modelos.GestorEventos;
import Main.Modelos.Recurso;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Aplicacion {
    private static GestorEventos gestorEventos = new GestorEventos();

    public static void main(String[] args) {
        gestorEventos.cargarEventos();
        SwingUtilities.invokeLater(Aplicacion::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Gestión de Eventos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Eventos Próximos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        titleLabel.setForeground(Color.DARK_GRAY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Lista de próximos eventos (seteado de fecha de hoy a 3 meses en adelante)
        JList<String> eventosList = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        eventosList.setModel(listModel);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date threeMonthsLater = calendar.getTime();

        // For para filtrar eventos que no son futuros de aca a 3 meses
        for (Evento evento : gestorEventos.getEventos()) {
            if (evento.getFecha().after(currentDate) && evento.getFecha().before(threeMonthsLater)) {
                listModel.addElement(evento.getNombre() + " - " + evento.getFecha());
            }
        }

        JScrollPane scrollPane = new JScrollPane(eventosList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton addEventButton = new JButton("Agregar Evento");
        addEventButton.setBackground(new Color(0x00796B));
        addEventButton.setForeground(Color.WHITE);
        addEventButton.setFocusPainted(false);
        addEventButton.addActionListener(e -> agregarEvento());
        buttonsPanel.add(addEventButton);

        JButton viewAllButton = new JButton("Ver Todos los Eventos");
        viewAllButton.setBackground(new Color(0x00796B));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setFocusPainted(false);
        viewAllButton.addActionListener(e -> verTodosEventos());
        buttonsPanel.add(viewAllButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void agregarEvento() {
        JFrame frame = new JFrame("Agregar Evento");
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nombre del evento:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel dateLabel = new JLabel("Fecha (dd/MM/yyyy):");
        JTextField dateField = new JTextField();
        panel.add(dateLabel);
        panel.add(dateField);

        JLabel locationLabel = new JLabel("Ubicación:");
        JTextField locationField = new JTextField();
        panel.add(locationLabel);
        panel.add(locationField);

        JLabel descriptionLabel = new JLabel("Descripción:");
        JTextField descriptionField = new JTextField();
        panel.add(descriptionLabel);
        panel.add(descriptionField);

        JButton submitButton = new JButton("Agregar Evento");
        submitButton.setBackground(new Color(0x00796B));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String dateStr = dateField.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(dateStr);
                String location = locationField.getText();
                String description = descriptionField.getText();

                Evento evento = new Evento(name, date, location, description);
                gestorEventos.agregarEvento(evento);
                JOptionPane.showMessageDialog(frame, "Evento agregado con éxito.");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al agregar el evento.");
            }
        });

        panel.add(submitButton);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    private static void verTodosEventos() {
        JFrame frame = new JFrame("Todos los Eventos");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        for (Evento evento : gestorEventos.getEventos()) {
            JPanel eventPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel eventLabel = new JLabel(evento.getNombre() + " - " + new SimpleDateFormat("dd/MM/yyyy").format(evento.getFecha()));
            eventPanel.add(eventLabel);

            JButton detailsButton = new JButton("Ver Detalles");
            detailsButton.setBackground(new Color(0x00796B));
            detailsButton.setForeground(Color.WHITE);
            detailsButton.setFocusPainted(false);
            detailsButton.addActionListener(e -> verDetallesEvento(evento));
            eventPanel.add(detailsButton);

            panel.add(eventPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private static void verDetallesEvento(Evento evento) {
        JFrame frame = new JFrame("Detalles del Evento");
        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Nombre del evento:"));
        panel.add(new JLabel(evento.getNombre()));

        panel.add(new JLabel("Fecha:"));
        panel.add(new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(evento.getFecha())));

        panel.add(new JLabel("Ubicación:"));
        panel.add(new JLabel(evento.getUbicacion()));

        panel.add(new JLabel("Descripción:"));
        panel.add(new JLabel(evento.getDescripcion()));

        panel.add(new JLabel("Asistentes:"));
        panel.add(new JLabel(evento.getAsistentes().toString()));

        panel.add(new JLabel("Recursos:"));
        panel.add(new JLabel(evento.getRecursos().toString()));

        JButton addAttendeeButton = new JButton("Agregar Participante");
        addAttendeeButton.setBackground(new Color(0x00796B));
        addAttendeeButton.setForeground(Color.WHITE);
        addAttendeeButton.setFocusPainted(false);
        addAttendeeButton.addActionListener(e -> agregarParticipante(evento));
        panel.add(addAttendeeButton);

        JButton addResourceButton = new JButton("Agregar Recurso");
        addResourceButton.setBackground(new Color(0x00796B));
        addResourceButton.setForeground(Color.WHITE);
        addResourceButton.setFocusPainted(false);
        addResourceButton.addActionListener(e -> agregarRecurso(evento));
        panel.add(addResourceButton);

        JButton editEventButton = new JButton("Editar Evento");
        editEventButton.setBackground(new Color(0x00796B));
        editEventButton.setForeground(Color.WHITE);
        editEventButton.addActionListener(e -> editarEvento(evento));
        panel.add(editEventButton);

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private static void agregarParticipante(Evento evento) {
        String nombre = JOptionPane.showInputDialog("Ingrese nombre y apellido del participante:");
        String email = JOptionPane.showInputDialog("Ingrese email del participante:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Asistente asistente = new Asistente(nombre, email);
            gestorEventos.registrarAsistenteEnEvento(evento, asistente);
            JOptionPane.showMessageDialog(null, "Participante agregado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Nombre inválido.");
        }
    }

    private static void agregarRecurso(Evento evento) {
        String nombre = JOptionPane.showInputDialog("Ingrese tipo de recurso:");
        String descripcion = JOptionPane.showInputDialog("Ingrese descripcion del recurso:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Recurso recurso = new Recurso(nombre, descripcion);
            gestorEventos.asignarRecursoAEvento(evento, recurso);
            JOptionPane.showMessageDialog(null, "Recurso agregado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Nombre inválido.");
        }
    }

    private static void editarEvento(Evento evento) {
        // Obtener los nuevos valores del evento
        String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del evento:", evento.getNombre());
        String nuevaFecha = JOptionPane.showInputDialog("Ingrese la nueva fecha del evento (dd/MM/yyyy):", evento.getFecha());
        String nuevaUbicacion = JOptionPane.showInputDialog("Ingrese la nueva ubicación del evento:", evento.getUbicacion());
        String nuevaDescripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción del evento:", evento.getDescripcion());

        try {
            // Convertir la fecha de String a Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(nuevaFecha);

            // Setear los nuevos valores del evento
            evento.setNombre(nuevoNombre);
            evento.setFecha(date);
            evento.setUbicacion(nuevaUbicacion);
            evento.setDescripcion(nuevaDescripcion);

            JOptionPane.showMessageDialog(null, "Evento editado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al editar la fecha. Asegúrese de ingresar la fecha en formato dd/MM/yyyy.");
        }
    }

}
