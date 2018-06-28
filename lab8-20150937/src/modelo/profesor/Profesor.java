/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.profesor;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Profesor {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY) private Long codigo;
    @Persistent private String nombre;
    @Persistent private String apellido;
    @Persistent private String profesion;
    @Persistent private String direccion;
    @Persistent private String email;
    @Persistent private boolean estado;
    public Profesor(String nombre, String apellidos, String profesion, String direccion, String email,boolean estado) {
    	
        this.nombre = nombre;
        this.apellido = apellidos;
        this.profesion = profesion;
        this.direccion = direccion;
        this.email = email;
        this.estado = estado;
    }
    
    public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Long getCodigo() {
        return codigo;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellidos) {
        this.apellido = apellidos;
    }
    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getProfesion() {
        return profesion;
    }
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEstadoDescripcion(){
    	if(estado)
    		return "ACTIVO";
    	return "INACTIVO";
    }
    
    
    
}
