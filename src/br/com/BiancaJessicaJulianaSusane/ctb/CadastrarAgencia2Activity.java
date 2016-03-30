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

public class CadastrarAgencia2Activity extends Activity {
	
	private EditText cadastrarAgenciaEditText;
	private Button okCadastrarAgenciaButton;
	private String contas = "contas.txt";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastrar_agencia2);
		cadastrarAgenciaEditText = (EditText) this.findViewById(R.id.cadastrarAgenciaEditText);
		okCadastrarAgenciaButton = (Button) this.findViewById(R.id.okCadastrarAgenciaButton);
		
		
		okCadastrarAgenciaButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String agenciaCadastrar = cadastrarAgenciaEditText.getEditableText().toString();
				Pattern padrao = Pattern.compile("[0-9]{4}");   //Define padrao de 4 digitos para inserir uma agencia
				Matcher dadoAgencia = padrao.matcher(agenciaCadastrar);
				
				if (!dadoAgencia.matches())
					Toast.makeText(CadastrarAgencia2Activity.this, "Agência deve conter exatamente 4 números.", Toast.LENGTH_SHORT).show();
				
			else
		  	 {	
				String conteudo = "";
				File arquivo = getFileStreamPath(contas);
				conteudo = buscaArquivo(arquivo);
				
				int controle = -1;
				String [] linhasTxt = conteudo.split("\r\n");                  	
			    for (int i = 0; i < linhasTxt.length; i++)                    
			   	  {
			    	   if (agenciaCadastrar.equals(linhasTxt[i])) 
			    	   {   
			    	       controle = 0;
			    	       break;
			    	   }
			      }
			    
			    if (controle == 0) 
			    {
			        Toast.makeText(CadastrarAgencia2Activity.this, "Já existe um registro com esse número de agência.", Toast.LENGTH_SHORT).show();
			        return;
			    }
			        
			    else
			    {
			    	criaArquivo(agenciaCadastrar);
			    	cadastrarAgenciaEditText.setText("");
			    	Toast.makeText(CadastrarAgencia2Activity.this, "Agência cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
			    	return;
			    	
		         }   	
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
		catch (IOException e) {
			Toast.makeText(CadastrarAgencia2Activity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		  }
		return conteudoTxt;
	}
	
	
	private void criaArquivo(String conteudoCadastrar) {
    	try 
	     {
		   FileOutputStream fos = openFileOutput(contas, Context.MODE_APPEND); // o mode append concatena com o que já tem no arquivo
		   conteudoCadastrar += "\r\n";
		   fos.write(conteudoCadastrar.getBytes());   
		   fos.close();
		   conteudoCadastrar = "";
	    }		
	  catch (FileNotFoundException e)
	    {
		   Toast.makeText(CadastrarAgencia2Activity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	  catch (IOException e)
	   {
		  Toast.makeText(CadastrarAgencia2Activity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	   }							
    }
	

}
