package br.com.BiancaJessicaJulianaSusane.ctb;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LigacaoActivity extends ListActivity {

	private String conta="", contato="";
	private String conteudo = "";
	private String senha = "", agencia="", saldo="";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent origem = this.getIntent();
		agencia = origem.getStringExtra("agencia");
		conta = origem.getStringExtra("conta");
		senha = origem.getStringExtra("senha");
        
		String [] itens = new String [] {"Hot line", "Atendimento normal"};
		ArrayAdapter <String> adapter = 
		new ArrayAdapter <String> 
		(this, android.R.layout.simple_list_item_1, itens);
		this.setListAdapter(adapter);
    }
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String numero="";
		switch (position){
			case 0:
				numero="56565656";
				contato="Hot line";
				if (!retiraSaldo())		
					numero = "";
			break;
			case 1:
				numero="98765432";
				contato = "atendimento normal";
			break;
			
		}
		if (numero!=""){
			insereHistorico();
			
			StringBuilder sb = new StringBuilder("");
			sb.append("tel:");
			sb.append(numero);
			String numeroFormatado = sb.toString();
			Uri uri = Uri.parse(numeroFormatado);
			Intent i = new Intent(Intent.ACTION_CALL, uri);
			startActivity(i);
		}
		else
		Toast.makeText(LigacaoActivity.this, "Não há saldo suficiente", Toast.LENGTH_SHORT).show();
	} 	
	
	
	public void insereHistorico()
	{
		Intent origem = this.getIntent();
		conta = origem.getStringExtra("conta");
		String nomeArquivo = agencia + conta+".txt";
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
		try{
			FileOutputStream fos = openFileOutput(nomeArquivo, Context.MODE_APPEND);
			String conteudoInsere = formatarDate.format(data) + " - Ligação para " + contato + ";";
			fos.write(conteudoInsere.getBytes());   //escrever em bytes, ela nao sabe falar em string
			fos.close();
		}
		catch(IOException e)
		{
			Toast.makeText(LigacaoActivity.this, "Arquivo não encontrado", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(LigacaoActivity.this, "Exceção genérica aconteceu", Toast.LENGTH_LONG).show();
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
		
	if(saldof + 5000 >= 5)
	{
			saldof -=5;
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
			Toast.makeText(LigacaoActivity.this, "Foi debitado R$ 5,00 do seu saldo", Toast.LENGTH_SHORT).show();
		
	}
	else return (false); //não tem saldo
	return true;
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
		   Toast.makeText(LigacaoActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  Toast.makeText(LigacaoActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	   }							
	}

}
