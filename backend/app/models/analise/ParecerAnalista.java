package models.analise;

import java.util.List;

public interface ParecerAnalista {

    Long getId();

    TipoResultadoAnalise getTipoResultadoAnalise();

    String getParecer();

    List<Documento> getDocumentosNotificacao();

}
