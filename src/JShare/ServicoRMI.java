package JShare;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface para acesso remoto. Toda interface usada com RMI deve estender de
 * {@link java.rmi.Remote}.
 * 
 * @author Fernando D'Agostini
 *
 */
public interface ServicoRMI extends Remote {

	/**
	 * Constante com o nome do serviço apenas para conveniência, evitar erros de
	 * digitação.
	 */
	public static final String NOME = "SERVICO_RMI";

	/**
	 * Método que recebe o nome da pessoa e retorna uma saudação.
	 * 
	 * Todo método de uma interface usada com RMI deve declarar que pode lançar
	 * {@link java.rmi.RemoteException}.
	 * 
	 * @param nome
	 *            Nome da pessoa.
	 * @return Saudação.
	 * 
	 * @throws RemoteException
	 */
	public String saudar(String nome) throws RemoteException;

}