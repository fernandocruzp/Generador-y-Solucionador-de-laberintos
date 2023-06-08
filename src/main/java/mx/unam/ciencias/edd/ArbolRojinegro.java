package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */

public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */

    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
	    color=Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
	    String str=elemento.toString()+"}";
	    if(color==Color.ROJO)
		str="R{"+str;
	    else
		str="N{"+str;
	    return str;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice){
	if(vertice==null)
	    return Color.NEGRO;
	Color color=((VerticeRojinegro)vertice).color;
	return color;
    }

    private boolean esRojo(VerticeRojinegro vertice){
	return (vertice !=null && vertice.color==Color.ROJO);
    }

    private boolean esIzquierdo(VerticeRojinegro vertice){
	if(!vertice.hayPadre())
	    return false;
	return vertice.padre.izquierdo==vertice;
    }
    private boolean esDerecho(VerticeRojinegro vertice){
	if(!vertice.hayPadre())
	    return false;
	return vertice.padre.derecho==vertice;
    }
    private VerticeRojinegro obtenPadre(VerticeRojinegro vertice){
	return (VerticeRojinegro)vertice.padre;
    }
    private VerticeRojinegro obtenAbuelo(VerticeRojinegro vertice){
	if(vertice.hayPadre())
	    return (VerticeRojinegro)vertice.padre.padre;
	return null;
    }
    private VerticeRojinegro obtenTio(VerticeRojinegro vertice){
	if(!vertice.hayPadre())
	    return null;
	if(obtenAbuelo(vertice)==null)
		return null;
	else
	    if(esIzquierdo((VerticeRojinegro)vertice.padre))
		return (VerticeRojinegro)obtenAbuelo(vertice).derecho;
	    else if(esDerecho((VerticeRojinegro)vertice.padre))
		return (VerticeRojinegro)obtenAbuelo(vertice).izquierdo;
	return null;
    }
    
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	VerticeRojinegro v= (VerticeRojinegro) ultimoAgregado;
	v.color=Color.ROJO;
	agregar(v);

    }
    
    private void agregar(VerticeRojinegro vertice){
	if(!vertice.hayPadre())
	    vertice.color=Color.NEGRO;
	else if(getColor(vertice.padre)==Color.NEGRO)
	    return;
	else{
	    VerticeRojinegro padre=obtenPadre(vertice);
	    VerticeRojinegro abuelo=obtenAbuelo(vertice);
	    VerticeRojinegro tio=obtenTio(vertice);
	    if(getColor(tio)==Color.ROJO){
		if(tio!=null) tio.color=Color.NEGRO;
		padre.color=Color.NEGRO;
		abuelo.color=Color.ROJO;
		agregar(abuelo);
	    }
	    else{
		if((esDerecho(padre)&&esIzquierdo(vertice)) || (esIzquierdo(padre)&&esDerecho(vertice))){
		    if(esDerecho(padre))
			super.giraDerecha(padre);
		    else
			super.giraIzquierda(padre);
		    VerticeRojinegro aux=padre;
		    padre=vertice;
		    vertice=aux;
		}
		padre.color=Color.NEGRO;
		abuelo.color=Color.ROJO;
		if(esDerecho(vertice))
		    super.giraIzquierda(abuelo);
		else
		    super.giraDerecha(abuelo);
	    }
	}
		
	    
    }

    private VerticeRojinegro obtenHijo(VerticeRojinegro v){
	if(v.izquierdo!=null)
	    return (VerticeRojinegro) v.izquierdo;
	if(v.derecho!= null)
	    return (VerticeRojinegro) v.derecho;
	return null;
    }
    private boolean tieneHermano(VerticeRojinegro v){
	if(esIzquierdo(v))
	    return v.padre.derecho!=null;
	else if(esDerecho(v))
	    return v.padre.izquierdo!=null;
	else
	    return false;
    }
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
	if(busca(elemento)==null)
	    return;
	VerticeRojinegro v=(VerticeRojinegro)busca(elemento);
	VerticeRojinegro hijo=obtenHijo(v);
	VerticeRojinegro fantasma=null;
	elementos--;
	if(hijo==null){
	    fantasma=(VerticeRojinegro)nuevoVertice(null);
	    fantasma.color=Color.NEGRO;
	    v.izquierdo=fantasma;
	    fantasma.padre=v;
	    hijo=fantasma;
	}
	else if(tieneHermano(hijo)){
	    v=(VerticeRojinegro)intercambiaEliminable(v);
	    hijo=obtenHijo(v);
	    if(hijo==null){
		fantasma=(VerticeRojinegro)nuevoVertice(null);
		fantasma.color=Color.NEGRO;
		v.izquierdo=fantasma;
		fantasma.padre=v;
		hijo=fantasma;
	    }
	}
	eliminaVertice(v);
	if(hijo.color==Color.ROJO)
	    hijo.color=Color.NEGRO;
	else if(v.color==Color.ROJO){
	    if(fantasma!=null){
		if(esIzquierdo(fantasma))
		   fantasma.padre.izquierdo=null;
		else
		    fantasma.padre.derecho=null;
	    }
	    return;
	}
	else
	    balanceoElimina(hijo);
	if(fantasma!=null){
	    if(fantasma.padre==null){
		fantasma=null;
		hijo=null;
		raiz=null;
	    }
	    else
	        if(esIzquierdo(fantasma))
		   fantasma.padre.izquierdo=null;
		else
		    fantasma.padre.derecho=null;
	}
    }
    private VerticeRojinegro obtenHermano(VerticeRojinegro v){
	if(esIzquierdo(v))
	    return (VerticeRojinegro) v.padre.derecho;
	else if(esDerecho(v))
	    return (VerticeRojinegro) v.padre.izquierdo;
	else
	    return null;
    }
    private void balanceoElimina(VerticeRojinegro vertice){
	if(!vertice.hayPadre())
	    return;
	else{
	    VerticeRojinegro padre=obtenPadre(vertice);
	    VerticeRojinegro hermano=obtenHermano(vertice);
	    if(getColor(hermano)==Color.ROJO){
		padre.color=Color.ROJO;
		hermano.color=Color.NEGRO;
		if(esIzquierdo(vertice))
		    super.giraIzquierda(padre);
		else if(esDerecho(vertice))
		    super.giraDerecha(padre);
	    }
	    hermano=obtenHermano(vertice);
	    VerticeRojinegro hi=(VerticeRojinegro) hermano.izquierdo;
	    VerticeRojinegro hd=(VerticeRojinegro) hermano.derecho;
	    if(hermano.color==Color.NEGRO && getColor(hi)==Color.NEGRO && getColor(hd)==Color.NEGRO && padre.color==Color.NEGRO){
		hermano.color=Color.ROJO;
		balanceoElimina(padre);
	    }
	    else if(hermano.color==Color.NEGRO && getColor(hi)==Color.NEGRO && getColor(hd)==Color.NEGRO && padre.color==Color.ROJO){
		hermano.color=Color.ROJO;
		padre.color=Color.NEGRO;
	    }
	    else{
		if((esIzquierdo(vertice)&&esRojo(hi)&&!esRojo(hd))||(esDerecho(vertice)&&!esRojo(hi)&&esRojo(hd))){
		    hermano.color=Color.ROJO;
		    if(esRojo(hi))
			hi.color=Color.NEGRO;
		    if(esRojo(hd))
			hd.color=Color.NEGRO;
		    if(esIzquierdo(vertice))
			super.giraDerecha(hermano);
		    else if(esDerecho(vertice))
			super.giraIzquierda(hermano);
		    
		}
		hermano=obtenHermano(vertice);
		hi=(VerticeRojinegro) hermano.izquierdo;
		hd=(VerticeRojinegro) hermano.derecho;
		hermano.color=padre.color;
		padre.color=Color.NEGRO;
		if(esIzquierdo(vertice)){
		    if(hd!=null)
			hd.color=Color.NEGRO;
		    super.giraIzquierda(padre);
		}
		else if(esDerecho(vertice)){
		    if(hi!=null)
			hi.color=Color.NEGRO;
		    super.giraDerecha(padre);
		}
		
	    }
	}
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
