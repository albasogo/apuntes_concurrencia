package concurrencia;

import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Para asegurarnos que todos los pacientes son atendidos en el orden de prioridad
 * se realiza una cola de prioridad  a la que se le añaden los objetos pacientes 
 * y se añade la interfaz Compareble para establecer el orden de los pacientes por
 * orden descendente. 
 * 
 */
class PacienteOK extends Thread implements Comparable<PacienteOK> {

    
    private final String enfermedad;
    private int prioridad;

    public PacienteOK(String nombre, String enfermedad) {
        super(nombre);
        this.enfermedad = enfermedad;
    }

    @Override
    public void run() {
        try {
            System.out.printf("Paciente %s con prioridad %d está siendo atendido%n", this.getName(), this.getPriority());
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Paciente %s ha sido atendido%n", this.getName());
        System.out.println(String.join("", Collections.nCopies(70, "-")));
    }
    
    public void asignarPrioridad(int prioridad) {
        this.setPriority(prioridad);
        this.prioridad = prioridad; 
    }

    public String solicitarAyuda() {
        return String.format("Hola soy %s y tengo %s. Necesito que me atiendan", this.getName(), this.enfermedad);
    }
    
    public String getEnfermedad() {
        return enfermedad;
    }


    @Override
    public int compareTo(PacienteOK o) {
        return Integer.compare(o.getPriority(), this.prioridad);
    }
}

public class UrgenciasOK {

    public final static int PRIORIDAD_BAJA = 1;
    public final static int PRIORIDAD_MEDIA = 4;
    public final static int PRIORIDAD_ALTA = 7;
    public final static int PRIORIDAD_MUY_ALTA = 10;

    private UrgenciasOK() {
    }
    
    public static void recibirPacientes(PacienteOK paciente, PriorityBlockingQueue<PacienteOK> queue) {
        cribadoPacientes(paciente);
        queue.add(paciente); // Añadir a la cola después de asignar la prioridad
    }
    
    public static void cribadoPacientes(PacienteOK paciente) {
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

    public static void main(String[] args) {
        
     

        PriorityBlockingQueue<PacienteOK> queue = new PriorityBlockingQueue<>();

        PacienteOK paciente1 = new PacienteOK("Maria", "GRIPE");
        PacienteOK paciente2 = new PacienteOK("Carlos", "NEUMONIA");
        PacienteOK paciente3 = new PacienteOK("Ana", "ROTURA_HUESO");
        PacienteOK paciente4 = new PacienteOK("Pedro", "INFARTO");

        recibirPacientes(paciente1, queue);
        recibirPacientes(paciente2, queue);
        recibirPacientes(paciente3, queue);
        recibirPacientes(paciente4, queue);

        while (!queue.isEmpty()) {
            try {
                PacienteOK paciente = queue.take();
                paciente.start();
                paciente.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos los pacientes han sido atendidos.");
    }
}
