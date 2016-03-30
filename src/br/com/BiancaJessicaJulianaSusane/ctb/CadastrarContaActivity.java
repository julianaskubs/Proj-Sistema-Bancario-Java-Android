package br.com.BiancaJessicaJulianaSusane.ctb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastrarContaActivity extends Activity {

	private EditText agContaNovaEditText;
	private EditText contaNovaEditText;
	private EditText senhaNovaEditText;
	private Button okContaNovaButton;
	private String contas = "contas.txt";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastrar_conta);
		agContaNovaEditText = (EditText) this.findViewById(R.id.agContaNovaEditText);
		contaNovaEditText = (EditText) this.findViewById(R.id.contaNovaEditText);
		senhaNovaEditText = (EditText) this.findViewById(R.id.senhaNovaEditText);
		okContaNovaButton = (Button) this.findViewById(R.id.okContaNovaButton);
		
	
		okContaNovaButton.setOnClickListener (new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String agencia = agContaNovaEditText.getEditableText().toString();
				String conta = contaNovaEditText.getEditableText().toString().trim();
				String senha = senhaNovaEditText.getEditableText().toString();
				ValidarDadosLogin vdl = new ValidarDadosLogin();
				
				Pattern padrao = Pattern.compile("[0-9]{4}");   //Define padrao de 4 digitos para inserir uma agencia
				Matcher dadoAgencia = padrao.matcher(agencia);
				
				if(agencia.equals("9999") && conta.equals("99999-9"))
					Toast.makeText(CadastrarContaActivity.this, "N�mero de ag�ncia e conta de adm, digite outro n�mero", Toast.LENGTH_SHORT).show();
				
				else if (!dadoAgencia.matches())
					Toast.makeText(CadastrarContaActivity.this, "O n�mero da Ag�ncia deve conter 4 n�meros.", Toast.LENGTH_SHORT).show();
				
				else if(!vdl.comprimentoPeloMenosConta(conta))
			         Toast.makeText(CadastrarContaActivity.this, "O campo Conta deve conter apenas 5 n�meros ou 6 com o d�gito. Se houver d�gito, coloque um h�fen (-) antes.", Toast.LENGTH_LONG).show();
				
				else if (!vdl.validaDigitoConta(conta))
					 Toast.makeText(CadastrarContaActivity.this, "O h�fen (-) do d�gito da conta est� no lugar errado. Coloque-o entre o n�mero da conta e o n�mero do d�gito", Toast.LENGTH_LONG).show();	  
				
				else if(!vdl.comprimentoSenha(senha, 4))
					 Toast.makeText(CadastrarContaActivity.this, "Preencha o campo Senha exatamente com 4 n�meros.", Toast.LENGTH_SHORT).show();		
				
				
		 else
				{
					File arquivo = getFileStreamPath(contas);
					String conteudo = buscaArquivo(arquivo);
					int posicao = -1;
					
					String guardaLinha = "";
					int aux = -1;
					String [] linhasTxt = conteudo.split("\r\n");   // coloca cada linha do txt numa posicao do vetor                	
				    
					for (int i = 0; i < linhasTxt.length; i++)                    
				   	  {
				    	 String verificaSeEhAgencia = linhasTxt[i];        // pega a linha do txt na posicao i 
				    		     if (agencia.equals(verificaSeEhAgencia))  //se a linha na posicao i for igual a agencia que eu quero cadastrar
				    		       {
				    		    	 if (i != linhasTxt.length-1)        //como o vetor d� problema quando a proxima posicao n�o existe, essa � uma forma de controlar
				    		    	 {
				    			       guardaLinha = linhasTxt[i+1];      //guardo a proxima linha, que pode ser de uma outra agencia ou pode ser n�meros de contas
				    			       verificaSeEhAgencia = "";
				    			       posicao = i+1;
				    			       break;
				    		    	 }
				    		    	 else
				    		    	 {
				    		    		 guardaLinha = "";               // cai no else se a proxima linha abaixo da agencia for nula ou vazia
				    		    		 aux = 0;
				    		    		 verificaSeEhAgencia = "";
				    		    		 break;
				    		    	 }
				    		       }
				    		       
				    		     else
				    		   	    verificaSeEhAgencia = "";	
				       }
					
					
					
		 // a partir daqui temos 4 situacoes diferentes:
					
		// 1� Situa��o................ se a linha guardada for a de n�mero de contas (ocorre quando existe uma linha contendo numeros de contas embaixo da linha da agencia - situa��o padr�o)
				
				
					if (guardaLinha.length() > 4) 
					{
						String [] contasSeparadas = guardaLinha.split(";");
						String dadosContaIndiv = "";
						int controle2 = -1;
						
						for (int i = 0; i < contasSeparadas.length; i++)  // olhar conta por conta que existir na linha para verificar se o numero dessa conta j� existe
						{
							dadosContaIndiv = contasSeparadas[i];                       // pego cada conta da linha e 
							String [] queroNumeroConta = dadosContaIndiv.split(",");   // separo conta, senha e saldo
							if (conta.equals(queroNumeroConta[0]))                     // verifico se a conta j� existe cadastrada
							    {
									Toast.makeText(CadastrarContaActivity.this, "Essa ag�ncia j� possui esse n�mero de conta cadastrada. Tente outro n�mero", Toast.LENGTH_LONG).show();
									controle2 = 0;
									break;
							    }
						 }
						if (controle2 == -1)  // se depois de verificado todas as contas da linha, n�o existir o numero cadastro, podemos cadastr�-lo:
							 {
								String novaConta = ";" + conta + "," + senha + "," + "0.00";
								String novaLinha = guardaLinha + novaConta;
								novaLinha = novaLinha.replace(";;", ";");
								linhasTxt[posicao] = novaLinha;
								conteudo = "";
							    
								for (int j = 0; j < linhasTxt.length; j++)
								        conteudo += linhasTxt[j] + "\r\n";
							   
							    if (arquivo.exists())
						    	    arquivo.delete();
							    criaArquivo(conteudo);
							    conteudo = "";
							    agContaNovaEditText.setText(""); 
								contaNovaEditText.setText(""); 
								senhaNovaEditText.setText(""); 
								
							    
							    Toast.makeText(CadastrarContaActivity.this, "Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();  
							    return;
							}
						}
						   
					
		// 2� Situa��o........... se a linha guardada for de um n�mero de agencia (ocorre quando temos agencias cadastradas uma embaixo da outra)
					
					else if (guardaLinha.length() == 4) 
						{
							String novaConta = conta + "," + senha + "," + "0.00" + "\r\n";
							String conteudoParteA = "";
							String conteudoParteB = "";
							
							for (int i = 0; i <= posicao-1; i++)   //percorro o linhasTxt at� a posi��o da agencia digitada pelo usu�rio
							{
								conteudoParteA += linhasTxt[i] + "\r\n"; 
							}
							
							for (int j = posicao; j < linhasTxt.length; j++)
							{
								conteudoParteB += linhasTxt[j] + "\r\n";  //percorro o linhasTxt a partir da posicao da outra agencia, que depois vai mudar de posicao 
							}
							
							conteudo = "";
							conteudo = conteudoParteA + novaConta + conteudoParteB;
							
							if (arquivo.exists())
					    	    arquivo.delete();
						    criaArquivo(conteudo);
						    conteudo = "";
						    agContaNovaEditText.setText(""); 
							contaNovaEditText.setText(""); 
							senhaNovaEditText.setText(""); 
						    Toast.makeText(CadastrarContaActivity.this, "Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
						    return;
						 }
						
		// 3� situa��o......... ocorre quando queremos cadastrar uma conta e o guardaLinha vem vazio porque n�o existem linhas abaixo da agencia digitada pelo usu�rio
				
					else if (aux == 0)
					{
							String montaConta = conta + "," + senha + "," + "0.00" + "\r\n";		
						    criaArquivo(montaConta);
						    agContaNovaEditText.setText(""); 
							contaNovaEditText.setText(""); 
							senhaNovaEditText.setText(""); 
						    Toast.makeText(CadastrarContaActivity.this, "Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
						   
				   }
					
					
		// 4� Situa��o..........ocorre quando a agencia digitada pelo usu�rio n�o est� cadastrada no txt 
					
					else  
						{
						     Toast.makeText(CadastrarContaActivity.this, "Ag�ncia n�o existe. A conta n�o foi cadastrada.", Toast.LENGTH_SHORT).show();
						     conteudo = "";
						     return;
						} 
					
					}		
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
		 catch (FileNotFoundException e)
		    {
			   Toast.makeText(CadastrarContaActivity.this, "Arquivo n�o encontrado", Toast.LENGTH_LONG).show();
		    } 
		catch (IOException e) {
			Toast.makeText(CadastrarContaActivity.this, "Exce��o gen�rica capturada", Toast.LENGTH_LONG).show();
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
		   Toast.makeText(CadastrarContaActivity.this, "Arquivo n�o encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  Toast.makeText(CadastrarContaActivity.this, "Exce��o gen�rica capturada", Toast.LENGTH_LONG).show();
	   }							
    }
	


}
