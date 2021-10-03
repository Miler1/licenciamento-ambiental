package models;

import play.data.validation.Validation;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import javax.validation.ValidationException;

@Entity
@Table(schema = "licenciamento", name = "usuario_licenciamento")
public class UsuarioLicenciamento extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_licenciamento_id_seq")
    @SequenceGenerator(name = "usuario_licenciamento_id_seq", sequenceName = "licenciamento.usuario_licenciamento_id_seq", allocationSize = 1)
    public Long id;

    public String login;

    @Override
    public UsuarioLicenciamento save() {

        validate();

        super.save();

        return super.refresh();
    }

    private void validate() {

        Validation validation = Validation.current();
        validation.clear();
        validation.valid(this);

        if (validation.hasErrors())
            throw new ValidationException(validation.errors().toString());

    }

    public static UsuarioLicenciamento findByLogin(String login) {

        return UsuarioLicenciamento.find("login", login).first();

    }

}
