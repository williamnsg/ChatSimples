package Servidor;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class EnviarReceberDados 
{
	private String nomeBanco;
	private ResultSet res;
	private EscreverTxt escrever;
	
	public EnviarReceberDados(String nomeBanco) 
	{
		escrever = new EscreverTxt();
		this.nomeBanco = nomeBanco;
	}
	
	public void cadastrarBanco(String new_login,String new_senha,String new_nome, String new_email)
	{		
		DB.connect(nomeBanco);
		DB.execQuery("INSERT INTO usuarios(login,senha,nome,email) VALUES ('"+ new_login +"','"+ new_senha +"','"+ new_nome +"','"+ new_email + "')");
	}
	
	public boolean compararDoBanco(String login,String senha){
		boolean resposta = false;
		
		DB.connect(nomeBanco);
		res = DB.query("SELECT count(*) as count, login,senha FROM usuarios where login='" + login+ "' and senha='" + senha + "'");
		String l = "";
		String s = ""; 
	
		try {
			
			if (res.getInt("count") > 0) {
				l = res.getString("login");
				s = res.getString("senha");

				if (l.equals(login) && s.equals(senha))
					resposta = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resposta;
	}
	
	public void puxarHistorico(String nome_arquivo) {
		String mensagens="";
		DB.connect(nomeBanco);
		res = DB.query("SELECT * FROM mensagem");
		
		try {
			while(res.next()) {
				mensagens = res.getString("login")  + ": " + res.getString("texto") + "; " +  res.getString("horario")  + "; " + res.getString("data") + "\n";
				escrever.write(mensagens, "C:\\Users\\Homer\\Desktop\\"+ nome_arquivo + ".txt", true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String puxarInfo(String coluna,String compararDigi){
		
		String resposta = "";
		
		DB.connect(nomeBanco);
		res = DB.query("SELECT * FROM usuarios");
		
		try{
			while(res.next()) 
			{				
				resposta = res.getString(coluna);
				if(compararDigi.equals(resposta))
					return resposta;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return resposta;
	}
	
	public String retornaNameUsuario(String login, String senha){
		String nome = "";

		DB.connect(nomeBanco);
		res = DB.query("SELECT * FROM usuarios where login = '" + login + "' and senha = '" + senha + "'");

		try{
			while(res.next())
			{
				nome = res.getString("nome");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nome;
	}
	 
	public String pegaUsuariosOn() { 
		
        String s = "";
		DB.connect(nomeBanco);
		res = DB.query("SELECT NOME FROM USUARIOS WHERE ONLINE = TRUE");
		
		try{
			while(res.next())
			{
				s = s + res.getString("nome") + ";";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	public int qntdUsuarios() {
		int num=0;
		DB.connect(nomeBanco);
		res = DB.query("SELECT count(*) as count, online FROM USUARIOS WHERE ONLINE = TRUE");
		
		try {
			num = res.getInt("count");
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return num;
	}
	
	public void salvarMensagens(String new_msg, String new_login, String new_horario, String new_data){
		
		DB.connect(nomeBanco);
		DB.execQuery("INSERT INTO mensagem(texto,login,horario,data) VALUES ('" + new_msg +"','" + new_login + "','" + new_horario + "','" + new_data+"')");
	}	
	
	public boolean execCMD(String s){
		boolean b;
		DB.connect(nomeBanco);
		return b = DB.execQuery(s);			
	}
}
