package modelo;

public class Archivo {
	private String archivo;
	private String ruta;
	
	
	public Archivo(String archivo, String ruta) {
		super();
		this.archivo = archivo;
		this.ruta = ruta;
	}
	
	
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
	
	

}
