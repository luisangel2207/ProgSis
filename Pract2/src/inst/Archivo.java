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
	
}
