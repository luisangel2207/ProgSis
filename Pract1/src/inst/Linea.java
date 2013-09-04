package inst;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

class Linea 
{
	/**		**PRACTICA 1**
	 * 
	 * Centro Universitario de Ciencias Exactas e ingenierias
	 * 	Departamento de Ciencias Computacionales
	 * 		Programacion de Sistemas
	 * 			Luis Angel Gomez Velazco
	 * 	Identificacion de las partes de un archivo con lenguaje ensamblador
	 */
	
	//ATRIBUTOS
	String linea;
	String etq;
	String codop;
	String oper;
			
	public String Comentario()
	{
		String comando="",aux;
		StringTokenizer st = new StringTokenizer(linea); 
		StringTokenizer st1 = new StringTokenizer(linea);
				
		int tokens = st1.countTokens();	//numero de palabras en la linea
				
		if(st.nextToken().startsWith(";")) //devolver NULL en caso de ser solo un comentario
			return "NULL";
				
		else
			while(st1.hasMoreTokens())	
			{
				aux = st1.nextToken();
				if(!aux.startsWith(";"))	
					comando = comando + aux + " ";	//concatenar mientras no encontremos el comentario
				else
				{
					if(tokens > 4)	//posible error de numero de palabras
						break;
							
					if(tokens == 4)	//maximo numero de palabras en una linea
					{
						comando = comando + "NULL";	//se asigna NULL al comentario
						break;
					}
				}
			}
			return comando;		
	}
			
