package inst;

import java.util.StringTokenizer;

class Direccionamiento 
{
	private String tipo;
	private int rango;
	private static String BASEHEX = "F";
	private static String BASEOCT = "7";
	private static String BASEBIN = "1";
	private static String PREDEC = "(-?X|-?Y|-?SP|PC)";
	private static String POSDEC = "(X-?|Y-?|PC|SP-?)";
	private static String PREINC = "(\\+?X|\\+?Y|PC|\\+?SP)";
	private static String POSINC = "(X\\+?|Y\\+?|PC|SP\\+?)";
			
	
	public void ingresaTipo(String tip)
	{
		tipo = tip;
	}
	public String regresaTipo()
	{
		return tipo;
	}
	public void ingresaRango(int ran)
	{
		rango = ran;
	}
	
	public int regresaRango()
	{
		return rango;
	}
	
	public boolean esDirOExtORel(String oper)
	{
		return (oper.startsWith("$")||oper.startsWith("@")||oper.startsWith("%")||oper.startsWith("0")||
				oper.startsWith("1")||oper.startsWith("2")||oper.startsWith("3")||oper.startsWith("4")||
				oper.startsWith("5")||oper.startsWith("6")||oper.startsWith("7")||oper.startsWith("8")||
				oper.startsWith("9")||oper.startsWith("-"));
	}
	
	public void baseHexadecimal(String baseHex,Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di, byte cont)
	{
		int rango = 0;
		if(baseHex.matches("[A-Fa-f0-9]+[A-Fa-f0-9]*")) //simbolos de base numerica validos
		{
			if(baseHex.startsWith(BASEHEX))
				rango = complementoA2(baseHex,(byte)1); 	//numero en formato complemento a 2
			else
			{
				rango = Integer.parseInt(baseHex, 16); //rango del numero Hex en Decimal
			}
			Di.ingresaRango(rango);
		}
		else
		{
			ArcErr.errorSimNumInv(An, cont); //simbolos numericos invalidos
			Au.ingresaEstErrNum(true);
		}
		
	}
	public void baseOctal(String baseOct,Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di, byte cont)
	{
		int rango = 0;
		if(baseOct.matches("[0-7]+[0-7]*"))
		{
			if(baseOct.startsWith(BASEOCT))
				rango = complementoA2(baseOct,(byte)2);
			else
			{
				rango = Integer.parseInt(baseOct, 8);
			}
			Di.ingresaRango(rango);
		}
		else
		{
			ArcErr.errorSimNumInv(An, cont); //Simbolos numericos Invalidos
			Au.ingresaEstErrNum(true);
		}
	}
	public void baseBinaria(String baseBin,Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di, byte cont)
	{
		int rango = 0;
		if(baseBin.matches("[01]+[01]*"))
		{
			if(baseBin.startsWith(BASEBIN))
				rango = complementoA2(baseBin,(byte) 3);
			else
			{
				rango = Integer.parseInt(baseBin, 2);
			}
			Di.ingresaRango(rango);
		}
		else
		{
			ArcErr.errorSimNumInv(An, cont); //Simbolos numericos Invalidos
			Au.ingresaEstErrNum(true);
		}
	}
	public void baseDecimal(String baseDec,Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,byte cont)
	{
		int rango = 0;
		if(baseDec.matches("[-]*[0-9]+"))
		{
			rango = Integer.parseInt(baseDec);
			Di.ingresaRango(rango);
		}
		else
		{
			ArcErr.errorSimNumInv(An, cont); //Simbolos numericos Invalidos
			Au.ingresaEstErrNum(true);
		}
	}
	
