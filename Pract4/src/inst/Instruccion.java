package inst;

import java.io.IOException;
import java.util.TreeMap;
import java.util.StringTokenizer;

class Instruccion 
{
	private String idCodop;
	private String inst;
	private String codMaq;
	private byte bytesCalc;
	private byte bytesPorCalc;
	private byte bytesTotales;
	private static short POSIBLES_INST = 589;
	
	public void ingresaIdCodop(String idcodop)
	{
		idCodop = idcodop;
	}
	public void ingresaInst(String instruccion)
	{
		inst = instruccion;
	}
	public void ingresaCodMaq(String codigomaquina)
	{
		codMaq = codigomaquina;
	}
	public void ingresaBC(byte bytescalculados)
	{
		bytesCalc = bytescalculados;
	}
	public void ingresaBPC(byte bytesporcalc)
	{
		bytesPorCalc = bytesporcalc;
	}
	public void ingresaBT(byte bytestotales)
	{
		bytesTotales = bytestotales;
	}
	
	public String regresaIdCodop()
	{
		return idCodop;
	}
	public String regresaInst()
	{
		return inst;
	}
	public String regresaCodMaq()
	{
		return codMaq;
	}
	public byte regresaBC()
	{
		return bytesCalc;
	}
	public byte regresaBPC()
	{
		return bytesPorCalc;
	}
	public  byte regresaBT()
	{
		return bytesTotales;
	}
	
	public Instruccion[] cargarTabop(Analizador An,Archivo Tabop, Archivo ArcErr)
	{
		Instruccion[] listadeInst = new Instruccion[POSIBLES_INST]; //se crea un arreglo de instrucciones
		if(Tabop.existeArchivo())	//Si el tabo existe
		{
			Tabop.crearBuffer();	//se crea un buffer para leer
			for(int i = 0;i < listadeInst.length; i++)
			{
				listadeInst[i] = new Instruccion();	//creamos un objeto Instruccion en cada posicion del arreglo
				listadeInst[i].crearInstruccion(Tabop);	//se llenan los atributos de la Instruccion
			}
		}
		else	//Tabop no encontrado
		{
			String comando = An.describirError((byte)6,"Verificar ruta del Archivo");
			try
			{
				ArcErr.escribir(comando);	//Se escribe el error en el archivo .err
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		return listadeInst;	//Arreglo de Instrucciones ya inicializados
	}
	
	
	public Instruccion crearInstruccion(Archivo Tabop)
	{
		String instruccion;
		
		if((instruccion = Tabop.lineaActual()) != null)	//Asignacion de la linea actual del Tabop
		{
			StringTokenizer sep = new StringTokenizer(instruccion);	// separamos cada token
				
			ingresaIdCodop(sep.nextToken());
			ingresaInst(sep.nextToken());
			ingresaCodMaq(sep.nextToken());
			ingresaBC(Byte.parseByte(sep.nextToken()));
			ingresaBPC(Byte.parseByte(sep.nextToken()));
			ingresaBT(Byte.parseByte(sep.nextToken()));
		}	
		return this;	// Objeto Instruccion con los atributos de la linea actual del Tabop
	}
	
	public boolean necesitaOper(String codop,Instruccion[] listadeInst)
	{
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equals(listadeInst[i].regresaIdCodop())) //Si el codop a buscar se encuentra
			{
				if(listadeInst[i].regresaBPC() != (byte)0 ) // si el Codop no necesita bytes por calcular
					return true;
				else
					return false;
			}
				
		}
		return false;
	}
	
	public TreeMap<String,String> arbolDeDirecc(Instruccion [] listadeInst)
	{
		String direcc = "";
		TreeMap<String,String> mapaDeInst = new TreeMap<String,String>(); //Arbol de clave-valor parametrizado
		
		for(int i = 0; i<listadeInst.length; i++)	//iterativa de acceso a cada Instruccion en el Arreglo
		{
			String actual = listadeInst[i].regresaIdCodop();	//Variable con el Codop de la linea actual
			if(!(i+1 == listadeInst.length)) //mientras siguiente no sea la ultima posicion
			{
				String sig = listadeInst[i+1].regresaIdCodop(); //Variable con el Codop de la linea Siguiente
		
				if(actual.equals(sig)) //Si los Codops son iguales
				{ 
					direcc += listadeInst[i].regresaInst() + ","; //Variable con los direccionamientos
				}
				else
				{
					direcc += listadeInst[i].regresaInst(); //Asignacion del ultimo direccionamiento del Codop
					mapaDeInst.put(actual, direcc);	//Se agrega al Arbol de clave-valor
					direcc = ""; //limpiamos la variable
				}
			}
			else
			{
				direcc += listadeInst[i].regresaInst(); //ultimo direccionamiento del arreglo
				mapaDeInst.put(actual, direcc); //se agrega al Arbol de clave-valor
				direcc = ""; //limpiamos variable
			}
		}
		
		return mapaDeInst; //Arbol de clave-valor con todos los Codops y direccionamientos
	}
	
}
