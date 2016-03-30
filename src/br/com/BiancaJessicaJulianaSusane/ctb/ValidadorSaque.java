package br.com.BiancaJessicaJulianaSusane.ctb;

public class ValidadorSaque {
	
	public String Notas(double dinheiro)
	{               
         String concatenar = "";
         String n100 = "";
         String n50 = "";
         String n20 = "";
         String n10 = "";
         String n5 = "";
       
         int nota100 = 0;
         int nota50 = 0;
         int nota20 = 0;
         int nota10 = 0;
         int nota5 = 0;
    
           
      //*** nota de 100
      if (dinheiro >= 100)
         {
            while (dinheiro >= 100)
               {
                     nota100++;
                     dinheiro -= 100;
               }
            if (nota100 == 1) n100 = Integer.toString(nota100) + " nota de 100";
            else if (nota100 > 1) n100 = Integer.toString(nota100) + " notas de 100";
            concatenar += n100;
          }

      //*** nota de 50
      if (dinheiro >= 50)
         {
             while (dinheiro >= 50)
             {
                 nota50++;
                 dinheiro -= 50;
             }
             if (nota50 == 1) n50 += Integer.toString(nota50) + " nota de 50";
             else if (nota50 > 1) n50 += Integer.toString(nota50) + " notas de 50";

             if (concatenar == "") concatenar += n50;
             else if (dinheiro >= 5) concatenar += ", " + n50;
             else concatenar += " e " + n50;
           }

       //*** nota de 20
       if (dinheiro >= 20)
         {
             while (dinheiro >= 20)
             {
                 nota20++;
                 dinheiro -= 20;
             }
          if (nota20 == 1) n20 += Integer.toString(nota20) + " nota de 20";
          else if (nota20 > 1) n20 += Integer.toString(nota20) + " notas de 20";

          if (concatenar == "") concatenar += n20;
          else if (dinheiro >= 5) concatenar += ", " + n20;
          else concatenar += " e " + n20;
        }
       
      //*** nota de 10
      if (dinheiro >= 10)
         {     
             while (dinheiro >= 10)
             {
                 nota10++;
                 dinheiro -= 10;
             }
           if (nota10 == 1) n10 += Integer.toString(nota10) + " nota de 10";
           else if (nota10 > 1) n10 += Integer.toString(nota10) + " notas de 10";

           if (concatenar == "") concatenar += n10;
           else if (dinheiro >= 5) concatenar += ", " + n10;
           else concatenar += " e " + n10;
         }

      
       //*** nota de 5
      if (dinheiro >= 5)
      {
          while (dinheiro >= 5)
          {
              nota5++;
              dinheiro -= 5;
          }
          if (nota5 == 1) n5 += Integer.toString(nota5) + " nota de 5";
          else  n5 += Integer.toString(nota5) + " notas de 5";
          if (concatenar == "") concatenar += n5;
          else concatenar += " e " + n5;
       }
  
      return("Saque realizado com sucesso. " + concatenar + ".");
      
	}

}
