package instrucciones;

import java.io.*;

class Linea 
{

	
	public static void main(String[] args) 
	{
		String cad;
		
		File f = new File(Linea.class.getResource("archivo.asm"));
		FileReader fr = new FileReader(f);
		BufferedReader in = new BufferedReader(fr);
		in.close();
		
		

	}

}
