package inst;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.HashSet;

public class Analizador 
{
	//Atributos
	private String etq;
	private String codop;
	private String oper;
	private boolean finEjecucion;
	protected static int contLoc;
	private Vector<String> listaDeDirect;
	private Vector<String> listaDeEtiq;
	private HashSet<String> ConjDeOperEtq;
	
	//Constructor 
	public Analizador()
	{
		etq = "";
		codop = "";
		oper = "";
		String[] directivas = {"ORG","END","EQU","DB","DC.B","FCB","DW","DC.W","FDB","FCC",
				"DS","DS.B","RMB","DS.W","RMW"};
		listaDeDirect = new Vector<String>();
		for(String directiva : directivas)
		{
			listaDeDirect.add(directiva);
		}
		listaDeEtiq = new Vector<String>();
		ConjDeOperEtq = new HashSet<String>();
	}
	
	public boolean esConst1Byte(String codop)
	{
		return (codop.equalsIgnoreCase("DB") || codop.equalsIgnoreCase("DC.B") || codop.equalsIgnoreCase("FCB"));
	}
	public boolean esConst2Bytes(String codop)
	{
		return (codop.equalsIgnoreCase("DW") || codop.equalsIgnoreCase("DC.W") || codop.equalsIgnoreCase("FDB"));
	}
	public boolean esMemoria1Byte(String codop)
	{
		return (codop.equalsIgnoreCase("DS") || codop.equalsIgnoreCase("DS.B") || codop.equalsIgnoreCase("RMB"));
	}
	public boolean esMemoria2Bytes(String codop)
	{
		return (codop.equalsIgnoreCase("DS.W") || codop.equalsIgnoreCase("RMW"));
	}
	
	public void agregarEtqAlista(String etq)
	{
		listaDeEtiq.add(etq);
	}
	public boolean contieneEtqLista(String etq)
	{
		if(listaDeEtiq.contains(etq))
			return true;
		else
			return false;
	}
	public void quitarEtqAlista(String etq)
	{
		listaDeEtiq.remove(etq);
	}
	public void agregarOpEtqAConj(String oper)
	{
		ConjDeOperEtq.add(oper);
	}
	public HashSet<String> regresaConjOpEtq()
	{
		return ConjDeOperEtq;
	}
	public void actualizarListYConj(HashSet<String>auxlist, HashSet<String>auxconj)
	{
		listaDeEtiq.clear();
		listaDeEtiq.addAll(auxlist);
		ConjDeOperEtq.clear();
		ConjDeOperEtq.addAll(auxconj);
	}
	public boolean existenTodasEtq()
	{
		ConjDeOperEtq.removeAll(listaDeEtiq);
		return ConjDeOperEtq.isEmpty();
	}
	public boolean contieneOpEtq(String oper)
	{
		return ConjDeOperEtq.contains(oper);
	}
	
	//Metodos
	public void ingresarEtiqueta(String etiqueta)
	{
		etq = etiqueta;
	}
	public void ingresarCodop(String codOp)
	{
		codop = codOp.toUpperCase();
	}
	public void ingresarOperando(String operando)
	{
		oper = operando;
	}
	public static void ingresaContLoc(int num)
	{
		contLoc = num;
	}
	public void ingresaFinEjecuccion(boolean estado)
	{
		finEjecucion = estado;
	}
	
	public String regresaEtq()
	{
		return etq;
	}
	public String regresaCodop()
	{
		return codop;
	}
	public String regresaOper()
	{
		return oper;
	}
	public static int regresaContLoc()
	{
		return contLoc;
	}
	public boolean regresaFinEjecucion()
	{
		return finEjecucion;
	}
	
	public byte validarEtiqueta()
	{
		byte tam;
				
		tam = (byte)etq.length();
				
		if(tam > 8)		//validar longitud
		{
			return 1;	//Devuelve error de tipo 1
		}
		else
		{
			if(!(etq.matches("[a-zA-Z]+[a-zA-Z0-9_]*")))	//validar sintaxis
				return 2;	//Devuelve error de tipo 2
		}
		return 0;		//No hay errores en la etiqueta
	}
	
