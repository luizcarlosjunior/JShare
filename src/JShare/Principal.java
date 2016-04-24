package JShare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

import br.dagostini.jshare.comum.pojos.Arquivo;
import br.dagostini.jshare.comun.Cliente;
import br.dagostini.jshare.comun.IServer;








public class Principal extends JFrame implements Remote, Runnable, IServer {

	
	//CLIENTE
	public Cliente cliente = new Cliente();
	// SERVIDOR
	IServer servidor;
	
	
	private String CONTEXTO = "CLIENTE";
	
	
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("'[Cliente] 'dd/MM/yyyy H:mm:ss:SSS' -> '");
		
	
	//OBJETOS ACESSIVEIS POR FUNCOES
	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPORTA;
	private JTextField textUSER;
	JPanel panel_2 = new JPanel();
	JTextArea taLog = new JTextArea();
	JComboBox cbCONTEXTO = new JComboBox();
	JButton btnConectar = new JButton("CONECTAR");
	JButton btnDesconectar = new JButton("DESCONECTAR");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	

	/**
	 * Create the frame.
	 */
	public Principal() {
		setTitle("PROJETO COMPARTILHANDO");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 802, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{580, 0};
		gbl_contentPane.rowHeights = new int[]{-27, 110, 117, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JLabel lblIp = new JLabel("IP:");
		panel.add(lblIp);
		
		textIP = new JTextField();
		textIP.setText("127.0.0.1");
		panel.add(textIP);
		textIP.setColumns(10);
		
		JLabel lblPorta = new JLabel("PORTA:");
		panel.add(lblPorta);
		
		textPORTA = new JTextField();
		textPORTA.setText("1818");
		panel.add(textPORTA);
		textPORTA.setColumns(10);
		
		JLabel lblContexto = new JLabel("CONTEXTO:");
		panel.add(lblContexto);
		
		
		cbCONTEXTO.setModel(new DefaultComboBoxModel(new String[] {"CLIENTE", "SERVIDOR"}));
		panel.add(cbCONTEXTO);
		
		
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				try {
					cliente.setNome( textUSER.getText() ); //SETA O NOME DO USUÁRIO
					cliente.setIp( textIP.getText() ); //SETA O IP
					cliente.setPorta( Integer.parseInt( textPORTA.getText() ) ); //SETA A PORTA
					
					CONTEXTO = String.valueOf(cbCONTEXTO.getSelectedItem()); //SETA O CONTEXTO
					
					desabilitarMenuConexao(); // desabilita os campos para evitar ser alterados...
					
					if (CONTEXTO.equals("SERVIDOR")) {
						Servidor();
					} else {
						Cliente();
					}
					
				} catch (Exception e) {
					habilitarMenuConexao();
				}
				
			}
		});
		
		JLabel lblUsurio = new JLabel("USUÁRIO:");
		panel.add(lblUsurio);
		
		textUSER = new JTextField();
		textUSER.setText("sem_nome");
		panel.add(textUSER);
		textUSER.setColumns(10);
		panel.add(btnConectar);
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// desconectar do serviço
			}
		});
		
		
		panel.add(btnDesconectar);
		
		
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		
		
		taLog.setEditable(true);
		taLog.setColumns(50);
		taLog.setRows(5);
		panel_2.add(taLog);
	}
	


	
	public void Servidor() {
		
		log("Iniciando o servidor.");
		
		try {
			// inicia o servidor...
			servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.createRegistry(cliente.getPorta());
			registry.rebind(IServer.NOME_SERVICO, servidor);
			log("Aguardando conexões.");  //gera o log de sucesso

		} catch (Exception e) {
			log("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE A APLICAÇÃO JÁ NÃO ESTÁ RODANDO"
					+ " OU SE A PORTA NÃO ESTÁ OCUPADA POR OUTRO PROGRAMA.\n"
					+ "-------------------------------------------------------------------\n\n");
			e.printStackTrace();
		}
		
	}
	
	
	
	public void Cliente() {

		log("Iniciando o cliente");
		
		try {
			Registry registry = LocateRegistry.getRegistry(cliente.getIp(), cliente.getPorta());
			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);
			
			registrarCliente(cliente); // solicita o registro do cliente no servidor
			
		} catch (Exception e) {
			log("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE O SERVIDOR ESTÁ RODANDO, SE O IP E PORTA ESTÃO"
					+ " CORRETOS, SE NÃO HÁ BLOQUEIO DE FIREWALL OU ANTIVIRUS.\n"
					+ "-------------------------------------------------------------------\n\n");
			e.printStackTrace();
		}
	}
	
	
	private void log(String string) {
		taLog.append(string + '\n');
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	public void desabilitarMenuConexao() {
		textUSER.setEditable(false);
		textIP.setEditable(false);
		textPORTA.setEditable(false);
		cbCONTEXTO.setEditable(false);
		btnConectar.setEnabled(false);
		btnDesconectar.setEnabled(true);
		
	}
	
	public void habilitarMenuConexao() {
		textUSER.setEditable(true);
		textIP.setEditable(true);
		textPORTA.setEditable(true);
		cbCONTEXTO.setEditable(true);
		btnConectar.setEnabled(true);
		btnDesconectar.setEnabled(false);
	}
	
	
	
	
	
	// IMPLEMENTAR FUNCOES DO ISERVER
	
	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		//registra no log do servidor
		log("Cliente \"" + c.getNome() + "\" conectou.");
	}




	@Override
	public void publicarListaArquivos(br.dagostini.jshare.comun.Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Map<br.dagostini.jshare.comun.Cliente, List<Arquivo>> procurarArquivo(String nome) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public byte[] baixarArquivo(Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void desconectar(br.dagostini.jshare.comun.Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	

}
