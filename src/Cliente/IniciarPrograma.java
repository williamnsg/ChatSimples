package Cliente;
import java.io.IOException;

import javax.swing.JOptionPane;

public class IniciarPrograma {

	public static void main(String[] args) throws IOException, InterruptedException 
	{		
		Cliente cliente = new Cliente();	
		cliente.conectar();
		cliente.escutar();
	}
}
