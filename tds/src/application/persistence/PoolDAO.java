package application.persistence;

// Esta clase implementa un pool para los adaptadores que lo necesiten

import java.util.Hashtable;

public class PoolDAO {
	// Atrbutos
	private static PoolDAO unicaInstancia;
	private Hashtable<Integer, Object> pool;

	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}

	// Patrón singleton
	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new PoolDAO();
		return unicaInstancia;
	}

	// Métodos de consulta
	public Object getObjeto(int id) {
		return pool.get(id);
	} // Devuelve null si no encuentra el objeto

	public void addObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	public boolean contiene(int id) {
		return pool.containsKey(id);
	}

}
