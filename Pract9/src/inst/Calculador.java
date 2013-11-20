package inst;

import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

class Calculador 
{
	private boolean encontrado = false;
	private boolean desplInval = false;
	private final String codX = "00";
	private final String codY = "01";
	private final String codSP = "10";
	private final String codPC = "11";
	private final String codA = "00";
	private final String codB = "01";
	private final String codD = "10";
	private final String z0 = "0";
	private final String z1 = "1";
	private final String s0 = "0";
	private final String s1 = "1";
	private final String pPre = "0";
	private final String pPos = "1";
	
	public void ingresaEstEncontrado(boolean estado)
	{ encontrado = estado;}
	public boolean regresaEstEncontrado()
	{return encontrado;}
	public void ingresaEstDespInval(boolean estado)
	{desplInval = estado;}
	public boolean regresaEstDespInval()
	{return desplInval;}
	
	public String regresaCodX()
	{return codX;}
	public String regresaCodY()
	{return codY;}
	public String regresaCodSP()
	{return codSP;}
	public String regresaCodPC()
	{return codPC;}
	public String regresaCodA()
	{return codA;}
	public String regresaCodB()
	{return codB;}
	public String regresaCodD()
	{return codD;}
	public String regresaCodZ0()
	{return z0;}
	public String regresaCodZ1()
	{return z1;}
	public String regresaCodSPos()
	{return s0;}
	public String regresaCodSNeg()
	{return s1;}
	public String regresaCodPPre()
	{return pPre;}
	public String regresaCodPPos()
	{return pPos;}
	
