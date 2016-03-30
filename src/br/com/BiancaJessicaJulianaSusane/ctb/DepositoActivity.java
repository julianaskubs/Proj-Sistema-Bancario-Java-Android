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

public class DepositoActivity extends Activity {
	
	private EditText valorDepositoEditText;
	private Button depositarButton;
	private String contas = "contas.txt";
	private String agencia = "";
	private String conta = "";
	private String senha = "";
	private String saldo = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deposito);
		Intent origem = this.getIntent();
		agencia = origem.getStringExtra("agencia");
		conta = origem.getStringExtra("conta");
		senha = origem.getStringExtra("senha");
		depositarButton = (Button) this.findViewById(R.id.DepositarButton);
		valorDepositoEditText = (EditText) this.findViewById(R.id.valorDepositoEditText);
		depositarButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					
				if(!valorDepositoEditText.getEditableText().toString().equals("")){
				
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
					
					double valorDeposito = Double.parseDouble(valorDepositoEditText.getEditableText().toString());
					double saldoAtual = Double.parseDouble(saldo);
					double novoSaldo = saldoAtual + valorDeposito;
					DecimalFormat df = new DecimalFormat("0.00");
					String saldo1 = df.format(novoSaldo);
					saldo = saldo1;
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
					valorDepositoEditText.setText("");
				
					
					insereHistorico((df.format(valorDeposito).toString()));
					Toast.makeText(DepositoActivity.this, "Valor depositado: R$" + df.format(valorDeposito), Toast.LENGTH_SHORT).show();
		
			
				}
				else
					Toast.makeText(DepositoActivity.this, "Digite algum valor", Toast.LENGTH_SHORT).show();
			}
			
			});
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
			Toast.makeText(DepositoActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
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
		   Toast.makeText(DepositoActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  Toast.makeText(DepositoActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	   }							
    }
	
	public void insereHistorico(String valorDeposito)
	{
		String nomeArquivo = agencia + conta +".txt";
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
		try{
			FileOutputStream fos = openFileOutput(nomeArquivo, Context.MODE_APPEND);
			String conteudoDep = formatarDate.format(data) + " - Depósito de " + valorDeposito + " reais;";
			fos.write(conteudoDep.getBytes());   //escrever em bytes, ela nao sabe falar em string
			fos.close();
		}
		catch(FileNotFoundException e)
		{
			Toast.makeText(DepositoActivity.this, "Arquivo não encontrado", Toast.LENGTH_SHORT).show();
		}
		 catch (IOException e)
		   {
			  Toast.makeText(DepositoActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		   }				
	}
}
