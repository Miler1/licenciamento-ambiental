#!/bin/bash

# DOC_PATTERN='\ *#+\ *@([a-zA-Z_]+)[\(]{1}([a-zA-Z_]+)[\)]{1}\ *(.*)'
DOC_PATTERN='\ *#+\ *@([a-zA-Z_]+)\((.+)\)\ *(.*)'
DOC_TEST_PATTERN=^"$DOC_PATTERN"$
DOC_TITLE_PATTERN='\ *([a-zA-Z_]+),*.*'
DOC_DATA_TYPE_PATTERN='.*,\ *type:\ *([a-zA-Z_]+)\ *(,.*|$)'
DOC_COMMENT_LINE_PATTERN='\ *#+(.*)'
DOC_COMMENT_LINE_BREAK_PATTERN='(.*^[\\n])\\n*(.*)'


doc_show_source_info() {

	local path=$1
	local param_doc_type=$2
	local current_doc_text=""
	local first=true

	cat $path | while read line 
	do

		if [[ $line =~ $DOC_TEST_PATTERN ]]; then

			current_doc_text=""		

			local doc_type=$(doc_extract_part "$line" 1 "$DOC_PATTERN")
			local doc_attributes=""

			if [ "$param_doc_type" == "$doc_type" ]; then

				local doc_declaration=$(doc_extract_part "$line" 2 "$DOC_PATTERN")

				local doc_title=$(doc_extract_part "$doc_declaration" 1 "$DOC_TITLE_PATTERN")

				local doc_data_type=$(doc_extract_attribute "$doc_declaration" "type")

				local doc_default_value=$(doc_extract_attribute "$doc_declaration" 'default')

				local doc_text=$(doc_extract_part "$line" 3 "$DOC_PATTERN")
				
				if [ -z "$current_doc_text" ]; then
					current_doc_text=" "
				fi

				if $first; then
					first=false
				else
					echo ""
					echo ""
				fi
				
				if [ ! -z "$doc_data_type$doc_default_value" ]; then

					if [ ! -z "$doc_data_type" ]; then
						doc_attributes="$doc_data_type"
					fi

					if [ ! -z "$doc_default_value" ]; then
						doc_attributes=$(doc_append_separator "$doc_attributes" " | ")
						doc_attributes="${doc_attributes}default = $doc_default_value"
					fi

					doc_title="$doc_title  [ $doc_attributes ]"
				fi
				
				ui_print "- $doc_title" $color_green

				echo "    $doc_text"
			fi

		elif [[ $line =~ $DOC_COMMENT_LINE_PATTERN ]]; then
			
			if [ ! -z "$current_doc_text" ]; then

				local comment=$(doc_extract_comment_line "$line")
				echo "    $comment"
			fi

		else

			current_doc_text=""
		fi
	done

	echo -e "\n"
}


doc_append_separator() {

	result="$1"

	if [ ! -z "$result" ]; then
		echo "$1$2"
	else
		echo "$1"
	fi
}


doc_extract_part() {

	local part=$(echo "$1" | sed -r "s/$3/\\$2/")
	echo $part
}


doc_extract_attribute() {

	local part=$(echo "$1" | sed -r 's/.*,\ *'$2':\ *(.+)\ *(,.*|$)/\1/')

	if [ "$part" != "$1" ]; then
		echo $part
	else
		echo ""
	fi
}


doc_extract_comment_line() {

	local part=$(echo "$1" | sed -r "s/$DOC_COMMENT_LINE_PATTERN/\1/")
	echo $part
}