	public byte validarCodop()
	{
		int tam;
				
		tam = (byte)codop.length();	
				
		if(tam > 5)	//valida longitud de codigo de operacion
		{
			return 1;		//Devuelve error de tipo 1
		}
		else
		{
			if(!(codop.matches("[a-zA-Z]+[a-zA-z]*[.]?[a-zA-z]*")))	//validar sintaxis de codigo de operacion
				return 2;		//Devuelve error de tipo 2
		}
		return 0;		//No hay errores en el Codigo de Operacion
	}
	
	public byte validarOperEtq()
	{
		byte tam;
		
		tam = (byte)oper.length();
				
		if(tam > 8)		//validar longitud
		{
			return 1;	//Devuelve error de tipo 1
		}
		else
		{
			if(!(oper.matches("[a-zA-z]+[a-zA-z0-9_]*")))	//validar sintaxis
				return 2;	//Devuelve error de tipo 2
		}
		return 0;		//No hay errores en la etiqueta
	}
	
	public void AumentarContLoc(int inc)
	{
		contLoc = contLoc + inc;
	}
	
	public boolean existeDirect(String directiva)
	{
		if(listaDeDirect.contains(directiva))
			return true;
		else
			return false;
	}
	
	public static String FormatoContLoc()
	{
		String cadcont = Integer.toHexString(regresaContLoc());
		
		if(regresaContLoc() >= 0 && regresaContLoc() <= 65535)
		{
			int opc = cadcont.length();
			switch(opc)
			{
			case 1:
				cadcont = "000" + cadcont;
				break;
			case 2:
				cadcont = "00" + cadcont;
				break;
			case 3:
				cadcont = "0" + cadcont;
				break;			
			}
		}
		else
		{
			cadcont = "XXXX";
		}
		return cadcont.toUpperCase();
	}
	
	public String describirError(byte error, String palabra)
	{
		String tipo = "";
		switch(error)
		{
			case 1:
				tipo = "Error, longitud de comando invalido " + palabra; 	//Error tipo 1
				break;
			case 2:
				tipo = "Error de sintaxis " + palabra;		//Error tipo 2
				break;
			case 3:
				tipo = "Error, numero de comandos invalidos " + palabra;		//Error tipo 3
				break;
			case 4:
				tipo = "Error, ausencia de directica END";		//Error tipo 4
				break;
			case 5:
				tipo = "Error, formato de instruccion erroneo " + palabra;
				break;
			case 6:
				tipo = "Error, Archivo Tabop no encontrado " + palabra;
				break;
			case 7:
				tipo = "Error, Codop con ausencia de Operando " + palabra;
				break;
			case 8:
				tipo = "Error, Existe Operando en Codop que no lo solicita " + palabra;
				break;
			case 9:
				tipo = "Error, Codop No existe en el Tabop";
				break;
			case 10:
				tipo = "Error, Directiva ORG sin Operando" + palabra;
				break;
			case 11:
				tipo = "Error, Simbolo de base numerica erroneo" + palabra;
				break;
			case 12:
				tipo = "Error, Simbolos numericos invalidos" + palabra;
				break;
			case 13:
				tipo = "Error, Oper fuera de Rango de ese Direccionamiento" + palabra;
				break;
			case 14:
				tipo = "Error,Formato de Oper Invalido para este Codop" + palabra;
				break;
			case 15:
				tipo = "Error, Oper con Registro Invalido" + palabra;
				break;
			case 16:
				tipo = "Error, Formato Invalido para cualquier Codop" + palabra;
				break;
			case 17:
				tipo = "Error, Directiva END con Operando" + palabra;
				break;
			case 18:
				tipo = "Error, Etiqueta en Oper con formato invalido" + palabra;
				break;
			case 19:
				tipo = "Error, Directiva ORG Invalida" + palabra;
				break;
			case 20:
				tipo = "Error, Directiva EQU Invalida" + palabra;
				break;
			case 21:
				tipo = "Error, Oper fuera de Rango" + palabra;
				break;
			case 22:
				tipo = "Error, Directiva sin Operando" + palabra;
				break;
			case 23:
				tipo = "Error, Etiqueta duplicada" + palabra;
				break;
			case 24:
				tipo = "Error, Operando-Etiqueta no se encuentra en ninguna Etq" +  palabra;
				break;
			case 25:
				tipo = "Error, Archivo Inst contiene Oper-Etq Invalidos" + palabra;
				break;
			case 26:
				tipo = "Error, Rango del Desplazamiento no valido" + palabra;
				break;
		}
		return tipo;
	}
	