	public int complementoA2(String base,byte tipo)
	{
		int valor=0;
		String aux,aux2="";
		switch(tipo)
		{
		case 1:
			valor = Integer.parseInt(base, 16); //valor decimal del numero negativo Hex
			break;
		case 2:
			valor = Integer.parseInt(base, 8); //valor decimal del numero negativo Oct
			break;
		case 3:
			valor = Integer.parseInt(base, 2); //valor decimal del numero negativo Bin
			break;
		}
		aux = Integer.toBinaryString(valor); //cadena con el valor en binario
		valor = Integer.parseInt(aux,2);	//valor binario en decimal
		valor--;		//descomplemento
		valor = ~valor;		//descomplemento
		aux = Integer.toBinaryString(valor);	//valor absoluto del numero
		aux = aux.substring(1);		//control del desbordamiento del entero
		
		for(int i = 0;i < aux.length()-1;i++)
		{	
			if(aux.charAt(i) == aux.charAt(i+1))	
				continue;
			else
			{
				aux2 = aux.substring(i+1);	//cadena con el binario puro del valor
				break;
			}
		}
		valor = Integer.parseInt(aux2,2);	//rango del numero
		if(valor == 0)	// casos especiales del Complemento a 2
		{
			byte inc=1;
			for(int i = aux.length()-1;i >=1;i--)
			{
				if(aux.charAt(i) == aux.charAt(i-1)) 
					inc++;	//incremento de la potencia
				else
				{
					valor = (int)Math.pow(2,inc);	//rango del numero
					break;
				}
			}
		}
		
		return -valor;
	}
	
	public void inherente(String direccs,Automata Au)
	{
		String Inh = "";
		StringTokenizer st = new StringTokenizer(direccs,",");
		while(st.hasMoreTokens())
		{
			Inh = st.nextToken();
			if(Inh.equalsIgnoreCase("INH"))
			{
				ingresaTipo(Inh);
				Au.ingresaEstDirValido(true); //Direcc valido para este Codop
				break;
			}
		}
	}
	
	public void inmediatoSinOper(String direccs,Automata Au)
	{
		String ImmSO = "";
		StringTokenizer st = new StringTokenizer(direccs,",");
		while(st.hasMoreTokens())
		{
			ImmSO = st.nextToken();
			if(ImmSO.equalsIgnoreCase("IMM"))
			{
				ingresaTipo(ImmSO);
				Au.ingresaEstDirValido(true); //Direcc valido para este Codop
				break;
			}
		}
	}
	
	public void direccConOper(Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,String direccs,byte cont)
	{
		String base = "", reg = ""; 	
		if(An.regresaOper().startsWith("#"))
			base = An.regresaOper().substring(1);	//ignoramos el numeral
		else
		{
			if(An.regresaOper().contains(","))
			{
				if(An.regresaOper().startsWith("[") && An.regresaOper().endsWith("]"))
				{
					StringTokenizer sepIdxInd = new StringTokenizer(An.regresaOper(),",");
					base = sepIdxInd.nextToken().substring(1);
					reg = sepIdxInd.nextToken().toUpperCase();
					reg = reg.substring(0,reg.length()-1);
				}
				else
				{
					StringTokenizer sepIdx = new StringTokenizer(An.regresaOper(),",");
					base = sepIdx.nextToken();
					reg = sepIdx.nextToken().toUpperCase();
				}
			}
			else if(esDirOExtORel(An.regresaOper()))
				base = An.regresaOper();
		}
		switch(base.charAt(0))		//switch con el simbolo de la base
		{
		case '$':
			baseHexadecimal(base = base.substring(1),An,Au,ArcErr,Di,cont);  //Verificamos valor Hexadecimal
			break;
		case '@':
			baseOctal(base = base.substring(1),An,Au,ArcErr,Di,cont);	//Verificamos rango y valor Octal
			break;
		case '%':
			baseBinaria(base = base.substring(1),An,Au,ArcErr,Di,cont); //Verificamos rango y valor Binario
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
			baseDecimal(base,An,Au,ArcErr,Di,cont); //Verificamos rango y valor Decimal
			break;
		case 'A':
		case 'B':
		case 'D':
		case 'a':
		case 'b':
		case 'd':
			break;
		default:
			ArcErr.errorSimboloDeBase(An,cont);
			Au.ingresaEstErrNum(true);
			break;
		}
		if(!Au.regresaEstErrNum()) //Si no hubo error en la base Numerica
		{
			if(An.regresaOper().startsWith("#")) //Si es un Direcc Inmediato
				rango8y16BitsImm(An,Au,ArcErr,Di,direccs,cont); //validacion del Rango del Direcc 
			else
			{
				if(An.regresaOper().contains(","))
				{
					if(An.regresaOper().startsWith("[") && An.regresaOper().endsWith("]"))
					{
						if(reg.matches("(X|Y|SP|PC)")) //[IDX2] con Oper numerico
						{
							if(base.equalsIgnoreCase("D")) // Indexado tipo [D,IDX]
								Au.ingresaEstRegABD(true);
							
							rangoIdxIndirecto(An,Au,ArcErr,Di,direccs,cont);
						}
					}
					else
					{
						if(reg.matches(PREINC)||reg.matches(PREDEC)||reg.matches(POSINC)||reg.matches(POSDEC)) //IDX´s con Oper numerico
						{
							if(base.matches("(A|a|B|b|D|d)")) //IDX´s con Oper Registro
								Au.ingresaEstRegABD(true); //estado de IDX Acumulador activado
							
							rangoIdxIdx1Idx2(An,Au,ArcErr,Di,direccs,reg,cont);
						}
						else
							ArcErr.errorRegistInv(An,cont); //Registro en Operando Inv para ese Direcc
					}
				}
				else if(esDirOExtORel(An.regresaOper()))
					rangoDirExtRel(An,Au,ArcErr,Di,direccs,cont);
			}
		}
	}
	
