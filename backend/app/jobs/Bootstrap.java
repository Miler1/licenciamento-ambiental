package jobs;

import java.io.File;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.Configuracoes;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() throws Exception {

		validarDiretorioExistente(Configuracoes.ARQUIVOS_PATH);
	}
	
	private void validarDiretorioExistente(String path) {
		
		File file = new File(path);
		
		if (!file.exists() || !file.isDirectory())
			throw new RuntimeException("A pasta de caminho \"" + path + "\" não existe.");
	}
}
