#!/bin/bash

prop_dist_backend_folder_name=""
prop_path_dist_backend=""
prop_environment=""
prop_version_name=""
prop_release_name=""
prop_release_path=""
prop_server_password=""


dp_show_title() {
	
	echo ""
	echo ""
	ui_print_line $color_green

	ui_print "     ____             _             " $color_green
	ui_print "    |  _ \  ___ _ __ | | ___  _   _ " $color_green
	ui_print "    | | | |/ _ \ '_ \| |/ _ \| | | |" $color_green
	ui_print "    | |_| |  __/ |_) | | (_) | |_| |" $color_green
	ui_print "    |____/ \___| .__/|_|\___/ \__, |" $color_green
	ui_print "               |_|            |___/ " $color_green
	echo ""
	ui_print "    Script de deploy para aplicações Play!" $color_green

	echo ""
	ui_print_line $color_green
}


dp_show_deploy_help() {

	dp_show_deploy_help_section "config_project" "CONFIGURAÇÕES DO PROJETO"

	dp_show_deploy_help_section "config_build" "CONFIGURAÇÕES DE BUILD"

	dp_show_deploy_help_section "config_release_version" "CONFIGURAÇÕES DA VERSÃO DA RELEASE"

	dp_show_deploy_help_section "config_server" "CONFIGURAÇÕES DE SERVIDOR"

	dp_show_deploy_help_section "config_play" "CONFIGURAÇÕES DO PLAY FRAMEWORK"

}


dp_show_configure_remote_server_help() {

	echo "
    Em situações onde o servidor que contém a aplicação não é acessível via ssh,
    é necessário efetuar o build da aplicação na máquina local e efetuar a 
    atualização acessando diretamente o servidor. Neste caso, é necessário
    configurar o \"deploy-play-app\" no servidor da aplicação. Este utilitário
    é capaz de efetuar os passos restantes para atualizar a aplicação e deve ser
    executado diretamente no servidor. Para configurá-lo é necessário:

        - Copiar o arquivo \"deploy-play-app\" contido no play-shell-utils para
          servidor, colocando no caminho /usr/bin/deploy-play-app;

        - Dar permissão de execução neste arquivo.

    Após estes dois passos o comando \"deploy-play-app\" estará disponível no
    servidor.
    "
}


dp_show_deploy_help_section() {

	echo -e "\n"
	ui_print "----- $2 -----" $color_green
	echo -e "\n\n"
	doc_show_source_info "$UTILS_SELF_DIR/configs.sh" "$1"
}


dp_init() {

	HELP_TEXT="
	Uso correto: `basename $0` <ambiente>
	
	Obs: os ambientes estão configurados em arquivos de nome scripts/conf/nome_do_ambiente.sh"

	dp_show_title
	validate_required_arguments "$1"
	prop_environment=$1
	prop_dist_backend_folder_name="dist"
	prop_path_dist_backend="$config_path_dist/$prop_dist_backend_folder_name"
	prop_include_deploy_configs_in_dist=$config_use_app_repository_server

	dp_load_environment_configs

	if $config_update_server_app; then
		config_copy_release_to_server=true
	fi

	prop_include_deploy_configs_in_dist=$config_use_app_repository_server
	
	dp_get_version_name

	dp_clean_backend

	echo ""
	ui_print " >>  Informações do deploy a ser executado:" $color_green
	echo ""
	ui_print "     Ambiente = $prop_environment" $color_green
	ui_print "     Versão = $prop_version_name" $color_green
	ui_print "     Pasta raiz do projeto = $ROOT_FOLDER" $color_green
	echo ""
}


