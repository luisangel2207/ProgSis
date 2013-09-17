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
		Instruccion[] listadeInst = new Instruccion[POSIBLES_INST];
		if(Tabop.existeArchivo())
		{
			Tabop.crearBuffer();
			for(int i = 0;i < listadeInst.length; i++)
			{
				listadeInst[i] = new Instruccion();
				listadeInst[i].crearInstruccion(Tabop);
			}
		}
		else
		{
			String comando = An.describirError((byte)6,"Verificar ruta del Archivo");
			try
			{
				ArcErr.escribir(comando);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		return listadeInst;
	}
	
	
	public Instruccion crearInstruccion(Archivo Tabop)
	{
		String instruccion;
		
		if((instruccion = Tabop.lineaActual()) != null)
		{
			StringTokenizer sep = new StringTokenizer(instruccion);
				
			ingresaIdCodop(sep.nextToken());
			ingresaInst(sep.nextToken());
			ingresaCodMaq(sep.nextToken());
			ingresaBC(Byte.parseByte(sep.nextToken()));
			ingresaBPC(Byte.parseByte(sep.nextToken()));
			ingresaBT(Byte.parseByte(sep.nextToken()));
		}	
		return this;	
	}
	
	public boolean necesitaOper(String codop,Instruccion[] listadeInst)
	{
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equals(listadeInst[i].regresaIdCodop()))
			{
				if(listadeInst[i].regresaBPC() != (byte)0 )
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
		TreeMap<String,String> mapaDeInst = new TreeMap<String,String>();
		
		for(int i = 0; i<listadeInst.length; i++)
		{
			String actual = listadeInst[i].regresaIdCodop();
			if(!(i+1 == listadeInst.length))
			{
				String sig = listadeInst[i+1].regresaIdCodop();
		
				if(actual.equals(sig))
				{ 
					direcc += listadeInst[i].regresaInst() + ",";
				}
				else
				{
					direcc += listadeInst[i].regresaInst();
					mapaDeInst.put(actual, direcc);
					direcc = "";
				}
			}
			else
			{
				direcc += listadeInst[i].regresaInst();
				mapaDeInst.put(actual, direcc);
				direcc = "";
			}
		}
		
		return mapaDeInst;
	}
	
}
