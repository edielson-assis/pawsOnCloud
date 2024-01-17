package br.com.pawsoncloud.email;

public class Email {

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