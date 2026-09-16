package edu.umg.programacion2.modelo;

public class Empleado {
    private int id;
    private String nombreCompleto;
    private String departamento;
    private double salario;
    private String fechaContratacion;
    private boolean activo;

    public Empleado(String nombreCompleto, String departamento, double salario, String fechaContratacion, boolean activo) {
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
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
    public String getNombreCompleto() { return nombreCompleto; }
    public String getDepartamento() { return departamento; }
    public double getSalario() { return salario; }
    public String getFechaContratacion() { return fechaContratacion; }
    public boolean isActivo() { return activo; }
}