package br.com.BiancaJessicaJulianaSusane.ctb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaqueActivity extends Activity {

	private EditText saqueEditext;
	private Button saqueButton;
	private String contas = "contas.txt";
	private String agencia = "";
	private String conta = "";
	private String senha = "";
	private String saldo = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saque);
		Intent origem = this.getIntent();
		agencia = origem.getStringExtra("agencia");
		conta = origem.getStringExtra("conta");
		senha = origem.getStringExtra("senha");
		saqueEditext = (EditText) this.findViewById(R.id.saqueEditText);
		saqueButton = (Button) this.findViewById(R.id.saqueButton);
		
		saqueButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
			    
			 if (!saqueEditext.getEditableText().toString().equals(""))
				{
				   File arquivo = getFileStreamPath(contas);
			 	   String conteudo = buscaArquivo(arquivo);
				   String guardaLinhaConta = "";
				   String [] constroiConta = null;
				   int posicao = -1;
				   int posicao2 = -1;
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
			        String sacar = saqueEditext.getEditableText().toString();
			        double valorSacar = Double.parseDouble(sacar);
			        double saldoAtual = Double.parseDouble(saldo);
			        double novoSaldo = saldoAtual - valorSacar;
			        ValidadorSaque vs = new ValidadorSaque();
			        double limite = 5000.00;
			
			        if(saldoAtual + limite  >= valorSacar)
			         {
			        	if (valorSacar % 5 == 0) 
			        	{
			        	   String resposta = vs.Notas(valorSacar);
					       saldo = Double.toString(novoSaldo);
					       String remontaConta = conta + "," + senha + "," + saldo;	
					       dadosContaIndividual[posicao] = remontaConta;
					       guardaLinhaConta = "";
					       for (int i = 0; i < dadosContaIndividual.length; i++)
					            guardaLinhaConta += dadosContaIndividual[i] + ";";	
					       linhasTxt[posicao2] = guardaLinhaConta;
					       conteudo = "";
					       for (int i = 0; i < linhasTxt.length; i++)
						        conteudo += linhasTxt[i] + "\r\n";
					
					      if (arquivo.exists())
				    	      arquivo.delete();
					      criaArquivo(conteudo);
					      saqueEditext.setText("");
					      DecimalFormat df = new DecimalFormat("0.00");
					      insereHistorico((df.format(valorSacar).toString()));
					      Toast.makeText(SaqueActivity.this, resposta, Toast.LENGTH_LONG).show();
				        }
			        	else 
			        		Toast.makeText(SaqueActivity.this,"Valor inválido para saque. Nota mínima disponível: R$ 5,00.", Toast.LENGTH_SHORT).show();
				      }
			       else
				        Toast.makeText(SaqueActivity.this, "Você não possui limite suficiente para efetuar esse saque", Toast.LENGTH_LONG).show();	
		         }
           else
	       Toast.makeText(SaqueActivity.this, "Digite algum valor", Toast.LENGTH_SHORT).show();
	}});
	}

		
	private String buscaArquivo(File arquivo)
	{
		String conteudoTxt = "";
		try {
			FileInputStream fis = openFileInput(contas);
			int tamanho = fis.available();
			byte[] bytes = new byte[tamanho];
			fis.read(bytes);
			fis.close();
			conteudoTxt = new String(bytes);
		   } 
		catch (IOException e) {
			Toast.makeText(SaqueActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		  }
		return conteudoTxt;
	}
	
	
	private void criaArquivo(String conteudoCadastrar) {
    	try 
	     {
		   FileOutputStream fos = openFileOutput(contas, Context.MODE_APPEND);
		   fos.write(conteudoCadastrar.getBytes());   
		   fos.close();
		   conteudoCadastrar = "";
	    }		
	  catch (FileNotFoundException e)
	    {
		   Toast.makeText(SaqueActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  Toast.makeText(SaqueActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	   }							
    }
	
	
	public void insereHistorico(String valorSaque)
	{
		String nomeArquivo = agencia + conta +".txt";
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyyy - hh:mm");
		try{
			FileOutputStream fos = openFileOutput(nomeArquivo, Context.MODE_APPEND);
			String conteudoInsere = formatarDate.format(data) + " - Saque de " + valorSaque + " reais;";
			fos.write(conteudoInsere.getBytes());   //escrever em bytes, ela nao sabe falar em string
			fos.close();
		}
		catch(FileNotFoundException e)
		{
			Toast.makeText(SaqueActivity.this, "Arquivo não encontrado", Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			Toast.makeText(SaqueActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		}				
		
	}
	
}