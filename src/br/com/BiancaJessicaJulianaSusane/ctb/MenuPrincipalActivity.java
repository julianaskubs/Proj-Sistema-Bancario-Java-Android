package br.com.BiancaJessicaJulianaSusane.ctb;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuPrincipalActivity extends ListActivity {

	private String [] opcao = new String [] { "Verificar Saldo", "Fazer Saque", "Depositar", "Ligar para o Banco", "Visualizar Histórico de Transações" };
	private String agencia = "";
	private String conta = "";
	private String senha = "";
	private String saldo = "";
	private String contas = "contas.txt";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent origem = this.getIntent();
		agencia = origem.getStringExtra("agencia");
		conta = origem.getStringExtra("conta");
		senha = origem.getStringExtra("senha");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcao);
		this.setListAdapter(adapter);	
	  }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String opcao = (String) this.getListAdapter().getItem(position);
		
		File arquivo = getFileStreamPath(contas);
		String conteudo = "";
		conteudo = buscaArquivo(arquivo);
	
		if (opcao.equals("Verificar Saldo"))
		{
			  String guardaLinhaConta = "";
			  String [] linhasTxt = conteudo.split("\r\n");                 
		      for (int i = 0; i < linhasTxt.length; i++)                    
		       {
		    	   if (agencia.equals(linhasTxt[i]))                       
		    	   guardaLinhaConta = linhasTxt[i+1];                   
		       }
	    	  String [] dadosContaIndividual = guardaLinhaConta.split(";");       				     
			  for (int i = 0; i < dadosContaIndividual.length; i++)
				{
				     String queroDadosConta = dadosContaIndividual[i];                      									
				     String [] constroiConta = queroDadosConta.split(",");                 
				     if (conta.equals(constroiConta[0])){
				         saldo = constroiConta[2];
				         break;
				     }
				     queroDadosConta = "";
				     constroiConta = null;
			    }
			  DecimalFormat df = new DecimalFormat("0.00"); 
			   Toast.makeText(MenuPrincipalActivity.this, "Saldo atual da conta R$ " + df.format(Float.parseFloat(saldo)), Toast.LENGTH_SHORT).show();
	     }
		
	else if (opcao.equals("Fazer Saque")){
			Intent i = new Intent (this, SaqueActivity.class);
			i.putExtra("agencia", agencia);
			i.putExtra("conta", conta);
			i.putExtra("senha", senha);
			startActivity(i);
		}
	else if(opcao.equals("Depositar"))
	{
		Intent i = new Intent (this, DepositoActivity.class);
		i.putExtra("agencia", agencia);
		i.putExtra("conta", conta);
		i.putExtra("senha", senha);
		startActivity(i);
		
	}
	else if(opcao.equals("Ligar para o Banco"))
	{
		Intent i = new Intent (this, LigacaoActivity.class);
		i.putExtra("conta", conta);
		i.putExtra("agencia", agencia);
		i.putExtra("senha", senha);
		startActivity(i);
	}
	else if(opcao.equals("Visualizar Histórico de Transações"))
	{
		     Intent i = new Intent (this, VisualizarHistoricoActivity.class);
		     i.putExtra("conta", conta);
		     i.putExtra("agencia", agencia);
		     i.putExtra("senha", senha);
		     startActivity(i);
		   
	  }
			
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
			   Toast.makeText(MenuPrincipalActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
		    } 
		catch (IOException e) {
			Toast.makeText(MenuPrincipalActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
		  }
		return conteudoTxt;
	}
	
	
}
