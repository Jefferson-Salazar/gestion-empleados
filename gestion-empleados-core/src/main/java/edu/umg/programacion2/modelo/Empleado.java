package edu.umg.programacion2.modelo;

public class Empleado {
    private int id;
    private String nombreCompleto;
    private String departamento;
    private double salario;
    private String fechaContratacion;
    private boolean activo;

    public Empleado() {
    }

    public Empleado(int id, String nombreCompleto, String departamento, double salario, String fechaContratacion, boolean activo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public String getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(String fechaContratacion) { this.fechaContratacion = fechaContratacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}