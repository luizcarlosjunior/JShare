package JShare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ATM.ModeloArquivo;
import ATM.ModeloCliente;

import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

import br.dagostini.jshare.comum.pojos.Arquivo;
import br.dagostini.jshare.comum.pojos.Diretorio;
import br.dagostini.jshare.comun.Cliente;
import br.dagostini.jshare.comun.IServer;
import br.dagostini.jshare.comun.Servidor;

import javax.swing.JTable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;


public class Principal extends JFrame {
	
	public static Cliente cliente;
	public static Servidor servidor;
	
	public Registry registry;
	
	//CONTEXTO
	private String CONTEXTO = "CLIENTE";
	
	//DIRETORIO PADRAO
	private String DIRETORIO = "c:\\jshare"; 
	//LISTA DE CLIENTES
	private ArrayList<Cliente> lista_clientes = new ArrayList<Cliente>();
	//MODELO DE CLIENTES
	private ModeloCliente modelo_cliente = new ModeloCliente();
	//LISTA DE ARQUIVOS
	private ArrayList<Arquivo> lista_arquivos = new ArrayList<Arquivo>();
	// MODELO DE ARQUIVOS
	private ModeloArquivo modelo_arquivo = new ModeloArquivo();
	//MAP COM CLIETES
	private Map<String, Cliente> mapaClientes = new HashMap<>();
	//FORMATO DE DATA
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");
		

	//OBJETOS ACESSIVEIS POR FUNCOES
	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPORTA;
	private JTextField textUSER;
	JComboBox cbCONTEXTO = new JComboBox();
	JButton btnConectar = new JButton("CONECTAR");
	JButton btnDesconectar = new JButton("DESCONECTAR");
	private JTable tbCliente;
	static JTextArea taLog = new JTextArea();
	private JTable tbArquivos;
	private JPanel panel_1;
	private JLabel lblPasta;
	private JTextField textDiretorio;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
					
					
					//CLIENTE
					cliente = new Cliente();
					log("Variável de Cliente criada.");
					
