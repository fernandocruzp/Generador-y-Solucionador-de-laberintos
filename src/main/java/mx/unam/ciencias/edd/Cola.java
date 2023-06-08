package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        Nodo n=cabeza;
	String cosa="";
	while(n!=null){
	    cosa+=n.elemento+",";
	    n=n.siguiente;
	}
	return cosa;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) throws IllegalArgumentException{
        if(elemento==null) throw new IllegalArgumentException();
	Nodo n=new Nodo(elemento);
	if(rabo==null){
	    cabeza=n;
	    rabo=n;
	}
	else{
	    rabo.siguiente=n;
	    rabo=n;
	}
    }
}
