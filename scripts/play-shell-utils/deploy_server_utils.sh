
prop_backend_folder_on_server=""


dp_get_server_default_declarations() {
	
	# quebra de linha
	local br="
	"
	
	echo "
export TERM=$TERM
export LINES=$LINES
export COLUMNS=$COLUMNS
config_msg_prefix_color=\"$config_msg_prefix_color\"
config_msg_prefix=\"$config_msg_prefix\"
color_default=\"$color_default\"

# Funções a serem disponibilizadas no servidor

$(cat $UTILS_SELF_DIR/ui_utils.sh)  $br
$(declare -f error)  $br
$(declare -f validate_cmd_result)  $br
$(declare -f dp_execute_sudo_command_on_server) $br
$(declare -f dp_remove_server_app_folder) $br
$(declare -f dp_before_start_app_on_server)  $br
$(declare -f dp_locate_app_backend_folder_on_server)  $br
$(declare -f dp_extract_release_file_on_server)  $br
$(declare -f dp_stop_application_on_server)  $br
$(declare -f dp_start_application_on_server)  $br
$(declare -f dp_backup_current_app_on_server)  $br
$(declare -f dp_update_app_on_server)  $br
$(declare -f dp_change_app_folder_owner_on_server)  $br
$(declare -f dp_execute_deploy_steps_on_server) $br
$(declare -f dp_show_deploy_success_msg_on_server) $br
$(declare -f dp_remove_release_file_on_server) $br
$(declare -f kill_process_by_path) $br
"

}


dp_get_server_variables_declarations() {

	echo "
config_update_server_app=$config_update_server_app
config_server_release_folder_path=\"$config_server_release_folder_path\"
config_server_app_path=\"$config_server_app_path\"
config_server_app_backup_folder_path=\"$config_server_app_backup_folder_path\"
config_server_app_start_cmd=\"$config_server_app_start_cmd\"
config_server_app_stop_cmd=\"$config_server_app_stop_cmd\"
config_server_app_folder_owner=\"$config_server_app_folder_owner\"
config_project_name=\"$config_project_name\"
config_clean_app_folder_on_deploy=$config_clean_app_folder_on_deploy
config_clean_app_subfolders_on_deploy=(${config_clean_app_subfolders_on_deploy[@]})
config_clean_app_folder_as_sudo=$config_clean_app_folder_as_sudo
config_server_force_kill_app_process=\"$config_server_force_kill_app_process\"
config_app_repository_server_download_release_url=\"$config_app_repository_server_download_release_url\"
config_server_enable_sudo_cmds=$config_server_enable_sudo_cmds
config_stop_start_app_as_sudo=$config_stop_start_app_as_sudo
prop_environment=\"$prop_environment\"
prop_version_name=\"$prop_version_name\"
prop_release_name=\"$prop_release_name\"
prop_dist_backend_folder_name=\"$prop_dist_backend_folder_name\"

	"
	
	if [ ! -z "$config_server_available_variables" ]; then

		for variable in "${config_server_available_variables[@]}"; do

			local variable_value="$(get_var $variable)"

			if [ ! -z "$variable_value" ]; then

				echo "$variable=\"$variable_value\" "
			fi
		done
	fi
}


dp_execute_sudo_command_on_server() {

	local as_sudo="$1"
	local empty_bool_msg="O primeiro parâmetro da função dp_execute_sudo_comando_on_server deve ser um booleano."

	if [ -z "$as_sudo" ]; then
		error "$empty_bool_msg"
	fi

	if [ $as_sudo != "true" ] && [ $as_sudo != "false" ]; then
		error "$empty_bool_msg"
	fi

	local cmd="${@:2}"
	
	if $config_server_enable_sudo_cmds && $as_sudo; then

		ui_show_msg "Executando comando: sudo $cmd"
		echo -e "$prop_server_password\n" | sudo -S $cmd

	else
		ui_show_msg "Executando comando: $cmd"
		$cmd
	fi
}


dp_locate_app_backend_folder_on_server() {

	cd "$config_server_app_path"
	prop_backend_folder_on_server="${PWD##*/}"
}


dp_extract_release_file_on_server() {

	ui_show_msg "Extraindo arquivo $prop_release_name"
	cd $config_server_release_folder_path
	rm -rf backend
	rm -rf $prop_dist_backend_folder_name
	unzip $prop_release_name
	validate_cmd_result "Não foi descompactar o pacote da inicializar a aplicação. "
}