	public int Etiqueta()
	{
		int tam;
				
		tam = etq.length();
				
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
			
	public int Codop()
	{
		int tam;
				
		tam = codop.length();	
				
		if(tam > 5)	//valida longitud de codigo de operacion
		{
			return 1;		//Devuelve error de tipo 1
		}
		else
		{
			if(!(codop.matches("[a-zA-Z]+[a-zA-z1-9]*[.]?[a-zA-z1-9]*")))	//validar sintaxis de codigo de operacion
				return 2;		//Devuelve error de tipo 2
		}
		return 0;		//No hay errores en el Codigo de Operacion
	}
			
	public void Archivo(String cadena, String ruta, String nom, int tipo) throws IOException
	{
		File archivo;
				
		try
		{
			if(tipo == 1)		//cambiar la extension a .inst
			{
				nom = nom.replace(".asm",".inst");	
				archivo = new File(ruta,nom);
			}
			else		//cambiar la extension a .err
			{
				nom = nom.replace(".asm",".err");
				archivo = new File(ruta,nom);
			}
					
			FileWriter escribir = new FileWriter(archivo, true); 	//Creacion del objeto con el que se escribira en el archivo
			BufferedWriter buffer = new BufferedWriter(escribir);	//Creacion del buffer para escribir en el archivo
			buffer.write(cadena);	//volcar el buffer 
			buffer.newLine(); //separador de linea
			buffer.close();	//buffer cerrado
		}
				
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
			
	public String Error(int error, String palabra)
	{
		String tipo = " ";
		switch(error)
		{
			case 1:
				tipo = "Error, longitud de cadena invalida " + palabra; 	//Error tipo 1
				break;
			case 2:
				tipo = "Error de sintaxis " + palabra;		//Error tipo 2
				break;
			case 3:
				tipo = "Error, numero de cadenas invalidas " + palabra;		//Error tipo 3
				break;
			case 4:
				tipo = "Error, ausencia de directica END";		//Error tipo 4
				break;
		}
		return tipo;
	}
					
	public static void main(String[] args) throws IOException 
	{
		String nombre,ruta,comando="null",encabezado;
		int termina = 0,parametro,error_det = 0;
		int error = 0,band = 0,cont = 1,finEjecucion = 0;
				
		Linea cadena = new Linea();
		Scanner s = new Scanner(System.in);
				
		System.out.println("Dame la ruta del archivo");		
		ruta = s.nextLine();
				
		System.out.println("Dame el nombre del archivo");
		nombre = s.nextLine();
				
		File archivo = new File(ruta,nombre);		//Crea el Objeto archivo .asm

		String nombre1 = nombre;
				
		nombre1 = nombre1.replace(".asm",".err");
		File archivoerror = new File(ruta,nombre1);	//Crea el Objeto archivo .err
				
		nombre1 = nombre1.replace(".err", ".inst");
		File archivoinst = new File(ruta,nombre1);	//Crea el Objeto archivo .inst
				
		try 
		{
			if(archivoerror.exists() || archivoinst.exists())	
			{
				archivoerror.delete();
				archivoinst.delete();	//eliminacion de archivos en caso de ejecuciones anteriores
			}
					
			encabezado = "LINEA	ETQ	CODOP	OPER";	//Encabezado del Archivo .inst
			System.out.println(encabezado);	//Salida  de la linea a consola
					
			cadena.Archivo(encabezado,ruta, nombre, 1);	//Escritura del Encabezado del Archivo
					
			encabezado = "Lista de Errores de Ejecucion"; //	Encabezado del Archivo .err
			cadena.Archivo(encabezado,ruta, nombre, 2);	//Escritura del Encabezado del Archivo
					
			FileReader fr = new FileReader(archivo);		//Se crea el Objeto con el que se va a leer el Archivo .asm
			BufferedReader br = new BufferedReader(fr);		//Se crea el Buffer de la lectura del Archivo .asm
								
			while((cadena.linea = br.readLine()) != null && termina == 0)  	//mientras se lea la linea final y no se active el final de ejecucion 
			{	//inicio while	
				if(cadena.linea.contains(";"))		//Deteccion de comentario en la linea leida
				{
					comando = cadena.Comentario();	//validamos la linea para separar el comentario
					if(comando.equalsIgnoreCase("null"))	//pasar contador a la siguiente linea
						cont++;	
				}
				else
				{
					comando = cadena.linea;	//validar comentario
				}
						
				//VALIDACION DE CODIGO
						
				if((comando.compareTo(" ") == 0)||(comando.compareTo("\t") ==0) || (comando.compareToIgnoreCase("null") == 0))
					continue;	//Saltar a la siguiente iteracion si se encuentran espacios, tabuladores o comentarios ("null")
				else
				{	//inicio else
					StringTokenizer token = new StringTokenizer(comando);
					parametro = token.countTokens(); 		//numero de tokens en la linea leida
						
					switch(parametro)
					{
					case 4:			// 4 palabras encontradas
						cadena.etq = token.nextToken();
						cadena.codop = token.nextToken();
						cadena.oper = token.nextToken();
						String comen = token.nextToken();
									
						if(!comen.equalsIgnoreCase("NULL"))		//cualquier otra cadena genera un error
						{
							cadena.etq = " ";
							cadena.codop = " ";		//limpiar atributos
							cadena.oper = " ";
							error = 3;
							comando = cadena.Error(error,", checar campo comentario");		//Error de numero de cadenas
							comando = "Linea " + cont + " " + comando;	//concatenado linea de error
							cadena.Archivo(comando, ruta, nombre, 2);	//escribir linea en el archivo .err
						}
						else
						{
							if((error = cadena.Etiqueta()) != 0) //validacion de la etiqueta
							{
								comando = cadena.Error(error,"en Etiqueta");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);	//Escritura del error en el archivo .err
								error_det = 1;		//bandera de error en algun comando
								error = 0;	// estado inicial para siguiente validacion
							}
										
							if((error = cadena.Codop()) != 0)		//Validacion del codigo de Operacion
							{
								comando = cadena.Error(error,"en Codop");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);		//Escribir en el archivo .err
							}
								
							if(cadena.codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
								termina = 1;	//Palabra End detectada
									
							if(error == 0)
								band = 1;	//linea de codigo ensamblador sin errores	
						}
						break;
					case 3:
						cadena.etq = token.nextToken();		//3 palabras encontradas
						cadena.codop = token.nextToken();
						cadena.oper = token.nextToken();
								
						if((error = cadena.Etiqueta())!= 0)		//validacion de la etiqueta
						{
							comando = cadena.Error(error,"en Etiqueta");		//Detalle del tipo de error
							comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
							cadena.Archivo(comando,ruta, nombre, 2);	//Escribir en el archivo .err
							error_det = 1;	//bandera de error en algun comando
							error = 0;		//estado inicial para siguiente validacion
						}
								
						if((error = cadena.Codop()) != 0)		//Validacion del codigo de Operacion
						{
							comando = cadena.Error(error,"en Codop");		//Detalle del tipo de error
							comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
							cadena.Archivo(comando,ruta, nombre, 2);	//Escribir en el archivo .err
						}
									
						if(cadena.codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
							termina = 1;	//Palabra End detectada
								
						if(error == 0)
							band = 1;	//linea de codigo ensamblador sin errores		
						break;
					case 2:
						if((cadena.linea.startsWith(" ")) || (cadena.linea.startsWith("\t")))
						{
							cadena.etq = "NULL";		//linea de codigo sin etiqueta
							cadena.codop = token.nextToken();
							cadena.oper = token.nextToken();
									
							if((error = cadena.Codop()) != 0)		//Validacion del codigo de Operacion
							{
								comando = cadena.Error(error,"en Codop");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);	//Escribir en archivo .err
							}
							else
								band = 1;	//linea de codigo ensamblador sin errores
									
							if(cadena.codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
								termina = 1;	//Palabra End detectada
									
						}
						else
						{
							cadena.etq = token.nextToken();
							cadena.codop = token.nextToken();
							cadena.oper = "NULL";		//linea de codigo sin operador
									
							if((error = cadena.Etiqueta()) != 0)		//Validacion de la etiqueta
							{
								comando = cadena.Error(error,"en Etiqueta");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);	//Escribir en archivo .err
								error_det = 1;		//bandera de error en algun comando
								error = 0;		//estado inicial para siguiente validacion
							}
									
							if((error = cadena.Codop()) != 0)		//Validacion del codigo de operacion
							{
								comando = cadena.Error(error,"en Codop");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);
							}
							else
								band = 1;	//Linea de codigo ensamblador sin errores
									
							if(cadena.codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
								termina = 1;	//Palabra End detectada	
						}
						break;
					case 1:
						if((cadena.linea.startsWith(" ")) || (cadena.linea.startsWith("\t")))
						{
							cadena.etq = "NULL";
							cadena.codop = token.nextToken();
							cadena.oper = "NULL";
									
							if((error = cadena.Codop()) != 0)		//Validacion del codigo de operacion
							{
								comando = cadena.Error(error,"en Codop");		//Detalle del tipo de error
								comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
								cadena.Archivo(comando,ruta, nombre, 2);
							}
							else
								band = 1;	//Linea de codigo ensamblador sin errores
										
							if(cadena.codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
							{
								termina = 1;	//Palabra End detectada
							}
									
						}
						else	//instruccion invalida
						{
							cadena.etq = " ";
							cadena.codop = " ";		//limpiando comandos
							cadena.oper = " ";
							comando = cadena.Error(3,"");		//Error de numero de cadenas
							comando = "Linea " + cont + " " + comando;	//concatenacion de la linea de error
							cadena.Archivo(comando, ruta, nombre, 2);	//Escribir en el archivo .err
						}
						break;	
					case 0:			//linea vacia
						break;
					default :		//instruccion invalida 
						cadena.etq = " ";
						cadena.codop = " ";
						cadena.oper = " ";
						comando = cadena.Error(3,"");		//Error de numero de cadenas
						comando = "Linea " + cont + " " + comando;	//concatenacion de la linea de error	
						cadena.Archivo(comando, ruta, nombre, 2);	//Escribir en el archivo .err
						break;
					}	//fin switch
				} //fin else
						
				if(!cadena.codop.equalsIgnoreCase("END"))	//Evita escribir en el Archivo la linea que contiene End
				{
					if(band == 1 && error == 0 && error_det == 0) 	//instruccion valida
					{
						comando = cont+"	"+cadena.etq+"	"+cadena.codop+"	"+cadena.oper;	//concatenacion de tokens
						System.out.println(comando);	//Salida  de la linea a consola
							
						cadena.Archivo(comando,ruta, nombre, band);	//Escritura de la linea en el archivo .inst
									
						band = 0; //regresar a estado inicial
							
					}
							
						cont++;	//control del numero de linea
			
				}
				else
					finEjecucion = 1;	//Activa la bandera para dejar de leer el archivo
			}	//fin while
					
			if(finEjecucion == 0)		//si la bandera no ha sido activada
			{
				if(!cadena.codop.equalsIgnoreCase("END"))		//Si el Codop End no se encontro
				{	
					comando = cadena.Error(4,"verificar Codop");		//Falta de directiva End
					comando = "Linea " + cont + " " + comando;	
					cadena.Archivo(comando, ruta, nombre, 2);		//Escribe el error en el Archivo .err
					System.out.println("ERROR!! Directiva END no encontrada");	//Aviso de Error por consola
				}
			}
							
			fr.close();	//buffer cerrado
			
		}	// fin try
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}	//fin main
}	//fin clase 