package application.model;



public class RolPremium {
	// ATRIBUTOS
	private Filtro filtroPremium;
	
	
	// CONSTRUCTOR
	public RolPremium(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}
	
	public RolPremium() {
		this(new NoFiltro());
	}


	// MÉTODO DE OBTENCIÓN
	public Filtro getFiltro() {
		return filtroPremium;
	}
	
	public void setFiltro(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}

	
	// FUNCIONALIDAD
}