dp_stop_application_on_server() {

	local cmd_stop_app="service $config_project_name stop"

	if [ ! -z "$config_server_app_stop_cmd" ]; then
		cmd_stop_app="$config_server_app_stop_cmd"
	fi
	
	ui_show_msg "Parando a execução da aplicação: $cmd_stop_app"
	dp_execute_sudo_command_on_server $config_stop_start_app_as_sudo $cmd_stop_app

	validate_cmd_result "Não foi possível parar a execução da aplicação no servidor."

	ui_show_msg "Aguarde..."

	sleep 5 # Aguardando a finalização do processo.

	error_msg="Não foi possível finalizar a execução da aplicação (arquivo $config_server_app_path/server.pid não foi removido).
    Finalize a aplicação manualmente, remova o arquivo server.pid e tente novamente."

	# Caso não tenha conseguido finalizar a aplicação, tentar matar o processo.
	if [ -f "$config_server_app_path/server.pid" ]; then

		if $config_server_force_kill_app_process; then

			local sudo_option=""
			local as_sudo=false

			if $config_server_enable_sudo_cmds && $config_stop_start_app_as_sudo; then
				sudo_option="-s"
				as_sudo=true
			fi

			kill_process_by_path $sudo_option "$prop_server_password" "$config_server_app_path"
			validate_cmd_result "$error_msg"
			
			dp_execute_sudo_command_on_server $as_sudo "rm $config_server_app_path/server.pid"

		else

			error "$error_msg"
		fi
	fi
}


dp_start_application_on_server() {

	local cmd_start_app="service $config_project_name start"

	if [ ! -z "$config_server_app_start_cmd" ]; then
		cmd_start_app="$config_server_app_start_cmd"
	fi

	ui_show_msg "Iniciando a execução da aplicação: $cmd_start_app"
	dp_execute_sudo_command_on_server $config_stop_start_app_as_sudo $cmd_start_app

	# echo -e "$prop_server_password\n" | sudo -S $cmd_start_app

	validate_cmd_result "Não foi possível inicializar a aplicação. Comando: sudo $cmd_start_app "
}


dp_backup_current_app_on_server() {

	if [ ! -z "$config_server_app_backup_folder_path" ]; then

		if [ ! -d $config_server_app_path ]; then

			ui_show_msg "A pasta da aplicação não existe: $config_server_app_path
     Ignorando etapa de backup."
	 		return 0
		fi

		echo ""
		local date_part=`date +%Y_%m_%d-%H_%M_%S`
		local bkp_file_name="$config_project_name-bkp-$date_part.tar.gz"

		cd "$config_server_app_path/../"
		
		ui_show_msg "Efetuando backup da aplicação ..."
		echo "     tar -czf $config_server_app_backup_folder_path/$bkp_file_name $prop_backend_folder_on_server"
		
		tar -czf "$config_server_app_backup_folder_path/$bkp_file_name" "$prop_backend_folder_on_server"
		validate_cmd_result "Não foi possível gerar o backup "
		ui_show_msg "Backup efetuado: $config_server_app_backup_folder_path/$bkp_file_name"
		cd -
	fi
}


dp_remove_server_app_folder () {

	local path="$1"
	ui_show_msg "Removendo a pasta $path"

	dp_execute_sudo_command_on_server $config_clean_app_folder_as_sudo "rm -Rf $path"
	validate_cmd_result "Não foi possível remover o diretório $path"
}


