package empresa;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Empresa {
    public static void main(String[] args) {
        Scanner lea=new Scanner(System.in);
        EmpleadoManager em=new EmpleadoManager();
        
        int opcion=0;
        try{
            do{
                System.out.println("\n\nMENU\n");
                System.out.println("1- Agregar Empleado");
                System.out.println("2- Listar Empleados No Despedidos");
                System.out.println("3- Agregar Venta a Empleado");
                System.out.println("4- Pagar Empleado");
                System.out.println("5- Despedir Empleado");
                System.out.println("6- Mostrar Empleado");
                System.out.println("7- Salir");
                System.out.println("Escoja una opcion: ");
                opcion=lea.nextInt();
            
                switch(opcion){
                    case 1:
                        lea.nextLine();
                        System.out.println("Nombre: ");
                        String nombre=lea.nextLine();
                        System.out.println("Salario: ");
                        double salario=lea.nextDouble();
                        em.addEmployees(nombre, salario);
                        break;
                }
            }while(opcion!=7);
        }catch(IOException e){
            System.out.println("Surgio un error: "+e.getMessage());
        }catch(InputMismatchException e){
            System.out.println("Ingrese un numero entero");
        }
    }
    
}
