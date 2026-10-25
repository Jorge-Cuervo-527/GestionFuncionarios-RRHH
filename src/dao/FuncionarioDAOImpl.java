package dao;

import modelo.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAOImpl implements FuncionarioDAO {
    
    private Connection con;

    // Al construir esta clase, le pasamos la conexión a la base de datos
    public FuncionarioDAOImpl(Connection con) {
        this.con = con;
    }

    @Override
    public void crear(Funcionario f) throws Exception {
        // Usamos ? para parametrizar la consulta y evitar Inyección SQL
        String sql = "INSERT INTO funcionario (id_tipo, numero_identificacion, nombres, apellidos, id_estado, sexo, direccion, telefono, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Usamos prepStmt
        try (PreparedStatement prepStmt = con.prepareStatement(sql)) {
            
            prepStmt.setInt(1, f.getIdTipo());
            prepStmt.setString(2, f.getNumeroIdentificacion());
            prepStmt.setString(3, f.getNombres());
            prepStmt.setString(4, f.getApellidos());
            prepStmt.setInt(5, f.getIdEstado());
            prepStmt.setString(6, f.getSexo());
            prepStmt.setString(7, f.getDireccion());
            prepStmt.setString(8, f.getTelefono());
            
            // Convertimos la fecha de Java a fecha de SQL
            java.sql.Date fechaSql = new java.sql.Date(f.getFechaNacimiento().getTime());
            prepStmt.setDate(9, fechaSql);
            
            prepStmt.executeUpdate();
            
        } catch (SQLException e) {
            // Regla "Fallar cerrado": atrapamos el error y lanzamos una excepción clara
            e.printStackTrace(); 
            throw new Exception("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public List<Funcionario> listar() throws Exception {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        
        
        try (PreparedStatement prepStmt = con.prepareStatement(sql);
             ResultSet rs = prepStmt.executeQuery()) {
             
            while (rs.next()) {
                Funcionario f = new Funcionario();
                
                f.setIdFuncionario(rs.getInt("id_funcionario"));
                f.setIdTipo(rs.getInt("id_tipo"));
                f.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                f.setNombres(rs.getString("nombres"));
                f.setApellidos(rs.getString("apellidos"));
                f.setIdEstado(rs.getInt("id_estado"));
                f.setSexo(rs.getString("sexo"));
                f.setDireccion(rs.getString("direccion"));
                f.setTelefono(rs.getString("telefono"));
                f.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                
                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al listar los datos: " + e.getMessage());
        }
        
        return lista;
    }

    @Override
    public void editar(Funcionario f) throws Exception {
        String sql = "UPDATE funcionario SET id_tipo=?, numero_identificacion=?, nombres=?, apellidos=?, id_estado=?, sexo=?, direccion=?, telefono=?, fecha_nacimiento=? WHERE id_funcionario=?";
        try (PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setInt(1, f.getIdTipo());
            prepStmt.setString(2, f.getNumeroIdentificacion());
            prepStmt.setString(3, f.getNombres());
            prepStmt.setString(4, f.getApellidos());
            prepStmt.setInt(5, f.getIdEstado());
            prepStmt.setString(6, f.getSexo());
            prepStmt.setString(7, f.getDireccion());
            prepStmt.setString(8, f.getTelefono());
            prepStmt.setDate(9, new java.sql.Date(f.getFechaNacimiento().getTime()));
            prepStmt.setInt(10, f.getIdFuncionario()); // El ID va al final para el WHERE
            
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al editar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idFuncionario) throws Exception {
        String sql = "DELETE FROM funcionario WHERE id_funcionario=?";
        try (PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setInt(1, idFuncionario);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al eliminar: " + e.getMessage());
        }
    }
}
