package JShare;

import java.awt.EventQueue;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

import br.dagostini.jshare.comum.pojos.Arquivo;
import br.dagostini.jshare.comum.pojos.Diretorio;
import br.dagostini.jshare.comun.Cliente;
import br.dagostini.jshare.comun.IServer;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class Principal extends JFrame implements Remote, Runnable, IServer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static Cliente cliente;
	public static IServer servidor;
	
	public static boolean isServer = false;
	
	public Registry registry;
	
	//DIRETORIO PADRAO
	public String DIRETORIO = "c:\\jshare"; 
	
	//porta padrão do servidor
	int PORTA_SERVER = 1818;
	String IP_SERVER = "";

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
	JButton btnDesconectar = new JButton("SAIR");
	static JTextArea taLog = new JTextArea();
	private JTable tbArquivos;
	private JPanel panel_1;
	private JLabel lblPasta;
	private JTextField textDiretorio;
	private JScrollPane scrollPane;
	private JScrollPane scrollPaneClientes;
	private JTable tbCliente;
	private JCheckBox chckbxServidor;
	private JPanel panel_2;
	private JScrollPane scrollPane_2;
	private JPanel panel_3;
	private JLabel lblPortaCliente;
	private JTextField textPORTACLI;
	
	
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
		setResizable(false);
		setTitle("PROJETO COMPARTILHANDO");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 994, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{479, 473, 0};
		gbl_contentPane.rowHeights = new int[]{19, 32, 185, 23, 176, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JLabel lblIp = new JLabel("IP SERV.:");
		
				textIP = new JTextField();
				textIP.setText( MeuIp() );
				textIP.setColumns(10);
				
						JLabel lblPorta = new JLabel("PORTA SERV. :");
						
								textPORTA = new JTextField();
								textPORTA.setText("1818");
								textPORTA.setColumns(10);
								
										JLabel lblUsurio = new JLabel("USUÁRIO:");
										
												textUSER = new JTextField();
												textUSER.setText("sem_nome");
												textUSER.setColumns(10);
												
												chckbxServidor = new JCheckBox("SERVIDOR");
												chckbxServidor.addItemListener(new ItemListener() {
													public void itemStateChanged(ItemEvent arg0) {
														checaContexto();
													}
												});
												chckbxServidor.addChangeListener(new ChangeListener() {
													public void stateChanged(ChangeEvent arg0) {
														
													}
												});
												panel.add(chckbxServidor);
												
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
												
												lblPortaCliente = new JLabel("PORTA CLI:");
												panel.add(lblPortaCliente);
												
												textPORTACLI = new JTextField();
												textPORTACLI.setText("1819");
												textPORTACLI.setColumns(10);
												panel.add(textPORTACLI);
												
												panel.add(btnConectar);
												btnDesconectar.setVisible(false);
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
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{82, 397, 0};
		gbl_panel_1.rowHeights = new int[]{14, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		lblPasta = new JLabel("Arquivos:");
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
		gbc_textDiretorio.anchor = GridBagConstraints.NORTHWEST;
		gbc_textDiretorio.gridx = 1;
		gbc_textDiretorio.gridy = 0;
		panel_1.add(textDiretorio, gbc_textDiretorio);
		textDiretorio.setColumns(40);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{479, 0};
		gbl_panel_2.rowHeights = new int[]{27, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_2.add(scrollPane, gbc_scrollPane);
		
		tbArquivos = new JTable();
		scrollPane.setViewportView(tbArquivos);
		tbArquivos.setModel(modelo_cliente_arquivo);
		
		scrollPaneClientes = new JScrollPane();
		scrollPaneClientes.setVisible(false);
		GridBagConstraints gbc_scrollPaneClientes = new GridBagConstraints();
		gbc_scrollPaneClientes.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneClientes.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneClientes.gridx = 1;
		gbc_scrollPaneClientes.gridy = 2;
		contentPane.add(scrollPaneClientes, gbc_scrollPaneClientes);
		
		tbCliente = new JTable();
		scrollPaneClientes.setViewportView(tbCliente);
		tbCliente.setModel(modelo_cliente);
		
		panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridwidth = 2;
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		contentPane.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{479, 0};
		gbl_panel_3.rowHeights = new int[]{23, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JButton btnBaixar = new JButton("BAIXAR");
		btnBaixar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				baixarArquivoSelecionado();
			}
		});
		GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
		gbc_btnBaixar.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnBaixar.gridx = 0;
		gbc_btnBaixar.gridy = 0;
		panel_3.add(btnBaixar, gbc_btnBaixar);
		
		scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 4;
		contentPane.add(scrollPane_2, gbc_scrollPane_2);
		scrollPane_2.setViewportView(taLog);
		
		taLog.setRows(5);
		taLog.setEditable(true);
		taLog.setColumns(50);
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
		
		chckbxServidor.setEnabled(false);
		textUSER.setEditable(false);
		textDiretorio.setEditable(false);
		textIP.setEditable(false);
		textPORTA.setEditable(false);
		textPORTACLI.setEditable(false);
		//esconde o botão de conectar
		btnConectar.setVisible(false);
		//mostra o botão de conectar
		btnDesconectar.setVisible(true);
		
	}
	
	// Função para haabilitar o menu de conexão
	public void habilitarMenuConexao() {
		chckbxServidor.setEnabled(true);
		textUSER.setEditable(true);
		textDiretorio.setEditable(true);
		textIP.setEditable(true);
		textPORTA.setEditable(true);
		textPORTACLI.setEditable(true);
		//mostra o botão de conectar
		btnConectar.setVisible(true);
		//esconde o botão de desconectar
		btnDesconectar.setVisible(false);
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
			
			// Checa o tamanho dos caracteres do campo porta do servidor
			if ( ( porta.length() == 0 ) && ( porta.length() <= 5 ) ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar uma porta entre 1 e 5 digitos!\n Ex: \"1818\"");
				return;
			}

			//coleta a porta
			String porta_cliente = textPORTACLI.getText().trim();

			// Checa o tamanho dos caracteres do campo porta do cliente
			if ( ( porta_cliente.length() == 0 ) && ( porta_cliente.length() <= 5 ) ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar uma porta do cliente.. entre 1 e 5 digitos!\n Ex: \"1818\"");
				return;
			}
			
			
			String ip = textIP.getText().trim();
			
			// Checa o tamanho dos caracteres do campo porta do servidor
			if ( checkIPv4(ip) ) {
				//JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um ip (IPv4) válido");
				//return;
			}
			
			// captura o ip do cliente :P
			String ip_cliente = MeuIp();
			
			//seta o ip do cliente...
			cliente.setIp( ip_cliente ); //SETA O IP ATUAL para o cliente
			

			// se estiver no contexto servidor... a porta do cliente será igual a porta do servidor... se não será o conteúdo do text...
			
			if (isServer) {
				// seta a porta do cliente
				cliente.setPorta( Integer.parseInt( porta ) );
				
			} else {
				// seta a porta do cliente
				cliente.setPorta( Integer.parseInt( porta_cliente ) );
			}
			
			PORTA_SERVER = Integer.parseInt(porta);
			IP_SERVER = ip;
			
			
			
			
			
			//coleta o nome do cliente
			String nome = textUSER.getText().trim();
			
			// checa o nome do cliente...
			if ( nome.equals("") ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um nome de usuário válido! Ex: \"João\"");
				return;
			}
			
			// seta o nome do cliente
			cliente.setNome( nome );
			
			//coleta o diretorio dos arquivos para listar
			DIRETORIO = textDiretorio.getText().trim();
			
			// checa o nome do cliente...
			File dir = new File(DIRETORIO);
			
			if ( !dir.exists() ) {
				JOptionPane.showMessageDialog(Principal.this, "Você precisa digitar um diretório que exista...");
				return;
			}
			

			
			
			// desabilita os campos para evitar ser alterados...
			desabilitarMenuConexao();
			

			ServidorLocal(); // inicia o servidor local no ip do cliente
			ConectarAoServidor(); // registra o cliente...
			
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
		mapa.put(c, lista);
		modelo_cliente_arquivo.setMap(mapa);
		log("Recebido os arquivos de " + c.getNome());
	}




	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String nome) throws RemoteException {
		// TODO IMPLEMENTAR ISSO MELHOR MAIS TARDE...
		return mapa;
	}




	@Override
	public byte[] baixarArquivo(Arquivo arq) throws RemoteException {

		Path path = Paths.get( new File(DIRETORIO + "\\" + arq.getNome() ).getPath() );
		
		try {
			byte[] dados = Files.readAllBytes(path);
			return dados;
		} catch (IOException e) {
			System.err.println("Erro ao ler arquivo");
			throw new RuntimeException(e);
		}
		
		
		
	}

	
	
	public void baixa_arquivo_cliente(Cliente c, Arquivo arq) {
		
		Thread t1 = new Thread(new Runnable() {
		     public void run() {

		    	 try {
		 			// conecta com o cliente
		 			Registry registry_cliente = LocateRegistry.getRegistry(c.getIp(), c.getPorta());
		 			IServer servidor_cliente = (IServer) registry_cliente.lookup(IServer.NOME_SERVICO);
		 			
		 			// cria um arquivo temporario (ex pasta RAIZ\downloads\meuarquivo.txt)
		 			File file = new File(DIRETORIO + "\\downloads\\" + c.getNome() + " - " + arq.getNome());
		 			
		 			FileOutputStream in = new FileOutputStream(file);
		 			
		 			// faz a captura dos bayts do cliente e escreve no arquivo...
		 			in.write(servidor_cliente.baixarArquivo(arq));
		 			// fecha o arquivo
		 			in.close();
		 			
		 			log("BAIXANDO O ARQUIVO: " + arq.getNome() + " de " + c.getNome());
		 			
		 		} catch (RemoteException e) {
		 			log("Erro ao iniciar download do arquivo.");
		 			e.printStackTrace();
		 		} catch (NotBoundException e) {
		 			log("Erro ao iniciar download do arquivo.");
		 			e.printStackTrace();

		 		} catch (FileNotFoundException e) {
		 			log("Erro: o arquivo não foi encontrado.");
		 			e.printStackTrace();
		 		} catch (IOException e) {
		 			log("Erro ao escrever o arquivo.");
		 			e.printStackTrace();
		 		}
		    	 
		    	 
		     }
		});  
		t1.start();
		
		
		
		
	}
	
	


	@Override
	public void desconectar(Cliente c) throws RemoteException {
		
		// remover o cliente da lista
		// aqui é fácil pois o key é um string...
		mapaClientes.remove(c.getNome());
		
		// remover a lista de arquivos do cliente...
		// aqui tinha uma pegadinha pois o key é um objeto
		for(Iterator<Entry<Cliente, List<Arquivo>>> it = mapa.entrySet().iterator(); it.hasNext(); ) {
	      
			Entry<Cliente, List<Arquivo>> entry = it.next();
			
			if(entry.getKey().getNome().equals(c.getNome())) {
				it.remove();
			}
		}
		
		
		// atualiza a lista de clientes...
		modelo_cliente.setMap(mapaClientes);
		
		//atualiza a lista de arquivos
		modelo_cliente_arquivo.setMap(mapa);
		
		//remove lisata de arquivos do cliente
		log(c.getNome() + " saiu..."); // mostra no contexto cliente
		
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	private void sair() throws RemoteException {
		// se contexto for servidor
		if (isServer) {
			// desconectar todos os clientes
			desconectar(cliente);
		} else {
			// se o contexto for cliente
			servidor.desconectar(cliente);
		}
		
		// limpa o mapa
		mapa = new HashMap<Cliente, List<Arquivo>>();
		modelo_cliente_arquivo.setMap(mapa);
		
		
		log("Você saiu..."); // mostra no contexto cliente
		habilitarMenuConexao();
	}
	
	
	
	public void ServidorLocal() {
		
		log("Iniciando o servidor...");
		
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
			//e.printStackTrace();
		}
		
	}
	
	
	
	public void ConectarAoServidor() throws IOException {

		log("Iniciando o cliente...");
		
		try {
			Registry registry = LocateRegistry.getRegistry(IP_SERVER, PORTA_SERVER);
			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);
			
			try {
				servidor.registrarCliente(cliente); // solicita o registro do cliente no servidor
				servidor.publicarListaArquivos(cliente, listarMeusArquivos()); // registrar os meus arquivos no servidor
				atualizarDados();
			} catch (Exception e) {
				log(" Problemas ao registrar os arquivos...");
				desconectar(cliente);
				habilitarMenuConexao();
			}
			
		} catch (Exception e) {
			log("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE O SERVIDOR ESTÁ RODANDO, SE O IP E PORTA ESTÃO"
					+ " CORRETOS, SE NÃO HÁ BLOQUEIO DE FIREWALL OU ANTIVIRUS.\n"
					+ "-------------------------------------------------------------------\n\n");
			//e.printStackTrace();
		}
		
	}
	
	
	
	// chega se o contexto é se conectar a um servidor ou ser o servidor...
	private void checaContexto(){
		
		// se o contexto é ser um servidor...
		if ( chckbxServidor.isSelected() == true) {
			// seta o ip do "servidor" como o ip da máquina...
			textIP.setText( MeuIp() );
			// desabilita o campo de texto para evitar que seja alterado...
			textIP.setEditable(false);
			//esconde a posta do cliente pois vai ser a mesma do srv...
			lblPortaCliente.setVisible(false);
			textPORTACLI.setVisible(false);
			// seta o contexto para servidor
			isServer = true;
			
			// mostra a lista de users
			scrollPaneClientes.setVisible(true);
			
		} else {
			//limpa o campo de ip do "servidor"...
			textIP.setText( "" );
			// habilita o campo de texto
			textIP.setEditable(true);
			// mostra posrta do clie
			lblPortaCliente.setVisible(false);
			textPORTACLI.setVisible(false);
			
			//seta o contexto para cliente
			isServer = false;
			
			//mostra a lista de users
			scrollPaneClientes.setVisible(false);
		}
		
	}
	
	
	private void baixarArquivoSelecionado(){
		
		// gera um modelo de arquivo temporario
		Arquivo arquivo = new Arquivo();
		// atribui um nome para o arquivo ... pega da linha selecionada
		arquivo.setNome( (String) modelo_cliente_arquivo.getValueAt(tbArquivos.getSelectedRow(), 3) );
		arquivo.setTamanho( (long) modelo_cliente_arquivo.getValueAt(tbArquivos.getSelectedRow(), 4) );
		
		// para compatibilizar com os dos colegas nao pode-se usar esse modelo...
		// resgata o modelo do cliente da lista de clientes... :p serviu para alguma coisa essa merda...
		//Cliente cli = mapaClientes.get( (String) modelo_cliente_arquivo.getValueAt( tbArquivos.getSelectedRow(), 0) );
		
		Cliente cli = new Cliente();
		cli.setNome( (String) modelo_cliente_arquivo.getValueAt(tbArquivos.getSelectedRow(), 0) );
		cli.setIp( (String) modelo_cliente_arquivo.getValueAt(tbArquivos.getSelectedRow(), 1) );
		cli.setPorta( (int) modelo_cliente_arquivo.getValueAt(tbArquivos.getSelectedRow(), 2) );
		
		
		// envia os dados do cliente e do arquivo para ser baixado...
		baixa_arquivo_cliente(cli, arquivo);
		
		
	}
	
	
	public static final boolean checkIPv4(final String ip) {
	    boolean isIPv4;
	    try {
	    final InetAddress inet = InetAddress.getByName(ip);
	    isIPv4 = inet.getHostAddress().equals(ip)
	            && inet instanceof Inet4Address;
	    } catch (final UnknownHostException e) {
	    isIPv4 = false;
	    }
	    return isIPv4;
	}
	
	
	private void atualizarDados() throws RemoteException {
		mapa = servidor.procurarArquivo("");
		//atualiza a lista de arquivos
		modelo_cliente_arquivo.setMap(mapa);
		
	}
	
	
	
	
}
