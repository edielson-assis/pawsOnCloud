package br.com.pawsoncloud.email;

/**
 * Classe responsável por construir o email.
 * 
 * @author Edielson Assis
 */
public class Email {

    /** 
     * Método estático que contém a mensagem de email que será enviada para o usuário na validação dos dados.
     * @param nome nome do destinatário.
     * @param link link de ativação.
     * @return mensagem
     */
    public static String construirEmail(String nome, String link) {
        return String.format("""
                <div>
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <p>%s,</p>
                                    <p>clique no link abaixo para ativar a sua conta na plataforma:</p>
                                    <p><a href="%s">Ativar agora</a><br></p>
                                    <p>O link vai expirar em 15 minutos. Caso não tenha feito essa solicitação, ignore esse email.</p>
                                    <p>Atenciosamente, <br>Equipe PawsOnCloud</p>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                """, nome, link);
    }
}