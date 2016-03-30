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

public class AdminActivity extends ListActivity {
	private String contas = "contas.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] itens = new String[] { "Cadastrar Agência", "Cadastrar Conta",
				"Verificar Saldos Individuais", "Verificar Saldo Global" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, itens);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent (this, CadastrarAgencia2Activity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent (this, CadastrarContaActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent (this, SaldoIndividualActivity.class);
			 startActivity(intent);
			break;
		case 3:
			saldoGlobal();
			break;
		}
	}
	
	public void saldoGlobal()
	{
		File arquivo = getFileStreamPath(contas);
		String conteudo = buscaArquivo(arquivo);
		String [] linhasTxt = conteudo.split("\r\n");
		String []dadosConta;
		String []saldoIndividual;
		float saldoG = 0;
		for	(int i = 0; i < linhasTxt.length; i++) //primeiro olhar linha por linha do txt 
		{
			String linha = linhasTxt[i];
			if (linha.length() > 4)        //se não for linha de agencia ele cai no if
			{
			    dadosConta = linha.split(";");
			    for (int k=0; k <dadosConta.length; k++)//pega o valor de cada saldo
			     {
				    saldoIndividual = dadosConta[k].split(",");
				    saldoG += Float.parseFloat(saldoIndividual[2]);//o índice 2 é onde está o saldo de cada conta
			     }
			}   
		}
		DecimalFormat df = new DecimalFormat("0.00");  
		Toast.makeText(AdminActivity.this, "Saldo Global: R$" + df.format(saldoG), Toast.LENGTH_SHORT).show();
	}
	
	
	private String buscaArquivo(File arquivo)
	{
	String conteudoTxt= "";
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
		   Toast.makeText(AdminActivity.this, "Arquivo não encontrado", Toast.LENGTH_LONG).show();
	    } 
	catch (IOException e) {
		Toast.makeText(AdminActivity.this, "Exceção genérica capturada", Toast.LENGTH_LONG).show();
	  }
	
	return conteudoTxt;
	}
}
