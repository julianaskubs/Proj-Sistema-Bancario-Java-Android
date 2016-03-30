package br.com.BiancaJessicaJulianaSusane.ctb;

public class ValidarDadosLogin {
	
	public boolean comprimentoAgencia (String s, int n)
	{
	    return s.length() == n;		
    }
 	
	public boolean comprimentoSenha (String s, int n)
	{
		return s.length() == 
				n;		
	}
	
	public boolean comprimentoPeloMenosConta (String s)
	{
		try
		{	
			Integer.parseInt(s.replace("-", ""));
		}
		catch(Exception e)
		{
			return false;
		}
		if (s.contains("-"))
		{
			return (s.length() == 7);
		}
		else
		{
			return (s.length() == 5);
		}
	}
	
	public boolean validaDigitoConta(String s)
	{
		if (s.length() == 5) return true;
		
		else if (s.charAt(5) == '-')
				return true;
		else 
				return false;		
	}
	
	
	
	
	// Não usados, porém podem ser úteis em outro código:
	
	
	public boolean comprimentoPeloMenosConta2 (String s, int a, int b)
	{
		return s.length() >= a && b >= s.length();		
	}
	
	
	public boolean validaInteiro(String s)
	{
		if (s.length() == 5)
			try{
				Integer.parseInt(s);
				return true;
		 	    }
			catch (NumberFormatException e){
				return false;
			}
		else
		{
			try{
				String a = s.substring(0, 4); 
				Integer.parseInt(a);		
				String b = s.substring(6);
				Integer.parseInt(b);		
				return true;
		 	    }
			catch (NumberFormatException e){
				return false;
			}
		}
	}
	
	
	public boolean ehInteiro (String s)
	{
		try{
			Integer.parseInt(s);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}
	}
	
	public boolean ehReal (String s)
	{
		try{
			Double.parseDouble(s);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}
	}
	
	public boolean ehPositivo (int n)
	{
		return n > 0;
	}
	
	public boolean ehPositivo (double n)
	{
		return n > 0;
	}
	
	public boolean noIntervalo (double n, double a1, double a2)
	{
		return n >= a1 && n <=a2;
	}
	
	


}
