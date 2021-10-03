
verify_not_empty () {

	for value in "$@"
	do

		if [ -z "$value" ]; then
			return 1
		fi
	done

	return 0
}

validate_required () {

	local msg="${!#}"

	for arg in "$@"
	do
	    if [ "$arg" != "$msg" ]; then

	    	if [ -z "$arg" ]; then

	    		error "$msg"
	    	fi
	    fi
	done
}

validate_required_arguments () {

	ui_show_msg "Validando parâmetros ..."

	if ! verify_not_empty "$@"; then
		
		error_help "Argumentos inválidos"
	fi
}


validate_files_exist () {

	for dir in "$@"
	do
		if [ ! -d "$dir" ] && [ ! -f "$dir" ]; then

			error "O dirétorio/arquivo não existe: $dir"
		fi
	done
}


validate_cmd_result() {

	local result=$?
	
	if [ $result -ne 0 ]; then
		error "$1"
	fi
}


get_var () {

	local var_name=$1
	local var="${!var_name}"

	echo $var
}

kill_process_by_path() {

	local path="$1"
	local use_sudo=false
	local sudo_password=""
	
	if [ "$1" == "-s" ]; then
		use_sudo=true
		sudo_password="$2"
		path="$3"
	fi

	local process_ids=($(ps aux | grep $path | grep -v grep | awk '{print $2}'))
	local number_of_process=${#process_ids[@]}

	if [ $number_of_process -ne 1 ]; then

		if [ $number_of_process -gt 1 ]; then

			ui_show_error "Não foi possível identificar um único processo referente ao path $path.
    Foram encontrados $number_of_process processos."

		else

			ui_show_error "Não foi possível identificar nenhum processo referente ao path $path"
		fi

		return 1
	fi
	
	local id=${process_ids[0]}
	local result=""

	ui_show_msg "Forçando a finalização do processo $id."
	
	if $use_sudo; then

		echo -e "$sudo_password\n" | sudo -S kill -9 $id
		result=$?

	else
	
		kill -9 $id
		result=$?
	fi

	return $result
}
