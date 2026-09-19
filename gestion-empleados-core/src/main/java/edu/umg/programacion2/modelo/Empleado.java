package edu.umg.programacion2.modelo;

public class Empleado {
    // Aquí van los datos del empleado tal cual los guarda la base de datos
    private int id;
    private String nombreCompleto;
    private String departamento;
    private double salario;
    private String fechaContratacion;
    private boolean activo;
    private String tipoContrato;
    
    // Este constructor se usa cuando creamos uno nuevo (sin ID porque la BD se lo pone solito)
    public Empleado(String nombreCompleto, String departamento, double salario, String fechaContratacion, boolean activo, String tipoContrato) {
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        this.tipoContrato = tipoContrato;
    }
    
    // Este otro se usa cuando lo traemos de la BD (aquí sí lleva ID porque ya existe en la tabla)
    public Empleado(int id, String nombreCompleto, String departamento, double salario, String fechaContratacion, boolean activo, String tipoContrato) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        this.tipoContrato = tipoContrato;
    }

    // Getters para poder consultar los datos desde el DAO o las ventanas
    public int getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getDepartamento() { return departamento; }
    public double getSalario() { return salario; }
    public String getFechaContratacion() { return fechaContratacion; }
    public boolean isActivo() { return activo; }
    public String getTipoContrato() {return tipoContrato;}
}