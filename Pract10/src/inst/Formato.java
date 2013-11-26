package inst;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

class Formato 
{
	final String regS0 = "S0";
	final String regS1 = "S1";
	final String regS9 = "S9";
	String contLocS1Act = "";
	
	public void generarRegS0(String nombre,String ruta,Archivo ArcS19)
	{
		int acum = 0;
		String s0 = "",checksum = "",dir = "0000";
		
		ruta = ruta.substring(0,2) + " "; //se toma el directorio donde esta el archivo .asm
		nombre = ruta + nombre; //se concatena al nombre del archivo
		
		for(char letra : nombre.toCharArray())
		{
			short num = (short)letra; 
			acum += num; //se va acumulando el valor de los caracteres ASCII
			s0 += Integer.toHexString(num); //Se va concatenando cada byte al Registro S0
		}
		acum += 10; //se suma el valor del LF
		s0 += "0A"; //se concatena el byte LF
		s0 = s0.toUpperCase();
		
		s0 = dir + s0; //se concatena la direccion 0000 al inicio del Registro S0
		
		String tam = Integer.toHexString(s0.length()/2 + 1); //se otiene el tamaño del registro
		acum += s0.length()/2 + 1; //se suma dicho valor al acumulador
		if(tam.length() == 1)
			tam = "0" + tam; //se complementa con un 0 de requerirlo el formato del byte
		tam = tam.toUpperCase();
		
		acum = ~acum; //se hace el complemento a 1 del acumulador
		checksum = Integer.toHexString(acum).toUpperCase(); //se obtiene el equivalente en HEX en una cadena
		checksum = checksum.substring(checksum.length()-2); //se hace una subcaneda con los 2 bytes menos significativos
		
		s0 = regS0 + tam + s0 + checksum; // S0 + 0000 + datos + checksum
		
		try
		{ArcS19.escribir(s0);} //se escribe el Registro S0 en el archivo S19
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void generarRegS1(Analizador An,Verificador Ve,Archivo ArcIns,Archivo ArcS19)
	{
		String linea = "",buffer = "",s1 = "",aux = "",lineaaux = "";
		Analizador.ingresaContLoc(0); //Se inicializa el Contloc
		contLocS1Act = Analizador.FormatoContLoc(); //se almacena dicho valor en su equivalente en HEX 
		ArcIns.crearBuffer();
		while((linea = ArcIns.lineaActual()) != null) //mientras no lleguemos al final del archivo .Inst
		{
			Vector<String> elemento = new Vector<String>();
			StringTokenizer st = new StringTokenizer(linea);
			while(st.hasMoreTokens())
				elemento.add(st.nextToken()); //guardamos cada elemento de la linea actual en el Vector
			
			if(elemento.get(3).equalsIgnoreCase("CODOP")) //Si se trata del encabezado del archivo
				continue;
			if(elemento.get(3).equalsIgnoreCase("ORG")) //directiva ORG
			{
				Analizador.ingresaContLoc(Ve.conversor(elemento.get(4))); //se obtiene el valor del Oper y se actualiza
				contLocS1Act = Analizador.FormatoContLoc(); //se ingresa la nueva direccion del ContLoc
				continue;
			}
			if(elemento.get(3).equalsIgnoreCase("EQU")) //directiva EQU
			{
				aux = buffer; //se respalda el buffer
				continue;
			}
			if(elemento.get(3).equalsIgnoreCase("FCC")) //directiva FCC
				aux = buffer; //se respalda el buffer
			
			
			if(!An.esMemoria1Byte(elemento.get(3)) && !An.esMemoria2Bytes(elemento.get(3)) && !elemento.get(3).equalsIgnoreCase("END")) //Si el Codop no es una directiva de memoria o END
			{
				if(aux.length() != 0) //si el auxiliar contiene datos
				{
					buffer = aux; // almacenan los datos de nuevo en el buffer
					aux = ""; //se limpia el auxiliar
				}
				
				buffer += elemento.lastElement(); //se agrega el CodMaq de la linea actual
				while(buffer.length() != 0) //mientras el buffer no este vacio
				{		
					if(buffer.length() <= 32) //si el buffer contiene menos o 16 bytes
					{
						while(lineaaux.length() < 32) //mientras el registro no exceda los 16 bytes
						{
							lineaaux += buffer.substring(0,2); //se va concatenando byte por byte del buffer al registro
							buffer = buffer.substring(2); //se elimina los bytes ya almacenados en el registro
							An.AumentarContLoc(1); //Aumentamos el Contador de localidades por cada inserccion
						
							if(buffer.length() == 0) //verificamos que el buffer no quede vacio
								break;
						}
					
						if(lineaaux.length() == 32) //si se alcanzo el limite de datos
						{
							formatoS1(lineaaux,s1,ArcS19); //da formato al registro S1
							lineaaux = ""; //limpiamos el registro
							aux = buffer; //respaldamos en caso de que el buffer aun contenga datos
							buffer = ""; //limpiamos el buffer
							contLocS1Act = Analizador.FormatoContLoc(); //Actualizamos el valor del ContLoc para el siguiente Registro
						}
					}
					else //Si el buffer contiene mas de 16 bytes
					{
						if(lineaaux.length() != 0) //si el registro no esta vacio
						{
							while(lineaaux.length() < 32) //mientras el registro no exceda los 16 bytes
							{
								lineaaux += buffer.substring(0,2); 
								buffer = buffer.substring(2);
								An.AumentarContLoc(1);
							
								if(buffer.length() == 0)
									break;
							}
						
							if(lineaaux.length() == 32) //si se alcanzo el limite de datos
							{
								formatoS1(lineaaux,s1,ArcS19); //da formato al registro S1
								lineaaux = ""; //limpiamos el registro
								aux = buffer; //respaldamos en caso de que el buffer aun contenga datos
								buffer = ""; //limpiamos el buffer
								contLocS1Act = Analizador.FormatoContLoc(); //Actualizamos el valor del ContLoc para el siguiente Registro
							}
						}
						else //si el Registro esta completamente vacio
						{
							lineaaux = buffer.substring(0,32); //obtenemos los 16 bytes del registro S1
							buffer = buffer.substring(32); //borramos del buffer los bytes que acabamos de obtener
					
							formatoS1(lineaaux,s1,ArcS19); //da formato al registro S1
							lineaaux = ""; //limpiamos el registro
							aux = buffer; //respaldamos en caso de que el buffer aun contenga datos
							buffer = ""; //limpiamos el buffer
							An.AumentarContLoc(16); //aumentamos el valor del ContLoc en 16 localidades
							contLocS1Act = Analizador.FormatoContLoc(); //Actualizamos el valor del ContLoc para el siguiente Registro
						}
					}	
				}
			}
			else //directivas de memoria o END
			{
				formatoS1(lineaaux,s1,ArcS19); //da formato al Registro S1
				lineaaux = ""; //limpiamos el registro
				aux = buffer; //respaldamos en caso de que el buffer aun contenga datos
				buffer = ""; //limpiamos el buffer
				
				if(An.esMemoria1Byte(elemento.get(3))) //memoria de un byte
				{
					An.AumentarContLoc(Ve.conversor(elemento.get(4))); //obtenemos el valor de Oper de la directiva y lo aumentamos al Contloc
					contLocS1Act = Analizador.FormatoContLoc();  //Actualizamos el valor del ContLoc para el siguiente Registro
				}
				else if(An.esMemoria2Bytes(elemento.get(3))) //memoria de 2 bytes
				{
					An.AumentarContLoc(Ve.conversor(elemento.get(4)) * 2); //obtenemos el valor de Oper de la directiva y aumentamos el doble al ContLoc
					contLocS1Act = Analizador.FormatoContLoc(); //Actualizamos el valor del ContLoc para el siguiente Registro
				}
			}
			
		}
	}
	
	public void generarRegS9(Archivo ArcS19)
	{
		int acum = 0;
		String s9 = "",checksum = "",dir = "0000";
		
		String tam = Integer.toHexString(dir.length()/2 + 1); //obtenemos tamaño
		acum += dir.length()/2 + 1; //sumar al acum valor de la longitud
		if(tam.length() == 1)
			tam = "0" + tam; //complementamos con un 0 si es necesario
		tam = tam.toUpperCase();
		
		acum = ~acum; //complemento a 1 del acum
		checksum = Integer.toHexString(acum).toUpperCase(); //Obtenemos el equivalente en HEX
		checksum = checksum.substring(checksum.length()-2); //tomamos los 2 bytes menos significativos
		
		s9 = regS9 + tam + dir + checksum; // S9 + 03 + 0000 + FC
		
		try
		{ArcS19.escribir(s9);} //Escribimos el Registro en el Archivo S19
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void formatoS1(String linea,String s1,Archivo ArcS19)
	{
		int acum = 0;
		String checksum = "";
		linea = contLocS1Act + linea;
		String tam = Integer.toHexString(linea.length()/2 + 1);
		
		if(tam.length() == 1)
			tam = "0" + tam;
		
		linea = tam + linea;
		s1 = linea;
		
		while(linea.length() != 0)
		{
			acum += Integer.parseInt(linea.substring(0,2),16); 
			linea = linea.substring(2);
		}
		acum = ~acum;
		checksum = Integer.toHexString(acum).toUpperCase();
		checksum = checksum.substring(checksum.length()-2);
		
		s1 = regS1 + s1 + checksum; //S1 + tam + direccion + datos + checksum
		
		try
		{ArcS19.escribir(s1);} //Escribimos el Registro en el Archivo S19
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	
	
			
	

}
