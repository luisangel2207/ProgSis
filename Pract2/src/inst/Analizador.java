package inst;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Analizador 
{
	//Atributos
	private String etq;
	private String codop;
	private String oper;
	
	//Constructor
	public Analizador()
	{
		etq = "";
		codop = "";
		oper = "";
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
			if(!(etq.matches("[a-zA-z]+[a-zA-z1-9_]*")))	//validar sintaxis
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
		//operacion despues del while por si las dudas
		return comando;		
	}
	
	public boolean espacioONull(String comando)
	{
		if((comando.compareTo(" ") == 0)||(comando.compareTo("\t") ==0) || (comando.compareToIgnoreCase("null") == 0))
			return true;
		else
			return false;
	}
	
	public void limpiarAnaliz()
	{
		etq = "";
		codop = "";
		oper = "";
	}
	
	public static void main(String[] args) throws IOException
	{
		String nombre,ruta,comando,encabezado,linea;
		byte tokens,cont = 1;
		boolean band,termina,finEjecuccion;
		band = termina = finEjecuccion = false;
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
		
		System.out.println("Dame la ruta del Tabop");
		ruta = S.nextLine();
		
		Archivo Tabop = new Archivo(ruta,"tabop.txt",(byte)0);	//Se creo un archivo tabop
		
		Automata Au = new Automata();	//se crea el Objeto Automata
		Instruccion In = new Instruccion();	//se crea el Objeto Instruccion
		
		listadeInst = In.cargarTabop(An, Tabop, ArcErr);	//Arreglo  de instrucciones de cada cada Codop
		ArbolDeInst = In.arbolDeDirecc(listadeInst);	//se llena el Arbol con los Direccionamientos de cada Codop
		
		try
		{
			if(ArcErr.existeArchivo() || ArcIns.existeArchivo())
			{
				ArcErr.borrarArchivo();
				ArcIns.borrarArchivo();		//eliminacion de Archivos en caso de ejecuciones anteriores
			}
			
			encabezado = "LINEA	ETQ	CODOP	OPER	MODOS";	//Encabezado del Archivo .inst
			System.out.println(encabezado);	//Salida  de la linea a consola
			
			ArcIns.escribir(encabezado);		//Escribe en el archivo .inst
			
			encabezado = "Lista de Errores de Ejecucion"; 	//Encabezado del Archivo .err
			ArcErr.escribir(encabezado);	//Escribe en el archivo .err
			
			ArcAsm.crearBuffer();	//crea un buffer con el atributo del Objeto Archivo
			
			while((linea = ArcAsm.lineaActual()) != null && termina == false)
			{
				if(linea.contains(";"))
				{
					comando = An.comentario(linea);	//localiza el comentario y lo cambia por "NULL"
					if(comando.equalsIgnoreCase("null"))	//pasar contador a la siguiente linea
						cont++;
				}
				else
					comando = linea;	//linea sin comentarios
				
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
				
				if(!Au.regresaEstEnd())		//Si el Codop aun no es la directica END
				{
					if(band && Au.regresaEstSinErr() && !Au.regresaEstErrDet())	//instruccion valida
					{
						if(ArbolDeInst.containsKey(An.regresaCodop()))	//Si el arbol contiene el Codop					
						{
							Au.estadoValidaInst(In, An, listadeInst, ArbolDeInst, ArcIns, ArcErr, cont); //entra al Automata para validar Codop-Operando
						}
						else if(An.regresaCodop().equalsIgnoreCase("ORG"))	//Directiva ORG encontrada
						{
							if(!(An.regresaOper().equalsIgnoreCase("NULL"))) //Si contiene Operando
							{
								comando = cont+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
								System.out.println(comando);	//Salida  de la linea a consola
						
								ArcIns.escribir(comando);	////Escritura de la linea en el archivo .inst
							}
							else //Ausencia de Operando
							{
								comando = An.describirError((byte)10,", Incluir la direccion en la Directiva");	//ORG sin Direccion
								comando = "Linea " + cont + " " + comando;
								ArcErr.escribir(comando);	//Escribe el error en el Archivo .err
							}
									
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
				}
				else //Directiva End encontrada
				{
					finEjecuccion = true;	//activa la bandera para dejar de leer el archivo
					comando = cont+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
					System.out.println(comando);	//Salida  de la linea a consola
					ArcIns.escribir(comando);	//Escritura de la linea en el archivo .inst
				}
			}	//fin while
			
			if(!finEjecuccion)		//si la bandera no ha sido activada
			{
				if(!Au.regresaEstEnd())		//Si el codop End no se encontro
				{
					comando = An.describirError((byte)4,"verificar Codop");		//Falta de directiva End
					ArcErr.escribir(comando);	//Escribe el error en el Archivo .err
					System.out.println("ERROR!! Directiva END no encontrada");	//Aviso de Error por consola
				}
			}
			
			ArcAsm.cerrarLector();	//cerrando el buffer del Archivo Asm
			
		}// fin try
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
	}	//fin main

}	//fin clase Analizador
