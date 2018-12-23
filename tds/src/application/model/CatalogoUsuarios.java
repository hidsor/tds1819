package application.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.persistence.*;

public class CatalogoUsuarios {

	// Atributos
	private Map<String, Usuario> usuarios;
	private static CatalogoUsuarios unicaInstancia = null;

	// Necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	// Patrón singleton
	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new CatalogoUsuarios();
		}
		return unicaInstancia;
	}

	// Constructor
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

	// Métodos de consulta
	public Usuario getUsuario(String login) {
		return usuarios.get(login);
	}

	// Funcionalidad
	public boolean addUsuario(Usuario usuario) {
		// Imitamos un set
		if (usuarios.get(usuario.getLogin()) != null) {
			return false;
		}
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
