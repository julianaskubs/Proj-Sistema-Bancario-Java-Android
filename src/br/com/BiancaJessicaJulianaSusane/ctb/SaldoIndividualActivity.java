package br.com.BiancaJessicaJulianaSusane.ctb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class SaldoIndividualActivity extends ListActivity {

	private String conteudo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String nomeArquivo = "contas.txt";
		String [] aux;
		
		int tam = 0;
		
		try {
			File arquivo = getFileStreamPath(nomeArquivo);
			if (arquivo.exists()) {
				FileInputStream fis = openFileInput(nomeArquivo);
				int tamanho = fis.available();
				byte[] bytes = new byte[tamanho];
				fis.read(bytes);
				fis.close();
				conteudo = new String(bytes);
			}
		} catch (IOException e) {
			Toast.makeText(SaldoIndividualActivity.this, "Exceção genérica aconteceu", Toast.LENGTH_SHORT).show();
		}

		String [] linhasTxt = conteudo.split("\r\n");
		for	(int i = 0; i < linhasTxt.length; i++) {//descobre o tamanho do vetor
			String linhas = linhasTxt[i];
			if (linhas.length() > 4)
			{
			   aux = linhas.split(";");
			   tam += aux.length;
			}
		}
		
		String [] itens = new String [tam];
		String LinhaConta="";
		String mostrarSaldo[] = new String[tam];
		
		for(int i = 0; i< linhasTxt.length; i++)   //coloca os dados de todas as contas em uma linha
		{
			String linhas = linhasTxt[i];
			if (linhas.length() > 4)
			LinhaConta+= linhas + ";";
		}
		
		LinhaConta = LinhaConta.replace(";;", ";");
		DecimalFormat df = new DecimalFormat("0.00");
		
		aux = LinhaConta.split(";");    //monta um vetor que em cada índice tenha os dados de uma conta
		for(int i = 0; i< tam;i++)
		{
			itens = aux[i].split(",");
			mostrarSaldo[i] = "Conta: " + itens[0] + " - Saldo: " + itens[2];
		}
		
		ArrayAdapter <String> adapter = 
		new ArrayAdapter <String> 
		(this, android.R.layout.simple_list_item_1, mostrarSaldo);
		this.setListAdapter(adapter);
		

	}

}
