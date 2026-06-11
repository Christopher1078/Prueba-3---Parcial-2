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
                    case 2:
                        em.employeeList();
                        break;
                    case 3:
                        System.out.println("Codigo: ");
                        int code=lea.nextInt();
                        System.out.println("Monto: ");
                        double monto=lea.nextDouble();
                        em.addSaleToEmployee(code, monto);
                        break;
                    case 4:
                        System.out.println("Codigo: ");
                        code=lea.nextInt();
                        em.payEmployee(code);
                        break;
                    case 5:
                        System.out.println("Codigo: ");
                        code=lea.nextInt();
                        em.fireEmployee(code);
                        break;
                    case 6:
                        System.out.println("Codigo: ");
                        code=lea.nextInt();
                        em.printEmployee(code);
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
