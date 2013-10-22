package inst;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

class Archivo 
{
	//Atributos
	private File arch;
	private FileReader fr;
	private BufferedReader buff;
	private String linea;
	private String direccion;
	private String nombre;
	private byte tipo;
	
	public Archivo(String ruta, String nom, byte tip)
	{
		tipo = tip;
		direccion = ruta;
		nombre = nom;
		arch = new File(direccion,nombre); 
	}
	
	public boolean existeArchivo()
	{
		
		if(arch.exists())
			return true;
		else
			return false;
	}
	
	public void  borrarArchivo()
	{
		arch.delete();
	}
	
	public void escribir(String linea) throws IOException
	{
		try
		{		
			FileWriter escribir = new FileWriter(arch, true); 	//Creacion del objeto con el que se escribira en el archivo
			BufferedWriter buffer = new BufferedWriter(escribir);	//Creacion del buffer para escribir en el archivo
			buffer.write(linea);	//volcar el buffer 
			buffer.newLine(); //separador de linea
			buffer.close();	//buffer cerrado
		}
				
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void crearBuffer()
	{
		try
		{
			fr = new FileReader(arch);
			buff = new BufferedReader(fr);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String lineaActual()
	{
		try
		{
			linea = buff.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return linea;
	}
	
	public void cerrarLector()
	{
		try
		{
			fr.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void errorSimboloDeBase(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)11,", formatos validos: $,@,% o un numero"); //simbolo de base erroneo
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorSimNumInv(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)12,", verifique que los numeros pertenecezcan a la base numerica"); //numeros invalidos
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorDeRango(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)13,", verifique rangos para ese formato de Oper"); //rango invalido
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorFormatoInv(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)14,", verifique los Direccs que acepta el Codop"); //Formato invalido para ese codop
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorRegistInv(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)15,", Verifique la sintaxis o validez del registro para ese Codop"); //Formato invalido de Reg PC
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorOperInv(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)16,", Verifique los formatos validos para los Direccionamientos"); //Oper Invalido para todo codop
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorOperEtq(Analizador An, byte cont) 
	{
		String comando;
		comando = An.describirError((byte)18,", Verifique los formatos validos para etiquetas"); //Oper Invalido para todo codop
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorOrgRepetida(Analizador An, byte cont)
	{
		String comando;
		comando = An.describirError((byte)19,", ORG ya habia sido activada"); //Oper Invalido para todo codop
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorEquSinComando(Analizador An,Direccionamiento Di, byte cont)
	{
		String comando = "";
		if(An.regresaEtq().equalsIgnoreCase("NULL"))
			comando = An.describirError((byte)20,", Agregar ETQ a la directiva"); //EQU sin Etiqueta
		else if(An.regresaOper().equalsIgnoreCase("NULL"))
			comando = An.describirError((byte)20,", Agregar OPER a la directiva"); //EQU sin Operando
		else if(Di.regresaRango() < 0 || Di.regresaRango() > 65535)
			comando = An.describirError((byte)20,", Oper fuera de Rango"); //EQU con Oper fuera de Rango
		
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
			
	}
	
	public void errorDeRangoDirect(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)21,", verifique rangos de esa Directiva"); //Oper Invalido para esa Directiva
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorFormatoOper(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)14,", el Oper debe iniciar y terminar con comillas"); //Sintaxis invalida en el Oper
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorDirectsinOper(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)22,", agregar Oper a la directiva"); //Directiva sin Oper
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorEtqRepetida(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)23,", cambiar Etq a la Inst"); //Etq repetida
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
	
	public void errorOrgInvalida(Analizador An,byte cont)
	{
		String comando;
		comando = An.describirError((byte)19,", Oper fuera de Rango"); //ORG con Oper fuera de Rango
		comando = "Linea " + cont + " " + comando;
		
		try
		{escribir(comando);}	//Escribe el error en el Archivo .err
		catch(IOException e)
		{e.printStackTrace();}
	}
}
