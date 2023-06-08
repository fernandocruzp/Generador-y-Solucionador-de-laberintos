package mx.unam.ciencias.edd.proyecto3;

public class Proyecto3 {
    public static void main(String args[]){
        CreadorLaberintos cr = new CreadorLaberintos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        byte [] m = cr.generarLaberinto();
        for(int i=0;i<m.length;i++)
            System.out.println(m[i]);
    }
}
