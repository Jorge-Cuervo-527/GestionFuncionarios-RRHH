package vista;

import dao.FuncionarioDAO;
import dao.FuncionarioDAOImpl;
import modelo.Conexion;
import modelo.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VistaFuncionario extends JFrame {

    private FuncionarioDAO dao;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtIdTipo, txtIdentificacion, txtNombres, txtApellidos, txtIdEstado, txtSexo, txtDireccion, txtTelefono, txtfechaNacimiento;
    private JButton btnGuardar, btnListar, btnEditar, btnEliminar;

    public VistaFuncionario() {
        Connection con = Conexion.obtenerConexion();
        dao = new FuncionarioDAOImpl(con);

        setTitle("Gestión de Funcionarios - RRHH");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR (Contenedor de campos y botones) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        
        // 1. Panel solo para los campos de texto 
        JPanel panelCampos = new JPanel(new GridLayout(5, 4, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
        panelCampos.add(new JLabel("ID Tipo Doc:"));
        txtIdTipo = new JTextField();
        panelCampos.add(txtIdTipo);

        panelCampos.add(new JLabel("Identificación:"));
        txtIdentificacion = new JTextField();
        panelCampos.add(txtIdentificacion);

        panelCampos.add(new JLabel("Nombres:"));
        txtNombres = new JTextField();
        panelCampos.add(txtNombres);

        panelCampos.add(new JLabel("Apellidos:"));
        txtApellidos = new JTextField();
        panelCampos.add(txtApellidos);

        panelCampos.add(new JLabel("ID Estado Civil:"));
        txtIdEstado = new JTextField();
        panelCampos.add(txtIdEstado);

        panelCampos.add(new JLabel("Sexo (M/F):"));
        txtSexo = new JTextField();
        panelCampos.add(txtSexo);
        
        panelCampos.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelCampos.add(txtTelefono);
        
        panelCampos.add(new JLabel("Dirección"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Fecha Nac. (AAAA-MM-DD):"));
        txtfechaNacimiento = new JTextField();
        panelCampos.add(txtfechaNacimiento);

        panelCampos.add(new JLabel(""));
        panelCampos.add(new JLabel(""));

        // 2. Panel solo para los botones (Centrados)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnGuardar = new JButton("Guardar Funcionario");
        btnListar = new JButton("Listar Funcionarios");
        btnEditar = new JButton("Editar Seleccionado");
        btnEliminar = new JButton("Eliminar Seleccionado");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnListar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // organizamos los paneles
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA DE DATOS ---
        String[] columnas = {"ID", "ID Tipo", "Identidad", "Nombres", "Apellidos", "ID Est", "Sexo", "Dirección", "Teléfono", "Fecha Nac"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Evento para que al hacer clic en la tabla se llenen las cajas de texto
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtIdTipo.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtIdentificacion.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtNombres.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtApellidos.setText(modeloTabla.getValueAt(fila, 4).toString());
                    txtIdEstado.setText(modeloTabla.getValueAt(fila, 5).toString());
                    txtSexo.setText(modeloTabla.getValueAt(fila, 6).toString());
                    txtDireccion.setText(modeloTabla.getValueAt(fila, 7).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 8).toString());
                    
                    
                    if (modeloTabla.getValueAt(fila, 9) != null) {
                        txtfechaNacimiento.setText(modeloTabla.getValueAt(fila, 9).toString());
                    } else {
                        txtfechaNacimiento.setText("");
                    }
                }
            }
        });

        // --- EVENTOS DE LOS BOTONES ---
        
        btnListar.addActionListener(eListar -> cargarDatos());

        btnGuardar.addActionListener(eGuardar -> {
            String identificacion = txtIdentificacion.getText();
            String telefono = txtTelefono.getText();

            if (!identificacion.matches("^[0-9]{5,15}$") || !telefono.matches("^[0-9]{7,10}$")) {
                JOptionPane.showMessageDialog(this, "Identificación (5-15) y Teléfono (7-10) deben ser solo números.");
                return; 
            }

            try {
                Funcionario f = new Funcionario();
                f.setIdTipo(Integer.parseInt(txtIdTipo.getText()));
                f.setNumeroIdentificacion(identificacion);
                f.setNombres(txtNombres.getText());
                f.setApellidos(txtApellidos.getText());
                f.setIdEstado(Integer.parseInt(txtIdEstado.getText()));
                f.setSexo(txtSexo.getText());
                f.setDireccion(txtDireccion.getText()); 
                f.setTelefono(telefono);
                java.sql.Date fechaSQL = java.sql.Date.valueOf(txtfechaNacimiento.getText());
                f.setFechaNacimiento(fechaSQL); 
                
                dao.crear(f);
                JOptionPane.showMessageDialog(this, "¡Funcionario guardado exitosamente!");
                cargarDatos(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(eEliminar -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un funcionario de la tabla primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            try {
                dao.eliminar(id);
                JOptionPane.showMessageDialog(this, "Funcionario eliminado.");
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(eEditar -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un funcionario de la tabla primero.");
                return;
            }
            
            String idenEdit = txtIdentificacion.getText();
            String telEdit = txtTelefono.getText();
            
            if (!idenEdit.matches("^[0-9]{5,15}$") || !telEdit.matches("^[0-9]{7,10}$")) {
                JOptionPane.showMessageDialog(this, "Identificación (5-15) y Teléfono (7-10) deben ser solo números.");
                return;
            }

            try {
                Funcionario f = new Funcionario();
                f.setIdFuncionario((int) modeloTabla.getValueAt(fila, 0));
                f.setIdTipo(Integer.parseInt(txtIdTipo.getText()));
                f.setNumeroIdentificacion(idenEdit);
                f.setNombres(txtNombres.getText());
                f.setApellidos(txtApellidos.getText());
                f.setIdEstado(Integer.parseInt(txtIdEstado.getText()));
                f.setSexo(txtSexo.getText());
                f.setDireccion(txtDireccion.getText());
                f.setTelefono(telEdit);
                java.sql.Date fechaSQL = java.sql.Date.valueOf(txtfechaNacimiento.getText());
                f.setFechaNacimiento(fechaSQL);
                
                dao.editar(f);
                JOptionPane.showMessageDialog(this, "Funcionario actualizado.");
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0); 
            List<Funcionario> lista = dao.listar();
            for (Funcionario f : lista) {
                Object[] fila = {
                    f.getIdFuncionario(), 
                    f.getIdTipo(), 
                    f.getNumeroIdentificacion(), 
                    f.getNombres(), 
                    f.getApellidos(), 
                    f.getIdEstado(), 
                    f.getSexo(), 
                    f.getDireccion(), 
                    f.getTelefono(), 
                    f.getFechaNacimiento()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaFuncionario().setVisible(true);
        });
    }
}