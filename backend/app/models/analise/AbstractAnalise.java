package models.analise;

import play.db.jpa.GenericModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAnalise extends GenericModel {

    public abstract List<? extends ParecerAnalista> getPareceresAnalistas();

    public abstract void setParecer(String parecer);

    public abstract void setDocumentos(List<Documento> documentos);

    void initParecerNotificacao(){
        Optional<? extends ParecerAnalista> parecerGerenteOp = getPareceresAnalistas().stream()
                .filter(p -> p.getTipoResultadoAnalise().id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO))
                .max(Comparator.comparing(ParecerAnalista::getId));

        this.setParecer(parecerGerenteOp.isPresent() ? parecerGerenteOp.get().getParecer() : "");
        this.setDocumentos(parecerGerenteOp.map(ParecerAnalista::getDocumentosNotificacao).orElse(Collections.emptyList()));
    }
}
