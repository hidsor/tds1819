package application.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.persistence.*;

public class CatalogoUsuarios {
	// ATRIBUTOS
	private Map<String, Usuario> usuarios;
	private static CatalogoUsuarios unicaInstancia = null;
	
	// necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	
	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null)
			return new CatalogoUsuarios();
		else
			return unicaInstancia;
	}
	
	// CONSTRUCTOR
	private CatalogoUsuarios() {
		try {
			dao = FactoriaDAO.getInstancia();
			adaptadorUsuario = dao.getUsuarioDAO();
			usuarios = new HashMap<String, Usuario>();
			this.cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	// MÉTODOS GET
	
	public Usuario getUsuario(String login) {
		return usuarios.get(login);
	}


	//FUNCIONALIDAD
	public boolean addUsuario(Usuario usuario) {
		// Imitamos un set
		if (usuarios.get(usuario.getLogin()) != null) return false;
		usuarios.put(usuario.getLogin(), usuario);
		return true;
	}
	
	public void removeUsuario(Usuario usuario) {
		usuarios.remove(usuario.getLogin());
	}
	
	private void cargarCatalogo() throws DAOException {
		List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		for (Usuario u : usuariosBD)
			usuarios.put(u.getLogin(), u);
	}
	

}
