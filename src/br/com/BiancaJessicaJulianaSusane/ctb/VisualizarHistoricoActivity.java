package br.com.BiancaJessicaJulianaSusane.ctb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class VisualizarHistoricoActivity extends ListActivity {
	private String conta = "";
	private String conteudoDaConta = "";
	private String conteudo = "";
	private String senha = "", agencia="", saldo="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent origem = this.getIntent();
		agencia = origem.getStringExtra("agencia");
		conta = origem.getStringExtra("conta");
		senha = origem.getStringExtra("senha");
		
	    if (retiraSaldo())
		 {
			try 
		  	  {   
				  File arquivo = getFileStreamPath(agencia + conta +".txt");	
			      if (arquivo.exists())
				   {
					  FileInputStream fis = openFileInput(agencia + conta +".txt");	
					  int tamanho = fis.available();  
					  byte [] bytes = new byte[tamanho];  
					  fis.read(bytes);
					  fis.close();
					  conteudoDaConta = new String(bytes);
				    }
			      else 
			    	  Toast.makeText(VisualizarHistoricoActivity.this, "A sua conta ainda não possui nenhuma transação realizada.", Toast.LENGTH_SHORT).show();
				  
			    } 
			 catch (IOException e)
			     {
				  Toast.makeText(VisualizarHistoricoActivity.this, "Exceção genérica aconteceu", Toast.LENGTH_LONG).show();
			     }
			
		    String [] itens = conteudoDaConta.split(";");
		    ArrayAdapter <String> adapter = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_1, itens); 
		    this.setListAdapter(adapter);
		}
		
		else 
		{
			Toast.makeText(VisualizarHistoricoActivity.this, "Não há saldo suficiente", Toast.LENGTH_SHORT).show();	
		}
	
	  }
	
	
	private boolean retiraSaldo()
	{
		String nomeArquivo = "contas.txt";
		File arquivo = getFileStreamPath(nomeArquivo);
		String guardaLinhaConta = "";
		int posicao = -1;
		int posicao2 = -1;
		String [] constroiConta = null;
		
		try 
		{	
			if (arquivo.exists())
			{
				FileInputStream fis = openFileInput("contas.txt");	
				int tamanho = fis.available();  
				byte [] bytes = new byte[tamanho];  
				fis.read(bytes);
				fis.close();
				conteudo = new String(bytes);	
			}
		 } 
		catch (IOException e)
		  {
			Toast.makeText(VisualizarHistoricoActivity.this, "Exceção genérica aconteceu", Toast.LENGTH_LONG).show();
	      }
		
		String [] linhasTxt = conteudo.split("\r\n");
		for (int i = 0; i < linhasTxt.length; i++)                    
 	   {
 		   if (agencia.equals(linhasTxt[i]))
 		   {                       
 			   guardaLinhaConta = linhasTxt[i+1]; 
 			   posicao2 = i+1;
 		       break;
 		   }
 	   }
		
		String [] dadosContaIndividual = guardaLinhaConta.split(";");
		 for (int i = 0; i < dadosContaIndividual.length; i++)
		   {
		      String queroDadosConta = dadosContaIndividual[i];                      									
			  constroiConta = queroDadosConta.split(",");                 
			  if (conta.equals(constroiConta[0]))
			   {
			 	   saldo = constroiConta[2];
			 	   posicao = i;
			 	   break;
		    	}
			  queroDadosConta = "";
		      constroiConta = null;
		   } 
		 
		double saldof = Double.parseDouble(saldo);
		linhasTxt = conteudo.split("\r\n");
		
	if(saldof + 5000 >= 2)
	{
			saldof -=2;
			saldo = Double.toString(saldof);
			String remontaConta = conta + "," + senha + "," + saldo;
								
			dadosContaIndividual[posicao] = remontaConta;
			guardaLinhaConta = "";
			for (int i = 0; i < dadosContaIndividual.length; i++)
			       guardaLinhaConta += dadosContaIndividual[i] + ";";	
			   
			linhasTxt[posicao2] = guardaLinhaConta;
			conteudo = "";
			for (int i = 0; i < linhasTxt.length; i++){
				conteudo += linhasTxt[i] + "\r\n";
			}
			if (arquivo.exists())
		    	arquivo.delete();
			criaArquivo(conteudo);
			
			Toast.makeText(VisualizarHistoricoActivity.this, "Foi debitado R$ 2,00 do seu saldo", Toast.LENGTH_SHORT).show();
			return true;
		}
	else return false;
		}
		
		
	
private void criaArquivo(String conteudoCadastrar) {
	try 
     {
	   FileOutputStream fos = openFileOutput("contas.txt", Context.MODE_APPEND);
	   fos.write(conteudoCadastrar.getBytes());   
	   fos.close();
	   conteudoCadastrar = "";
    }		
  catch (FileNotFoundException e)
    {
	   Toast.makeText(VisualizarHistoricoActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
    } 
  catch (IOException e)
   {
	  Toast.makeText(VisualizarHistoricoActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
   }							
}
}
