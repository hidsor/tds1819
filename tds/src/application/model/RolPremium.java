package application.model;

public class RolPremium {

	// Atributos
	private Filtro filtroPremium;

	// Constructores
	public RolPremium(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}

	public RolPremium() {
		this(new NoFiltro());
	}

	// Métodos de consulta
	public Filtro getFiltro() {
		return filtroPremium;
	}

	public void setFiltro(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}
	// Funcionalidad
}