	public String comentario(String linea)
	{
		String comando="",aux;
		StringTokenizer st = new StringTokenizer(linea); 
		
		while(st.hasMoreTokens())	
		{
			aux = st.nextToken();  
			if(!aux.contains(";"))		//si el comando no tiene un ;
				comando = comando + aux + " ";	//concatenar mientras no encontremos el comentario
			else
			{
				if(aux.startsWith(";"))		//si empieza con ;
				{
					comando = comando + "NULL";
					break;
				}
				else
				{
					StringTokenizer sep = new StringTokenizer(aux,";");	//separamos el comando del comentario
					comando = comando + sep.nextToken(";") + " NULL"; //se asigna NULL al comentario
				}
				break;
			}
		}
		return comando;		
	}
	
	public String cadenaEntreComillas(String linea)
	{
		String cad;
		if(linea.contains("\\\\")) //si contiene un escape de diagonal invertida
			linea = linea.replace("\\\\","''"); //simbolo especial para reconocimiento de diagonal invertida
		if(linea.contains("\\\"")) //si contiene un escape de comillas
			linea = linea.replace("\\\"","\\�"); //simbolo especial para reconocimiento de comillas
		if(linea.contains(";")) //si contiene simbolo  de comentario
			linea = linea.replace(";","�"); //simbolo especial para reconocimeinto de simbolo de comentario
		StringTokenizer st = new StringTokenizer(linea);
		linea = "";
		
		while(st.hasMoreTokens())
		{
			cad = st.nextToken(); //Se almacena el Token actual
			if(cad.startsWith("\"")) //si el token comienza con comillas
			{
				while(!(cad.startsWith("\"") && cad.endsWith("\"")) && st.hasMoreElements()) //mientras el token no este entre comillas y existan mas tokens
				{
					cad = cad + " " + st.nextToken(); //concatenacion del contenido del token entre comillas
				}
				if(cad.matches("\".*\"")) //si cumple con la sintaxis
				{
					cad = cad.replace(" ", "|"); //se remplaza el espacio por un separador especial
				}
				linea = linea + " " + cad; //concatenacion del token en la linea
			}
			else
				linea = linea + " " + cad + " ";
		}
		return linea;
	}
	
	public boolean espacioONull(String comando)
	{
		if((comando.compareTo(" ") == 0)||(comando.compareTo("\t") ==0) || (comando.compareToIgnoreCase("null") == 0))
			return true;
		else
			return false;
	}
	
