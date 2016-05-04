package JShare;

import java.awt.EventQueue;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ATM.ModeloCliente;
import ATM.ModeloClienteArquivo;

import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

import br.dagostini.comum.Servidor;
import br.dagostini.jshare.comum.pojos.Arquivo;
import br.dagostini.jshare.comum.pojos.Diretorio;
import br.dagostini.jshare.comun.Cliente;
import br.dagostini.jshare.comun.IServer;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;


public class Principal extends JFrame implements Remote, Runnable, IServer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static Cliente cliente;
	public static IServer servidor;

	
	public Registry registry;
	
	//DIRETORIO PADRAO
	private String DIRETORIO = "c:\\jshare"; 

	//LISTA DE CLIENTES
	private Map<String, Cliente> mapaClientes = new HashMap<>();

	/// Map<Integer, String> mapaBasico;
	Map<Cliente, List<Arquivo>> mapa = new HashMap<Cliente, List<Arquivo>>();

	// MODELO DE ARQUIVOS
	private ModeloClienteArquivo modelo_cliente_arquivo = new ModeloClienteArquivo(mapa);
	
	// Modelo de Clientes
	private ModeloCliente modelo_cliente = new ModeloCliente(mapaClientes);
		
	//FORMATO DE DATA
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");

	//OBJETOS ACESSIVEIS POR FUNCOES
	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPORTA;
	private JTextField textUSER;
	JButton btnConectar = new JButton("CONECTAR");
	JButton btnDesconectar = new JButton("DESCONECTAR");
	static JTextArea taLog = new JTextArea();
	private JTable tbArquivos;
	private JPanel panel_1;
	private JLabel lblPasta;
	private JTextField textDiretorio;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTable tbCliente;
	
	
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
		setBounds(100, 100, 898, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{479, 393, 0};
		gbl_contentPane.rowHeights = new int[]{33, 32, 27, 83, 23, 314, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 2;
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
								
										JLabel lblUsurio = new JLabel("USUÁRIO:");
										
												textUSER = new JTextField();
												textUSER.setText("sem_nome");
												textUSER.setColumns(10);
												
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
												btnDesconectar.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent arg0) {
														try {
															sair();
														} catch (RemoteException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
												});
												panel.add(btnDesconectar);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{82, 397, 0};
		gbl_panel_1.rowHeights = new int[]{14, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		lblPasta = new JLabel("Pasta:");
		GridBagConstraints gbc_lblPasta = new GridBagConstraints();
		gbc_lblPasta.anchor = GridBagConstraints.WEST;
		gbc_lblPasta.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasta.insets = new Insets(0, 0, 0, 5);
		gbc_lblPasta.gridx = 0;
		gbc_lblPasta.gridy = 0;
		panel_1.add(lblPasta, gbc_lblPasta);
		
		textDiretorio = new JTextField();
		textDiretorio.setText("c:\\jshare\\user1");
		GridBagConstraints gbc_textDiretorio = new GridBagConstraints();
		gbc_textDiretorio.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiretorio.anchor = GridBagConstraints.NORTH;
		gbc_textDiretorio.gridx = 1;
		gbc_textDiretorio.gridy = 0;
		panel_1.add(textDiretorio, gbc_textDiretorio);
		textDiretorio.setColumns(50);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.anchor = GridBagConstraints.NORTH;
		gbc_scrollPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		tbArquivos = new JTable();
		scrollPane.setViewportView(tbArquivos);
		tbArquivos.setModel(modelo_cliente_arquivo);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridheight = 4;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 2;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		tbCliente = new JTable();
		scrollPane_1.setViewportView(tbCliente);
		tbCliente.setModel(modelo_cliente);
		
		JButton btnBaixar = new JButton("BAIXAR");
		GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
		gbc_btnBaixar.anchor = GridBagConstraints.EAST;
		gbc_btnBaixar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBaixar.gridx = 0;
		gbc_btnBaixar.gridy = 4;
		contentPane.add(btnBaixar, gbc_btnBaixar);
		
		taLog.setRows(5);
		taLog.setEditable(true);
		taLog.setColumns(50);
		GridBagConstraints gbc_taLog = new GridBagConstraints();
		gbc_taLog.anchor = GridBagConstraints.SOUTH;
		gbc_taLog.fill = GridBagConstraints.HORIZONTAL;
		gbc_taLog.insets = new Insets(0, 0, 0, 5);
		gbc_taLog.gridx = 0;
		gbc_taLog.gridy = 5;
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
		String ip_user = "127.0.0.1";
		
		try {
			IP = InetAddress.getLocalHost();
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
		btnConectar.setEnabled(false);
		//habilita o botão de conectar
		btnDesconectar.setEnabled(true);
		
	}
	
	// Função para haabilitar o menu de conexão
	public void habilitarMenuConexao() {
		textUSER.setEditable(true);
		textIP.setEditable(true);
		textPORTA.setEditable(true);
		btnConectar.setEnabled(true);
		//desabilita o botão de desconectar
		btnDesconectar.setEnabled(false);
	}
	
	
	public List<Arquivo> listarMeusArquivos() {
		File dirStart = new File(DIRETORIO);

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
	
	
	
	public void conectar() {
		try {
			
			
			//CLIENTE
			cliente = new Cliente();
			log("Variável de Cliente criada.");
			
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
			

			Servidor(); // cria o conceito de servidor para o app
			Cliente(); // registra o cliente... Sim ele mesmo será um cliente...
			
		} catch (Exception e) {
			habilitarMenuConexao();
		}
		
	}




	@Override
	public void registrarCliente(Cliente c) throws RemoteException {

		// verifica se o cliente já existe no servidor...
		if (mapaClientes.get(c.getNome()) != null) {
			log("O cliente: \"" + c.getNome() + "\" já está registrado no sistema, por favor escolha outro nome.");
			throw new RemoteException("Alguém já está usando o nome: " + c.getNome());
		}
		
		// Adiciona o Cliente no mapa do sistema
		mapaClientes.put(c.getNome(), c);
		
		// registra a entrado do cliente no servidor
		log("Cliente \"" + c.getNome() + "\" conectou.");
		
		modelo_cliente.setMap(mapaClientes);
		
	}




	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String nome) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public byte[] baixarArquivo(Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		FileInputStream in = null;
		try {
			in = new FileInputStream( DIRETORIO + "\\" + arq.getNome() );
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int bytesread = 0;
			byte[] tbuff = new byte[512];
			while (true) {
				bytesread = in.read(tbuff);
				if (bytesread == -1) // if EOF
					break;
				buffer.write(tbuff, 0, bytesread);
			}
			
			return buffer.toByteArray();
			
		} catch (IOException e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
				}
			}
			return null;
		}
	}

	
	
	public void baixa_araquivo_cliente (Cliente c, Arquivo arq) {
		// conecta com o cliente
		
		Registry registry_cliente = LocateRegistry.getRegistry(c.getIp(), c.getPorta());
		IServer servidor_cliente = (IServer) registry.lookup(IServer.NOME_SERVICO);
		
		// cria um arquivo temporario (ex pasta downloads\meuarquivo.txt)
		java.io.File file = new java.io.File(DIRETORIO + "\\" + arq.getNome());
		FileOutputStream in = new FileOutputStream(file) ;  
		in.write(servidor_cliente.baixarArquivo(arq));
		in.close();
		// faz a captura dos bayts do cliente e escreve no arquivo...
		//arquivo = servidor_cliente.baixarArquivo(a);
		
	}
	
	


	@Override
	public void desconectar(Cliente c) throws RemoteException {

		// remover o cliente da lista
		mapaClientes.remove(c.getNome());
		
		//remove lisata de arquivos do cliente
		log(c.getNome() + " saiu..."); // mostra no contexto cliente
		
		if (cliente.getNome().equals(c.getNome())) {
			
			UnicastRemoteObject.unexportObject(servidor, true);
			
			registry = null;
			servidor = null;
			
		}
		
		modelo_cliente.setMap(mapaClientes);
		
		//atualizarListasDeParticipantes();
		habilitarMenuConexao();
	}
	
	

	public void sair() throws RemoteException{
		servidor.desconectar(cliente);
		log("Você saiu..."); // mostra no contexto cliente
		
	}

	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
