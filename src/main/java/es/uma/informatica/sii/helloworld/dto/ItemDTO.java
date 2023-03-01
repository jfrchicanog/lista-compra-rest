package es.uma.informatica.sii.helloworld.dto;

public class ItemDTO {
	
	private Long id;
	private String nombre;
	private Float cantidad;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Float getCantidad() {
		return cantidad;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

}
