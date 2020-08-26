package Servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {

	private static ArrayList<BufferedWriter> clientes;
	private static ServerSocket server;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private EnviarReceberDados dados;
	private String usuario = null; 
	private String senhaDigi = null;
	private String onlines = "";
	protected int qtdeUsuarios;

	static JServidor serverFrame;
	
	public Servidor(Socket con) {		
		 
		this.con = con;
		dados = new EnviarReceberDados("bdaps.db3");
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// construtor

	public void run() {
		try {
			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			msg = bfr.readLine();
			String param;
			String usuarioNome;
			boolean login;

			while (!"Sair".equalsIgnoreCase(msg) || msg != null) {

				if (msg == null || msg.contains("**DESCONECTADO**")) {
					
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat formater = new SimpleDateFormat("dd/M/yyyy");
					Date date = new Date(System.currentTimeMillis());
					String hora = formatter.format(date);
					String data = formater.format(new Date());
					
					dados.execCMD("UPDATE USUARIOS SET ONLINE = FALSE  where login = '" + usuario + "' and senha = '"
							+ senhaDigi + "'");
					onlines = ";" + dados.pegaUsuariosOn();
					enviaParaTodos(bfw, "**DESCONECTADO**;" + msg.split(";")[1] + onlines);
					clientes.remove(bfw);
					con.close();
					getOnlineUsers();
					dados.salvarMensagens("Saiu do servidor",msg.split(";")[1], hora , data);
					serverFrame.onlinesField.setText(Integer.toString(qtdeUsuarios));
					serverFrame.logArea.append( msg.split(";")[1] + " saiu do servidor as " + hora + " " + data  + "\n");
					msg = "";
				}
				param = msg.split(";")[0];

				if (param.toUpperCase().equals("**LOGIN**")) {

					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat formater = new SimpleDateFormat("dd/M/yyyy");
					Date date = new Date(System.currentTimeMillis());
					String hora = formatter.format(date);
					String data = formater.format(new Date());

					usuario = msg.split(";")[1];
					senhaDigi = msg.split(";")[2];
					login = dados.compararDoBanco(usuario, senhaDigi);

					if (login == true) {
						usuarioNome = dados.retornaNameUsuario(usuario, senhaDigi);
						dados.execCMD("UPDATE USUARIOS SET ONLINE = true  where login = '" + usuario + "' and senha = '"
								+ senhaDigi + "'");
						enviaParaTodos(bfw, "**logado**;" + usuarioNome);

						onlines = dados.pegaUsuariosOn();
						System.out.println(onlines+"<<");
						getOnlineUsers();
						enviaParaTodos(bfw, "**ONLINE**;"+qtdeUsuarios+";"+ onlines);						
						serverFrame.onlinesField.setText(Integer.toString(qtdeUsuarios));
						dados.salvarMensagens("Entrou no servidor",usuarioNome, hora , data);
						System.out.println("Entrou no servidor as " + hora + " " + data);
						serverFrame.logArea.append(usuarioNome + " entrou no servidor as " + hora + " " + data + "\n");
						login = false;
						param = "";
						msg = "";
					} else {
						enviaParaTodos(bfw, "**FALHALOGIN**");
						param = "";
						msg = "";
					}
				} else if (param.toUpperCase().equals("**CADASTRAR**")) {
					System.out.println("entrou para cadastro");
					usuario = msg.split(";")[1];
					senhaDigi = msg.split(";")[2];
					String nome = msg.split(";")[3];
					String email = msg.split(";")[4];

					if (checarInfo(usuario)) {
						System.out.println("existe !");
						enviaParaTodos(bfw, "**EXISTENTE**" + usuario);
						param = "";
						msg = "";
					} else {
						dados.cadastrarBanco(usuario, senhaDigi, nome, email);
						param = "";
						msg = "";
					}
				} else {
					if (!con.isClosed()) {
						msg = bfr.readLine();
						if (msg != null && !msg.toUpperCase().contains("**LOGIN**")
								&& !msg.toUpperCase().contains("**CADASTRAR**") && !msg.contains("**DESCONECTADO**")) {
							System.out.println(msg);
							enviaParaTodos(bfw, msg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// run
	
	public void getOnlineUsers() {
		qtdeUsuarios = 0;
        for (BufferedWriter bw : clientes) {
        	qtdeUsuarios++;
        }
    }

	public void enviaParaTodos(BufferedWriter bwSaida, String msg) throws  IOException
    {
    	BufferedWriter bwS;
    	SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat formater= new SimpleDateFormat("dd/M/yyyy");
		Date date = new Date(System.currentTimeMillis());
		String hora = formatter.format(date);
		String data = formater.format(new Date());

        if(!msg.equals("")) 
        {
        	for(BufferedWriter bw : clientes){
               bwS = (BufferedWriter)bw;
               
				if (msg.contains("**logado**") || msg.contains("**EXISTENTE**") || msg.contains("**FALHALOGIN**")) {
					if (bwSaida == bwS) {
						bw.write(msg+"\r\n");
	                    bw.flush();
					}
				}
				else if (msg.contains("**DESCONECTADO**")) {
					if (bwSaida == bwS) {
						continue;
					}
					else {
						bw.write(msg+"\r\n");
	                    bw.flush();
					}
				}
				else {
					if(msg.contains("**ONLINE**")){	
						bw.write(msg+"\r\n");
						bw.flush();
					}
					else {
						bw.write(msg+"\r\n");
			
					    if (bwSaida == bwS) {
					    	dados.salvarMensagens(msg,msg.split(":")[0] , hora , data);
						    serverFrame.logArea.append(msg + "\n");
						    serverFrame.logArea.setCaretPosition(serverFrame.logArea.getDocument().getLength());
						 }
					    bw.flush();	
					}    
				}
            }
        }
    }// enviaParaTodos

	public boolean checarInfo(String new_login) {

		boolean verifica = false;

		if (new_login.equals(dados.puxarInfo("login", new_login)))
			verifica = true;

		return verifica;
	}

	public static void main(String[] args) {

		InetAddress inetAddress;
		
		try {
						
			inetAddress = InetAddress.getLocalHost();
			serverFrame = new JServidor();
			serverFrame.setVisible(true);
			serverFrame.ipField.setText(inetAddress.getHostAddress());
			server = new ServerSocket(9871);
			clientes = new ArrayList<BufferedWriter>();
			System.out.println("Servidor ativo na porta: 9871");
			serverFrame.logArea.append("Servidor ativo na porta: 9871\n");

			while (true) {
				serverFrame.logArea.append("Aguardando conexao...\n");
				System.out.println("Aguardando conexaoo...");
				Socket con = server.accept();
				serverFrame.logArea.append("Cliente conectado...\n");
				System.out.println("Cliente conectado...");
				Thread t = new Servidor(con);
				t.start();				
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Servidor ou porta ocupados");
			System.exit(0);
			e.printStackTrace();
		}
	}// Fim do metodo main

}// class
