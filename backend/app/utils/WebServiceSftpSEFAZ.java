package utils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import play.Play;

import java.io.File;
import java.io.IOException;

public class WebServiceSftpSEFAZ {

    private static final String REMOTE_HOST = Play.configuration.getProperty("sefaz.processamento.remote.host.externo");
    private static final String USER = Play.configuration.getProperty("sefaz.processamento.user");
    private static final String PASSWORD = Play.configuration.getProperty("sefaz.processamento.password");
    private static final String REMOTE_FILE = Play.configuration.getProperty("sefaz.processamento.password");
    private final static String APPLICATION_TEMP_FOLDER =
            Play.applicationPath + File.separator + Play.configuration.getProperty("path.arquivos.tempFolder")+"DAE/";

    private SSHClient setupSshj() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(REMOTE_HOST);
        client.authPassword(USER, PASSWORD);
        return client;
    }

    public void whenDownloadFileUsingSshj_thenSuccess(String fileName) throws IOException {
        SSHClient sshClient = setupSshj();
        SFTPClient sftpClient = sshClient.newSFTPClient();

        File diretorio = new File(APPLICATION_TEMP_FOLDER);
        if(!diretorio.exists()) diretorio.mkdirs();

        sftpClient.get("/home/sema/pagamento_dar/5020/" + fileName, APPLICATION_TEMP_FOLDER + fileName);

        sftpClient.close();
        sshClient.disconnect();
    }

}
