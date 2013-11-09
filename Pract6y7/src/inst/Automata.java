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
	protected boolean estOrgAct;
	protected boolean estOperEtq;
	protected boolean estInstConErr;
	
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
		estOrgAct = false;
		estOperEtq = false;
		estInstConErr = false;
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
	public void ingresaEstOrgAct(boolean estado)
	{
		estOrgAct = estado;
	}
	public void ingresaEstOperEtq(boolean estado)
	{
		estOperEtq = estado;
	}
	public void ingresaEstInstConErr(boolean estado)
	{
		estInstConErr = estado;
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
	public boolean regresaEstOrgAct()
	{
		return estOrgAct;
	}
	public boolean regresaEstOperEtq()
	{
		return estOperEtq;
	}
	public boolean regresaEstInstConErr()
	{
		return estInstConErr;
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
	
	public void estadoValidaInst(Instruccion In,Analizador An,Automata Au,Direccionamiento Di,Instruccion[] listadeInst,TreeMap<String,String> ArbolDeInst,Archivo ArcIns,Archivo ArcErr,Archivo ArcTds,byte cont)
	{
		String comando;
		if(In.necesitaOper(An.regresaCodop(), listadeInst) && !An.regresaOper().equalsIgnoreCase("NULL")) //necesita operando y tiene operando
		{
			estadoConOper(An,Au,Di,ArcIns,ArcErr,ArcTds,ArbolDeInst,listadeInst,cont); //Transicion al estado de Codops con Oper
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
			estadoSinOper(An,Au,Di,ArcIns,ArcErr,ArcTds,ArbolDeInst,listadeInst,cont); //transicion al estado de Codops sin Oper
		}
	
	}
	
	public void estadoSinOper(Analizador An, Automata Au, Direccionamiento Di,Archivo ArcIns,Archivo ArcErr,Archivo ArcTds, TreeMap<String,String> ArbolDeInst,Instruccion[] listadeInst, byte cont)
	{
		String comando;
		String dir = ArbolDeInst.get(An.regresaCodop()); //direcc posibles del codop actual
		
		if(dir.contains("INH")) 	//Si el codop acepta el formato INH
			Di.inherente(dir,Au); 		// Formato de Direcc Inherente
		else
			Di.inmediatoSinOper(dir,Au); 	//Formato de Direcc Inmediato sin Oper
		
		if(regresaEstDirValido()) //Si se encontro el Direcc indicado
		{
			comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper()+"	"+Di.regresaTipo();	//concatenacion de tokens
			
			if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
			{
				System.out.println(comando);	//Salida  de la linea a consola
				try
				{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
				catch(IOException e)
				{e.printStackTrace();}
			
				An.sumarBytesContLoc(Di, listadeInst); //Busca los bytes correspondientes al Codop y el Direccionamiento
			}
			else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
			{
				System.out.println(comando);	//Salida  de la linea a consola
				try
				{
					ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
					comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
					ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
					An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
				}
				catch(IOException e)
				{e.printStackTrace();}
				
				An.sumarBytesContLoc(Di, listadeInst); //Busca los bytes correspondientes al Codop y el Direccionamiento
			}
			else
				ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
		}
	}
	
	public void estadoConOper(Analizador An,Automata Au,Direccionamiento Di,Archivo ArcIns,Archivo ArcErr,Archivo ArcTds, TreeMap<String,String> ArbolDeInst,Instruccion[] listadeInst, byte cont)
	{
		String comando;
		String dir = ArbolDeInst.get(An.regresaCodop()); //direcc posibles del Codop actual
		
		if(An.regresaOper().startsWith("#") || (Di.esDirOExtORel(An.regresaOper()) && !An.regresaOper().contains(",")))
			Di.direccConOper(An,Au,ArcErr,Di,dir,cont); //formato Inmediato,Dir,Ext y Rel
		
		else if(An.regresaOper().matches("\\[[^\\[][A-Za-z0-9]*,[+|-]?[A-Za-z]+[+|-]?\\]"))
			Di.direccConOper(An,Au,ArcErr,Di,dir,cont); //formato Idx indirecto
		else if(An.regresaOper().matches(",[+|-]?[a-zA-Z]+[+|-]?") || An.regresaOper().matches("[\\$|@|%|\\-]?[a-zA-Z0-9]+,[+|-]?[a-zA-Z]+[+|-]?")) //checar
			Di.direccConOper(An, Au, ArcErr, Di, dir, cont); //formato Indexado
		
		else if(An.regresaOper().matches("[a-zA-z]+[a-zA-z0-9_]*")) //Sintaxis valida para un Operando-Etiqueta
		{
			if(An.validarOperEtq() == 0)
			{
				Di.direccConOperEtq(An,Au,ArcErr,dir,cont); //Formato Operando-Etiqueta
				Au.ingresaEstOperEtq(true); //se activa la bandera de Operando-Etiqueta
			}
			else
				ArcErr.errorOperEtq(An, cont); //error en longitud del Operando-Etiqueta
		}
		
		else
			ArcErr.errorOperInv(An,cont); // Oper invalido para cualquier Codop
		
		if(regresaEstDirValido()) //Si se encontro el Direcc indicado
		{
			comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper()+"	"+Di.regresaTipo();	//concatenacion de tokens
			
			if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
			{
				System.out.println(comando);	//Salida  de la linea a consola
				try
				{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
				catch(IOException e)
				{e.printStackTrace();}
				if(Au.regresaEstOperEtq())
				{
					An.agregarOpEtqAConj(An.regresaOper()); //Ingresa el Oper-Etq al conjunto 
				}
			
				An.sumarBytesContLoc(Di, listadeInst); //Busca los bytes correspondientes al Codop y el Direccionamiento
			}
			else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
			{
				System.out.println(comando);	//Salida  de la linea a consola
				try
				{
					ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
					comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
					ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
					An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
				}
				catch(IOException e)
				{e.printStackTrace();}
				if(Au.regresaEstOperEtq())
				{
					An.agregarOpEtqAConj(An.regresaOper()); //Ingresa el Oper-Etq al conjunto 
				}
				
				An.sumarBytesContLoc(Di, listadeInst); //Busca los bytes correspondientes al Codop y el Direccionamiento
			}
			else
				ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
		}
		
	}
	
	public void estadoValidaDirect(Analizador An,Automata Au,Archivo ArcIns,Archivo ArcErr,Archivo ArcTds,Direccionamiento Di,byte cont)
	{
		String comando;
		if(An.regresaCodop().equalsIgnoreCase("ORG"))	//Directiva ORG encontrada
		{
			if(!(An.regresaOper().equalsIgnoreCase("NULL"))) //Si contiene Operando
			{
				if(!regresaEstOrgAct())  //si aun no se activa esta directiva
				{
					Di.conversorDeBase(An, Au, ArcErr, Di, cont); //se obtiene el valor del Operando
					if(!Au.regresaEstErrNum()) //si no hubo errores en la conversion
					{
						if(Di.regresaRango() >= 0 && Di.regresaRango() <= 65535) //Si el Oper esta dentro del rango
						{
							comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
							
							if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
							{
								System.out.println(comando);	//Salida  de la linea a consola
								try
								{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
								catch(IOException e)
								{e.printStackTrace();}
								
								Analizador.ingresaContLoc(Di.regresaRango()); //se traslada el ContLoc a la direccion indicada
								ingresaEstOrgAct(true); //activar bandera de Directiva ORG detectada
							}
							else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
							{
								System.out.println(comando);	//Salida  de la linea a consola
								try
								{
									ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
									comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
									ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
									An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
								}
								catch(IOException e)
								{e.printStackTrace();}
								
								Analizador.ingresaContLoc(Di.regresaRango()); //se traslada el ContLoc a la direccion indicada
								ingresaEstOrgAct(true); //activar bandera de Directiva ORG detectada
							}
							else
								ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
						}
						else
							ArcErr.errorOrgInvalida(An, cont); //Oper fuera de rango
					}
				}
				else
					ArcErr.errorOrgRepetida(An,cont); //mas de una directiva ORG en el codigo
			}
			else //Ausencia de Operando
			{
				comando = An.describirError((byte)10,", Incluir la direccion en la Directiva");	//ORG sin Direccion
				comando = "Linea " + cont + " " + comando;
				try
				{ArcErr.escribir(comando);}	//Escribe el error en el Archivo .err
				catch(IOException e)
				{e.printStackTrace();}
			}
					
		}
		else if(An.regresaCodop().equalsIgnoreCase("END")) //Directiva END encontrada
		{
			if(An.regresaOper().equalsIgnoreCase("NULL"))
			{
				if(An.regresaEtq().equalsIgnoreCase("NULL"))
				{
					An.ingresaFinEjecuccion(true);	//activa la bandera para dejar de leer el archivo
					comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
					System.out.println(comando);	//Salida  de la linea a consola
			
					try
					{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
					catch(IOException e)
					{e.printStackTrace();}
				}
				else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
				{
					An.ingresaFinEjecuccion(true);	//activa la bandera para dejar de leer el archivo
					comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
					System.out.println(comando);	//Salida  de la linea a consola
					
					try
					{
						ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
						comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
						ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
						An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
					}
					catch(IOException e)
					{e.printStackTrace();}
				}
				else
					ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
			}
			else
			{
				comando = An.describirError((byte)17,", eliminar Oper de la Directiva");	//Directiva END con Oper
				try
				{ArcErr.escribir(comando);}	//Escribe el error en el Archivo .err
				catch(IOException e)
				{e.printStackTrace();}
			}
		}
		else if(An.regresaCodop().equalsIgnoreCase("EQU")) //Directiva EQU encontrada
		{
			if(!An.regresaEtq().equalsIgnoreCase("NULL") && !An.regresaOper().equalsIgnoreCase("NULL")) //Si tiene etiqueta y Operando la directiva
			{
				Di.conversorDeBase(An, Au, ArcErr, Di, cont); //obtenemos el valor del Oper
				
				if(!Au.regresaEstErrNum())  //Si no hubo error en la conversion
				{
					if(Di.regresaRango() >= 0 && Di.regresaRango() <= 65535)
					{
						int auxContLoc = Analizador.regresaContLoc(); //guardamos el ContLoc
						
						Analizador.ingresaContLoc(Di.regresaRango()); //ingresamos el valor del Oper al ContLoc
						comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
						
						if(!An.contieneEtqLista(An.regresaEtq())) //si la Etq no esta repetida
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{
								ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
								comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
								ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
								An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
								
							}
							catch(IOException e)
							{e.printStackTrace();}
						}
						else
							ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
						
						Analizador.ingresaContLoc(auxContLoc); //reestablecemos el ContLoc del Analizador
					}
					else
						ArcErr.errorEquSinComando(An,Di,cont); //error de Rango
				}
			}
			else
				ArcErr.errorEquSinComando(An,Di,cont); //Ausencia de Etq u Oper
		}
		else if(An.esConst1Byte(An.regresaCodop())) //directivas de constantes de 1 byte
		{
			if(!An.regresaOper().equalsIgnoreCase("NULL"))
			{ 
				Di.conversorDeBase(An,Au,ArcErr,Di,cont); //obtenemos el valor del Operando
				if(!Au.regresaEstErrNum()) //Si no hubo errores en la conversion
				{
					if(Di.regresaRango() >= 0 && Di.regresaRango() <= 255)
					{
						comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
						
						if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
							catch(IOException e)
							{e.printStackTrace();}
						
							An.AumentarContLoc(1); //aumenta el contador de localidades
						}
						else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{
								ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
								comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
								ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
								An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
							}
							catch(IOException e)
							{e.printStackTrace();}
							
							An.AumentarContLoc(1); //aumenta el contador de localidades
						}
						else
							ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
					}
					else
						ArcErr.errorDeRangoDirect(An,cont);
				}
			} 
			else
				ArcErr.errorDirectsinOper(An, cont); //Directiva sin Oper
		}
		else if(An.esConst2Bytes(An.regresaCodop())) //directivas de constantes de 2 bytes
		{
			if(!An.regresaOper().equalsIgnoreCase("NULL"))
			{ 
				Di.conversorDeBase(An,Au,ArcErr,Di,cont); //obtenemos el valor del Operando
				if(!Au.regresaEstErrNum()) //Si no hubo errores en la conversion
				{
					if(Di.regresaRango() >= 0 && Di.regresaRango() <= 65535)
					{
						comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
						
						if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
							catch(IOException e)
							{e.printStackTrace();}
						
							An.AumentarContLoc(2); //aumenta el contador de localidades
						}
						else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{
								ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
								comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
								ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
								An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
							}
							catch(IOException e)
							{e.printStackTrace();}
							
							An.AumentarContLoc(2); //aumenta el contador de localidades
						}
						else
							ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
					}
					else
						ArcErr.errorDeRangoDirect(An,cont);
				}
			} 
			else
				ArcErr.errorDirectsinOper(An, cont); //Directiva sin Oper
		}
		else if(An.esMemoria1Byte(An.regresaCodop()) || An.esMemoria2Bytes(An.regresaCodop())) //directivas de memoria de 1 y 2 bytes
		{
			if(!An.regresaOper().equalsIgnoreCase("NULL"))
			{ 
				Di.conversorDeBase(An,Au,ArcErr,Di,cont);
				if(!Au.regresaEstErrNum()) //Si no hubo errores en la conversion
				{
					if(Di.regresaRango() >= 0 && Di.regresaRango() <= 65535)
					{
						comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
						
						if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
							catch(IOException e)
							{e.printStackTrace();}
						
							if(An.esMemoria1Byte(An.regresaCodop()))
								An.AumentarContLoc(Di.regresaRango()); //Aumenta el ContLoc con el valor del Oper
							else
								An.AumentarContLoc(Di.regresaRango() * 2); //Aumenta el ContLoc con el valor del Oper x 2
						}
						else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
						{
							System.out.println(comando);	//Salida  de la linea a consola
							try
							{
								ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
								comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
								ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
								An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
							}
							catch(IOException e)
							{e.printStackTrace();}
							
							if(An.esMemoria1Byte(An.regresaCodop()))
								An.AumentarContLoc(Di.regresaRango()); //Aumenta el ContLoc con el valor del Oper
							else
								An.AumentarContLoc(Di.regresaRango() * 2); //Aumenta el ContLoc con el valor del Oper x 2
						}
						else
							ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
					}
					else
						ArcErr.errorDeRangoDirect(An,cont);
				}
			} 
			else
				ArcErr.errorDirectsinOper(An, cont); //Directiva sin Oper
		}
		else if(An.regresaCodop().equalsIgnoreCase("FCC"))
		{
			if(!An.regresaOper().equalsIgnoreCase("NULL"))
			{
				if(An.regresaOper().matches("\"[^\"]*\""))
				{
					if(An.regresaOper().contains("|"))
						An.ingresarOperando(An.regresaOper().replace("|"," ")); //se remplaza el separador especial por el espacio
					
					String cad = An.regresaOper().substring(1, An.regresaOper().length()-1);
					
					comando = cont+"	"+Analizador.FormatoContLoc()+"	"+An.regresaEtq()+"	"+An.regresaCodop()+"	"+An.regresaOper();	//concatenacion de tokens
					
					if(An.regresaEtq().equalsIgnoreCase("NULL")) //si no hay Etq
					{
						System.out.println(comando);	//Salida  de la linea a consola
						try
						{ArcIns.escribir(comando);} //Escritura de la linea en el archivo .inst
						catch(IOException e)
						{e.printStackTrace();}
					
						An.AumentarContLoc(cad.length()); //aumenta el ContLoc segun el numero de caracteres en el Oper
					}
					else if(!An.contieneEtqLista(An.regresaEtq())) //Si existe Etq y no esta repetida
					{
						System.out.println(comando);	//Salida  de la linea a consola
						try
						{
							ArcIns.escribir(comando); //Escritura de la linea en el archivo .inst
							comando = An.regresaEtq()+"	"+Analizador.FormatoContLoc(); //concatenacion de tokens
							ArcTds.escribir(comando); //Escritura de la linea en el archivo .tds
							An.agregarEtqAlista(An.regresaEtq()); //agregamos la etiqueta a la lista
						}
						catch(IOException e)
						{e.printStackTrace();}
						
						An.AumentarContLoc(cad.length()); //aumenta el ContLoc segun el numero de caracteres en el Oper
					}
					else
						ArcErr.errorEtqRepetida(An, cont); //Etiqueta repetida
				}
				else
					ArcErr.errorFormatoOper(An,cont);
			} 
			else
				ArcErr.errorDirectsinOper(An, cont); //Directiva sin Oper
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
		estOperEtq = false;
	}

}
