package dao;

import modelo.Funcionario;
import java.util.List;

public interface FuncionarioDAO {
    // Definimos las operaciones CRUD básicas y obligamos a que manejen Excepciones
    void crear(Funcionario f) throws Exception;
    List<Funcionario> listar() throws Exception;
    void editar(Funcionario f) throws Exception;
    void eliminar(int idFuncionario) throws Exception;
}
