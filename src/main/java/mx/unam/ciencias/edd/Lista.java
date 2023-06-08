package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
	    this.elemento=elemento;
        }
    }

   /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente!=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() throws NoSuchElementException{
            if(siguiente==null)throw new NoSuchElementException();
	    Nodo p=siguiente;
	    anterior=siguiente;
	    siguiente=p.siguiente;
	    return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior!=null;
        }

	@Override public T previous() throws NoSuchElementException{
	    if(anterior==null) throw new NoSuchElementException(); 
	    Nodo p=anterior;
	    siguiente=anterior;
	    anterior=p.anterior;
	    return siguiente.elemento;
	}
	@Override public void start(){
	    anterior=null;
	    siguiente=cabeza;
	}
	@Override public void end(){
	    anterior=rabo;
	    siguiente=null;
	}
	
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;
    
    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud(){
	return getElementos();
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza==null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) throws IllegalArgumentException{
	if(elemento==null) throw new IllegalArgumentException();
	longitud++;
	Nodo nuevo= new Nodo(elemento);
	if((cabeza==null) & rabo==null){
	    cabeza=nuevo;
	    rabo=nuevo;
	}
	else{
	    rabo.siguiente=nuevo;
	    nuevo.anterior=rabo;
	    rabo=nuevo;
	}
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento)throws IllegalArgumentException{
	if(elemento==null) throw new IllegalArgumentException();
	agrega(elemento);
    }
    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) throws IllegalArgumentException{
	if(elemento==null) throw new IllegalArgumentException();
	longitud++;
	Nodo nuevo= new Nodo(elemento);
	if((cabeza==null) & rabo==null){
	    cabeza=nuevo;
	    rabo=nuevo;
	}
	else{
	    cabeza.anterior=nuevo;
	    nuevo.siguiente=cabeza;
	    cabeza=nuevo;
	}
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    private Nodo buscarIndice(int i){
	Nodo n = cabeza;
	int j=0;
	while(n!=null){
	    if(j==i) return n;
	    j++;
	    n=n.siguiente;
	}
	return n;
    }
    public void inserta(int i, T elemento)throws IllegalArgumentException{
	if(elemento==null) throw new IllegalArgumentException();
	if(i<1){
	    agregaInicio(elemento);
	}
	else if(i>longitud-1){
	    agregaFinal(elemento);
	}
	else{
	    longitud++;
	    Nodo nuevo= new Nodo(elemento);
	    Nodo aux=buscarIndice(i);
	    aux.anterior.siguiente=nuevo;
	    nuevo.anterior=aux.anterior;
	    nuevo.siguiente=aux;
	    aux.anterior=nuevo;
	}
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento){
	Nodo n=buscar(elemento);
	Nodo apoyo;
	if(n==null) return;
	longitud--;
	if(cabeza==rabo){
	    rabo=null;
	    cabeza=null;
	}
	else if(cabeza==n){
	    apoyo=cabeza.siguiente;
	    apoyo.anterior=null;
	    cabeza=apoyo;
	}else if(rabo==n){
	    apoyo=rabo.anterior;
	    apoyo.siguiente=null;
	    rabo=apoyo;
	}
	else{
	    apoyo=n.anterior;
	    apoyo.siguiente=n.siguiente;
	    n.siguiente.anterior=apoyo;
	}
    }
    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() throws NoSuchElementException{
	if(esVacia()) throw new NoSuchElementException();
	T prim=cabeza.elemento;
	elimina(prim);
	return prim;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo()  throws NoSuchElementException{
	if(esVacia()) throw new NoSuchElementException();
	T fina= rabo.elemento;
	//rabo=rabo.anterior;
	//rabo.siguiente=null;
	if(cabeza==rabo){
	    rabo=null;
	    cabeza=null;
	}else{
	    Nodo apoyo=rabo.anterior;
	    apoyo.siguiente=null;
	    rabo=apoyo;

	}
	longitud--;
	return fina;
    }
    private Nodo buscar(T elemento){
	Nodo n = cabeza;
	while(n != null){
	    if(n.elemento.equals(elemento)){
		return n;
	    }
	    n=n.siguiente;
	}
	return n;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return (buscar(elemento)!=null);
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> l2=new Lista<T>();
	Nodo nodo= cabeza;
	while(nodo != null){
	    l2.agregaInicio(nodo.elemento);
	    nodo = nodo.siguiente;
	}
	return l2;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> l2=new Lista<T>();
	Nodo nodo= cabeza;
	while(nodo != null){
	    l2.agregaFinal(nodo.elemento);
	    nodo = nodo.siguiente;
	}
	return l2;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza=null;
	rabo=null;
	longitud=0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() throws NoSuchElementException{
	if(cabeza==null) throw new NoSuchElementException();
	return cabeza.elemento;
    }
    
    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() throws NoSuchElementException{
	if(rabo==null) throw new NoSuchElementException();
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) throws ExcepcionIndiceInvalido{
	if(i<0 || i>=longitud){
	    throw new ExcepcionIndiceInvalido();
	}
	Nodo m= buscarIndice(i);
	return m.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento){
	Nodo n = cabeza;
	int j=0;
	while(n!=null){
	    if(n.elemento.equals(elemento)) return j;
	    j++;
	    n=n.siguiente;
	}
	return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString(){
	String cosa="[";
	Nodo nodo= cabeza;
	while(nodo != null){
	    cosa+=nodo.elemento;
	    nodo = nodo.siguiente;
	    if(nodo!=null)cosa+= ", ";
	}
	cosa+="]";
	return cosa;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(this.longitud!=lista.getLongitud())
	    return false;
	Nodo n=cabeza;
	Iterator<T> i=lista.iterator();
	while(i.hasNext()){
	    if(!n.elemento.equals(i.next())) return false;
	    n=n.siguiente;
	}
	return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
    private int obtenerMedio(Lista<T> s){
	int i=s.getLongitud();
	if(i%2==0) i=i/2;
	else i= (i-1)/2;
	return i;
    }
    private Lista<T> sub(Lista<T> lista, int m, int j){
	Iterator<T> i=lista.iterator();
	int a=0;
	Lista<T> l2=new Lista<T>();
	while(i.hasNext()&&(a<j)){
	    T l=i.next();
	    if(a>=m)l2.agrega(l);
	    a++;
	}
	return l2;
    }
    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador){
	if(this.longitud<=1){
	    return this;
	}
	int medio= (int) longitud/2;
	Lista<T> lista1=new Lista<T>();
	Lista<T> lista2=new Lista<T>();
	lista1=this.sub(this,0,medio);
	lista2=this.sub(this,medio,this.getLongitud());
	lista1=lista1.mergeSort(comparador);
	lista2=lista2.mergeSort(comparador);
	Lista<T> l=lista1.mezcla(comparador,lista1,lista2);
	return l;
    }
    
    private Lista<T> mezcla(Comparator<T> comparador, Lista<T> lista1,Lista<T> lista2){
	Lista<T> l=new Lista<T>();
	while(!(lista1.esVacia())&&!(lista2.esVacia())){
	    if(comparador.compare(lista1.getPrimero(),lista2.getPrimero())<=0){
		l.agrega(lista1.eliminaPrimero());
	    }else{
		l.agrega(lista2.eliminaPrimero());
	    }
	}
	while(!(lista1.esVacia()))l.agrega(lista1.eliminaPrimero());
	while(!(lista2.esVacia()))l.agrega(lista2.eliminaPrimero());
	return l;
    }
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Lista<T> li=this.mergeSort(comparador);
	Iterator<T> i = li.iterator();
	while(i.hasNext()){
	    T m= i.next();
	    if(comparador.compare(m,elemento)==0)return true;
	    else if(comparador.compare(m,elemento)>0)return false;
	}
	return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
