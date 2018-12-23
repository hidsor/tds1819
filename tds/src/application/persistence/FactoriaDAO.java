package application.persistence;

// Define una factoría abstracta que devuelve todos los DAO de la aplicación

public abstract class FactoriaDAO {
	// Atributos
	private static FactoriaDAO unicaInstancia;
	
	public static final String DAO_TDS = "application.persistence.TDSFactoriaDAO";
		
	/** 
	 * Crea un tipo de factoria DAO.
	 * Solo existe el tipo TDSFactoriaDAO
	 */

	public static FactoriaDAO getInstancia(String tipo) throws DAOException{
		if (unicaInstancia == null)
			try { unicaInstancia=(FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {	
				throw new DAOException(e.getMessage());
			} 
		return unicaInstancia;
	}


	public static FactoriaDAO getInstancia() throws DAOException{
			if (unicaInstancia == null) return getInstancia (FactoriaDAO.DAO_TDS);
					else return unicaInstancia;
	}

	protected FactoriaDAO (){}
		
	// Metodos factoría que devuelven adaptadores que implementen estas interfaces
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorListaVideosDAO getListaVideosDAO();
	public abstract IAdaptadorVideoDAO getVideoDAO();

}
