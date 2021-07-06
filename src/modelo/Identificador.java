package modelo;

public class Identificador {
	private String id;
	private String tipo;
	private String valor;
	
	
	public Identificador(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	@Override
	public String toString() {
		return "Identifcador [id=" + id + ", tipo=" + tipo + ", valor=" + valor + "]";
	}
	
	
	
	
	

}