	public String obtenerCodMaqSimples(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					int codoper = 0;
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 1: //1 byte
						if(oper.startsWith("$F")||oper.startsWith("@7")||oper.startsWith("%1")|| oper.startsWith("-")) //Si es un numero negativo
						{
							if(oper.startsWith("$F")) //negativo HEX
								codoper = Di.complementoA2(oper.substring(1),(byte)1);
							else if(oper.startsWith("@7")) //megativo OCT
								codoper = Di.complementoA2(oper.substring(1),(byte)2);
							else if(oper.startsWith("%1")) //negativo BIN
								codoper = Di.complementoA2(oper.substring(1), (byte)3);
							else //negativo DEC
								codoper = Integer.parseInt(oper);
							codhex = Integer.toHexString(codoper); //lo pasamos a base HEX en forma de cadena
							codhex = codhex.toUpperCase(); 
							codhex = codhex.substring(codhex.length()-2,codhex.length());
							codmaq = codmaq + codhex; //se concatena CodMaq con el valor de los bytes por calcular
							ingresaEstEncontrado(true);
						}
						else
						{
							codoper = Ve.conversor(oper); //encontramos el valor
							codhex = Integer.toHexString(codoper); //lo pasamos a base HEX en forma de cadena
							codhex = codhex.toUpperCase(); 
							if(codhex.length() == 1) //si su longitud es menor a un byte
								codmaq = codmaq + "0" + codhex; //se complementa con un 0 y se concatena con el CodMaq ya obtenido
							else
								codmaq = codmaq + codhex; //se concatena CodMaq con el valor de los bytes por calcular
							ingresaEstEncontrado(true);
						}
						break;
					case 2: //2 bytes
						if(oper.matches("[a-zA-z]+[a-zA-z0-9_]*"))
							return codmaq;
						else
						{
							if(oper.startsWith("$F")||oper.startsWith("@7")||oper.startsWith("%1")|| oper.startsWith("-"))
							{
								if(oper.startsWith("$F"))
									codoper = Di.complementoA2(oper.substring(1),(byte)1);
								else if(oper.startsWith("@7"))
									codoper = Di.complementoA2(oper.substring(1),(byte)2);
								else if(oper.startsWith("%1"))
									codoper = Di.complementoA2(oper.substring(1), (byte)3);
								else
									codoper = Integer.parseInt(oper);
								codhex = Integer.toHexString(codoper); //lo pasamos a base HEX en forma de cadena
								codhex = codhex.toUpperCase(); 
								codhex = codhex.substring(codhex.length()-4,codhex.length());
								codmaq = codmaq + codhex; //se concatena CodMaq con el valor de los bytes por calcular
								ingresaEstEncontrado(true);
							}
							else
							{
								codoper = Ve.conversor(oper);
								codhex = Integer.toHexString(codoper);
								codhex = codhex.toUpperCase();
								switch(codhex.length())
								{
								case 1:
									codmaq = codmaq + "000" + codhex;
									break;
								case 2:
									codmaq = codmaq + "00" + codhex;
									break;
								case 3:
									codmaq = codmaq + "0" + codhex;
									break;
								case 4:
									codmaq = codmaq + codhex;
									break;
								}
								ingresaEstEncontrado(true);
							}
						}
						break;
					case 3: //3 bytes
						if(oper.matches("[a-zA-z]+[a-zA-z0-9_]*"))
							return codmaq + "00";
						else
						{
							codoper = Ve.conversor(oper);
							codhex = Integer.toHexString(codoper);
							codhex = codhex.toUpperCase();
							switch(codhex.length())
							{
							case 1:
								codmaq = codmaq + "00000" + codhex;
								break;
							case 2:
								codmaq = codmaq + "0000" + codhex;
								break;
							case 3:
								codmaq = codmaq + "000" + codhex;
								break;
							case 4:
								codmaq = codmaq + "00" + codhex;
								break;
							case 5:
								codmaq = codmaq + "0" + codhex;
								break;
							case 6:
								codmaq = codmaq + codhex;
								break;
							}
							ingresaEstEncontrado(true);
						}
						break;
					case 4: //4 bytes
						if(oper.matches("[a-zA-z]+[a-zA-z0-9_]*"))
							return codmaq + "0000";
						else
						{
							codoper = Ve.conversor(oper);
							codhex = Integer.toHexString(codoper);
							codhex = codhex.toUpperCase();
							switch(codhex.length())
							{
							case 1:
								codmaq = codmaq + "0000000" + codhex;
								break;
							case 2:
								codmaq = codmaq + "000000" + codhex;
								break;
							case 3:
								codmaq = codmaq + "00000" + codhex;
								break;
							case 4:
								codmaq = codmaq + "0000" + codhex;
								break;
							case 5:
								codmaq = codmaq + "000" + codhex;
								break;
							case 6:
								codmaq = codmaq + "00" + codhex;
								break;
							case 7:
								codmaq = codmaq + "0" + codhex;
								break;
							case 8:
								codmaq = codmaq + codhex;
								break;
							}
							ingresaEstEncontrado(true);
						}
						break;
					}		
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqIDX(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					int codoper = 0;
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 1:
						if(oper.startsWith(",")) //IDX con oper vacio
						{
							codhex = "000000"; //nnnnn = 00000 + 0
							String registro = oper.substring(1); //se omite la coma del registro
							if(registro.equalsIgnoreCase("X")) 
								codhex = regresaCodX() + codhex; //nnnnn + 00
							else if(registro.equalsIgnoreCase("Y"))
								codhex = regresaCodY() + codhex; //nnnnn + 01
							else if(registro.equalsIgnoreCase("SP"))
								codhex = regresaCodSP() + codhex;  //nnnnn + 10
							else if(registro.equalsIgnoreCase("PC"))
								codhex = regresaCodPC() + codhex;  //nnnnn + 11
							int part1 = Integer.parseInt(codhex.substring(0,4),2); //se obtiene el valor alto del byte
							int part2 = Integer.parseInt(codhex.substring(4),2); //se obtiene el valor bajo del byte
							codhex = Integer.toHexString(part1) + Integer.toHexString(part2); //se genera el byte
							codmaq += codhex.toUpperCase(); //se concatena con los Bytes Calculados
							ingresaEstEncontrado(true);
						}
						else if(oper.contains("+") || oper.contains("-") && !oper.startsWith("-")) //Indexado Pre/Pos
						{
							StringTokenizer st = new StringTokenizer(oper,",");
							codoper = Ve.conversor(st.nextToken()); //obtenemos valor del primer operando
							String registro = st.nextToken();
							if(registro.startsWith("+") || registro.startsWith("-")||registro.endsWith("-")||registro.endsWith("+")) //si el registro tiene PRE/POS INC/DEC
							{ 
								String regist = "";
								if(registro.startsWith("+")||registro.startsWith("-")) //Si es PRE INC/DEC
									regist = registro.substring(1);
								else // si es POS INC/DEC
									regist = registro.substring(0,registro.length()-1);
								
								if(regist.equalsIgnoreCase("X")) //Si es el registro X
								{
									if(registro.startsWith("+")||registro.startsWith("-"))
										codhex += regresaCodX() + "1" + regresaCodPPre(); //rr + 1 + P(0)
									else
										codhex += regresaCodX() + "1" + regresaCodPPos(); //rr + 1 + P(1)
								}
								else if(regist.equalsIgnoreCase("Y")) //Si es el registro Y
								{
									if(registro.startsWith("+")||registro.startsWith("-"))
										codhex += regresaCodY() + "1" + regresaCodPPre();
									else
										codhex += regresaCodY() + "1" + regresaCodPPos();
								}
								else if(regist.equalsIgnoreCase("SP")) //Si es el registro SP
								{
									if(registro.startsWith("+")||registro.startsWith("-"))
										codhex += regresaCodSP() + "1" + regresaCodPPre();
									else
										codhex += regresaCodSP() + "1" + regresaCodPPos();
								}
								String nnnn;
								if(registro.startsWith("+") || registro.endsWith("+"))
								{
									nnnn = Integer.toBinaryString(codoper-1); 
									switch(nnnn.length())
									{
									case 1:
										nnnn = "000" + nnnn;
										break;
									case 2:
										nnnn = "00" + nnnn;
										break;
									case 3:
										nnnn = "0" + nnnn;
										break;
									}
									codhex += nnnn;
									int part1 = Integer.parseInt(codhex.substring(0,4),2);
									int part2 = Integer.parseInt(codhex.substring(4),2);
									codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
									codmaq += codhex.toUpperCase();
								}
								else if(registro.startsWith("-") || registro.endsWith("-"))
								{
									nnnn = Integer.toBinaryString(codoper * -1);
									nnnn = nnnn.substring(28); 
									codhex += nnnn;
									int part1 = Integer.parseInt(codhex.substring(0,4),2);
									int part2 = Integer.parseInt(codhex.substring(4),2);
									codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
									codmaq += codhex.toUpperCase();
								}
								ingresaEstEncontrado(true);
							}
						}
						else if(oper.startsWith("A")||oper.startsWith("B")||oper.startsWith("D")) //Indexado de Acumulador
						{
							codhex = "111";
							StringTokenizer st = new StringTokenizer(oper,",");
							String aa  = st.nextToken();
							String rr = st.nextToken();
							
							if(rr.equalsIgnoreCase("X"))
								codhex += regresaCodX() + "1";
							else if(rr.equalsIgnoreCase("Y"))
								codhex += regresaCodY() + "1";
							else if(rr.equalsIgnoreCase("SP"))
								codhex += regresaCodSP() + "1";
							else if(rr.equalsIgnoreCase("PC"))
								codhex += regresaCodPC() + "1";
							
							if(aa.equalsIgnoreCase("A"))
								codhex += regresaCodA();
							else if(aa.equalsIgnoreCase("B"))
								codhex += regresaCodB();
							else if(aa.equalsIgnoreCase("D"))
								codhex += regresaCodD();
							
							int part1 = Integer.parseInt(codhex.substring(0,4),2);
							int part2 = Integer.parseInt(codhex.substring(4),2);
							codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
							codmaq += codhex.toUpperCase();
							ingresaEstEncontrado(true);
						}
						else
						{
							StringTokenizer st = new StringTokenizer(oper,",");
							String oper1;
							codoper  = Ve.conversor(oper1 = st.nextToken());
							String rr = st.nextToken();
							
							if(rr.equalsIgnoreCase("X"))
								codhex += regresaCodX() + "0";
							else if(rr.equalsIgnoreCase("Y"))
								codhex += regresaCodY() + "0";
							else if(rr.equalsIgnoreCase("SP"))
								codhex += regresaCodSP() + "0";
							else if(rr.equalsIgnoreCase("PC"))
								codhex += regresaCodPC() + "0";
							String nnnnn = "";
							//
							if(oper1.startsWith("$F")||oper1.startsWith("@7")||oper1.startsWith("%1")|| oper1.startsWith("-"))
							{
								if(oper1.startsWith("$F"))
									codoper = Di.complementoA2(oper1.substring(1),(byte)1);
								else if(oper1.startsWith("@7"))
									codoper = Di.complementoA2(oper1.substring(1),(byte)2);
								else if(oper1.startsWith("%1"))
									codoper = Di.complementoA2(oper1.substring(1), (byte)3);
								else
									codoper = Integer.parseInt(oper1);
								
								nnnnn = Integer.toBinaryString(codoper);
								nnnnn = nnnnn.substring(27); 
							}
							//
							else if(codoper >= 0)
							{
								nnnnn = Integer.toBinaryString(codoper); 
								switch(nnnnn.length())
								{
								case 1:
									nnnnn = "0000" + nnnnn;
									break;
								case 2:
									nnnnn = "000" + nnnnn;
									break;
								case 3:
									nnnnn = "00" + nnnnn;
									break;
								case 4:
									nnnnn = "0" + nnnnn;
									break;
								}
							}
							
							codhex += nnnnn;
							int part1 = Integer.parseInt(codhex.substring(0,4),2);
							int part2 = Integer.parseInt(codhex.substring(4),2);
							codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
							codmaq += codhex.toUpperCase();
							ingresaEstEncontrado(true);
						}
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqIDX1(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					int codoper = 0;
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 2:
						StringTokenizer st = new StringTokenizer(oper,",");
						String oper1;
						codoper  = Ve.conversor(oper1 = st.nextToken());
						String rr = st.nextToken();
						
						codhex = "111";
						
						if(rr.equalsIgnoreCase("X"))
							codhex += regresaCodX() + "0" + regresaCodZ0();
						else if(rr.equalsIgnoreCase("Y"))
							codhex += regresaCodY() + "0" + regresaCodZ0();
						else if(rr.equalsIgnoreCase("SP"))
							codhex += regresaCodSP() + "0" + regresaCodZ0();
						else if(rr.equalsIgnoreCase("PC"))
							codhex += regresaCodPC() + "0" + regresaCodZ0();
						
						if(oper1.startsWith("$F")||oper1.startsWith("@7")||oper1.startsWith("%1")|| oper1.startsWith("-"))
							codhex += regresaCodSNeg();
						else
							codhex += regresaCodSPos();
						
						int part1 = Integer.parseInt(codhex.substring(0,4),2);
						int part2 = Integer.parseInt(codhex.substring(4),2);
						codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
						
						String ff = Integer.toHexString(codoper);
						//
						if(oper1.startsWith("$F")||oper1.startsWith("@7")||oper1.startsWith("%1")|| oper1.startsWith("-"))
						{
							if(oper1.startsWith("$F"))
								codoper = Di.complementoA2(oper1.substring(1),(byte)1);
							else if(oper1.startsWith("@7"))
								codoper = Di.complementoA2(oper1.substring(1),(byte)2);
							else if(oper1.startsWith("%1"))
								codoper = Di.complementoA2(oper1.substring(1), (byte)3);
							else
								codoper = Integer.parseInt(oper1);
							
							ff = Integer.toHexString(codoper);
							ff = ff.substring(6);
						}
						//
						else if(codoper >= 0)
						{
							if(ff.length() == 1)
								ff = "0" + ff;
						}
						
						codhex += ff;
						codmaq += codhex.toUpperCase();
						ingresaEstEncontrado(true);
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqIDX2(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					int codoper = 0;
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 3:
						StringTokenizer st = new StringTokenizer(oper,",");
						String oper1;
						codoper  = Ve.conversor(oper1 = st.nextToken());
						String rr = st.nextToken();
						
						codhex = "111";
						
						if(rr.equalsIgnoreCase("X"))
							codhex += regresaCodX() + "0" + regresaCodZ1(); 
						else if(rr.equalsIgnoreCase("Y"))
							codhex += regresaCodY() + "0" + regresaCodZ1();
						else if(rr.equalsIgnoreCase("SP"))						//rr + 0 + Z(1)
							codhex += regresaCodSP() + "0" + regresaCodZ1();
						else if(rr.equalsIgnoreCase("PC"))
							codhex += regresaCodPC() + "0" + regresaCodZ1();
						
						if(oper1.startsWith("$F")||oper1.startsWith("@7")||oper1.startsWith("%1")|| oper1.startsWith("-"))
							codhex += regresaCodSNeg();
						else
							codhex += regresaCodSPos();
						
						int part1 = Integer.parseInt(codhex.substring(0,4),2);
						int part2 = Integer.parseInt(codhex.substring(4),2);
						codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
						
						String eeff = Integer.toHexString(codoper);
						//
						if(oper1.startsWith("$F")||oper1.startsWith("@7")||oper1.startsWith("%1")|| oper1.startsWith("-"))
						{
							if(oper1.startsWith("$F"))
								codoper = Di.complementoA2(oper1.substring(1),(byte)1);
							else if(oper1.startsWith("@7"))
								codoper = Di.complementoA2(oper1.substring(1),(byte)2);
							else if(oper1.startsWith("%1"))
								codoper = Di.complementoA2(oper1.substring(1), (byte)3);
							else
								codoper = Integer.parseInt(oper1);
							eeff = Integer.toHexString(codoper);
							eeff = eeff.substring(4);
						}
						//
						else if(codoper >= 0)
						{
							switch(eeff.length())
							{
							case 1:
								eeff = "000" + eeff;
								break;
							case 2:
								eeff = "00" +  eeff;
								break;
							case 3:
								eeff = "0" + eeff;
								break;
							}
						}
							
						codhex += eeff;
						codmaq += codhex.toUpperCase();
						ingresaEstEncontrado(true);
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqIDXInd(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					int codoper = 0;
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 3:
						StringTokenizer st = new StringTokenizer(oper.substring(1,oper.length()-1),",");
						codoper  = Ve.conversor(st.nextToken());
						String rr = st.nextToken();
						
						codhex = "111";
						
						if(rr.equalsIgnoreCase("X"))
							codhex += regresaCodX() + "011";
						else if(rr.equalsIgnoreCase("Y"))
							codhex += regresaCodY() + "011";
						else if(rr.equalsIgnoreCase("SP"))
							codhex += regresaCodSP() + "011";
						else if(rr.equalsIgnoreCase("PC"))
							codhex += regresaCodPC() + "011";
						
						int part1 = Integer.parseInt(codhex.substring(0,4),2);
						int part2 = Integer.parseInt(codhex.substring(4),2);
						codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
						
						String eeff = Integer.toHexString(codoper);
						switch(eeff.length())
						{
						case 1:
							eeff = "000" + eeff;
							break;
						case 2:
							eeff = "00" +  eeff;
							break;
						case 3:
							eeff = "0" + eeff;
							break;
						}
						codhex += eeff;
						codmaq += codhex.toUpperCase();
						ingresaEstEncontrado(true);
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqIDXIndD(String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 1:
						String rr = oper.substring(3,oper.length()-1);
						
						codhex = "111";
						
						if(rr.equalsIgnoreCase("X"))
							codhex += regresaCodX() + "111";
						else if(rr.equalsIgnoreCase("Y"))
							codhex += regresaCodY() + "111";
						else if(rr.equalsIgnoreCase("SP"))
							codhex += regresaCodSP() + "111";
						else if(rr.equalsIgnoreCase("PC"))
							codhex += regresaCodPC() + "111";
						
						int part1 = Integer.parseInt(codhex.substring(0,4),2);
						int part2 = Integer.parseInt(codhex.substring(4),2);
						codhex = Integer.toHexString(part1) + Integer.toHexString(part2);
						
						codmaq += codhex.toUpperCase();
						ingresaEstEncontrado(true);
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqRel8(String contloc,String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst,TreeMap<String,String> arbolDeEtq)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 1:
						if(oper.matches("[a-zA-z]+[a-zA-z0-9_]*"))
						{
							int valoper = Integer.parseInt(arbolDeEtq.get(oper),16);
			
							int valcontloc = Integer.parseInt(contloc,16) + (1 + codmaq.length()/2);
							valoper -= valcontloc;
							if(valoper >= -128 && valoper <= 127)
							{
								codhex = Integer.toHexString(valoper).toUpperCase();
								if(valoper >= 0)
								{
									if(codhex.length() == 1)
										codhex = "0" + codhex;
								}
								else
									codhex = codhex.substring(6);
								ingresaEstEncontrado(true);
							}
							else
								return "XXXX"; //error de rango detectado, regresa simbolo de error
						}
						else
						{
							int rr = 0;
							
							if(oper.startsWith("$F")||oper.startsWith("@7")||oper.startsWith("%1")|| oper.startsWith("-"))
							{
								if(oper.startsWith("$F"))
									rr = Di.complementoA2(oper.substring(1),(byte)1);
								else if(oper.startsWith("@7"))
									rr = Di.complementoA2(oper.substring(1),(byte)2);
								else if(oper.startsWith("%1"))
									rr = Di.complementoA2(oper.substring(1), (byte)3);
								else
									rr = Integer.parseInt(oper);
							}
							else
								rr = Ve.conversor(oper);
							
							int valcontloc = Integer.parseInt(contloc,16) + (1 + codmaq.length()/2);
							rr = rr - valcontloc;
							
							if(rr >= -128 && rr <= 127)
							{
								codhex = Integer.toHexString(rr).toUpperCase();
								if(rr >= 0)
								{
									if(codhex.length() == 1)
										codhex = "0" + codhex;
								}
								else
									codhex = codhex.substring(6);
								ingresaEstEncontrado(true);
							}
							else
								return "XXXX"; //error de rango detectado, regresa simbolo de error
							
						}
						codmaq += codhex;
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqRel16(String contloc,String codop,String direcc,String oper,Direccionamiento Di,Verificador Ve,Instruccion[] listadeInst,TreeMap<String,String> arbolDeEtq)
	{
		String codmaq = "";
		for(int i = 0; i<listadeInst.length;i++)
		{
			if(codop.equalsIgnoreCase(listadeInst[i].regresaIdCodop()) 
					&& direcc.equalsIgnoreCase(listadeInst[i].regresaInst())) //Si coinciden Codop y Direcc 
			{
				codmaq = listadeInst[i].regresaCodMaq(); //se guarda el CodMaq que corresponde
				if(listadeInst[i].regresaBPC() != 0) //Si existen Bytes por Calcular
				{
					String codhex = "";
					switch(listadeInst[i].regresaBPC()) //segun el numero de bytes por calcular..
					{
					case 2:
						if(oper.matches("[a-zA-z]+[a-zA-z0-9_]*"))
						{
							int valoper = Integer.parseInt(arbolDeEtq.get(oper),16);
							int valcontloc = Integer.parseInt(contloc,16) + (2 + codmaq.length()/2);
							valoper -= valcontloc;
							if(valoper >= -32768 && valoper <= 65535)
							{
								codhex = Integer.toHexString(valoper).toUpperCase();
								if(valoper >= 0)
								{
									switch(codhex.length())
									{
									case 1:
										codhex = "000" + codhex;
										break;
									case 2:
										codhex = "00" + codhex;
										break;
									case 3:
										codhex = "0" + codhex;
										break;
									}
								}
								else
									codhex = codhex.substring(4);
								ingresaEstEncontrado(true);
							}
							else
								return "XXXX"; //error de rango detectado, regresa simbolo de error
						}
						else
						{
							int rrll = 0;
							
							if(oper.startsWith("$F")||oper.startsWith("@7")||oper.startsWith("%1")|| oper.startsWith("-"))
							{
								if(oper.startsWith("$F"))
									rrll = Di.complementoA2(oper.substring(1),(byte)1);
								else if(oper.startsWith("@7"))
									rrll = Di.complementoA2(oper.substring(1),(byte)2);
								else if(oper.startsWith("%1"))
									rrll = Di.complementoA2(oper.substring(1), (byte)3);
								else
									rrll = Integer.parseInt(oper);
							}
							else
								rrll = Ve.conversor(oper);

							int valcontloc = Integer.parseInt(contloc,16) + (2 + codmaq.length()/2);
							rrll = rrll -valcontloc;
							
							if(rrll >= -32768 && rrll <= 65535)
							{
								codhex = Integer.toHexString(rrll).toUpperCase();
								if(rrll >= 0)
								{
									switch(codhex.length())
									{
									case 1:
										codhex = "000" + codhex;
										break;
									case 2:
										codhex = "00" + codhex;
										break;
									case 3:
										codhex = "0" + codhex;
										break;
									}
								}
								else
									codhex = codhex.substring(4);
								ingresaEstEncontrado(true);
							}
							else
								return "XXXX"; //error de rango detectado, regresa simbolo de error
							//
						}
						codmaq += codhex;
						break;
					}
				}
				if(regresaEstEncontrado())
					break;
			}
		}
		return codmaq;
	}
	
	public String obtenerCodMaqConst1Byte(String oper,Verificador Ve)
	{
		String codmaq;
		codmaq = Integer.toHexString(Ve.conversor(oper)).toUpperCase(); //se obtiene el valor del oper y se pasa a una cadena HEX
		if(codmaq.length() == 1)
			codmaq = "0" + codmaq; //se agrega un 0 si es necesario en el formato del CodMaq
		return codmaq;
	}
	
	public String obtenerCodMaqConst2Bytes(String oper,Verificador Ve)
	{
		String codmaq;
		codmaq = Integer.toHexString(Ve.conversor(oper)).toUpperCase(); //se obtiene el valor del oper y se pasa a una cadena HEX
		switch(codmaq.length())
		{
		case 1 :
			codmaq = "000" + codmaq;
			break;
		case 2 :
			codmaq = "00" + codmaq; //se agregan ceros segun necesite el formato del CodMaq
			break;
		case 3 : 
			codmaq = "0" + codmaq;
			break;
		}
		return codmaq;
	}
	
	public String obtenerCodMaqFCC(Vector<String> elemento)
	{
		String codmaq = "",codhex;
		codhex = elemento.toString();
		int ind = codhex.indexOf("\"");
		codhex = codhex.substring(ind);
		codhex = codhex.substring(1, codhex.lastIndexOf("\""));
		codhex = codhex.replace(",","");
		
		if(codhex.contains("\\\\"))
			codhex = codhex.replace("\\\\","\\"); //eliminacion de la diagonal invertida de escape
		if(codhex.contains("\\\""))
			codhex = codhex.replace("\\\"","\""); //eliminacion de la diagonal invertida de escape
		for(char letra : codhex.toCharArray())
		{
			short num = (short)letra;
			codmaq += Integer.toHexString(num).toUpperCase();
		}
		
		return codmaq;
	}
					
					
}
