package concurrencia;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Apuntes sobre concurrencia
 *
 * @autor malbasogo
 */
/**
 * En la JVM se ejecuta al menos: 1 hilo para main 1 hilo para el GC
 *
 * Es posible que existan más hilos, por ejemplo:
 *  - Hilos que arranquen en la aplicación mediante start() 
 *  - Los Frameworks AWT y Swing crean hilos de ejecución para los eventos de la IU 
 *  - La clase Timer es un temporizador que crea hilos para ejecución de tareas programadas.
 */
public class Concurrencia extends Thread {

    private static final Object lock = new Object();

    public Concurrencia(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            switch (getName()) {
                case "Hilo 1":
                    synchronized (lock) {
                        unSoloHilo("Tarea del " + getName());
                    }   break;
                case "Hilo 2":
                    synchronized (lock) {
                        unSoloHilo("Tarea del " + getName());
                    }   break;
                case "Hilo 3":
                    synchronized (lock) {
                        unSoloHilo("Tarea del " + getName());
                    }   break;
                default:
                    break;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Concurrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metodoNoSeEjecuta() {
        System.out.println("No es invocado desde run por lo que no se ejecuta");
    }

    public static  void unSoloHilo(String tarea) throws InterruptedException {
        System.out.println(String.join("", Collections.nCopies(70, "-")));
        for (int i = 0; i < 10; i++) {
            System.out.printf("Ejecutando %s: %d %n", tarea, i);
            Thread.sleep(500); // Simula una tarea que toma tiempo y causa espera temporal
        }
    }

    /**
     * Ciclo de vida: 
     *  - Nuevo: instanciado pero no ejecutado 
     *  - Vivo: start() invocado y JVM ha planificado la ejecución tiene 4 subestados: 
     *      - Ejecutable: planificado en JVM y run(ejecutándose) 
     *      - Bloqueado: está bloqueado esperando a que otro hilo libere el cerrojo (lock) del monitor
     *      - Espera: está esperando indefinidamente que otro hilo finalice una acción particular. 
     *      - Espera-temporal: está esperando que otro hilo finalice una acción particular pero en un tiempo definido. 
     * - Terminado: cuando ha finalizado su ejecución.
     *
     */
    public static void main(String[] args) {
        try {
            System.out.println(String.join("", Collections.nCopies(70, "-")));
            System.out.printf("Ciclo de vida%n");
            System.out.println(String.join("", Collections.nCopies(70, "-")));
            Concurrencia hilo1 = new Concurrencia("Hilo 1");
            Concurrencia hilo2 = new Concurrencia("Hilo 2");
            Concurrencia hilo3 = new Concurrencia("Hilo 3");
            System.out.printf(
                    "1) Creo instancias:%n  -%s Estado: %s. ¿Está vivo? : %s %n  -%s Estado: %s. ¿Está vivo? : %s%n  -%s Estado: %s. ¿Está vivo? : %s%n",
                    hilo1.getName(), hilo1.getState(), hilo1.isAlive() ? "SI" : "NO",
                    hilo2.getName(), hilo2.getState(), hilo2.isAlive() ? "SI" : "NO",
                    hilo3.getName(), hilo3.getState(), hilo3.isAlive() ? "SI" : "NO"
            );
            System.out.println(String.join("", Collections.nCopies(70, "-")));
            hilo1.start();
            hilo2.start();
            hilo3.start();
            System.out.printf(
                    "2) Llamo a start:%n  -%s Estado: %s. subestado: %s%n  -%s Estado: %s. subestado: %s%n  -%s Estado: %s. subestado: %s%n",
                    hilo1.getName(), hilo1.isAlive() ? "VIVO" : "TERMINATED", hilo1.getState(),
                    hilo2.getName(), hilo2.isAlive() ? "VIVO" : "TERMINATED", hilo2.getState(),
                    hilo3.getName(), hilo3.isAlive() ? "VIVO" : "TERMINATED", hilo3.getState()
            );

            Thread.sleep(5200);
            System.out.printf(
                    "3) Después de dormir:%n  -%s Estado: %s. subestado: %s%n  -%s Estado: %s. subestado: %s%n  -%s Estado: %s. subestado: %s%n",
                    hilo1.getName(), hilo1.isAlive() ? "VIVO" : "TERMINATED", hilo1.getState(),
                    hilo2.getName(), hilo2.isAlive() ? "VIVO" : "TERMINATED", hilo2.getState(),
                    hilo3.getName(), hilo3.isAlive() ? "VIVO" : "TERMINATED", hilo3.getState()
            );
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Concurrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
