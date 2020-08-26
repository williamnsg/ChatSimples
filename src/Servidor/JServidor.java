package Servidor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class JServidor extends JFrame {

	private JPanel contentPane;
	protected JTextField onlinesField;
	protected JTextField ipField;
	protected JTextArea logArea;
	private EnviarReceberDados dados;
	private JFrame self = new JFrame();

	public JServidor() {
		
		dados = new EnviarReceberDados("bdaps.db3");
		self = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 380, 440);
		setTitle("Servidor");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Servidor:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(42, 11, 63, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Usuarios:");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel_1.setBounds(42, 46, 78, 14);
		contentPane.add(lblNewLabel_1);
		
		onlinesField = new JTextField();
		onlinesField.setText("0");
		onlinesField.setEditable(false);
		onlinesField.setBounds(116, 44, 46, 20);
		contentPane.add(onlinesField);
		onlinesField.setColumns(10);
		
		ipField = new JTextField();
		ipField.setEditable(false);
		ipField.setBounds(116, 9, 86, 20);
		contentPane.add(ipField);
		ipField.setColumns(10);
		
		JLabel labelOnlines = new JLabel("online");
		labelOnlines.setBounds(132, 46, 46, 14);
		contentPane.add(labelOnlines);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 119, 344, 271);
		contentPane.add(scrollPane);
		
		logArea = new JTextArea();
		logArea.setAutoscrolls(true);
		
		scrollPane.setViewportView(logArea);
		
		JLabel lblNewLabel_2 = new JLabel("Log");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(33, 92, 46, 19);
		contentPane.add(lblNewLabel_2);
		
		JButton historicoBtn = new JButton("Historico");
		historicoBtn.setFont(new Font("Arial", Font.BOLD, 12));
		historicoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				historico();	
			}
		});
		historicoBtn.setBounds(265, 90, 89, 23);
		contentPane.add(historicoBtn);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(0, 0, 364, 401);
		lblNewLabel_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("fundoServidor.png")));
		contentPane.add(lblNewLabel_3);
	}
	
	public void historico() {
		dados.puxarHistorico(JOptionPane.showInputDialog("Digite um nome para o arquivo"));
		JOptionPane.showMessageDialog(self, "Historico salvo !");

	}
}
