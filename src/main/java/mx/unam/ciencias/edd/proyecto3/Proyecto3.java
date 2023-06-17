package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;

import java.io.*;

public class Proyecto3 {
    public static void main(String args[]) throws IOException {

        if(args.length==0){
            segundaOpcion();
        }
        else{
            boolean g=false,col=false,fil=false,ran=false;
            int c=0,f=0;
            long rand=0;
            for(int i=0;i<args.length;i++){
                if(args[i].equals("-g"))
                    g=true;
                if(col){
                    try {
                        c=Integer.parseInt(args[i]);
                        col=false;
                    }catch (NumberFormatException n){
                        System.out.println("Después de -w debes introducir un número");
                        System.exit(0);
                    }

                }
                if(fil){
                    try {
                        f=Integer.parseInt(args[i]);
                        fil=false;
                    }catch (NumberFormatException n){
                        System.out.println("Después de -h debes introducir un número");
                        System.exit(0);
                    }
                }
                if(ran){
                    try {
                        rand=Long.parseLong(args[i]);
                        ran=false;
                    }catch (NumberFormatException n){
                        System.out.println("Después de -s debes introducir un número");
                        System.exit(0);
                    }
                }
                if(args[i].equals("-w")) {
                    col=true;
                }
                if(args[i].equals("-h")) {
                    fil=true;
                }
                if(args[i].equals("-s")) {
                    ran=true;
                }
            }
            if(g) {
                if(c>255 || c < 2 || f<2 || f> 255){
                    System.out.println("El número mínimo de filas y columnas es 2, y el máximo es 255");
                    System.exit(0);
                }
                CreadorLaberintos cr=null;
                if(rand==0)
                    cr = new CreadorLaberintos(c, f);
                else
                    cr = new CreadorLaberintos(c,f,rand);
                byte[] m = cr.generarLaberinto();
                byte[] a = {0x4d, 0x41, 0x5a, 0x45, (byte) c, (byte) f};
                OutputStream outputStream = new BufferedOutputStream(System.out);
                try {
                    outputStream.write(a);
                    outputStream.write(m);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                outputStream.flush();
            }
            else{
                segundaOpcion();
            }
        }
    }

    private static void segundaOpcion(){
        byte[][] byteArray=leerArchivo();
        if(byteArray==null) {
            System.out.println("Debe o incluir el argumento -g para crear laberinto o introducir un laberinto por la entrada estandar");
            System.exit(0);
        }
        Dibujante dibujante=new Dibujante(byteArray);
        System.out.print(dibujante.dibujaLaberintoSolucion());
    }

    private static byte[][] leerArchivo(){
        byte[][] byteArray=null;
        try {
            InputStream inputStream = System.in;

            Lista<Byte> byteList = new Lista<>();

            int byteValor;
            int i=0;
            int m=0,n=0;
            while ((byteValor = inputStream.read()) != -1) {
                byte byteDato = (byte) byteValor;
                if(i==4)
                    m=byteDato;
                if(i==5)
                    n=byteDato;
                if(i>5)
                    byteList.agrega(byteDato);
                i++;
            }
            byteArray = new byte[m][n];
            for (int j = 0; j < byteList.getElementos(); j++) {
                byteArray[j/m][j%m] = byteList.get(j);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
