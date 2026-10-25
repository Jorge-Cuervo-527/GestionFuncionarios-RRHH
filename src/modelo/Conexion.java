package modelo;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static String URL;
    private static String USUARIO;
    private static String CONTRASENA;

    // Bloque estático para cargar las credenciales de forma segura al iniciar la clase
static {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            URL = prop.getProperty("URL_DB");
            USUARIO = prop.getProperty("USUARIO_DB");
            CONTRASENA = prop.getProperty("CONTRASENA_DB");
        } catch (Exception ex) {
            System.err.println("Error al cargar el archivo config.properties: " + ex.getMessage());
        }
    }

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("¡Conexión exitosa a la base de datos!");
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return conexion;
    }
}