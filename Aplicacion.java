package Main;

import javax.swing.*;

import Main.Modelos.Asistente;
import Main.Modelos.Evento;
import Main.Modelos.GestorEventos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Aplicacion {
    private static GestorEventos gestorEventos = new GestorEventos();

    public static void main(String[] args) {
        gestorEventos.cargarEventos();  // Carga los eventos desde el almacenamiento (ej. archivo o base de datos)
        SwingUtilities.invokeLater(Aplicacion::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Gestión de Eventos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Título de la interfaz con estilo minimalista
        JLabel titleLabel = new JLabel("Eventos Próximos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        titleLabel.setForeground(Color.DARK_GRAY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Lista de eventos próximos
        JList<String> eventosList = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        eventosList.setModel(listModel);
        
        // Filtrar eventos de aquí a 3 meses
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date threeMonthsLater = calendar.getTime();
        
        for (Evento evento : gestorEventos.getEventos()) {
            if (evento.getFecha().after(currentDate) && evento.getFecha().before(threeMonthsLater)) {
                listModel.addElement(evento.getNombre() + " - " + evento.getFecha());
            }
        }

        // Scroll para la lista de eventos
        JScrollPane scrollPane = new JScrollPane(eventosList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Botón para agregar un nuevo evento
        JButton addEventButton = new JButton("Agregar Evento");
        addEventButton.setBackground(new Color(0x00796B));
        addEventButton.setForeground(Color.WHITE);
        addEventButton.setFocusPainted(false);
        addEventButton.addActionListener(e -> agregarEvento());
        buttonsPanel.add(addEventButton);

        // Botón para ver todos los eventos
        JButton viewAllButton = new JButton("Ver Todos los Eventos");
        viewAllButton.setBackground(new Color(0x00796B));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setFocusPainted(false);
        viewAllButton.addActionListener(e -> verTodosEventos());
        buttonsPanel.add(viewAllButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Mostrar la interfaz
        frame.add(panel);
        frame.setVisible(true);
    }

    private static void agregarEvento() {
        // Ventana para agregar un evento nuevo
        JFrame frame = new JFrame("Agregar Evento");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
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
        // Ventana para ver todos los eventos y permitir editarlos/agregar participantes
        JFrame frame = new JFrame("Todos los Eventos");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        for (Evento evento : gestorEventos.getEventos()) {
            JLabel eventoLabel = new JLabel(evento.toString());
            panel.add(eventoLabel);

            JButton editButton = new JButton("Editar");
            editButton.setBackground(new Color(0x00796B));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.addActionListener(e -> editarEvento(evento));
            panel.add(editButton);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private static void editarEvento(Evento evento) {
        // Ventana para editar los atributos de un evento
        JFrame frame = new JFrame("Editar Evento");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        panel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("Nombre del evento:");
        JTextField nameField = new JTextField(evento.getNombre());
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel dateLabel = new JLabel("Fecha (dd/MM/yyyy):");
        JTextField dateField = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(evento.getFecha()));
        panel.add(dateLabel);
        panel.add(dateField);

        JLabel locationLabel = new JLabel("Ubicación:");
        JTextField locationField = new JTextField(evento.getUbicacion());
        panel.add(locationLabel);
        panel.add(locationField);

        JLabel descriptionLabel = new JLabel("Descripción:");
        JTextField descriptionField = new JTextField(evento.getDescripcion());
        panel.add(descriptionLabel);
        panel.add(descriptionField);

        // Botón para agregar un participante dentro del formulario de edición
        JButton addAttendeeButton = new JButton("Agregar Participante");
        addAttendeeButton.setBackground(new Color(0x00796B));
        addAttendeeButton.setForeground(Color.WHITE);
        addAttendeeButton.setFocusPainted(false);
        addAttendeeButton.addActionListener(e -> agregarParticipante(evento));
        panel.add(addAttendeeButton);

        JButton submitButton = new JButton("Guardar Cambios");
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
                
                evento.setNombre(name);
                evento.setFecha(date);
                evento.setUbicacion(location);
                evento.setDescripcion(description);
                JOptionPane.showMessageDialog(frame, "Evento actualizado.");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al actualizar el evento.");
            }
        });
        
        panel.add(submitButton);
        
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    private static void agregarParticipante(Evento evento) {
        // Ventana para agregar un participante a un evento
        JFrame frame = new JFrame("Agregar Participante");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nombre del asistente:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("Correo del asistente:");
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        JButton submitButton = new JButton("Agregar Participante");
        submitButton.setBackground(new Color(0x00796B));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            Asistente asistente = new Asistente(name, email);
            evento.registrarAsistente(asistente);
            JOptionPane.showMessageDialog(frame, "Participante agregado.");
            frame.dispose();
        });
        
        panel.add(submitButton);
        
        frame.add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
