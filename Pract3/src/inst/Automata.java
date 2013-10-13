package inst;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;

class Automata 
{
	protected boolean estError;
	protected boolean estEtq;
	protected boolean estCodop;
	protected boolean estErrDet;
	protected boolean estEnd;
	protected boolean estSinErr;
	protected boolean estDireccVal;
	protected boolean estImm;
	protected boolean estRel;
	protected boolean estDir;
	protected boolean estErrNum;
	protected boolean estIdx;
	protected boolean estIncDec;
	protected boolean estRegABD;
	protected boolean estIdxVacio;
	
	public Automata()
	{
		estError = false;
		estEtq = false;
		estCodop = false;
		estErrDet = false;
		estEnd = false;
		estSinErr = false;
		estDireccVal = false;
		estImm = false;
		estRel = false;
		estDir = false;
		estErrNum = false;
		estIdx = false;
		estIncDec = false;
		estRegABD = false;
		estIdxVacio = false;
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
	public void ingresaEstDirValido(boolean estado)
	{
		estDireccVal = estado;
	}
	public void ingresaEstImm(boolean estado)
	{
		estImm = estado;
	}
	public void ingresaEstRel(boolean estado)
	{
		estRel = estado;
	}
	public void ingresaEstDir(boolean estado)
	{
		estDir = estado;
	}
	public void ingresaEstErrNum(boolean estado)
	{
		estErrNum = estado;
	}
	public void ingresaEstIdx(boolean estado)
	{
		estIdx = estado;
	}
	public void ingresaEstIncDec(boolean estado)
	{
		estIncDec = estado;
	}
	public void ingresaEstRegABD(boolean estado)
	{
		estRegABD = estado;
	}
	public void ingresaEstIdxVacio(boolean estado)
	{
		estIdxVacio = estado;
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
	public boolean regresaEstDirValido()
	{
		return estDireccVal;
	}
	public boolean regresaEstImm()
	{
		return estImm;
	}
	public boolean regresaEstRel()
	{
		return estRel;
	}
	public boolean regresaEstDir()
	{
		return estDir;
	}
	public boolean regresaEstErrNum()
	{
		return estErrNum;
	}
	public boolean regresaEstIdx()
	{
		return estIdx;
	}
	public boolean regresaEstIncDec()
	{
		return estIncDec;
	}
	public boolean regresaEstRegABD()
	{
		return estRegABD;
	}
	public boolean regresEstIdxVacio()
	{
		return estIdxVacio;
	}
	
	public void inicia4Comandos(Analizador An,StringTokenizer token,Archivo ArcErr,String linea,byte cont)
	{
		An.ingresarEtiqueta(token.nextToken());
		An.ingresarCodop(token.nextToken());		//Ingresa los atributos correspondientes
		An.ingresarOperando(token.nextToken());
		String comen = token.nextToken();
		if(linea.startsWith(An.regresaEtq()))	//verifica que la linea comience con la Etiqueta
		{
			if(!comen.equalsIgnoreCase("NULL"))		//cualquier otra cadena genera un error
				ingresaEstErr(estadoError(An,ArcErr,cont));		//transicion al estado de Error y se ingresa el valor del atributo
			else
			{
				ingresaEstEtq(estadoEtiqueta(An,ArcErr,cont));	//entra al estado donde se evalua la etiqueta
				ingresaEstCodop(estadoCodop(An,ArcErr,cont));	//entra al estado donde se evalua el codop
				ingresaEstEnd(estadoEnd(An));		//Verificamos si el codop es la directiva END
				ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));	//ultimo estado, se verifica que todos los comandos fueron validos
			}
		}
		else
		{
			String comando = An.describirError((byte)5,", la linea no comienza con Etq" );	//posicion incorrecta de la etiqueta
			comando = "Linea " + cont + " " + comando;	//concatenado linea de error
			try
			{
				ArcErr.escribir(comando); 	//escribir linea en el archivo .err
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
			
	}
	
	public void inicia3Comandos(Analizador An,StringTokenizer token,Archivo ArcErr,String linea,byte cont)
	{
		An.ingresarEtiqueta(token.nextToken());
		An.ingresarCodop(token.nextToken());
		An.ingresarOperando(token.nextToken());
		if(!An.regresaOper().equalsIgnoreCase("NULL"))	//Si el tercer comando no es un comentario..
		{
			if(linea.startsWith(An.regresaEtq()))	//y si la linea inicia con la etiqueta
			{
				ingresaEstEtq(estadoEtiqueta(An,ArcErr,cont));
				ingresaEstCodop(estadoCodop(An,ArcErr,cont));	//transiciones del Automata
				ingresaEstEnd(estadoEnd(An));
				ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
			}
			else
			{
				String comando = An.describirError((byte)5,", la linea no comienza con Etq" );	//posicion incorrecta de la etiqueta
				comando = "Linea " + cont + " " + comando;	//concatenado linea de error
				try
				{
					ArcErr.escribir(comando); 	//escribir linea en el archivo .err
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else	//Si el tercer comando es un comentario..
			continua2Comandos(An,ArcErr,linea,cont);	//transicion al estado de 2 comandos detectados	
	}
	
	public void inicia2Comandos(Analizador An,StringTokenizer token,Archivo ArcErr,String linea,byte cont)
	{
		if((linea.startsWith(" ")) || (linea.startsWith("\t")))
		{
			An.ingresarEtiqueta("NULL");
			An.ingresarCodop(token.nextToken());		//ingresa los atributos correspondientes
			An.ingresarOperando(token.nextToken());
			
			ingresaEstCodop(estadoCodop(An,ArcErr,cont));
			ingresaEstEnd(estadoEnd(An));		//transiciones del Automata
			ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
		}
		else
		{
			An.ingresarEtiqueta(token.nextToken());
			An.ingresarCodop(token.nextToken());		//ingresa los atributos correspondientes
			An.ingresarOperando("NULL");
			
			if(!An.regresaCodop().equalsIgnoreCase("NULL"))		//Verificamos el formato correspondiente a esta linea
			{
				ingresaEstEtq(estadoEtiqueta(An,ArcErr,cont));
				ingresaEstCodop(estadoCodop(An,ArcErr,cont));
				ingresaEstEnd(estadoEnd(An));
				ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
			}
			else		
			{
				String comando = An.describirError((byte)3,", formato de instruccion invalido" );		//insuficientes comandos
				comando = "Linea " + cont + " " + comando;	//concatenado linea de error
				try
				{
					ArcErr.escribir(comando); 	//escribir linea en el archivo .err
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
				
		}
	}
	
	public void continua2Comandos(Analizador An,Archivo ArcErr,String linea,byte cont)
	{
		if((linea.startsWith(" ")) || (linea.startsWith("\t")))	
		{
			An.ingresarOperando(An.regresaCodop());		//movemos los
			An.ingresarCodop(An.regresaEtq());			//comandos a sus
			An.ingresarEtiqueta("NULL");				//respectivos atributos
			
			ingresaEstCodop(estadoCodop(An,ArcErr,cont));
			ingresaEstEnd(estadoEnd(An));		//transiciones del Automata
			ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
		}
		else
		{
			ingresaEstEtq(estadoEtiqueta(An,ArcErr,cont));
			ingresaEstCodop(estadoCodop(An,ArcErr,cont));	//transiciones del Automata
			ingresaEstEnd(estadoEnd(An));
			ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
		}
	}
	
	public void inicia1Comando(Analizador An,StringTokenizer token,Archivo ArcErr,String linea,byte cont)
	{
		if((linea.startsWith(" ")) || (linea.startsWith("\t")))
		{
			An.ingresarEtiqueta("NULL");
			An.ingresarCodop(token.nextToken());		//ingresa atributos correspondientes
			An.ingresarOperando("NULL");
			
			ingresaEstCodop(estadoCodop(An,ArcErr,cont));
			ingresaEstEnd(estadoEnd(An));		//transiciones del Automata
			ingresaEstSinErr(estadoSinErrores(estError,estEtq,estCodop,estErrDet));
		}
		else
			ingresaEstErr(estadoError(An,ArcErr,cont));		//transicion al estado de Error y se ingresa el valor del atributo
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
				ArcErr.escribir(comando);		//Escribir linea en el archivo de errores
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
	
	public void estadoValidaInst(Instruccion In,Analizador An,Automata Au,Direccionamiento Di,Instruccion[] listadeInst,TreeMap<String,String> ArbolDeInst,Archivo ArcIns,Archivo ArcErr,byte cont)
	{
		String comando;
		if(In.necesitaOper(An.regresaCodop(), listadeInst) && !An.regresaOper().equalsIgnoreCase("NULL")) //necesita operando y tiene operando
		{
			estadoConOper(An,Au,Di,ArcIns,ArcErr,ArbolDeInst,cont); //Transicion al estado de Codops con Oper
		}
		else if(!In.necesitaOper(An.regresaCodop(), listadeInst) && !An.regresaOper().equalsIgnoreCase("NULL")) //no necesita operando y tiene operando
		{
			comando = An.describirError((byte)8,", retirar Operando de la instruccion"); //Codop con Operando innecesario
			comando = "Linea " + cont + " " + comando;
			try
			{ArcErr.escribir(comando);}	//Escribe el error en el Archivo .err
			
			catch(IOException e)
			{e.printStackTrace();}
		}
		else if(In.necesitaOper(An.regresaCodop(), listadeInst) && An.regresaOper().equalsIgnoreCase("NULL")) //necesita operando y no tiene operando
		{
			comando = An.describirError((byte)7,", Agregar Operando a la Instruccion");	//Ausencia de Operando en Codop
			comando = "Linea " + cont + " " + comando;
			try
			{ArcErr.escribir(comando);}	//Escribe el error en el Archivo .err
			
			catch(IOException e)
			{e.printStackTrace();}
		}
		else 	//no necesita operando y no lo tiene
		{
			estadoSinOper(An,Au,Di,ArcIns,ArbolDeInst,cont); //transicion al estado de Codops sin Oper
		}
	
	}
	
	public void estadoSinOper(Analizador An, Automata Au, Direccionamiento Di,Archivo ArcIns, TreeMap<String,String> ArbolDeInst, byte cont)
	{
		String comando;
		String dir = ArbolDeInst.get(An.regresaCodop()); //direcc posibles del codop actual
		
		if(dir.contains("INH")) 	//Si el codop acepta el formato INH
			Di.inherente(dir,Au); 		// Formato de Direcc Inherente
		else
			Di.inmediatoSinOper(dir,Au); 	//Formato de Direcc Inmediato sin Oper
		
		if(regresaEstDirValido()) //Si se encontro el Direcc indicado
		{
			comando = cont+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper()+"	"+Di.regresaTipo();	//concatenacion de tokens
			System.out.println(comando);	//Salida  de la linea a consola
			try
			{ArcIns.escribir(comando);}	//Escritura de la linea en el archivo .inst
			catch(IOException e)
			{e.printStackTrace();}
		}
	}
	
	public void estadoConOper(Analizador An,Automata Au,Direccionamiento Di,Archivo ArcIns,Archivo ArcErr, TreeMap<String,String> ArbolDeInst, byte cont)
	{
		String comando;
		String dir = ArbolDeInst.get(An.regresaCodop()); //direcc posibles del Codop actual
		
		if(An.regresaOper().startsWith("#") || (Di.esDirOExtORel(An.regresaOper()) && !An.regresaOper().contains(",")))
			Di.direccConOper(An,Au,ArcErr,Di,dir,cont); //formato Inmediato,Dir,Ext y Rel
		
		//else if(An.regresaOper().contains(","))
		//{
		else if(An.regresaOper().matches("\\[[^\\[][A-Za-z0-9]*,[+|-]?[A-Za-z]+[+|-]?\\]"))
			Di.direccConOper(An,Au,ArcErr,Di,dir,cont); //formato Idx indirecto
		else if(An.regresaOper().matches(",[+|-]?[a-zA-Z]+[+|-]?") || An.regresaOper().matches("[\\$|@|%|\\-]?[a-zA-Z0-9]+,[+|-]?[a-zA-Z]+[+|-]?")) //checar
			Di.direccConOper(An, Au, ArcErr, Di, dir, cont); //formato Indexado
		//}
		
		else if(An.validarOperEtq() == 0)
			Di.direccConOperEtq(An,Au,ArcErr,dir,cont); //Formato Operando-Etiqueta
		
		else
			ArcErr.errorOperInv(An,cont); // Oper invalido para cualquier Codop
		
		if(regresaEstDirValido()) //Si se encontro el Direcc indicado
		{
			comando = cont+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper()+"	"+Di.regresaTipo();	//concatenacion de tokens
			System.out.println(comando);	//Salida  de la linea a consola
			try
			{ArcIns.escribir(comando);}	//Escritura de la linea en el archivo .inst
		
			catch(IOException e)
			{e.printStackTrace();}
		}
		
	}
	
	public void reiniciarAutomata()
	{
		estError = false;
		estEtq = false;
		estCodop = false;
		estErrDet = false;		//regresamos al estado inicial para nueva transicion 
		estEnd = false;
		estSinErr = false;
		estDireccVal = false;
		estImm = false;
		estRel = false;
		estDir = false;
		estErrNum = false;
		estIdx = false;
		estIncDec = false;
		estRegABD = false;
		estIdxVacio = false;
	}

}
