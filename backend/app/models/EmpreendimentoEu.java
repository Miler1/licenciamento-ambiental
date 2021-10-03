package models;

import br.ufla.lemaf.beans.Empreendimento;
import br.ufla.lemaf.beans.pessoa.Estado;
import br.ufla.lemaf.beans.pessoa.EstadoCivil;
import br.ufla.lemaf.beans.pessoa.Localizacao;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geolatte.geom.GeometryType;

import java.util.List;

public interface EmpreendimentoEu {

	public Endereco getEnderecoPrincipal();

	public Endereco getEnderecoCorrespondencia();

	public Municipio getMunicipio();

	public TipoLocalizacao getLocalizacao();

	public TipoLocalizacao getTipoLocalizacao();

	public Estado getEstado();

	public List<Pessoa> getResponsaveisTecnicos();

	public List<br.ufla.lemaf.beans.pessoa.Pessoa> getProprietarios();

	public List<br.ufla.lemaf.beans.pessoa.Endereco> getEnderecos(List<br.ufla.lemaf.beans.pessoa.Endereco> enderecos,List<br.ufla.lemaf.beans.pessoa.Endereco> enderecosFormatados);

	public Geometry getGeometry();

	public Geometry getLimiteMunicipio();

	public Geometry getLimiteEstado();

	public Point centroid();

	public String getGeometryType();

	public Empreendimento getEmpreendimentoEU();

	public String getDescricao(int max, Boolean comCep);

}
