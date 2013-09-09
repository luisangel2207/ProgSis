package inst;

import java.io.IOException;
import java.util.StringTokenizer;

class Automata 
{
	protected boolean estError;
	protected boolean estEtq;
	protected boolean estCodop;
	protected boolean estErrDet;
	protected boolean estEnd;
	protected boolean estSinErr;
	
	public Automata()
	{
		estError = false;
		estEtq = false;
		estCodop = false;
		estErrDet = false;
		estEnd = false;
		estSinErr = false;
	}
	
	public void ingresaEstErr(boolean estado)
	{
		estError = estado;
	}
	public void ingresaEstEtq(boolean estado)
	{
		estEtq = estado;
	}
	public void ingresaEstCodop(boolean estado)
	{
		estCodop = estado;
	}
	public void ingresaEstErrDet(boolean estado)
	{
		estErrDet = estado;
	}
	public void ingresaEstEnd(boolean estado)
	{
		estEnd = estado;
	}
	public void ingresaEstSinErr(boolean estado)
	{
		estSinErr = estado;
	}
	
	public boolean regresaEstEnd()
	{
		return estEnd;
	}
	public boolean regresaEstSinErr()
	{
		return estSinErr;
	}
	public boolean regresaEstErrDet()
	{
		return estErrDet;
	}
	
	public void inicia4Comandos(Analizador An,StringTokenizer token,Archivo ArcErr,byte cont)
	{
		An.ingresarEtiqueta(token.nextToken());
		An.ingresarCodop(token.nextToken());
		An.ingresarOperando(token.nextToken());
		String comen = token.nextToken();
		
		if(!comen.equalsIgnoreCase("NULL"))		//cualquier otra cadena genera un error
			ingresaEstErr(estadoError(An,ArcErr,cont));		//transicion al estado de Error y se ingresa el valor del atributo
		else
		{
			ingresaEstEtq(estadoEtiqueta(An,ArcErr,cont));
			ingresaEstCodop(estadoCodop(An,ArcErr,cont));
			ingresaEstEnd(estadoEnd(An));
			ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
		}
	}
	
	public void inicia3Comandos(Analizador An,StringTokenizer token)
	{
		
	}
	
	public void inicia2Comandos(Analizador An,StringTokenizer token)
	{
		
	}
	
	public void inicia1Comando(Analizador An,StringTokenizer token)
	{
		
	}
	
	public boolean estadoError(Analizador An,Archivo ArcErr,byte cont)
	{
		An.ingresarEtiqueta(" ");
		An.ingresarCodop(" ");		//limpiar atributos
		An.ingresarOperando(" ");
		String comando = An.describirError((byte)3,", checar campo comentario" );		//Error de numero de cadenas
		comando = "Linea " + cont + " " + comando;	//concatenado linea de error
		try
		{
			ArcErr.escribir(comando); 	//escribir linea en el archivo .err
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return true;		//indica un error en la linea
	}
	
	public boolean estadoEtiqueta(Analizador An,Archivo ArcErr,byte cont)
	{
		byte error;
		String comando;
		if((error = An.validarEtiqueta()) != 0) //validacion de la etiqueta
		{
			comando = An.describirError(error,"en Etiqueta" );		//Detalle del tipo de error
			comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
			try
			{
				ArcErr.escribir(comando); //Escritura del error en el archivo .err
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			ingresaEstErrDet(true);		//estado de error en algun comando
			return true;
		}	
		else
			return false;
	}
	
	public boolean estadoCodop(Analizador An,Archivo ArcErr,byte cont)
	{
		byte error;
		if((error = An.validarCodop()) != 0)		//Validacion del codigo de Operacion
		{
			String comando = An.describirError(error,"en Codop" );		//Detalle del tipo de error
			comando = "Linea " + cont + " " + comando;		//concatenacion de tipo de error y su ubicacion
			try
			{
				ArcErr.escribir(comando);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return true;
		}
		else
			return false;
	}
	
	public boolean estadoEnd(Analizador An)
	{
		String codop = An.regresaCodop();	//asigna el valor actual del Codop
		if(codop.equalsIgnoreCase("END"))	//Verifica que el codop no sea la palabra End
			return true;	//directiva END detectada
		else
			return false;
	}
	
	public boolean estadoSinErrores(boolean err1,boolean err2,boolean err3, boolean err4)
	{
		if(err1 || err2 || err3 || err4)
			return false;	// hay errores en alguna validacion
		else
			return true;	//linea de codigo sin errores
	}
	
	public void reiniciarAutomata()
	{
		estError = false;
		estEtq = false;
		estCodop = false;
		estErrDet = false;
		estEnd = false;
		estSinErr = false;
	}

}
