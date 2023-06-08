package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    private static int be(byte a, byte b, byte c, byte d){
	return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }
    
    private static int le(byte a, byte b, byte c, byte d){
	return ((a & 0xFF)) + ((b & 0xFF) << 8) + ((c & 0xFF) << 16) + ((d & 0xFF) << 24);
    }

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
       int r = 0;
       int i = 0;
       int l = llave.length;
       while (l >= 4) {
	   r ^= be(llave[i], llave[i+1], llave[i+2], llave[i+3]);
	   i += 4;
	   l -= 4;
       }
       int t = 0;
       switch (l) {
       case 3:
	   t |= (llave[i+2] & 0xFF) << 8;
       case 2:
	   t |= (llave[i+1] & 0xFF) << 16;
       case 1:
	   t |= (llave[i] & 0xFF)   << 24;
       }
       r ^= t;
       return r;
    }

    private static int[] mezcla(int a, int b, int c){
	a-=b;
	a-=c;
	a^=(c>>>13);
	b-=c;
	b-=a;
	b^=(a<<8);
	c-=a;
	c-=b;
	c^=(b>>>13);
	////////////////////////////////////////
	a-=b;
	a-=c;
	a^=(c>>>12);
	b-=c;
	b-=a;
	b^=(a<<16);
	c-=a;
	c-=b;
	c^=(b>>>5);
	////////////////////////////////////////
	a-=b;
	a-=c;
	a^=(c>>>3);
	b-=c;
	b-=a;
	b^=(a<<10);
	c-=a;
	c-=b;
	c^=(b>>>15);

	int r[]={a,b,c};
	return r;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
	int a = 0x9e3779B9;
	int b = 0x9e3779b9;
	int c = 0xFFFFFFFF;
	int l = llave.length;
	int i = 0;
	int[] r;
	while(l>=12){
	    a+=le(llave[i],llave[i+1],llave[i+2],llave[i+3]);
	    b+=le(llave[i+4],llave[i+5],llave[i+6],llave[i+7]);
	    c+=le(llave[i+8],llave[i+9],llave[i+10],llave[i+11]);
	    r=mezcla(a,b,c);
	    a=r[0];
	    b=r[1];
	    c=r[2];
	    l-=12;
	    i+=12;
	}
	c += llave.length;
	switch (l) {
        case 11:
	    c += (llave[i + 10] & 0xFF) << 24;
        case 10:
	    c += (llave[i + 9] & 0xFF) << 16;
        case 9:
	    c += (llave[i + 8] & 0XFF) << 8;
        case 8:
	    b += (llave[i + 7] & 0xFF) << 24;
        case 7:
	    b += (llave[i + 6] & 0xFF) << 16;
        case 6:
	    b += (llave[i + 5] & 0xFF) << 8;
        case 5:
	    b += (llave[i + 4] & 0xFF);
        case 4:
	    a += (llave[i + 3] & 0xFF) << 24;
        case 3:
	    a += (llave[i + 2] & 0xFF) << 16;
        case 2:
	    a += (llave[i + 1] & 0xFF) << 8;
        case 1:
	    a += (llave[i] & 0xFF);
	    break;
	}
	r = mezcla(a, b, c);
	return r[2];
      
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
	int h = 5381;
        for (int i = 0; i < llave.length; i++)
	    h += (h << 5) + (llave[i] & 0xFF);
        return h;
    }
}
