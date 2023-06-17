package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;

import java.io.*;

public class Proyecto3 {
    public static void main(String args[]) throws IOException {

        if(args[2].equals("1")){
            byte[][] byteArray=leerArchivo();
            Dibujante dibujante=new Dibujante(byteArray);
            System.out.print(dibujante.dibujaLaberintoSolucion());
        }
        else{
            CreadorLaberintos cr = new CreadorLaberintos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            byte [] m = cr.generarLaberinto();
            byte[] a={0x4d,0x41,0x5a,0x45,(byte) Integer.parseInt(args[0]),(byte)Integer.parseInt(args[1])};
            OutputStream outputStream = new BufferedOutputStream(System.out);
            try {
                outputStream.write(a);
                outputStream.write(m);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputStream.flush();
        }
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
