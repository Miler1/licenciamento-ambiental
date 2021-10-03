

ssh_get_password() {

	local ssh_server=$1
	local password=$(ssh_get_saved_password "$ssh_server")

	if [ -z "$password" ]; then

		password=$(ui_get_password "Senha do servidor: ")

		if $config_ssh_save_passwords; then

			ssh_save_password "$ssh_server" "$password"
		fi
	fi

	echo "$password"
}


ssh_save_password() {

	utils_create_saved_info_folder
	
	local server=$(cut -d ":" -f 1 <<< "$1")
	local pw_file="$SAVED_INFO_FOLDER_PATH/ssh_$server"

	rm -f $pw_file

	local encrypted=$(echo "$2" | openssl enc -aes-128-cbc -a -salt -pass "pass:243wsdf$USERNAMEs4fs45rt$server")

	touch $pw_file
	echo "$encrypted" >> $pw_file
}


ssh_get_saved_password() {

	local server=$(cut -d ":" -f 1 <<< "$1")
	local pw_file="$SAVED_INFO_FOLDER_PATH/ssh_$server"

	if [ ! -f  $pw_file ]; then
		echo ""
	else

		local decrypted=$(cat $pw_file | openssl enc -aes-128-cbc -a -d -salt -pass "pass:243wsdf$USERNAMEs4fs45rt$server")
		
		echo "$decrypted"
	fi
}

ssh_list_saved_passwords_servers() {

	ui_show_msg "Seguem os servidores para os quais a senha de acesso está salva neste computador:"
	echo ""
	local has_servers=false
	local files=$SAVED_INFO_FOLDER_PATH/ssh_*

	for f in $files
	do
		local filename="${f##*/}"
		local server=${filename#ssh_}

		if [ "$server" != "*" ]; then
			echo "      - $server"
			has_servers=true
		fi
	done

	if ! $has_servers; then
		echo "     Nenhum servidor encontrado."
	fi

	echo ""
}

ssh_clean_passwords() {

	if [ -z "$2" ]; then

		rm -f $SAVED_INFO_FOLDER_PATH/ssh_*

	else

		local server=$(cut -d ":" -f 1 <<< "$2")
		rm -f "$SAVED_INFO_FOLDER_PATH/ssh_$server"
	fi
}