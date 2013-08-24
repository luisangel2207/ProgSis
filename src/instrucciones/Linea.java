package instrucciones;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;

class Linea 
{
	public boolean crearArchivo(String nom_arc, String escribe)
	{
		Scanner teclado = new Scanner(System.in);
		File f;
		FileWriter fw;
		try
		{
		f = new File("archivos\\" + nom_arc);
		fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter salida = new PrintWriter(bw);
		
		String res = "si";
		
		do
		{
			salida.write(escribe + "\n");
			System.out.println("Desea agregar otra linea?"); 
			res = teclado.next();
			
		}while(res.equalsIgnoreCase("si"));
		
		salida.close();
		bw.close();
		return true;
		}
		catch(IOException e)
		{
			System.out.println("ERROR:" + e.getMessage() );
			return false;
		}
	}
	
	public boolean leerArchivo(String nom_arc)
	{
		try
		{
			File f = new File("archivos\\" + nom_arc);
			FileReader  fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String aux ="", linea = "";
			
			while(true)
			{
				aux = br.readLine();
				
				if(aux != null)
					linea = linea + aux + "\n";
				else
					break;
			}
			
			br.close();
			
			System.out.println(linea);
			return true;
		}
		catch(IOException e)
		{
			System.out.println("ERROR:" + e.getMessage());
			return false;
		}
	}

	
	public static void main(String[] args) 
	{
		Scanner teclado = new Scanner(System.in);
	
		
		
		System.out.println("Escribe el nombre de tu archivo y su extension");
		String nom = teclado.nextLine();
		System.out.println("Escriba el contenido del archivo");
		String cont = teclado.nextLine();
		Linea l = new Linea ();
		if(l.crearArchivo(nom,cont))
				System.out.println("Archivo creado!");
		
		System.out.println("Contenido de su archivo: \n");
		l.leerArchivo(nom);
				
		
		

	}

}