# Carrega as configurações relativas ao ambiente de deploy. O padrão de caminho
# para este arquivo está contido na configuração "config_environment_config_path".
# Parâmetros: $1 - Ambiente
dp_load_environment_configs() {

	local real_path=${config_environment_config_path//<env>/$prop_environment}

	ui_show_msg "Carregando configurações do ambiente \"$prop_environment\" a partir do arquivo: $real_path"

	if [ ! -f $real_path ]; then

		error_help "Ambiente inválido. Não existe o arquivo de configuração '$real_path'."
	fi

	source "$real_path"
}


# Remove as pastas utilizadas para geração da release.
dp_clean_backend() {
	
	ui_show_msg "Removendo pastas antigas de deploy ..."

	if [ -d $config_path_dist ]; then 
		rm -Rf $config_path_dist
	fi

	mkdir $config_path_dist
	mkdir $prop_path_dist_backend
	rm -Rf $config_path_backend/precompiled
}


dp_copy_backend_static_dirs() {

	ui_show_msg "Copiando pastas estáticas do backend ..."

	for path in "${config_backend_static_folders[@]}"; do

		local full_path="$config_path_backend/$path"

		if [ -d "$full_path" ] || [ -f "$full_path" ]; then

			ui_show_msg "$full_path -> $prop_path_dist_backend"
			cp -Rf $full_path $prop_path_dist_backend
		fi
	done
}

# Precompilando o codido e movendo para a pasta dist/precompiled
# Copiando também o codigo dos templates para dist/app/view (devido a erro do play 1.2.*)
dp_precompile_backend() {

	cd $config_path_backend

	play_configure_command

	ui_show_msg "Precompilando Java ..."

	$PLAY_CMD precompile
	local result=$?

	if [ $result -ne 0 ]; then
		error "Erro ao compilar código do backend. "
	fi

	cd $ROOT_FOLDER
	
	mkdir $prop_path_dist_backend/precompiled
	cp -Rf $config_path_backend/precompiled/java $prop_path_dist_backend/precompiled/java
	mkdir $prop_path_dist_backend/app
	cp -Rf $config_path_backend/app/views $prop_path_dist_backend/app
	cp -Rf $config_path_backend/app/templates $prop_path_dist_backend/app

	# Verifica se todos os arquivos estáticos de /backend/app foram copiados para /dist/app
	dp_validate_backend_copied_static_files "$config_path_backend/app" "$prop_path_dist_backend/app"

	ui_show_msg "Precompilação efetuada com sucesso."

	if $config_backend_encrypt_code; then

		# Criptografando codigo fonte do backend.
		dp_encrypt_backend_source_code

	else

		# Senão não criptografar o código, remover classe do classloader.
		rm -f $prop_path_dist_backend/precompiled/java/plugins/a.class
		rm -f $prop_path_dist_backend/precompiled/java/plugins/a\$b.class
	fi

	# Remover a classe não ofuscada do classloader
	rm -f $prop_path_dist_backend/precompiled/java/plugins/ClassLoaderPlugin.class
	rm -f $prop_path_dist_backend/precompiled/java/plugins/ClassLoaderPlugin\$ClassLoader.class
}


# Verifica e retorna os nomes dos arquivos estáticos (que não são .java) que estão
# na pasta $1 e não estão presentes na pasta $3. Este é um médodo recursivo que deve
# receber o mesmo valor nos parâmetros $1 e $2.
dp_list_backend_not_existing_static_files () {

	for file in $2/*	# Para cada arquivo na pasta
	do
		if [ -d "$file" ]; then		# Se for uma pasta, fazer chamada recursiva

			dp_list_backend_not_existing_static_files $1 $file $3

		elif [ -f "$file" ] && [[ ! $file == *.java ]]; then	# Se for um arquivo e não terminar com ".java"

			local file_relativo=${file#$1}

			if [ ! -f "$3$file_relativo" ]; then	# Se o arquivo não existir na pasta $2

				echo -e "$1$file_relativo "
			fi
		fi
	done
}


# Valida se todos os arquivos estáticos (que não são .java) ques estão na pasta $1
# também estão na pasta $2. Validação necessária pois, ao utilizar a precompilação,
# o Play não copia os arquivos estáticos da pasta "app" para a pasta "precompiled/java".
dp_validate_backend_copied_static_files () {

	local nomes_arquivos=$(dp_list_backend_not_existing_static_files $1 $1 $2)

	if [[ -z "$nomes_arquivos" ]]; then
		return 0	# Nenhum arquivo estático pendente
	fi

	ui_show_error "\n\nERRO: Os seguintes arquivos estáticos não foram copiados para $2:\n"

	local arr=$(echo $nomes_arquivos | tr " " "\n" )

	for x in $arr; do
		echo "  > $x"
	done

	echo -e "\n\nERRO: Deploy não gerado.\n\n"

	exit $E_BADARGS
}


dp_encrypt_backend_source_code() {

	ui_show_msg "Criptografando o código fonte do backend ..."

	# Criptografando as classes java, exceto o classloader customizado

	echo ""
	
	mv $prop_path_dist_backend/precompiled $prop_path_dist_backend/bkp_precompiled
	
	java -jar $CLASS_ENCRYPTION_JAR -i $prop_path_dist_backend/bkp_precompiled/java/ -o $prop_path_dist_backend/precompiled/java -key $config_backend_code_encryption_key -ignore "a.class,a\$b.class,*package-info.class"
	local result=$?

	if [ $result -ne 0 ]; then
		error "Erro ao criptografar código fonte do backend. "
	fi

	rm -Rf $prop_path_dist_backend/bkp_precompiled

	# Adicionar configuração de plugin para classloader customizado
	echo "" >> $prop_path_dist_backend/conf/play.plugins
	echo "1:plugins.a" >> $prop_path_dist_backend/conf/play.plugins
	echo ""

	ui_show_msg "Código fonte do backend criptografado com sucesso."
}


dp_set_backend_configs() {

	local app_conf="$prop_path_dist_backend/conf/application.conf"
	cd $prop_path_dist_backend/conf
	
	if $config_backend_include_app_conf; then

		local real_include=${config_backend_app_conf_include_path//<env>/$prop_environment}

		ui_show_msg "Incluindo arquivo de configuração do backend: '@include.production = $real_include'"
		

		if [ ! -f "$real_include" ]; then
			error "Arquivo de configuração conf/$real_include não encontrado."
		fi

		echo "" >> $app_conf
		echo "@include.production = $real_include" >> $app_conf
	fi

	local current_date=$(date +'%d/%m/%Y %H:%M')
	local git_user_name="$(git_get_user_name)"
	local git_user_email="$(git_get_user_email)"
	local mac_address="$(net_get_local_mac_address)"

	echo "" >> $app_conf
	

	if [ ! -z "$config_backend_version_property" ]; then
		echo "$config_backend_version_property=$prop_version_name" >> $app_conf
	fi

	if [ ! -z "$config_backend_update_property" ]; then
		echo "$config_backend_update_property=$current_date" >> $app_conf
	fi

	if [ ! -z "$config_backend_deploy_user_gitname_property" ]; then
		echo "$config_backend_deploy_user_gitname_property=$git_user_name" >> $app_conf
	fi

	if [ ! -z "$config_backend_deploy_user_gitemail_property" ]; then
		echo "$config_backend_deploy_user_gitemail_property=$git_user_email" >> $app_conf
	fi

	if [ ! -z "$config_backend_deploy_user_mac_address_property" ]; then
		echo "$config_backend_deploy_user_mac_address_property=$mac_address" >> $app_conf
	fi

	cd $ROOT_FOLDER
}


dp_copy_modules_source_code() {

	ui_show_msg "Copiando o código fonte dos módulos."

	if [ ! -d "$prop_path_dist_backend/modules" ]; then 

		ui_show_msg "Nenhum módulo encontrado"
		return 
	fi

	modules_list=($(ls "$prop_path_dist_backend/modules"))

	for path in "${modules_list[@]}"; do

		if [ -f  "$prop_path_dist_backend/modules/$path" ]; then
			
			local source_path=$( head -1 "$prop_path_dist_backend/modules/$path" )

			echo "    - $path : copiando módulo de '$source_path'."

			if [ ! -d  "$source_path" ]; then

				error "A pasta '$source_path', apontada pelo arquivo modules/$path não foi encontrada."
			fi

			rm "$prop_path_dist_backend/modules/$path"
			cp -Rf "$source_path" "$prop_path_dist_backend/modules"
			
		else
			echo "    - $path : módulo não copiado pois já é um diretório"
		fi
		
	done
}


dp_get_version_name() {

	local last_tag=$(git describe --tags --abbrev=0 2>/dev/null)

	if $config_require_version_name; then

		if [ -z "$last_tag" ]; then
			ui_show_msg "Informe o número / nome da versão gerada"
		else
			ui_show_msg "Informe o número / nome da versão gerada (obs: a última tag local é \"$last_tag\")"
		fi
		
		prop_version_name=$(ui_get_input "Versão: ")

	elif $config_use_branch_as_version_name; then

		 prop_version_name=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')

	elif $config_use_last_tag_as_version_name; then

		if [ -z "$last_tag" ]; then

			error "O projeto foi configurado para utilizar a última tag como versão, mas não há nenhuma tag criada. \
Observe a configuração \"config_use_last_tag_as_version_name\"."

		fi

		prop_version_name="$last_tag"
	fi
}


dp_build_backend() {

	ui_show_msg "Efetuando o build do backend ..."

	if $config_backend_encrypt_code; then
		config_backend_precompile=true
	fi

	dp_copy_backend_static_dirs

	if $config_backend_precompile; then

		dp_precompile_backend

	else

		ui_show_msg "Copiando codigo fonte ..."
		cp -Rf $config_path_backend/app $prop_path_dist_backend
	fi

	dp_set_backend_configs

	if $config_backend_copy_modules_source_code; then
		dp_copy_modules_source_code
	fi

	ui_show_msg "Build do backend finalizado."
}


dp_get_frontend_task_runner() {

	local path="$1"

	if [ -f "$path/Gulpfile.js" ]; then

		echo "gulp"

	elif [ -f "$path/Gruntfile.js" ]; then	

		echo "grunt"

	else

		error "Não foram encontrados os Gruntfile.js nem Gulpfile.js."
	fi
}


dp_build_frontend() {

	ui_show_msg "Efetuando o build do frontend ..."

	local frontend_path=""

	if [ -z "$1" ]; then
		frontend_path="$config_path_frontend"
	else
		frontend_path="$1"
	fi

	validate_files_exist "$frontend_path"

	cd $frontend_path
	task_cmd=""

	if [ -z "$config_compile_frontend_cmd" ]; then

		local frontend_task_runner="$(dp_get_frontend_task_runner $frontend_path)"

		local config_cmd="config_${frontend_task_runner}_cmd"
		
		task_cmd=${!config_cmd}

		if [ -z "$task_cmd" ]; then
			task_cmd="$frontend_task_runner $prop_environment"
		fi	
	else
		task_cmd="$config_compile_frontend_cmd"
	fi

	ui_show_msg "Executando comando \"$task_cmd\" ..."
	echo -e "\n\n"
	$task_cmd
	validate_cmd_result "Erro ao executar comando \"$task_cmd\"."

	ui_show_msg "Build do frontend finalizado."

	cd $ROOT_FOLDER
}


dp_generate_release_file_name() {

	if [ -z "$config_project_name" ]; then
		error "Para gerar a release é necessário configurar a variável \"config_project_name\"."
	fi

	prop_release_name="release-$config_project_name-$prop_environment"

	if [ ! -z "$prop_version_name" ]; then

		local release_version_part="${prop_version_name//'.'/'_'}"
		prop_release_name="$prop_release_name-$release_version_part"
	fi	

	local date_part=`date +%Y_%m_%d-%H_%M_%S`

	prop_release_name="$prop_release_name-$date_part.zip"
	prop_release_path="$config_path_dist/$prop_release_name"

	ui_show_msg "Nome do arquivo da release: $prop_release_name"
}


dp_compact_release() {

	dp_generate_release_file_name

	if $prop_include_deploy_configs_in_dist; then

		dp_export_server_deploy_scripts
	fi
	
	cd $config_path_dist

	zip -q -r $prop_release_path $prop_dist_backend_folder_name
	validate_cmd_result "Erro ao compactar release."

	ui_show_msg "Arquivo da release gerado: $prop_release_path"
}


dp_copy_release_to_server() {

	if $config_use_app_repository_server; then
		local server_name="repositório"
		local server_ssh="$config_app_repository_server_ssh"
		local server_release_folder_path="$config_app_repository_server_release_folder_path"
	else
		local server_name="${prop_environment}"
		local server_ssh="$config_server_ssh"
		local server_release_folder_path="$config_server_release_folder_path"
	fi

	ui_show_msg "Enviando release para o servidor ${server_name}
     SSH servidor:  $server_ssh
     Pasta destino: $server_release_folder_path"

	utils_require_command "sshpass"

	validate_required "$server_ssh" "$server_release_folder_path" \
		"Para enviar uma versão para o servidor é necessário configurar as variáveis \"server_ssh\" e \"server_release_folder_path\" para o servidor $prop_environment."
	
	prop_server_password=$(ssh_get_password $server_ssh)

	ui_show_msg "Executando \"scp $prop_release_path $server_ssh:$server_release_folder_path\""

	sshpass -p "$prop_server_password" scp "$prop_release_path" "$server_ssh:$server_release_folder_path"

	validate_cmd_result "Erro ao enviar release para o servidor."

	ui_show_msg "Release enviada para o servidor com sucesso."
}


dp_show_deploy_success_msg() {
	
	local default_info="
     Projeto: $config_project_name
     Ambiente: $prop_environment
     Versão: $prop_version_name
     Release: $prop_release_name"

	local server_app_info="
     Servidor: $config_server_ssh
     Caminho da aplicação no servidor: $config_server_app_path"
	
	local server_release_info="
     Servidor: $config_server_ssh
     Release no servidor: $config_server_release_folder_path/$prop_release_name"
	
	local gateway_info="
     Servidor: $config_app_repository_server_ssh
     Release no servidor: $config_app_repository_server_release_folder_path/$prop_release_name"

    local local_info="
     Caminho: $prop_release_path"
	 
	# Atualização direta no servidor.
	if $config_update_server_app && ! $config_use_app_repository_server; then

		ui_show_title "Deploy do projeto \"$config_project_name\" efetuado com sucesso."

		ui_show_msg "Informações gerais:" $color_green
		echo "$default_info"

		ui_show_msg "Informações do servidor:" $color_green
		echo "$server_app_info"

	# Envio da release para servidor para execução de próximos passos no próprio servidor.
	elif $config_copy_release_to_server && $config_use_app_repository_server; then

		ui_show_title "Release do projeto \"$config_project_name\" enviada com sucesso para o servidor."

		ui_show_msg "Informações gerais:" $color_green
		echo "$default_info"

		ui_show_msg "Informações do servidor: " $color_green
		echo "$gateway_info"

		dp_show_update_app_on_server_help

	# Envio da release para servidor para deploy manual.
	elif $config_copy_release_to_server; then
		
		ui_show_title "Release do projeto \"$config_project_name\" enviada com sucesso para o servidor."
		
		ui_show_msg "Informações gerais:" $color_green
		echo "$default_info"
		
		ui_show_msg "Informações do servidor: " $color_green
		echo "$server_release_info"

	# Geração da release apenas
	else

		ui_show_title "Release do projeto \"$config_project_name\" gerada com sucesso com sucesso."

		ui_show_msg "Informações gerais: " $color_green
		echo "$default_info $local_info"

	fi

	echo ""
	echo ""
	echo ""
}


dp_execute_deploy_procedure() {

	dp_init "$1"

	dp_before_build_frontend

	if $config_build_frontend; then
		
		dp_build_frontend

		dp_after_build_frontend
	fi

	dp_before_build_backend
	
	dp_build_backend

	dp_after_build_backend
	
	dp_compact_release

	dp_after_compact_release

	if $config_copy_release_to_server; then

		dp_copy_release_to_server
	fi

	if $config_update_server_app; then

		dp_update_server_app
	fi

	dp_show_deploy_success_msg
}


### Funções a serem sobrescritas pelo usuário.


dp_before_build_frontend() {
	echo ""
}


dp_after_build_frontend() {
	echo ""
}


dp_before_build_backend() {
	echo ""
}


dp_after_build_backend() {
	echo ""
}


dp_after_compact_release() {
	echo ""
}


dp_before_start_app_on_server() {
	echo ""
}

