package br.com.BiancaJessicaJulianaSusane.ctb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText agenciaEditText;
	private EditText contaEditText;
	private EditText senhaEditText;
	private Button okLoginButton;
	private String contas = "contas.txt";
	private String conteudo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		agenciaEditText = (EditText) this.findViewById(R.id.agenciaEditText);
		contaEditText = (EditText) this.findViewById(R.id.contaEditText);
		senhaEditText = (EditText) this.findViewById(R.id.senhaEditText);
		okLoginButton = (Button) this.findViewById(R.id.okLoginButton);
		
		
		okLoginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String agencia = agenciaEditText.getEditableText().toString();
			    String conta = contaEditText.getEditableText().toString().trim();
				String senha = senhaEditText.getEditableText().toString();
				ValidarDadosLogin vdl = new ValidarDadosLogin();
				String guardaLinhaConta = "";
				
				
				File arquivo = getFileStreamPath(contas);
				if (!arquivo.exists())
					 criaArquivo();	
				conteudo = buscaArquivo(arquivo);    //quando cadastra uma conta e volta ao menu para acessar o mesmo da erro
			
							
				if(!vdl.comprimentoAgencia(agencia, 4))
			    	 Toast.makeText(MainActivity.this, "Preencha o campo Agência exatamente com 4 números.", Toast.LENGTH_SHORT).show();
				
				else if(!vdl.comprimentoPeloMenosConta(conta))
			         Toast.makeText(MainActivity.this, "O campo Conta deve conter apenas 5 números ou 6 com o dígito. Se houver dígito, coloque um hífen (-) antes.", Toast.LENGTH_LONG).show();
							
				else if (!vdl.validaDigitoConta(conta))
					 Toast.makeText(MainActivity.this, "O hífen (-) do dígito da conta está no lugar errado. Coloque-o entre o número da conta e o número do dígito", Toast.LENGTH_LONG).show();	 
				
				else if(!vdl.comprimentoSenha(senha, 4))
					 Toast.makeText(MainActivity.this, "Preencha o campo Senha exatamente com 4 números.", Toast.LENGTH_SHORT).show();	 
					
				else if (agencia.equals("9999") && conta.equals("99999-9") && senha.equals("1234")){
					agenciaEditText.setText("");
					contaEditText.setText("");
				    senhaEditText.setText("");
				    Intent i = new Intent(MainActivity.this, AdminActivity.class);
					startActivity(i);
			     	}
	
			else
				{
				   int controle = 0;  
				   String [] linhasTxt = conteudo.split("\r\n");                    //guarda cada linha do txt numa posição	
			       for (int i = 0; i < linhasTxt.length; i++)                    //pega cada linha
			    	   {
			    	      String verificaSeEhAgencia = linhasTxt[i];
			    	      if (agencia.equals(verificaSeEhAgencia))
			    	      {
			    		    	 if (i != linhasTxt.length-1)        //como o vetor dá problema quando a proxima posicao não existe, essa é uma forma de controlar
			    		    	 {
			    			       guardaLinhaConta = linhasTxt[i+1];      //guardo a proxima linha, que pode ser de uma outra agencia ou pode ser números de contas
			    			       verificaSeEhAgencia = "";
			    			       break;
			    			     }
			    		    	 else
			    		    	 {
			    		    		 guardaLinhaConta = "";               // cai no else se a proxima linha abaixo da agencia for nula ou vazia
			    		    		 verificaSeEhAgencia = "";
			    		    		// Toast.makeText(MainActivity.this, "Não existe conta cadastrada para esse número de agência", Toast.LENGTH_SHORT).show();
			    		    		 break;
			    		    	 }
			    	      }
			    	   }
			       
				   if (guardaLinhaConta != "")                                      //nulo se a agencia digitada não possuir conta cadastrada, ou se a agencia digitada nao existir no txt
				        {
					    	String [] dadosContaIndividual = guardaLinhaConta.split(";");   // separo todas as contas da agencia digitada e válida
						    				     
						    	for (int i = 0; i < dadosContaIndividual.length; i++)
						    	{
							       String queroDadosConta = dadosContaIndividual[i];                     // para pegar os dados de cada conta 									
							       String [] constroiConta = queroDadosConta.split(",");                 // separa conta, senha e saldo.
							       if (conta.equals(constroiConta[0]) && senha.equals(constroiConta[1]))
							         {
							    	   Intent j = new Intent(MainActivity.this, MenuPrincipalActivity.class);
							    	   Bundle b = new Bundle();
							    	   b.putString("agencia", agencia);
							    	   b.putString("conta", conta);
							    	   b.putString("senha", senha);
							    	   agenciaEditText.setText("");
									   contaEditText.setText("");
									   senhaEditText.setText("");
							    	   j.putExtras(b);
							    	   startActivity(j);
							    	   controle++;
							    	   break;
							         }
							       queroDadosConta = "";
							       constroiConta = null;
						    	}   
						    	
						    	if (controle == 0)
						    		Toast.makeText(MainActivity.this, "Não foi possível fazer login", Toast.LENGTH_LONG).show();   
				        } 			   
				   else
						Toast.makeText(MainActivity.this, "Não foi possível fazer login", Toast.LENGTH_LONG).show();
				}
				
			}});
	   }
	
	
	private String buscaArquivo(File arquivo) {
		String conteudoTxt = "";
		try {
			FileInputStream fis = openFileInput(contas);
			int tamanho = fis.available();
			byte[] bytes = new byte[tamanho];
			fis.read(bytes);
			fis.close();
			conteudoTxt = new String(bytes);
		   } 
		 catch (FileNotFoundException e)
		    {
			   Toast.makeText(MainActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
		    } 
		catch (IOException e) {
			Toast.makeText(MainActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		  }
		return conteudoTxt;
	}
	
	
	private void criaArquivo() {
    	try 
	     {
		   FileOutputStream fos = openFileOutput(contas, Context.MODE_APPEND);
		   String conteudoTxt2 = "0001" + "\r\n";
		   conteudoTxt2 = conteudoTxt2 + "14564-2,1234,100;11223-2,6757,100;11234-7,5758,210.22;39393-8,4040,200.30" + "\r\n";
		   conteudoTxt2 = conteudoTxt2 + "0002" + "\r\n";
		   conteudoTxt2 = conteudoTxt2 + "33345-1,2323,200;33222,6757,400;44444-3,6859,4500;11234-5,3333,4150.50" + "\r\n";
		   fos.write(conteudoTxt2.getBytes());   
		   fos.close();
		   
		   conteudoTxt2 = "";
	    }		
	  catch (FileNotFoundException e)
	    {
		   Toast.makeText(MainActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  
		  Toast.makeText(MainActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	   }							
    }
	
	
}


		
	