dp_update_app_on_server() {

	ui_show_msg "Atualizando a aplicação ..."

	if $config_clean_app_folder_on_deploy; then

		dp_remove_server_app_folder $config_server_app_path
		
		# Movendo pasta da release para o local da aplicação
		ui_show_msg "Criando nova pasta $config_server_app_path"
		mv "$config_server_release_folder_path/$prop_dist_backend_folder_name" "$config_server_app_path"
		validate_cmd_result "Não foi possível mover o diretório $config_server_release_folder_path/$prop_dist_backend_folder_name \
para $config_server_app_path"

	else

		# Removendo pastas segundo configuração
		if [ ! -z "$config_clean_app_subfolders_on_deploy" ]; then

			for folder_to_clean in "${config_clean_app_subfolders_on_deploy[@]}"; do

				dp_remove_server_app_folder $config_server_app_path/$folder_to_clean
			done
		fi

		# Sobrescrevendo arquivos da release na aplicação
		ui_show_msg "Copiando arquivos da release para a pasta $config_server_app_path"
		cp -Rf $config_server_release_folder_path/$prop_dist_backend_folder_name/* $config_server_app_path
		validate_cmd_result "Não foi possível copiar os arquivos da release para a pasta $config_server_app_path"
	fi
}


dp_change_app_folder_owner_on_server() {

	if [ ! -z "$config_server_app_folder_owner" ]; then

		ui_show_msg "Configurando o usuário $config_server_app_folder_owner como dono da pasta da \
aplicação e seus arquivos"

		chown -R $config_server_app_folder_owner $config_server_app_path

		validate_cmd_result "Não foi possível alterar o dono da pasta e seus arquivos.
     Pasta: $config_server_app_path 
     Grupo e Usuário: $config_server_app_folder_owner"
	 
	fi
}

dp_remove_release_file_on_server() {
	
	ui_show_msg "Removendo arquivo da release $config_server_release_folder_path/$prop_release_name"
	rm -f $config_server_release_folder_path/$prop_release_name
}

dp_execute_deploy_steps_on_server() {

	dp_locate_app_backend_folder_on_server

	dp_extract_release_file_on_server

	dp_stop_application_on_server

	dp_backup_current_app_on_server

	dp_update_app_on_server

	dp_change_app_folder_owner_on_server

	dp_before_start_app_on_server
	
	dp_start_application_on_server

	dp_remove_release_file_on_server
}


dp_update_ssh_server_app() {

	ui_show_msg "Atualizando aplicação do servidor ${prop_environment} ..."

	utils_require_command "sshpass"

	if [ -z "$prop_server_password" ]; then
		prop_server_password=$(ssh_get_password $config_server_ssh)
	fi

	sshpass -p "$prop_server_password" ssh -T $config_server_ssh << ATUALIZACAO

		$(dp_get_server_default_declarations)

		$(dp_get_server_variables_declarations)

		# Variáveis a serem disponibilizadas no servidor
		
		prop_server_password="$prop_server_password"

		dp_execute_deploy_steps_on_server

ATUALIZACAO
	
	validate_cmd_result "Não foi possível efetuar o deploy na aplicação no servidor $prop_environment."

	ui_show_msg "Aplicação do servidor ${prop_environment} efetuada com sucesso"
}

dp_show_deploy_success_msg_on_server() {

	local default_info="
     Projeto: $config_project_name
     Ambiente: $prop_environment
     Versão: $prop_version_name
     Release: $prop_release_name
     Servidor: $config_server_ssh
     Caminho da aplicação no servidor: $config_server_app_path"

	 ui_show_title "Deploy do projeto \"$config_project_name\" efetuado com sucesso."

	ui_show_msg "Informações:" $color_green
	echo "
     Projeto: $config_project_name
     Ambiente: $prop_environment
     Versão: $prop_version_name
     Release: $prop_release_name
     Caminho da aplicação no servidor: $config_server_app_path
	 
	 "

}


dp_export_server_deploy_scripts() {

	ui_show_msg "Exportando configurações do script de deploy para o ambiente ${prop_environment} ..."

	local error_msg="Não foi possível exportar as configurações de deploy para o pacote da release."

	local deploy_scripts_folder="$config_path_dist/$prop_dist_backend_folder_name/.deploy"

	mkdir $deploy_scripts_folder
	validate_cmd_result "$error_msg"

	rm -Rf "$deploy_scripts_folder/*"
	validate_cmd_result "$error_msg"

	echo "$(dp_get_server_variables_declarations)" >> $deploy_scripts_folder/configs.sh
	validate_cmd_result "$error_msg"

	echo "#!/bin/bash

		DEPLOY_SELF_DIR=\"\$( cd \"\$( dirname \"\${BASH_SOURCE[0]}\" )\" && pwd )\"
		
		source \$DEPLOY_SELF_DIR/configs.sh 
		
		$(dp_get_server_default_declarations)
		
		if \$config_server_enable_sudo_cmds; then
			prop_server_password=\"\$(ui_get_password \"Senha do servidor: \")\"
		fi

		dp_execute_deploy_steps_on_server

		dp_show_deploy_success_msg_on_server
		
	" >> $deploy_scripts_folder/deploy.sh

	validate_cmd_result "$error_msg"
	
	ui_show_msg "Configurações do script de deploy para o ambiente ${prop_environment} ..."
}

dp_show_update_app_on_server_help() {

	echo ""
	echo ""
	ui_show_msg "PRÓXIMOS PASSOS " $color_green
	
	local url_info=""
	
	if [ ! -z "$config_remote_app_server_url" ]; then
		url_info="URL: $config_remote_app_server_url"
	fi

	echo "
     - Acesse o servidor do ambiente \"$prop_environment\".
         $url_info
         $config_remote_app_server_extra_instructions
		 
     - Execute neste servidor o comando:
	 "
	
	ui_print "       deploy-play-app \"$config_app_repository_server_download_release_url/$prop_release_name\" \"$config_server_release_folder_path\"" $color_yellow


	echo "
	
     Obs.: caso o comando \"deploy-play-app\" ainda não esteja instalado no servidor, execute em sua máquina local o comando \"play-shel-utils --help-configure-remote-server\" para instruções."
}

dp_update_server_app() {

	if ! $config_use_app_repository_server; then
		
		dp_update_ssh_server_app
	fi
}