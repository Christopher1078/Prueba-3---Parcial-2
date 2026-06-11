package empresa;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    
    /*
    Formato: 
    1- Codigo.emp
    int code;
    2- Empleado.emp
    int code;
    String name;
    double salary;
    long hdate;
    long tdate;
    
    */
    
    public EmpleadoManager(){
        try{
            File mf=new File("company");
            mf.mkdir();
            rcods=new RandomAccessFile("company/codigo.emp","rw");
            remps=new RandomAccessFile("company/empleado.emp","rw");
            initCodes();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void initCodes() throws IOException{
        if(rcods.length()==0)
            rcods.writeInt(1);
    }
    
    private int getCode()throws IOException{
        rcods.seek(0);
        int code=rcods.readInt();   
        rcods.seek(0);
        rcods.writeInt(code+1);
        return code;
    }
    
    public void addEmployees(String name, double salary)throws IOException{
        remps.seek(remps.length());
        int code=getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(salary);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);
        createEmployeeFolder(code);
        
    }
    
    private String employeeFolder(int code){
        return "company/empleado"+code;
    }
    
    private RandomAccessFile salesFilefor(int code)throws IOException{
        String dirPadre=employeeFolder(code);
        int yearActual=Calendar.getInstance().get(Calendar.YEAR);
        String path=dirPadre+"/ventas"+yearActual+".emp";
        return new RandomAccessFile(path, "rw");
    }
    /*
    Formato
    ventaAnual.emp
     1. double monto;
     2. boolean pago;
    */
    
    private void createSaleFileFor(int code)throws IOException{
        RandomAccessFile ryear=salesFilefor(code);
        if(ryear.length()==0){
            for(int mes=0;mes<12;mes++){
                ryear.writeDouble(0);
                ryear.writeBoolean(false);
            }
        }
    } 
    
    private void createEmployeeFolder(int code)throws IOException{
        File edir=new File(employeeFolder(code));
        edir.mkdir();
        createSaleFileFor(code);
    }
    
    public void employeeList()throws IOException{
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int code=remps.readInt();
            String name=remps.readUTF();
            double sal=remps.readDouble();
            Date fecha=new Date(remps.readLong());
            if(remps.readLong()==0){
                System.out.println(code+"-"+name+"-"+" - Lps. "+sal+"\nContratado el: "+fecha);
            }
        }
    }
    
    private boolean isEmployeeActive(int code)throws IOException{
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codeI=remps.readInt();
            long pos=remps.getFilePointer();
            remps.readUTF();
            remps.skipBytes(16);
            if(remps.readLong()==0 && codeI==code){
                remps.seek(pos);
                return true;
            }
        }
        return false;
    }
    
    public boolean fireEmployee(int code)throws IOException{
        if(isEmployeeActive(code)){
            String name=remps.readUTF();
            remps.skipBytes(16);
            remps.writeLong(new Date().getTime());
            System.out.println("Despidiendo a "+name);
            return true;
        }
        return false;
    }
    
    public void addSaleToEmployee(int code, double monto)throws IOException{
        if(!isEmployeeActive(code))
            return;
        RandomAccessFile ventas=salesFilefor(code);
        int mes=Calendar.getInstance().get(Calendar.MONTH);
        long pos=mes*9;
        ventas.seek(pos);
        double ventaActual=ventas.readDouble();
        ventas.seek(pos);
        ventas.writeDouble(ventaActual+monto);
    }
    
    private RandomAccessFile billsFilefor(int code)throws IOException{
        String path=employeeFolder(code)+"/recibos.emp";
        return new RandomAccessFile(path,"rw");
    }
    
    public void payEmployee(int code)throws IOException{
        if(!isEmployeeActive(code) || isEmployeePayed(code)){
            System.out.println("No se pudo pagar");
            return;
        }
        int year=Calendar.getInstance().get(Calendar.YEAR);
        int mes=Calendar.getInstance().get(Calendar.MONTH);
        
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigo=remps.readInt();
            if(codigo==code)
                break;
            remps.readUTF();
            remps.skipBytes(24);
        }
        
        String nombre=remps.readUTF();
        double salario=remps.readDouble();
        
        RandomAccessFile ventas=salesFilefor(code);
        long pos=mes*9;
        ventas.seek(pos);
        double totalVentas=ventas.readDouble();       
        
        double sueldo=salario+(totalVentas*0.10);
        double deduccion=sueldo*0.035;
        double total=sueldo-deduccion;
        
        RandomAccessFile recibos=billsFilefor(code);
        recibos.seek(recibos.length());
        recibos.writeLong(new Date().getTime());
        recibos.writeDouble(sueldo);
        recibos.writeDouble(deduccion);
        recibos.writeInt(year);
        recibos.writeInt(mes+1);
        ventas.seek(pos+8);
        ventas.writeBoolean(true);
        
        System.out.println("Empleado "+nombre+" se le pago Lps. "+total);
    }
    
    public boolean isEmployeePayed(int code)throws IOException{
        RandomAccessFile ventas=salesFilefor(code);
        int mes=Calendar.getInstance().get(Calendar.MONTH);
        long pos=(mes*9)+8;
        ventas.seek(pos);
        return ventas.readBoolean();
    }
    
    public void printEmployee(int code)throws IOException{
        boolean existe=false;
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigo=remps.readInt();
            if(codigo==code){
                existe=true;
                break;
            }
            remps.readUTF();
            remps.skipBytes(24);
        }
        if(!existe){
            System.out.println("El empleado no existe");
            return;
        }
        
        String nombre=remps.readUTF();
        double salario=remps.readDouble();
        Date fecha=new Date(remps.readLong());
        
        System.out.println("Codigo: "+code);
        System.out.println("Nombre: "+nombre);
        System.out.println("Salario: "+salario);
        System.out.println("Fecha de contratacion: "+fecha);
        
        RandomAccessFile ventas=salesFilefor(code);
        double totalVentas=0;
        System.out.println("\nVENTAS");
        ventas.seek(0);
        for(int i=1;i<12;i++){
            double venta=ventas.readDouble();
            ventas.readBoolean();
            totalVentas+=venta;
            System.out.println("Mes "+i+" : "+venta);
        }
        System.out.println("\nTotal de ventas anual: "+totalVentas);
        
        RandomAccessFile recibos=billsFilefor(code);
        int pagos=0;
        recibos.seek(0);
        
        while(recibos.getFilePointer()<recibos.length()){
            recibos.readLong();
            recibos.readDouble();
            recibos.readDouble();
            recibos.readInt();
            recibos.readInt();
            pagos++;
        }
        
        System.out.println("Total de pagos realizados: "+pagos);
        
    }
}
