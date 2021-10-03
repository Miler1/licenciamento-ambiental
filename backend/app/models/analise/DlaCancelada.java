package models.analise;

import exceptions.AppException;
import models.Documento;
import models.EmailNotificacaoCancelamentoDla;
import models.UsuarioLicenciamento;
import models.caracterizacao.DispensaLicenciamento;
import models.caracterizacao.StatusCaracterizacao;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Entity
@Table(schema="licenciamento", name="dispensa_licencamento_cancelada")
public class DlaCancelada extends GenericModel {

	public static final String SEQ = "licenciamento.dispensa_licencamento_cancelada_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name="id_dispensa_licencamento")
	public DispensaLicenciamento dispensaLicenciamento;

	@ManyToOne
	@JoinColumn(name="id_usuario_executor")
	public UsuarioLicenciamento usuario;

	@Column(name="data_cancelamento")
	public Date dataCancelada;

	@Required
	public String justificativa;

	public DlaCancelada () {

	}

	public void cancelarDla(UsuarioLicenciamento usuarioExecutor) {

		Calendar c = Calendar.getInstance();
		Date dataAtual = c.getTime();

		DispensaLicenciamento dispensaLicenciamento = DispensaLicenciamento.findById(this.dispensaLicenciamento.id);
		dispensaLicenciamento.ativo = false;

		this.dispensaLicenciamento = dispensaLicenciamento;
		this.usuario = usuarioExecutor;
		this.dataCancelada = dataAtual;

		Validacao.validar(this);

		dispensaLicenciamento._save();

		this.save();

		dispensaLicenciamento.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.CANCELADO);
		dispensaLicenciamento.caracterizacao._save();

		dispensaLicenciamento.caracterizacao = dispensaLicenciamento.caracterizacao.refresh();

		try {

			Documento documento = dispensaLicenciamento.documento;
			dispensaLicenciamento.gerarPDFDispensa();
			documento.delete();

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_PROCESSAR_DLA);
		}

		try {

			enviarNotificacaoCanceladoPorEmail();

		} catch(Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
		}

	}

	private void enviarNotificacaoCanceladoPorEmail() {

		Set<String> destinatarios = this.dispensaLicenciamento.caracterizacao.empreendimento.getEmailsProprietariosECadastrantesEU();

		if(destinatarios.size() > 0) {
			EmailNotificacaoCancelamentoDla emailNotificacao = new EmailNotificacaoCancelamentoDla(this, destinatarios);
			emailNotificacao.enviar();
		}
	}

}
