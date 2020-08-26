package Cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Cliente extends JFrame implements ActionListener, KeyListener {

	private JFrame self;
	private JTextArea historicoChat;
	private JTextArea usuariosArea;	
	private JScrollPane scroll;
	private JScrollPane scrollPane;
	private JButton btnSend;
	private JButton btnSair;
	private JButton acessarBtn;		
	private JPanel pnlChat;	
	private JPanel pnlMenu;
	private JPasswordField senhaField;
	private JPasswordField conf_senhaField;
	private JPasswordField senhaCadField;		
	private JTextField usuarioField;	
	private JTextField nomeField;
	private JTextField emailField;	
	private JTextField loginField;
	private JTextField msgField;	
	private JLabel chatLbl;
	private JLabel usuariosLbl;
	private JLabel avisoLabel;
	private JLabel lblAcessoAoSistema;
	private JLabel lblLogin;
	private JLabel lblSenha;
	private JLabel lblEmail;
	private JLabel usuarioLabel;
	private JLabel senhaLabel;
	private JLabel conf_senhaLabel;
	private JLabel labelCadFundo;
	private JLabel formLabel;
	private JTabbedPane tabContent;
	
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedWriter bfw;
	
	private String[] usuariosOn = null;	
	private String nameUser;
	private int qntd;
	private JButton cancelarBtn;
	
	
	public Cliente() throws IOException {
		
		self = this;
		qntd = 0;
		pnlChat = new JPanel(); 
		historicoChat = new JTextArea(10, 20);
		historicoChat.setSelectionColor(SystemColor.inactiveCaption);
		historicoChat.setEditable(false);
		historicoChat.setBackground(SystemColor.inactiveCaptionBorder);
		historicoChat.setLineWrap(true);
		usuariosArea = new JTextArea();
		usuariosArea.setBackground(SystemColor.inactiveCaptionBorder);
		msgField = new JTextField(20);
		msgField.setBackground(SystemColor.inactiveCaptionBorder);
		msgField.setBounds(10, 308, 344, 18);
		msgField.addKeyListener(this);
		btnSend = new JButton("\u2192");
		btnSend.setFont(new Font("Arial", Font.PLAIN, 14));
		btnSend.setBounds(364, 306, 63, 23);
		btnSend.setToolTipText("Enviar Mensagem");
		btnSair = new JButton("Sair");
		btnSair.setBounds(10, 337, 63, 23);
		btnSair.setToolTipText("Sair do Chat");
		btnSend.addActionListener(this);
		btnSair.addActionListener(this);
		btnSend.addKeyListener(this);
		scroll = new JScrollPane(historicoChat);
		scroll.setBounds(10, 52, 417, 243);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(454, 52, 130, 245);
		scrollPane.setViewportView(usuariosArea);
		chatLbl = new JLabel("Chat");
		chatLbl.setFont(new Font("Arial", Font.BOLD, 20));
		chatLbl.setBounds(10, 11, 63, 31);
		usuariosLbl = new JLabel("Usu\u00E1rios");
		usuariosLbl.setFont(new Font("Arial", Font.BOLD, 20));
		usuariosLbl.setBounds(454, 14, 111, 25);
		pnlChat.setLayout(null);
		labelCadFundo = new JLabel("");
		labelCadFundo.setBounds(0, 0, 595, 364);
		labelCadFundo.setIcon(new javax.swing.ImageIcon(getClass().getResource("fundo_chat.jpg")));

		pnlChat.add(scroll);
		pnlChat.add(msgField);
		pnlChat.add(btnSair);
		pnlChat.add(btnSend);
		pnlChat.setBackground(Color.WHITE);
		pnlChat.add(scrollPane);
		pnlChat.add(chatLbl);
		pnlChat.add(usuariosLbl);
		pnlChat.add(labelCadFundo);

		tabContent = new JTabbedPane();
		tabContent.setVisible(true);

		// Criacao do conteudo do Tab Login
		pnlMenu = new JPanel();
		pnlMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlMenu.setLayout(null);		
		cancelarBtn = new JButton("Cancelar");	
		cancelarBtn.setFont(new Font("Arial", Font.BOLD, 12));
		cancelarBtn.setBounds(321, 303, 113, 23);
		cancelarBtn.addActionListener(this);
		pnlMenu.add(cancelarBtn);
		lblAcessoAoSistema = new JLabel("Acesso ao sistema");
		lblAcessoAoSistema.setForeground(Color.WHITE);
		lblAcessoAoSistema.setBounds(202, 110, 198, 24);
		lblAcessoAoSistema.setFont(new Font("Arial", Font.BOLD, 20));
		pnlMenu.add(lblAcessoAoSistema);
		lblLogin = new JLabel("Login:");
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setBounds(190, 164, 51, 17);
		lblLogin.setFont(new Font("Arial", Font.BOLD, 14));
		pnlMenu.add(lblLogin);
		loginField = new JTextField();
		loginField.setBounds(246, 162, 141, 20);
		loginField.setColumns(10);
		pnlMenu.add(loginField);
		lblSenha = new JLabel("Senha:");
		lblSenha.setForeground(Color.WHITE);
		lblSenha.setBounds(190, 192, 54, 17);
		lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
		pnlMenu.add(lblSenha);
		senhaField = new JPasswordField();
		senhaField.setBounds(246, 190, 141, 20);
		pnlMenu.add(senhaField);

		// BOTAO DE ACESSO AO SISTEMA "LOGIN"
		acessarBtn = new JButton("Acessar");
		acessarBtn.setBounds(176, 303, 113, 23);
		acessarBtn.setFont(new Font("Arial", Font.BOLD, 12));
		acessarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarLogin();				
			}
		});

		avisoLabel = new JLabel("Login ou senha incorreto");
		avisoLabel.setForeground(Color.WHITE);
		avisoLabel.setFont(new Font("Arial", Font.BOLD, 12));
		avisoLabel.setBounds(232, 223, 148, 45);
		avisoLabel.setVisible(false);
		pnlMenu.add(avisoLabel);
		getRootPane().setDefaultButton(acessarBtn);
		pnlMenu.add(acessarBtn);
		tabContent.add("Menu", pnlMenu);

		JLabel labelFundo = new JLabel("");
		labelFundo.setBounds(0, 0, 595, 364);
		labelFundo.setIcon(new javax.swing.ImageIcon(getClass().getResource("fundo_menu.jpg")));
		pnlMenu.add(labelFundo);

		// Criacao do conteudo da tab CADASTRAR

		JPanel pnlCadastrar = new JPanel();
		pnlCadastrar.setBorder(new EmptyBorder(5, 5, 5, 5));
		tabContent.addTab("Cadastro", null, pnlCadastrar, null);
		pnlCadastrar.setLayout(null);

		formLabel = new JLabel("Preencha o formul\u00E1rio");
		formLabel.setHorizontalAlignment(SwingConstants.CENTER);
		formLabel.setForeground(Color.WHITE);
		formLabel.setBounds(192, 51, 236, 31);
		formLabel.setFont(new Font("Arial", Font.BOLD, 20));
		pnlCadastrar.add(formLabel);

		usuarioLabel = new JLabel("Usu\u00E1rio");
		usuarioLabel.setForeground(Color.WHITE);
		usuarioLabel.setBounds(180, 140, 44, 15);
		usuarioLabel.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(usuarioLabel);

		usuarioField = new JTextField();
		usuarioField.setBounds(256, 138, 190, 20);
		usuarioField.setColumns(10);
		pnlCadastrar.add(usuarioField);

		senhaLabel = new JLabel("Senha");
		senhaLabel.setForeground(Color.WHITE);
		senhaLabel.setBounds(180, 170, 36, 15);
		senhaLabel.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(senhaLabel);

		senhaCadField = new JPasswordField();
		senhaCadField.setBounds(256, 166, 190, 20);
		pnlCadastrar.add(senhaCadField);

		conf_senhaLabel = new JLabel("Confirmar senha");
		conf_senhaLabel.setForeground(Color.WHITE);
		conf_senhaLabel.setBounds(151, 196, 95, 15);
		conf_senhaLabel.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(conf_senhaLabel);

		conf_senhaField = new JPasswordField();
		conf_senhaField.setBounds(256, 193, 190, 20);
		pnlCadastrar.add(conf_senhaField);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setForeground(Color.WHITE);
		lblNome.setBounds(180, 216, 33, 27);
		lblNome.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(lblNome);

		nomeField = new JTextField();
		nomeField.setBounds(256, 221, 190, 20);
		nomeField.setColumns(10);
		pnlCadastrar.add(nomeField);

		lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setBounds(180, 243, 31, 23);
		lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(lblEmail);

		emailField = new JTextField();
		emailField.setBounds(256, 247, 190, 20);
		emailField.setColumns(10);
		pnlCadastrar.add(emailField);

		JButton confirmarBtn = new JButton("Confirmar");
		confirmarBtn.setBounds(243, 315, 127, 21);
		confirmarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviarCadastro();				
			}
		});
		confirmarBtn.setFont(new Font("Arial", Font.BOLD, 12));
		pnlCadastrar.add(confirmarBtn);

		JLabel labelCadFundo = new JLabel("");
		labelCadFundo.setBounds(0, -1, 595, 365);
		labelCadFundo.setIcon(new javax.swing.ImageIcon(getClass().getResource("fundo_cadastro.jpg")));
		pnlCadastrar.add(labelCadFundo);

		// Criacao da janela CLIENTE
		setTitle("Sistema IBAMA");
		setAlwaysOnTop(true); 
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(606, 421);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setContentPane(tabContent);
	}

	public void conectar() {
		try {
			socket = new Socket("localhost", 9871);
			ou = socket.getOutputStream();
			ouw = new OutputStreamWriter(ou);
			bfw = new BufferedWriter(ouw);
			bfw.flush();

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(self, "Servidor offline");
			System.exit(0);
		}
	}

	public void escutar() throws IOException, InterruptedException {
		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";

		while (!"/Sair".equalsIgnoreCase(msg))
			if (bfr.ready()) {
				msg = bfr.readLine();

				if (msg.equals("/Sair"))
					historicoChat.append("Servidor caiu! \r\n");
				else if (msg.contains("**logado**"))
				{
					tabContent.add("Sala de chat", pnlChat);
					tabContent.remove(0);
					tabContent.remove(0);
					this.nameUser = msg.split(";")[1];
				} else if (msg.contains("**EXISTENTE**" + loginField.getText())) {
					JOptionPane.showMessageDialog(self, "Usuario existente !");
					
				} else if (msg.toUpperCase().equals("**FALHALOGIN**")) {
					avisoLabel.setVisible(true);
					Thread.sleep(2000);
					avisoLabel.setVisible(false);
				}

				else {
					if (msg.contains("**ONLINE**")) {
						usuariosOn = msg.split(";");
						qntd = Integer.parseInt(msg.split(";")[1]);
						usuariosArea.setText(null);
						for (int i = 2; i < usuariosOn.length; i++) 
							usuariosArea.append(usuariosOn[i] + "\n");
					} 
					else if (msg.contains("**DESCONECTADO**")) {
						System.out.println("cliente recebeu que foi desconectado");
						usuariosOn = msg.split(";");
						qntd--;
						usuariosArea.setText(null);
						for (int i = 2; i < usuariosOn.length; i++) {
							usuariosArea.append(usuariosOn[i] + "\n");
						}
						historicoChat.append(usuariosOn[1] + " saiu do chat \r\n");
					}

					else {
						if (qntd > 1) {
							System.out.println(qntd);
							historicoChat.append(msg + " »\r\n");
							historicoChat.setCaretPosition(historicoChat.getDocument().getLength());
						} else {
							historicoChat.append(msg + " \r\n");
							historicoChat.setCaretPosition(historicoChat.getDocument().getLength());
							System.out.println(qntd);
						}
					}
				}
				msg = "";
			}
	}
	
	public void enviarLogin() {
		String msg = "**LOGIN**;" + loginField.getText() + ";" + senhaField.getText();
		try {
			enviarMensagem(msg);
			System.out.println("enviou");
			msg = "";
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void enviarCadastro() {
		
		String nome = nomeField.getText();
		String usuario = usuarioField.getText();
		String senha = String.valueOf(senhaCadField.getPassword());
		String conf_senha = String.valueOf(conf_senhaField.getPassword());
		String email = emailField.getText();

		if (usuario.equals(""))
			JOptionPane.showMessageDialog(self, "Digite um usuario!");

		else if (senha.equals(""))
			JOptionPane.showMessageDialog(self, "Digite uma senha !");

		else if (!conf_senha.equals(senha))
			JOptionPane.showMessageDialog(self, "Senhas diferentes !");
		else {
			try {
				enviarMensagem("**CADASTRAR**;" + usuario + ";" + senha + ";" + nome + ";" + email);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void enviarMensagem(String msg) throws IOException {
		if (msg.equals("Sair")) {
			bfw.write(nameUser + " Desconectado \r\n");
			bfw.write("Sair");			
		} else {
			bfw.write(msg + "\r\n");
		}
		bfw.flush();
		msgField.setText("");
	}

	public void sair() throws IOException {
		try {
			enviarMensagem("**DESCONECTADO**;" + nameUser);
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 @Override
	    public void actionPerformed(ActionEvent e)
	    {
	        try {
	            if(e.getActionCommand().equals(btnSend.getActionCommand())) 
	            {
	                enviarMensagem(nameUser+": " + msgField.getText());
	            }
	            else
	            if(e.getActionCommand().equals(btnSair.getActionCommand())){
	                sair();
	                this.setVisible(false);
	            }
	            else
		            if(e.getActionCommand().equals(cancelarBtn.getActionCommand())){
		                sair();
		                this.setVisible(false);
		            }

	        } catch (IOException e1) {
	           e1.printStackTrace();
	        }
	    }

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			btnSend.doClick();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}// class
