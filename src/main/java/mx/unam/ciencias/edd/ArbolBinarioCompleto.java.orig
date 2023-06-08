package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola= new Cola<Vertice>();
	    if(!esVacia()) cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            if(cola.esVacia())
		return false;
	    return true;
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice vertice = cola.saca();
	    if(vertice.hayIzquierdo())
		cola.mete(vertice.izquierdo);
	    if(vertice.hayDerecho())
		cola.mete(vertice.derecho);
	    return vertice.get();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() {
	super();
    }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) throws IllegalArgumentException{
        if(elemento == null)
	    throw new IllegalArgumentException();
	Vertice vertice = nuevoVertice(elemento);
	elementos++;
	if(raiz == null){
	    raiz= vertice;
	    return;
	}
	Cola<Vertice> c = new Cola<Vertice>();
	c.mete(raiz);
	while(!c.esVacia()){
	    Vertice v = c.saca();
	    if(v.izquierdo == null){
		v.izquierdo=vertice;
		vertice.padre=v;
		break;
	    }
	    else
		c.mete(v.izquierdo);
	    if(v.derecho == null){
		v.derecho=vertice;
		vertice.padre=v;
		break;
	    }
	    else
		c.mete(v.derecho);
	}
	
    }
    
    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	Vertice eliminar = (Vertice)busca(elemento);
	if(eliminar == null) return;
	elementos--;
	if(elementos == 0){
	    raiz = null;
	    return;
	}
	Cola<Vertice> cola = new Cola<Vertice>();
	cola.mete(raiz);
	Vertice vertice = null;
	while(!(cola.esVacia())){
	    vertice = cola.saca();
	    if(vertice.hayIzquierdo())
		cola.mete(vertice.izquierdo);
	    if(vertice.hayDerecho())
		cola.mete(vertice.derecho);
	}
	eliminar.elemento = vertice.elemento;
	Vertice padre = vertice.padre;
	if(padre.derecho == null)
	    padre.izquierdo = null;
      else 
	  padre.derecho = null;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
	return super.altura();
	/**int n = elementos;
	int i=0;
	while(n>1){
	    i++;
	    n=n>>1;
	}   
	return i;**/
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(esVacia())
	    return ;
	Cola<Vertice> c=new Cola<Vertice>();
	c.mete(raiz);
	while(!c.esVacia()){
	    Vertice v;
	    v=c.saca();
	    try{
		if(v.hayIzquierdo())
		    c.mete(v.izquierdo);
		if(v.hayDerecho())
		    c.mete(v.derecho);
		accion.actua(v);
	    } catch(Exception e){
		System.out.println(e);
	    }
	}
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