	public void direccConOperEtq(Analizador An,Automata Au,Archivo ArcErr,String direccs,byte cont)
	{
		String dirEtq = "";
		StringTokenizer st = new StringTokenizer(direccs,",");
		while(st.hasMoreTokens())
		{
			dirEtq = st.nextToken();
			if(dirEtq.equalsIgnoreCase("EXT"))
			{
				ingresaTipo(dirEtq);
				Au.ingresaEstDirValido(true);
				break;
			}
			else if(dirEtq.equalsIgnoreCase("REL8"))
			{
				ingresaTipo(dirEtq);
				Au.ingresaEstDirValido(true);
				break;
			}
			else if(dirEtq.equalsIgnoreCase("REL9"))
			{
				ingresaTipo(dirEtq);
				Au.ingresaEstDirValido(true);
				break;
			}
			else if(dirEtq.equalsIgnoreCase("REL16"))
			{
				ingresaTipo(dirEtq);
				Au.ingresaEstDirValido(true);
				break;
			}
		}
		if(!Au.regresaEstDirValido())
			ArcErr.errorFormatoInv(An,cont);
	}
	
	public void rango8y16BitsImm(Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,String direccs,byte cont)
	{
		if(Di.regresaRango() >= -256 && Di.regresaRango() <= 255) //Rango de IMM8
		{
			String Imm8 = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				Imm8 = st.nextToken();
				if(Imm8.equalsIgnoreCase("IMM8")) //Si acepta ese Direcc el codop
				{
					ingresaTipo(Imm8);
					Au.ingresaEstDirValido(true); //Direcc valido para este codop
					Au.ingresaEstImm(true); //bandera de control entre Imm8 y Imm16
					break;
				}
			}
		}
		if(Di.regresaRango() >= -32768 && Di.regresaRango() <= 65535) //Rango de IMM16
		{
			if(!Au.regresaEstImm())
			{
				String Imm16 = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Imm16 = st.nextToken();
					if(Imm16.equalsIgnoreCase("IMM16")) //Si acepta ese Direcc el codop
					{
						ingresaTipo(Imm16);
						Au.ingresaEstDirValido(true); //Direcc valido para este Codop
						Au.ingresaEstImm(true);
						break;
					}
				}
			}
		}
		if(Di.regresaRango() < -32768 || Di.regresaRango() > 65535)
			ArcErr.errorDeRango(An, cont); //Rango invalido para ese Direcc
		else if(!Au.regresaEstImm())
			ArcErr.errorFormatoInv(An,cont);	//Oper invalido para Codop actual
	}
	
	public void rangoDirExtRel(Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,String direccs,byte cont)
	{
		if(Di.regresaRango() >= -128 && Di.regresaRango() <= 127) //Rango de Rel8
		{
			String Rel8 = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				Rel8 = st.nextToken();
				if(Rel8.equalsIgnoreCase("REL8")) //Si acepta ese Direcc el codop
				{
					ingresaTipo(Rel8);
					Au.ingresaEstDirValido(true); //Direcc valido para este Codop
					Au.ingresaEstRel(true); //estado de control entre Direccs
					break;
				}
			}
		}
		if(Di.regresaRango() >= -256 && Di.regresaRango() <= 255 && !Au.regresaEstRel()) //Rango Rel9
		{
			String Rel9 = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				Rel9 = st.nextToken();
				if(Rel9.equalsIgnoreCase("REL9")) //Si acepta ese Direcc el codop
				{
					ingresaTipo(Rel9);
					Au.ingresaEstDirValido(true); //Direcc valido para este Codop
					Au.ingresaEstRel(true); //estado de control entre Direccs
					break;
				}
			}
			if(Di.regresaRango() >= 0 && Di.regresaRango() <= 255 && !Au.regresaEstRel()) //Rango DIR
			{
				String Dir = "";
				st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Dir = st.nextToken();
					if(Dir.equalsIgnoreCase("DIR")) //Si acepta ese Direcc el codop
					{
						ingresaTipo(Dir);
						Au.ingresaEstDirValido(true); //Direcc valido para este Codop
						Au.ingresaEstDir(true); //estado de control entre Direccs
						break;
					}
				}
			}
		}
		if(Di.regresaRango() >= -32768 && Di.regresaRango() <= 65535 && (!Au.regresaEstRel() && !Au.regresaEstDir())) //Rango Ext
		{
			String Ext = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				Ext = st.nextToken();
				if(Ext.equalsIgnoreCase("EXT")) //Si acepta ese Direcc el codop
				{
					ingresaTipo(Ext);
					Au.ingresaEstDirValido(true); //Direcc valido para este Codop
					Au.ingresaEstDir(true);
					break;
				}
			}
			if(!Au.regresaEstDir()) //Rango Rel16
			{
				String Rel16 = "";
				st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Rel16 = st.nextToken();
					if(Rel16.equalsIgnoreCase("REL16")) //Si acepta ese Direcc el Codop
					{
						ingresaTipo(Rel16);
						Au.ingresaEstDirValido(true); //Direcc valido para este Codop
						Au.ingresaEstRel(true);
						break;
					}
				}
			}
		}
		if(Di.regresaRango() < -32768 || Di.regresaRango() > 65535)
			ArcErr.errorDeRango(An, cont); //Rango invalido para ese Direcc
		else if(!Au.regresaEstDir() && !Au.regresaEstRel())
			ArcErr.errorFormatoInv(An,cont);	//Oper invalido para Codop actual
	}
	
	public void rangoIdxIdx1Idx2(Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,String direccs,String reg,byte cont)
	{
		if(Au.regresaEstRegABD()) //IDX con Registro
		{
			if(!(reg.startsWith("+")||reg.startsWith("-")||reg.endsWith("+")||reg.endsWith("-")))
			{
				String IdxReg = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					IdxReg = st.nextToken();
					if(IdxReg.equalsIgnoreCase("IDX"))
					{
						ingresaTipo(IdxReg);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
			else
				Au.ingresaEstIncDec(true);
		}
		if(Di.regresaRango() >= 1 && Di.regresaRango() <= 8 && !Au.regresaEstIdx()) //Rango IDX Pre/Pos-Inc/Dec
		{
			if(reg.startsWith("+")||reg.startsWith("-")||reg.endsWith("+")||reg.endsWith("-"))
			{
				Au.ingresaEstIncDec(true); //estado de Inc/Dec activado
				
				String Idx = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Idx = st.nextToken();
					if(Idx.equalsIgnoreCase("IDX"))
					{
						ingresaTipo(Idx);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
		}
		if(Di.regresaRango() >= -16 && Di.regresaRango() <= 15 && !Au.regresaEstIdx() && !Au.regresaEstIncDec()) //Rango IDX
		{
			if(!(reg.startsWith("+")||reg.startsWith("-")||reg.endsWith("+")||reg.endsWith("-")))
			{
				String Idx = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Idx = st.nextToken();
					if(Idx.equalsIgnoreCase("IDX"))
					{
						ingresaTipo(Idx);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
			else
				Au.ingresaEstIncDec(true);
		}
		if(Di.regresaRango() >= -256 && Di.regresaRango() <= 255 && !Au.regresaEstIdx() && !Au.regresaEstIncDec()) // Rango IDX1
		{
			if(!(reg.startsWith("+")||reg.startsWith("-")||reg.endsWith("+")||reg.endsWith("-")))
			{
				String Idx1 = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Idx1 = st.nextToken();
					if(Idx1.equalsIgnoreCase("IDX1"))
					{
						ingresaTipo(Idx1);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
			else
				Au.ingresaEstIncDec(true);
		}
		if(Di.regresaRango() >= 0 && Di.regresaRango() <= 65535 && !Au.regresaEstIdx() && !Au.regresaEstIncDec()) // Rango IDX2
		{
			if(!(reg.startsWith("+")||reg.startsWith("-")||reg.endsWith("+")||reg.endsWith("-")))
			{
				String Idx2 = "";
				StringTokenizer st = new StringTokenizer(direccs,",");
				while(st.hasMoreTokens())
				{
					Idx2 = st.nextToken();
					if(Idx2.equalsIgnoreCase("IDX2"))
					{
						ingresaTipo(Idx2);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
			else
				Au.ingresaEstIncDec(true);
		}
		if(Di.regresaRango() < -32768 || Di.regresaRango() > 65535)
			ArcErr.errorDeRango(An, cont); //Rango invalido para ese Direcc
		else if(!Au.regresaEstIdx())
			ArcErr.errorFormatoInv(An,cont);	//Oper invalido para Codop actual
	}
	
	public void rangoIdxIndirecto(Analizador An,Automata Au,Archivo ArcErr,Direccionamiento Di,String direccs,byte cont)
	{
		if(Au.regresaEstRegABD())
		{
			String DIdxInd = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				DIdxInd = st.nextToken();
				if(DIdxInd.startsWith("["))
				{
					DIdxInd += "," + st.nextToken();
					if(DIdxInd.equalsIgnoreCase("[D,IDX]"))
					{
						ingresaTipo(DIdxInd);
						Au.ingresaEstDirValido(true);
						Au.ingresaEstIdx(true);
						break;
					}
				}
			}
		}
		if(!Au.regresaEstIdx() && Di.regresaRango() >= 0 && Di.regresaRango() <= 65535)
		{
			String Idx2Ind = "";
			StringTokenizer st = new StringTokenizer(direccs,",");
			while(st.hasMoreTokens())
			{
				Idx2Ind = st.nextToken();
				if(Idx2Ind.equalsIgnoreCase("[IDX2]"))
				{
					ingresaTipo(Idx2Ind);
					Au.ingresaEstDirValido(true);
					Au.ingresaEstIdx(true);
					break;
				}
			}
		}
		if(Di.regresaRango() < -32768 || Di.regresaRango() > 65535)
			ArcErr.errorDeRango(An, cont); //Rango invalido para ese Direcc
		else if(!Au.regresaEstIdx())
			ArcErr.errorFormatoInv(An,cont);	//Oper invalido para Codop actual
	}
	
}
