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

public class Principal extends JFrame implements Remote, Runnable, ServicoRMI {


	private String USER = "sem_nome";
	private String CONTEXTO = "CLIENTE";
	private String IP = "127.0.0.1";
	private int PORTA = 1818;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("'[Cliente] 'dd/MM/yyyy H:mm:ss:SSS' -> '");
		
	
	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPORTA;
	private JTextField textUSER;
	JPanel panel_2 = new JPanel();
	JTextArea taLog = new JTextArea();
	JComboBox cbCONTEXTO = new JComboBox();
	JButton btnConectar = new JButton("CONECTAR");
	
	
	
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
		setBounds(100, 100, 655, 280);
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
					//SETA O NOME DO USUÁRIO
					USER = textUSER.getText();
					//SETA O IP
					IP = textIP.getText();
					//SETA A PORTRA
					PORTA = Integer.parseInt(textPORTA.getText());
					//DEPENDENDO DO CONTEXTO INICIA O SERVIÇO
					CONTEXTO = String.valueOf(cbCONTEXTO.getSelectedItem());
					
					if (CONTEXTO.equals("SERVIDOR")) {
						Servidor();
					} else {
						Cliente();
					}
					desabilitarMenuConexao();
					
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
			ServicoRMI servico = (ServicoRMI) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.createRegistry(PORTA);
			registry.rebind(ServicoRMI.NOME, servico);
			log("Aguardando conexões.");

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
			Registry registry = LocateRegistry.getRegistry(IP, PORTA);
			ServicoRMI servico = (ServicoRMI) registry.lookup(ServicoRMI.NOME);
			//manda para o servidor o nome do ususário
			String retorno = servico.saudar(USER);
			//mostra no nome o que recebeu de volta
			log(retorno);
			
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
	
	
	@Override
	public String saudar(String nome) throws RemoteException {
		//registra no log do servidor a 
		log("Cliente \"" + nome + "\" conectou.");
		//retorna para o cliente a saudação
		return "Olá " + nome + "!";
	}
	
	
	public void desabilitarMenuConexao() {
		textUSER.setEditable(false);
		textIP.setEditable(false);
		textPORTA.setEditable(false);
		cbCONTEXTO.setEditable(false);
		btnConectar.setEnabled(false);
		
	}
	
	public void habilitarMenuConexao() {
		textUSER.setEditable(true);
		textIP.setEditable(true);
		textPORTA.setEditable(true);
		cbCONTEXTO.setEditable(true);
		btnConectar.setEnabled(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
