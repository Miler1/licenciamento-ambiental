 var FormUtils = function() {
	// Objeto utilizado para auxiliar em tratamentos nos campos do formulário.
	// Por exemplo, para setar todos os campos como dirty, para que as mensagens
	// de erro sejam exibidas, ou então ocultar todas.

	// Exemplo:
	// var formUtils = new app.FormUtils($scope, "form);
	// O Segundo parâmetro é o nome do formulário (atributo name) no html.  Caso não
	// seja informado, o padrão é "form".
	this.initialize = function(scope, formName) {

		this.scope = scope;
		this.formName = formName || "form";
		//this.formElement = $(form.$name)[0];
		this._fields = null;
	};

	this.getForm = function() {

		return this.scope[this.formName];
	};

	// Configura todos os elementos do formulário adicionando a classe "ng-dirty"
	// e removendo a classe "ng-pristine". Também altera o atributos do objeto do
	// form, o equivalente "form.meucampo.$dirty = true" e
	// "form.meucampo.$pristine = false".
	this.setDirty = function(fieldsNames) {

		this._eachField(function(field) {
				setDirtyAttribute(field, true);
			},
			fieldsNames);
	};

	// Faz o oposto da função "cleanDirty".
	this.cleanDirty = function(fieldsNames) {

		this._eachField(function(field) {
				setDirtyAttribute(field, false);
			},
			fieldsNames);
	};

	this.cleanCustomValidations = function(fieldsNames) {

		var self = this;

		this._eachField(function(field) {

				for (var err in field.model.$error) {

					if (isCustomValidation(err) && field.model.$error[err]) {
						field.model.$setValidity(err, true);
					}
				}
			},
			fieldsNames);
	};

	this.validate = function() {

		var fields = this.getFields();

		for (var i in fields) {

			if (!fields[i].model.$valid)
				return false;
		}

		return true;
	};

	this.getFields = function() {

		if (!this._fields)
			this._loadFields();

		return this._fields;
	};

	// Carrega os campos em um objeto, mapeando-os pelo nome (atributo "name" no html).
	// Exemplo de como é armazenado o campo cpf:
	// this._fields["cpf"] = { elem: <elementoDOM>, model: <model do form no escopo> }.
	this._loadFields = function() {

		this._fields = {};
		var form = this.getForm();

		for (var prop in form) {

			if (form.hasOwnProperty(prop) && prop.indexOf("$") !== 0)
				this._addElement(form, prop);
		}
	};

	this._addElement = function(form, name) {

		this._fields[name] = {
			elem: $("[name=" + name + "]"),
			model: form[name]
		};
	};

	// Executa uma função para cada objeto contido em this._fields.
	// Pode-se passar um array com o nome dos campos a serem utilizados,
	// caso não seja passado, todos os campos são utilizados.
	this._eachField = function(func, fieldsNames) {

		var i = 0,
			fields = this.getFields();

		if (fieldsNames) {

			for (i = 0; i < fieldsNames.length; i++) {
				func(fields[fieldsNames[i]]);
			}
		} else {

			for (i in fields) {
				func(fields[i]);
			}
		}
	};

	this.setDirtyAttribute = function(field, dirty) {

		field.elem.removeClass((dirty) ? "ng-pristine" : "ng-dirty");
		field.elem.addClass((dirty) ? "ng-dirty" : "ng-pristine");
		field.model.$dirty = dirty;
		field.model.$pristine = !dirty;
	};

	this.isCustomValidation = function(validation) {
		return app.CustomValidations.indexOf(validation) >= 0;
	};

 };

 exports.utils.FormUtils = FormUtils;