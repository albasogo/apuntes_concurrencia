package concurrencia;

import java.util.Collections;

/**
 * Asignar niveles de prioridad solo sugierer a la JVM que se ejecuten en ese
 * orden pero no lo garantiza. El método yield() que se utiliza para que los
 * hilos con menos prioridad cedan el paso tampoco lo garantiza.
 *
 * Consecuencia: Pedro no siempre es atendido el primero y muere en la sala de
 * urgencias. Los demás pacientes tampoco son asignados correctamente y sufren 
 * las consecuencias, excepto el paciente con catarro que sale pensando que las 
 * urgencias son un servicio para acudir cuando te viene bien.
 *
 * @author malbasogo
 */
class PacienteUD extends Thread {

    private final String enfermedad;

    public PacienteUD(String nombre, String enfermedad) {
        super(nombre);
        this.enfermedad = enfermedad;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(2000);
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Paciente %s ha sido atendido%n", this.getName());

    }

    public void asignarPrioridad(int prioridad) {
        this.setPriority(prioridad);
    }

    public String solicitarAyuda() {
        return String.format("Hola soy %s y tengo %s. Necesito que me atiendan", this.getName(), this.enfermedad);
    }

    public String getEnfermedad() {
        return enfermedad;
    }

}

public class UrgenciasDesastre {

    public final static int PRIORIDAD_BAJA = 1;
    public final static int PRIORIDAD_MEDIA = 4;
    public final static int PRIORIDAD_ALTA = 7;
    public final static int PRIORIDAD_MUY_ALTA = 10;

    private UrgenciasDesastre() {
    }

    public static void recibirPacientes(PacienteUD paciente) {
        paciente.solicitarAyuda();
        cribadoPacientes(paciente);
    }

    public static void cribadoPacientes(PacienteUD paciente) {
        System.out.println(paciente.solicitarAyuda());
        switch (paciente.getEnfermedad()) {
            case "GRIPE":
                paciente.asignarPrioridad(PRIORIDAD_BAJA);
                break;
            case "NEUMONIA":
                paciente.asignarPrioridad(PRIORIDAD_MEDIA);
                break;
            case "ROTURA_HUESO":
                paciente.asignarPrioridad(PRIORIDAD_ALTA);
                break;
            case "INFARTO":
                paciente.asignarPrioridad(PRIORIDAD_MUY_ALTA);
                break;
        }
        System.out.printf("Paciente %s asignado con prioridad %d%n", paciente.getName(), paciente.getPriority());
        System.out.println(String.join("", Collections.nCopies(70, "-")));
    }

    public static void atender(PacienteUD paciente) {
        paciente.start();
    }

    public static void main(String[] args) {

        PacienteUD paciente1 = new PacienteUD("Maria", "GRIPE");
        PacienteUD paciente2 = new PacienteUD("Carlos", "NEUMONIA");
        PacienteUD paciente3 = new PacienteUD("Ana", "ROTURA_HUESO");
        PacienteUD paciente4 = new PacienteUD("Pedro", "INFARTO");

        recibirPacientes(paciente1);
        recibirPacientes(paciente2);
        recibirPacientes(paciente3);
        recibirPacientes(paciente4);

        atender(paciente1);
        atender(paciente2);
        atender(paciente3);
        atender(paciente4);

        try {

            paciente1.join();
            paciente2.join();
            paciente3.join();
            paciente4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Todos los pacientes han sido atendidos pero para algunos fue demasiado tarde.");
    }

}
