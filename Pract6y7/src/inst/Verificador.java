package inst;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.HashSet;

class Verificador 
{	
	private String formComp2;
	
	public void ingresaComp2(String comp2)
	{
		formComp2 = comp2;
	}
	
	public String regresaComp2()
	{
		return formComp2;
	}
	
	public boolean esNumNeg(String oper)
	{
		return (oper.startsWith("$F") || oper.startsWith("$f") || oper.startsWith("@7") 
					|| oper.startsWith("%1") || oper.startsWith("-"));
	}
	
	public LinkedList<String> respaldo(Archivo ArcIns)
	{
		String linea;
		LinkedList<String> cola = new LinkedList<String>();
		ArcIns.crearBuffer();
		while((linea = ArcIns.lineaActual()) != null)
		{
			cola.addLast(linea);
		}
		return cola;
	}
	
	public void corregirEtqs(Analizador An,Archivo ArcIns,Archivo ArcErr,Archivo ArcTds,Instruccion[] listadeInst,TreeMap<String,String> ArbolDeInst,LinkedList<String> cola)
	{
		Analizador.ingresaContLoc(0);
		
		try
		{ArcIns.escribir(cola.removeFirst());}
		catch(IOException e)
		{e.printStackTrace();}
		
		HashSet<String> auxlist = new HashSet<String>();
		HashSet<String> auxconj = new HashSet<String>();
		
		while(!cola.isEmpty())
		{
			String[] campos = new String[10];
			byte i = 0;
			StringTokenizer st = new StringTokenizer(cola.removeFirst()); //separamos los campos de la Instruccion
			while(st.hasMoreTokens())
			{
				campos[i++] = st.nextToken();
			}
			if(ArbolDeInst.containsKey(campos[3])) //Si no es una Directiva
			{
				if(campos[5].equalsIgnoreCase("EXT")) //si es de tipo EXT
				{
					if(campos[4].matches("[a-zA-z]+[a-zA-z0-9_]*") && !An.contieneEtqLista(campos[4])) //si es una Etq que no existe
					{
						ArcErr.errorOpEtqInv(An,campos[0]); // Oper no existe como Etq
						continue; //Se salta ala siguiente linea
					}
					if(campos[4].matches("[a-zA-z]+[a-zA-z0-9_]*"))
						auxconj.add(campos[4]); //afrefar Oper-Etq a lista auxiliar
				}	
			}
			if(!campos[2].equalsIgnoreCase("NULL"))
				auxlist.add(campos[2]); //agregar Etq a lista auxiliar
			
			campos[1] = Analizador.FormatoContLoc(); //Se da formato al ContLoc
			String linea = "";
			for(byte x = 0;x < campos.length;x++) //se concatena los elementos de la linea
			{
				if(campos[x] == null)
					campos[x] = " ";
				linea = linea + "	" + campos[x];
			}
			linea = linea.trim(); //se eliminan los espacios en blanco a los extremos de la linea
			
			if(campos[3].equalsIgnoreCase("FCC")) //Si la linea contiene la Directiva FCC
			{
				String cad = "";
				try
				{
					ArcIns.escribir(linea);		//escribimos la linea en el Archivo Inst
					if(!campos[2].equalsIgnoreCase("NULL")) //si contiene una etiqueta
						ArcTds.escribir(campos[2] + "	" + campos[1]);  //escribimos en el TDS
				}
				catch(IOException e)
				{e.printStackTrace();}
				
				for(byte y = 4; y < campos.length;y++) //concatenamos el operando (cadena de caracteres)
				{
					if(campos[y] == " ")
						continue;
					cad = cad + " " + campos[y];
				}
				cad = cad.trim(); //quitamos espacios en los extremos
				cad = cad.substring(1,cad.length()-1); //subcadena sin las comillas
				An.AumentarContLoc(cad.length()); //se aumenta en ContLoc con la longitud de la cadena
			}
			else if(campos[3].equalsIgnoreCase("EQU")) //Si es la directiva EQU
			{
				linea = ""; //limpiamos la linea
				int auxCL = Analizador.regresaContLoc(); //guardamos el ContLoc actual en un auxiliar
				Analizador.ingresaContLoc(conversor(campos[4])); //ingresamos el valor del operando EQU al ContLoc
				campos[1] = Analizador.FormatoContLoc(); //remplazamos el ContLoc anterior de esta linea por el actual
				
				for(byte x = 0;x < campos.length;x++) //concatenamos los elementos de esta linea
				{
					if(campos[x] == null)
						campos[x] = " ";
					linea = linea + "	" + campos[x];
				}
				linea = linea.trim();  //eliminamos los espcacios en los extremos
				
				try
				{
					ArcIns.escribir(linea); //escribimos en el Archivo Inst
					if(!campos[2].equalsIgnoreCase("NULL")) //si tiene Etq
						ArcTds.escribir(campos[2] + "	" + campos[1]); //escribimos en el TDS
				}
				catch(IOException e)
				{e.printStackTrace();}
				
				Analizador.ingresaContLoc(auxCL); //Se regresa el valor del ContLoc actual del Analizador
			}
			else if(campos[3].equalsIgnoreCase("ORG")) //Si es la Directiva ORG
			{
				try
				{
					ArcIns.escribir(linea); //Se escribe en el Archivo Inst
					if(!campos[2].equalsIgnoreCase("NULL")) //Si tiene Etq
						ArcTds.escribir(campos[2] + "	" + campos[1]); //se escribe en el TDS
				}
				catch(IOException e)
				{e.printStackTrace();}
				Analizador.ingresaContLoc(conversor(campos[4])); //saltamos el ContLoc al valor que el Oper indica
			}
			else //Si se trata de un Codop del Tabop o una directiva de Constante o memoria
			{
				try
				{
					ArcIns.escribir(linea); //Se escribe en el Archivo Inst
					if(!campos[2].equalsIgnoreCase("NULL")) //si tiene Etiqueta
						ArcTds.escribir(campos[2] + "	" + campos[1]); //Se escribe en el TDS
				}
				catch(IOException e)
				{e.printStackTrace();}
				
				if(ArbolDeInst.containsKey(campos[3])) //Si es un Codop del Tabop
					sumarBytes(An, campos[3], campos[5], listadeInst); //Se suma al ContLoc la cantidad de Bytes que corresponden al Codop y al Direccionamiento
				else //Si es una Directiva de Constante o memoria
				{
					if(An.esConst1Byte(campos[3]))  //Constante de 1 Byte
					{
						An.AumentarContLoc(1); //Se suma 1 al ContLoc
					}
					else if(An.esConst2Bytes(campos[3])) //Constante de 2 Bytes
					{
						An.AumentarContLoc(2); //Se suma 2 al ContLoc
					}
					else if(An.esMemoria1Byte(campos[3])) //Memoria de 1 Byte
					{
						An.AumentarContLoc(conversor(campos[4])); //Se suma al ContLoc el valor del  Operando
					}
					else if(An.esMemoria2Bytes(campos[3])) //Memorua de 2 Bytes
					{
						int numbts = conversor(campos[4]);
						An.AumentarContLoc(numbts * 2); //Se suma al ContLoc el valor del Oper x 2
					}
				}
				
			}
		}
		An.actualizarListYConj(auxlist, auxconj); //actualizamos las Etiquetas validas
	}
	
	public int conversor(String oper)
	{
		int rango = 0;
		switch(oper.charAt(0))		//switch con el simbolo de la base
		{
		case '$':
			if(oper.substring(1).matches("[A-Fa-f0-9]+[A-Fa-f0-9]*")) //simbolos de base numerica validos
				rango = Integer.parseInt(oper.substring(1), 16); //rango del numero Hex en Decimal
			break;
		case '@':
			if(oper.substring(1).matches("[0-7]+[0-7]*")) //simbolos de base numerica validos))
				rango = Integer.parseInt(oper.substring(1),8); //rango del numero Oct en Decimal
			break;
		case '%':
			if(oper.substring(1).matches("[01]+[01]*")) //simbolos de base numerica validos
				rango = Integer.parseInt(oper.substring(1),2);
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			if(oper.matches("[-]*[0-9]+"))  //Simbolos de base numerica validos
				rango = Integer.parseInt(oper);
			break;
		}
		return rango;
	}
	public void sumarBytes(Analizador An,String codop,String direcc,Instruccion[] listadeInst)
	{
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				An.AumentarContLoc(listadeInst[i].regresaBT()); //Aumenta ContLoc con los bytes correspondientes
				break;
			}
		}
	}
}
