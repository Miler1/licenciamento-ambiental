
net_get_local_mac_address() {

	local interfaces="$(net_get_interfaces)"

	if [ -z "$interfaces" ]; then
		return 0
	fi

	interfaces=($interfaces)

	for interface in "${interfaces[@]}"; do

		# Ignorando interfaces docker
		if ! echo "$interface" | grep -q "docker"; then
			
			local mac="$(net_get_interface_mac_address $interface)"

			if [ ! -z "$mac" ]; then

				echo "$mac"
				return 0
			fi
		fi
	done
}


net_get_interfaces() {

	ip addr show | awk '/inet.*brd/{print $NF}' &> /dev/null

	if [ $? -ne 0 ]; then
		return 0
	fi

	ip addr show | awk '/inet.*brd/{print $NF}'
}


net_get_interface_mac_address() {

	local interface="$1"

	ip link show eno1 | awk '/ether/ {print $2}' &> /dev/null

	if [ $? -ne 0 ]; then
		return 0
	fi

	ip link show eno1 | awk '/ether/ {print $2}'
}
