package models.caracterizacao;

import beans.DadosSobreposicaoVO;
import com.vividsolutions.jts.geom.Geometry;

import java.util.List;

public interface GuardaSobreposicao {

    public List getListaSobreposicao();

    public void setListaSobreposicao(List sobreposicoes);

    public <T extends SobreposicaoDistancia> T getObjetoSobreposicao
            (DadosSobreposicaoVO dadosSobreposicao, GuardaSobreposicao guardaSobreposicao, TipoSobreposicao tipoSobreposicao);

}