					// SERVIDOR
					try {
						servidor = new Servidor();
					    log("Variável de Servidor criada.");
					} catch (Exception e) {
					   log("ocorreu um erro: " + e);
					}
					
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
		setBounds(100, 100, 898, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{475, 117, 0};
		gbl_contentPane.rowHeights = new int[]{38, 37, 110, 0, 117, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JLabel lblIp = new JLabel("IP:");

		textIP = new JTextField();
		textIP.setText("127.0.0.1");
		textIP.setColumns(10);

		JLabel lblPorta = new JLabel("PORTA:");

		textPORTA = new JTextField();
		textPORTA.setText("1818");
		textPORTA.setColumns(10);
		
		JLabel lblContexto = new JLabel("CONTEXTO:");

		cbCONTEXTO.setModel(new DefaultComboBoxModel(new String[] {"CLIENTE", "SERVIDOR"}));

		JLabel lblUsurio = new JLabel("USUÁRIO:");

		textUSER = new JTextField();
		textUSER.setText("sem_nome");
		textUSER.setColumns(10);
		
		
		// ITENS AO PANEL
		
		panel.add(lblContexto);
		panel.add(cbCONTEXTO);
		
		panel.add(lblUsurio);
		panel.add(textUSER);
		
		panel.add(lblIp);
		panel.add(textIP);
		
		panel.add(lblPorta);
		panel.add(textPORTA);
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				conectar();
			}
		});
		
		panel.add(btnConectar);
		panel.add(btnDesconectar);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		
		lblPasta = new JLabel("Pasta:");
		panel_1.add(lblPasta);
		
		textDiretorio = new JTextField();
		textDiretorio.setText("c:\\jshare\\user1");
		panel_1.add(textDiretorio);
		textDiretorio.setColumns(50);
		
		
		tbArquivos = new JTable();
		tbArquivos.setModel(modelo_arquivo);
		GridBagConstraints gbc_tbArquivos = new GridBagConstraints();
		gbc_tbArquivos.insets = new Insets(0, 0, 5, 5);
		gbc_tbArquivos.fill = GridBagConstraints.BOTH;
		gbc_tbArquivos.gridx = 0;
		gbc_tbArquivos.gridy = 2;
		contentPane.add(tbArquivos, gbc_tbArquivos);
		
		tbCliente = new JTable();
		tbCliente.setModel(modelo_cliente);
		GridBagConstraints gbc_tbCliente = new GridBagConstraints();
		gbc_tbCliente.gridheight = 3;
		gbc_tbCliente.fill = GridBagConstraints.BOTH;
		gbc_tbCliente.gridx = 1;
		gbc_tbCliente.gridy = 2;
		contentPane.add(tbCliente, gbc_tbCliente);
		
		JButton btnBaixar = new JButton("BAIXAR");
		GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
		gbc_btnBaixar.anchor = GridBagConstraints.EAST;
		gbc_btnBaixar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBaixar.gridx = 0;
		gbc_btnBaixar.gridy = 3;
		contentPane.add(btnBaixar, gbc_btnBaixar);
		
		taLog.setRows(5);
		taLog.setEditable(true);
		taLog.setColumns(50);
		GridBagConstraints gbc_taLog = new GridBagConstraints();
		gbc_taLog.insets = new Insets(0, 0, 0, 5);
		gbc_taLog.fill = GridBagConstraints.BOTH;
		gbc_taLog.gridx = 0;
		gbc_taLog.gridy = 4;
		contentPane.add(taLog, gbc_taLog);
	}

	// imprime na janela de log o texto
	private static void log(String string) {
		taLog.append("[ " + sdf.format(new Date()) + " ]");
		taLog.append(" -> ");
		taLog.append(string);
		taLog.append("\n");
	}
	
	public String MeuIp() {
		InetAddress IP;
		String ip_user = "";
		
		try {
			IP = InetAddress.getLocalHost();
			String IPString = IP.getHostAddress();
			ip_user = IP.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ip_user;
	}
	
	
	// Função para desabilitar o menu de conexão
	public void desabilitarMenuConexao() {
		textUSER.setEditable(false);
		textIP.setEditable(false);
		textPORTA.setEditable(false);
		cbCONTEXTO.setEnabled(false);
		btnConectar.setEnabled(false);
		//habilita o botão de conectar
		btnDesconectar.setEnabled(true);
		
	}
	// Função para haabilitar o menu de conexão
	public void habilitarMenuConexao() {
		textUSER.setEditable(true);
		textIP.setEditable(true);
		textPORTA.setEditable(true);
		cbCONTEXTO.setEnabled(true);
		btnConectar.setEnabled(true);
		//desabilita o botão de desconectar
		btnDesconectar.setEnabled(false);
	}
	
	
	
	
	public void conectar() {
		try {
			
			//coleta a porta
			String porta = textPORTA.getText().trim();
			
			// Checa o tamanho dos caracteres do campo porta
			if ( ( porta.length() == 0 ) && ( porta.length() <= 5 ) ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar uma porta entre 1 e 5 digitos!\n Ex: \"1818\"");
				return;
			}

			// seta a porta do cliente
			cliente.setPorta( Integer.parseInt( porta ) );
			
			
			//coleta o nome do cliente
			String nome = textUSER.getText().trim();
			
			// checa o nome do cliente...
			if ( nome.equals("") ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um nome de usuário válido! Ex: \"João\"");
				return;
			}
			
			// seta o nome do cliente
			cliente.setNome( nome );
			
			//coleta o nome do cliente
			String diretorio = textDiretorio.getText().trim();
			
			// checa o nome do cliente...
			File dir = new File(diretorio);
			
			if ( !dir.exists() ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um diretório que exista...");
				return;
			}
			
			//seta o ip do cliente...
			cliente.setIp( MeuIp() ); //SETA O IP ATUAL
			
			// desabilita os campos para evitar ser alterados...
			desabilitarMenuConexao();
			
			
			
			// dependendo so cntexto... inicia um servidor ou conecta-se a uma rede já existente...
			
			if (CONTEXTO.equals("SERVIDOR")) {
				//IniciarServidor();
			} else {
				//IniciarCliente();
			}
			
		} catch (Exception e) {
			habilitarMenuConexao();
		}
		
	}
	

}