	public void sumarBytesContLoc(Direccionamiento Di,Instruccion[] listadeInst)
	{
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(regresaCodop().equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& Di.regresaTipo().equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				AumentarContLoc(listadeInst[i].regresaBT()); //Aumenta ContLoc con los bytes correspondientes
				break;
			}
		}
	}
	
	public void limpiarAnaliz()
	{
		etq = "";
		codop = "";
		oper = "";
	}
	
	public static void main(String[] args) throws IOException
	{
		String nombre,ruta,comando = "",encabezado,linea;
		byte tokens,cont = 1;
		boolean band,termina;
		band = termina = false;
		Instruccion[] listadeInst;
		TreeMap<String,String> ArbolDeInst = new TreeMap<String,String>();	//Se crea el Objeto Arbol de los direccionamientos
		
		Analizador An = new Analizador();
		Scanner S = new Scanner(System.in);
				
		System.out.println("Dame la ruta del archivo");		
		ruta = S.nextLine();
				
		System.out.println("Dame el nombre del archivo");
		nombre = S.nextLine();
		
		Archivo ArcAsm = new Archivo(ruta,nombre,(byte)0);	//Se creo un archivo .asm
		
		String nombre1 = nombre;
		
		nombre1 = nombre1.replace(".asm",".err");
		Archivo ArcErr = new Archivo(ruta,nombre1,(byte)2);	//Se creo un archivo .err
				
		nombre1 = nombre1.replace(".err", ".inst");
		Archivo ArcIns = new Archivo(ruta,nombre1,(byte)1);	//Se creo un archivo .inst
		
		nombre1 = nombre1.replace(".inst", ".tds");
		Archivo ArcTds = new Archivo(ruta,nombre1,(byte)1);	//Se creo un archivo .tds
		
		//String ruta2 = ruta;
		
		System.out.println("Dame la ruta del Tabop");
		ruta = S.nextLine();
		
		Archivo Tabop = new Archivo(ruta,"tabop.txt",(byte)0);	//Se creo un archivo tabop
		
		Automata Au = new Automata();	//se crea el Objeto Automata
		Instruccion In = new Instruccion();	//se crea el Objeto Instruccion
		Direccionamiento Di = new Direccionamiento(); //se crea el Objeto Direccionamiento
		
		listadeInst = In.cargarTabop(An, Tabop, ArcErr);	//Arreglo  de instrucciones de cada cada Codop
		ArbolDeInst = In.arbolDeDirecc(listadeInst);	//se llena el Arbol con los Direccionamientos de cada Codop
		
		try
		{
			if(ArcErr.existeArchivo() || ArcIns.existeArchivo() || ArcTds.existeArchivo())
			{
				ArcErr.borrarArchivo();
				ArcIns.borrarArchivo();		//eliminacion de Archivos en caso de ejecuciones anteriores
				ArcTds.borrarArchivo();
			}
			
			encabezado = "LINEA	CONTLOC	ETQ	CODOP	OPER	MODDIR";	//Encabezado del Archivo .inst
			System.out.println(encabezado);	//Salida  de la linea a consola
			
			ArcIns.escribir(encabezado);		//Escribe en el archivo .inst
			
			encabezado = "Lista de Errores de Ejecucion"; 	//Encabezado del Archivo .err
			ArcErr.escribir(encabezado);	//Escribe en el archivo .err
			
			encabezado = "ETIQ	VALOR"; 	//Encabezado del Archivo .tds
			ArcTds.escribir(encabezado);	//Escribe en el archivo .tds
			
			ArcAsm.crearBuffer();	//crea un buffer con el atributo del Objeto Archivo
			
			while((linea = ArcAsm.lineaActual()) != null && termina == false)
			{
				if(linea.contains("\""))
					linea = An.cadenaEntreComillas(linea); //localiza la cadena entre comillas y la concatena con un separador especial
				
				if(linea.contains(";"))
				{
					comando = An.comentario(linea);	//localiza el comentario y lo cambia por "NULL"
					if(comando.equalsIgnoreCase("null"))	//pasar contador a la siguiente linea
						cont++;
				}
				else
					comando = linea;	//linea sin comentarios
				
				//if(comando.contains("\""))
					//comando = An.cadenaEntreComillas(comando); //localiza la cadena entre comillas y la concatena con un separador especial
				
				if(An.espacioONull(comando))
					continue;	//salta a la sig. iteracion si solo hay espacios,tabuladores o comentarios en la linea
				else
				{
					StringTokenizer token = new StringTokenizer(comando);
					tokens = (byte)token.countTokens();		//numero de tokens en la linea leida
					
					switch(tokens)
					{
					case 4:		//4 palabras encontradas
						Au.inicia4Comandos(An, token, ArcErr,linea,cont);	//inicia el automata con 4 tokens
						termina = Au.regresaEstEnd();	//verifica si el Codop es la palabra END
						band = Au.regresaEstSinErr();	//veridica que el codigo no tenga errores
						break;
					case 3:		//3 palabras encontradas
						Au.inicia3Comandos(An, token,ArcErr,linea,cont);
						termina = Au.regresaEstEnd();	//verifica si el Codop es la palabra END
						band = Au.regresaEstSinErr();	//veridica que el codigo no tenga errores
						break;
					case 2:		//2 palabras encontradas
						Au.inicia2Comandos(An, token,ArcErr,linea,cont);
						termina = Au.regresaEstEnd();	//verifica si el Codop es la palabra END
						band = Au.regresaEstSinErr();	//veridica que el codigo no tenga errores
						break;
					case 1:
						Au.inicia1Comando(An,token,ArcErr,linea,cont);
						termina = Au.regresaEstEnd();	//verifica si el Codop es la palabra END
						band = Au.regresaEstSinErr();	//veridica que el codigo no tenga errores
						break;
					case 0:		//linea en blanco
						break;
					default:
						comando = An.describirError((byte)3,", revisar limite de comandos");		//exceso de comandos en la linea
						comando = "Linea " + cont + " " + comando;
						ArcErr.escribir(comando);	//Escribe el error en el Archivo .err
						break;
					}	//fin switch
				}	//fin else
				
				if(band && Au.regresaEstSinErr() && !Au.regresaEstErrDet())	//instruccion valida
				{
					if(ArbolDeInst.containsKey(An.regresaCodop()))	//Si el arbol contiene el Codop					
					{
						Au.estadoValidaInst(In, An, Au, Di, listadeInst, ArbolDeInst, ArcIns, ArcErr, ArcTds, cont); //entra al Automata para validar Codop-Operando
					}
					else if(An.existeDirect(An.regresaCodop())) //Si la lista contiene la Directiva
					{
						Au.estadoValidaDirect(An,Au,ArcIns,ArcErr,ArcTds,Di,cont);
					}
					else	//No se encontro Codop
					{
						comando = An.describirError((byte)9,"");	//Codop Inexistente en el Tabop
						comando = "Linea " + cont + " " + comando;
						ArcErr.escribir(comando);	//Escribe el error en el Archivo .err
					}
				}
				
				cont++;	//control del numero de linea
				band = false;	//Bandera en estado inicial
				An.limpiarAnaliz();	//limpiar atributos del Analizador
				Au.reiniciarAutomata();		//Reestablece los valores del automata
				
			}	//fin while
			
			if(!An.regresaFinEjecucion())		//si la bandera no ha sido activada
			{
				if(!Au.regresaEstEnd())		//Si el codop End no se encontro
				{
					comando = An.describirError((byte)4,"verificar Codop");		//Falta de directiva End
					ArcErr.escribir(comando);	//Escribe el error en el Archivo .err
					System.out.println("ERROR!! Directiva END no encontrada");	//Aviso de Error por consola
				}
			}
			
			ArcAsm.cerrarLector();	//cerrando el buffer del Archivo Asm
			
			//Verificacion para Paso 2 del Ensamblador
			while(!An.existenTodasEtq()) //Mientras se encuentran Op-Etq que no esten como Etq
			{
				Au.ingresaEstInstConErr(true); //bandera que impide pasar al paso 2
				LinkedList<String> tmp = new LinkedList<String>(); //creamos una cola temporal
				Verificador Ve = new Verificador(); //creamos un Objeto Verificador
				tmp = Ve.respaldo(ArcIns); //Guardamos el contenido del Archivo Inst
				
				ArcIns.cerrarLector(); //cerramos el buffer
				ArcIns.borrarArchivo(); //borramos el archivo Inst
				ArcTds.borrarArchivo(); //borramos el TDS
				try
				{
					encabezado = "ETIQ	VALOR";
					ArcTds.escribir(encabezado);
					encabezado = "LISTA DE ERRORES, ENSAMBLADOR DE DOS PASOS";  //Encabezados
					ArcErr.escribir(encabezado);
				}
				catch(IOException e)
				{e.printStackTrace();}
					
				Ve.corregirEtqs(An,ArcIns,ArcErr,ArcTds,listadeInst,ArbolDeInst,tmp); //se eliminan todos los errores por etiquetas que no existen
				
			}
			//Paso 2 del Ensamblador
			if(!Au.regresaEstInstConErr()) //Si no tiene errores el Archivo Inst
			{
				LinkedList<String> tmp = new LinkedList<String>(); //creamos un respaldo del Archivo Inst
				Verificador Ve = new Verificador(); //creamos un Objeto Verificador
				Calculador Ca = new Calculador(); //creamos un Objeto Calculador
				tmp = Ve.respaldo(ArcIns); //guardamos en una cola las lineas del Archivo Inst
				
				ArcIns.cerrarLector(); //cerramos el buffer
				ArcIns.borrarArchivo(); //borramos el Archivo Inst
				TreeMap<String,String> arbolDeEtq = new TreeMap<String,String>(); //Se crea un Objeto Arbol
				In.crearArbolDeEtqs(arbolDeEtq,ArcTds); //se llena el arbol clave-valor con las lineas del Archivo Tds
				
				ArcIns.escribir(tmp.removeFirst() + "	" + "CODMAQ"); //Se escribe el nuevo encabezado del Archivo Inst
				while(!tmp.isEmpty()) //mientras la pila no este vacia
				{
					linea = tmp.removeFirst(); //se guarda y quita la linea del frente de la cola
					Vector<String> elemento = new Vector<String>();
					StringTokenizer st = new StringTokenizer(linea);
					while(st.hasMoreTokens())
						elemento.add(st.nextToken()); //guardamos cada elemento de la linea actual en el Vector
					if(ArbolDeInst.containsKey(elemento.get(3))) //Si es un Codop del Tabop
					{
						if(elemento.get(5).equalsIgnoreCase("INH")) //Direccionamiento INH
						{
							String CodMaq = Ca.obtenerCodMaqSimples(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst); //Se obtiene el CodMaq
							linea = linea + "	" + CodMaq; //se concatena con la linea actual
						}
						else if(elemento.get(5).startsWith("IMM")) //Direccionamiento IMM, IMM8 y IMM16
						{
							String CodMaq = Ca.obtenerCodMaqSimples(elemento.get(3), elemento.get(5),elemento.get(4).substring(1),Di,Ve,listadeInst); //Se obtiene el CodMaq
							linea = linea + "	" + CodMaq; //Se concatena con la linea actual
						}
						else if(elemento.get(5).equalsIgnoreCase("DIR")) //Direccionamiento DIR
						{
							String CodMaq = Ca.obtenerCodMaqSimples(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst); //Se obtiene el CodMaq
							linea = linea + "	" + CodMaq; //se concatena con la linea actual
						}
						else if(elemento.get(5).equalsIgnoreCase("EXT")) //Direccionamiento EXT
						{
							if(elemento.get(4).matches("[a-zA-z]+[a-zA-z0-9_]*")) //Si el Oper es una Etq
							{
								String CodMaq = arbolDeEtq.get(elemento.get(4)); //Se obtiene el valor que le corresponde al Oper segun el Archivo Tds
								CodMaq = Ca.obtenerCodMaqSimples(elemento.get(3),elemento.get(5),elemento.get(4),Di,Ve,listadeInst) + CodMaq; //se concatena el CodMaq con el valor obtenido
								linea = linea + "	" + CodMaq; //se concatena con la linea actual
							}
							else //Si se trata de un valor
							{
								String CodMaq = Ca.obtenerCodMaqSimples(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst); //Se obtiene el CodMaq
								linea = linea + "	" + CodMaq; //Se concatena con la linea actual
							}
						}
						else if(elemento.get(5).equalsIgnoreCase("IDX")) //Direccionamiento IDX
						{
							String CodMaq = Ca.obtenerCodMaqIDX(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst);
							linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("IDX1")) //Direccionamiento IDX1
						{
							String CodMaq = Ca.obtenerCodMaqIDX1(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst);
							linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("IDX2")) //Direccionamiento IDX2
						{
							String CodMaq = Ca.obtenerCodMaqIDX2(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst);
							linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("[IDX2]")) //Direccionamiento [IDX2]
						{
							String CodMaq = Ca.obtenerCodMaqIDXInd(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst);
							linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("[D,IDX]")) //Direccionamiento IDX2
						{
							String CodMaq = Ca.obtenerCodMaqIDXIndD(elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst);
							linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("REL8")) //Direccionamiento REL8
						{
							String CodMaq = Ca.obtenerCodMaqRel8(elemento.get(1),elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst,arbolDeEtq);
							if(CodMaq.equalsIgnoreCase("XXXX"))
							{
								if(!elemento.get(2).equalsIgnoreCase("NULL"))
									An.quitarEtqAlista(elemento.get(2));
								ArcErr.errorEtqFueraRangoRel(An,elemento.get(0));
								linea = CodMaq + linea;
							}
							else
								linea = linea + "	" + CodMaq;
						}
						else if(elemento.get(5).equalsIgnoreCase("REL16")) //Direccionamiento REL16
						{
							String CodMaq = Ca.obtenerCodMaqRel16(elemento.get(1),elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst,arbolDeEtq);
							if(CodMaq.equalsIgnoreCase("XXXX"))
							{
								if(!elemento.get(2).equalsIgnoreCase("NULL"))
									An.quitarEtqAlista(elemento.get(2));
								ArcErr.errorEtqFueraRangoRel(An,elemento.get(0));
								linea = CodMaq + linea;
							}
							else
								linea = linea + "	" + CodMaq;
						}
					}
					else //si se trata de una directiva
					{
						if(An.esConst1Byte(elemento.get(3))) //directivas constantes de 1 byte
						{
							String CodMaq = Ca.obtenerCodMaqConst1Byte(elemento.get(4), Ve);
							linea = linea + "		" + CodMaq; //doble tabulacion
						}
						else if(An.esConst2Bytes(elemento.get(3))) //directivas constantes de 2 bytes
						{
							String CodMaq = Ca.obtenerCodMaqConst2Bytes(elemento.get(4), Ve);
							linea = linea + "		" +CodMaq; //doble tabulacion
						}
						else if(elemento.get(3).equalsIgnoreCase("FCC")) //directiva FCC
						{
							String CodMaq = Ca.obtenerCodMaqFCC(elemento);
							linea = linea + "	" + CodMaq;
						}
						
					}
					
					if(linea.startsWith("XXXX"))
						Au.ingresaEstDespInval(true);
					else
						ArcIns.escribir(linea); //Despues de encontrar y concatenar el CodMaq, se escribe en el Archivo Inst
					
				}
				if(Au.regresaEstDespInval()) //Si ocurre un error de desplazamiento
				{
					do //hacer
					{
						tmp = Ve.respaldo(ArcIns); //Guardamos el contenido del Archivo Inst
					
						ArcIns.cerrarLector(); //cerramos el buffer
						ArcIns.borrarArchivo(); //borramos el archivo Inst
						ArcTds.cerrarLector(); //cerramos el buffer
						ArcTds.borrarArchivo(); //borramos el TDS
						try
						{
							encabezado = "ETIQ	VALOR";
							ArcTds.escribir(encabezado);
						}
						catch(IOException e)
						{e.printStackTrace();}
						
						Ve.corregirEtqs(An,ArcIns,ArcErr,ArcTds,listadeInst,ArbolDeInst,tmp); //se eliminan todos los errores por etiquetas que no existen
						tmp.clear();
						tmp = Ve.respaldo(ArcIns);
						ArcIns.cerrarLector(); //cerramos el buffer
						ArcIns.borrarArchivo(); //borramos el archivo Inst
					
						arbolDeEtq.clear(); //limpiamos el arbol
						In.crearArbolDeEtqs(arbolDeEtq,ArcTds); //se llena el arbol clave-valor con las lineas del Archivo Tds
						ArcIns.escribir(tmp.removeFirst()); //Se escribe el nuevo encabezado del Archivo Inst
						while(!tmp.isEmpty()) //mientras la pila no este vacia
						{
							linea = tmp.removeFirst(); //se guarda y quita la linea del frente de la cola
							Vector<String> elemento = new Vector<String>();
							StringTokenizer st = new StringTokenizer(linea);
							while(st.hasMoreTokens())
								elemento.add(st.nextToken()); //guardamos cada elemento de la linea actual en el Vector
							if(ArbolDeInst.containsKey(elemento.get(3))) //Si es un Codop del Tabop
							{
								if(elemento.get(5).equalsIgnoreCase("REL8")) //Direccionamiento REL8
								{
									linea = "";
									String CodMaq = Ca.obtenerCodMaqRel8(elemento.get(1),elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst,arbolDeEtq);
									linea = elemento.get(0);
									for(int i = 1;i <= 5;i++)
										linea += "	" + elemento.get(i);
									linea = linea + "	" + CodMaq;
								}
								else if(elemento.get(5).equalsIgnoreCase("REL16")) //Direccionamiento REL16
								{
									linea = "";
									String CodMaq = Ca.obtenerCodMaqRel16(elemento.get(1),elemento.get(3), elemento.get(5),elemento.get(4),Di,Ve,listadeInst,arbolDeEtq);
									linea = elemento.get(0);
									for(int i = 1;i <= 5;i++)
										linea += "	" + elemento.get(i);
									linea = linea + "	" + CodMaq;
								}
							}
							ArcIns.escribir(linea); //Despues de encontrar y concatenar el CodMaq, se escribe en el Archivo Inst
						}
					}
					while(!An.existenTodasEtq()); //mientras no existan todas las Etq
				}
			}
			else
				ArcErr.errorVerifPasoDos(An); //Etq-Oper Invalidas en Archivo Inst
		}// fin try
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
	}	//fin main

}	//fin clase Analizador
