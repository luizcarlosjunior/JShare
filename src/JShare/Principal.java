package JShare;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

import br.dagostini.jshare.comum.pojos.Arquivo;
import br.dagostini.jshare.comum.pojos.Diretorio;
import br.dagostini.jshare.comun.Cliente;
import br.dagostini.jshare.comun.IServer;
import javax.swing.JTable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;


public class Principal extends JFrame implements Remote, Runnable, IServer {

	
	//CLIENTE
	public Cliente cliente = new Cliente();
	// SERVIDOR
	IServer servidor;
	Registry registry;
	
	
	private String CONTEXTO = "CLIENTE";
	private String DIRETORIO = "c:\\jshare"; 
	
	//LISTA DE CLIENTES
	private ArrayList<Cliente> lista_clientes = new ArrayList<Cliente>();
	private ModeloCliente modelo_cliente = new ModeloCliente();
	//LISTA DE ARQUIVOS
	private ArrayList<Arquivo> lista_arquivos = new ArrayList<Arquivo>();
	private ModeloArquivo modelo_arquivo = new ModeloArquivo();
	
	//MAP COM CLIETES
	private Map<String, Cliente> mapaClientes = new HashMap<>();
	
	
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("'[Cliente] 'dd/MM/yyyy H:mm:ss:SSS' -> '");
		
	
	//OBJETOS ACESSIVEIS POR FUNCOES
	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPORTA;
	private JTextField textUSER;
	JComboBox cbCONTEXTO = new JComboBox();
	JButton btnConectar = new JButton("CONECTAR");
	JButton btnDesconectar = new JButton("DESCONECTAR");
	private JTable tbCliente;
	JTextArea taLog = new JTextArea();
	private JTable tbArquivos;
	
	
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
		setBounds(100, 100, 797, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{475, 117, 0};
		gbl_contentPane.rowHeights = new int[]{-27, 110, 0, 117, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
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
		cbCONTEXTO.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				CONTEXTO = String.valueOf(cbCONTEXTO.getSelectedItem());
				
				if (CONTEXTO.equals("SERVIDOR")) {
					textIP.setEditable(false);
					textIP.setText( MeuIp() );
				} else {
					textIP.setEditable(true);
				}
			}
		});
		
		
		cbCONTEXTO.setModel(new DefaultComboBoxModel(new String[] {"CLIENTE", "SERVIDOR"}));
		
		panel.add(cbCONTEXTO);
		
		
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				try {
					if (textPORTA.getText().trim().length() == 0 ) {
						JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar uma porta!");
						return;
					}
					
					cliente.setNome( textUSER.getText().trim() ); //SETA O NOME DO USUÁRIO
					cliente.setIp( MeuIp() ); //SETA O IP ATUAL
					cliente.setPorta( Integer.parseInt( textPORTA.getText().trim() ) ); //SETA A PORTA
					
					
					if (cliente.getNome().length() == 0) {
						JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um nome!");
						return;
					}

					
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
				try {
					desconectar(cliente);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		panel.add(btnDesconectar);
		
		tbArquivos = new JTable();
		GridBagConstraints gbc_tbArquivos = new GridBagConstraints();
		gbc_tbArquivos.insets = new Insets(0, 0, 5, 5);
		gbc_tbArquivos.fill = GridBagConstraints.BOTH;
		gbc_tbArquivos.gridx = 0;
		gbc_tbArquivos.gridy = 1;
		contentPane.add(tbArquivos, gbc_tbArquivos);
		
		tbCliente = new JTable();
		tbCliente.setModel(modelo_cliente);
		GridBagConstraints gbc_tbCliente = new GridBagConstraints();
		gbc_tbCliente.gridheight = 3;
		gbc_tbCliente.fill = GridBagConstraints.BOTH;
		gbc_tbCliente.gridx = 1;
		gbc_tbCliente.gridy = 1;
		contentPane.add(tbCliente, gbc_tbCliente);
		
		JButton btnBaixar = new JButton("BAIXAR");
		GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
		gbc_btnBaixar.anchor = GridBagConstraints.EAST;
		gbc_btnBaixar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBaixar.gridx = 0;
		gbc_btnBaixar.gridy = 2;
		contentPane.add(btnBaixar, gbc_btnBaixar);
		
		
		taLog.setRows(5);
		taLog.setEditable(true);
		taLog.setColumns(50);
		GridBagConstraints gbc_taLog = new GridBagConstraints();
		gbc_taLog.insets = new Insets(0, 0, 0, 5);
		gbc_taLog.fill = GridBagConstraints.BOTH;
		gbc_taLog.gridx = 0;
		gbc_taLog.gridy = 3;
		contentPane.add(taLog, gbc_taLog);
	}
	


	
	public void Servidor() {
		
		log("Iniciando o servidor.");
		
		try {
			// inicia o servidor...
			servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(cliente.getPorta());
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
	
	
	
	public void Cliente() throws IOException {

		log("Iniciando o cliente");
		
		try {
			Registry registry = LocateRegistry.getRegistry(cliente.getIp(), cliente.getPorta());
			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);
			
			servidor.registrarCliente(cliente); // solicita o registro do cliente no servidor
			servidor.publicarListaArquivos(cliente, listarMeusArquivos()); // registrar os meus arquivos no servidor
			
			
		} catch (Exception e) {
			log("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE O SERVIDOR ESTÁ RODANDO, SE O IP E PORTA ESTÃO"
					+ " CORRETOS, SE NÃO HÁ BLOQUEIO DE FIREWALL OU ANTIVIRUS.\n"
					+ "-------------------------------------------------------------------\n\n");
			e.printStackTrace();
		}
	}
	
	
	private void log(String string) {
		taLog.append("[ " + sdf.format(new Date()) + " ]");
		taLog.append(" -> ");
		taLog.append(string);
		taLog.append("\n");
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

		if (mapaClientes.get(c.getNome()) != null) {
			log("O cliente: \"" + c.getNome() + "\" já está registrado no sistema, por favor escolha outro nome.");
			habilitarMenuConexao();
			throw new RemoteException("Alguém já está usando o nome: " + c.getNome());
		}

		mapaClientes.put(c.getNome(), cliente);

		log("Cliente \"" + c.getNome() + "\" conectou."); //registra no log local

		atualizarListasDeParticipantes();
		
	}
	
	
	private void atualizarListasDeParticipantes() throws RemoteException {
		log("Enviando lista atualizada de participantes para todos.");
		for (Cliente c : mapaClientes.values()) {
			//c.receberListaParticipantes(new ArrayList<String>(mapaClientes.keySet()));
			lista_clientes.add(c);
			// refresh na lista de clientes
			modelo_cliente.setList(lista_clientes);
		}
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
		
		if (CONTEXTO.equals("SERVIDOR")) {
			log(c.getNome() + " saiu do chat."); // mostra no contexto cliente
			atualizarListasDeParticipantes();
		} else {
			if (servidor != null) {
				UnicastRemoteObject.unexportObject(registry, true);
				servidor = null;
			}

			log("Você saiu do chat."); // mostra no contexto cliente
			lista_clientes = new ArrayList<Cliente>(); // zera a lista de clientes...
			registry = null;
			servidor = null;	
			
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<Arquivo> listarMeusArquivos() {
		File dirStart = new File("c:\\jshare");

		List<Arquivo> listaArquivos = new ArrayList<>();
		List<Diretorio> listaDiretorios = new ArrayList<>();
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setNome(file.getName());
				arq.setTamanho(file.length());
				listaArquivos.add(arq);
			} else {
				Diretorio dir = new Diretorio();
				dir.setNome(file.getName());
				listaDiretorios.add(dir);				
			}
		}
		
		log("Arquivos Encontrados:");
		for (Arquivo arq : listaArquivos) {
			log("\t" + arq.getTamanho() + "\t" + arq.getNome());
		}
		
		return listaArquivos;
		
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
	
	
	
	
	
	

}
