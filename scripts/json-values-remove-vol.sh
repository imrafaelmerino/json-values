#!/usr/bin/env bash
set -o errexit
echo "Executing script:$0 $@"

main(){
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CONF_FILE_NOT_FOUND_MESSAGE=".defaults configuration not found in ${DIR}."

CONF_FILE="${DIR}"/.defaults

if [ ! -f "${CONF_FILE}" ]; then
    echo "$CONF_FILE_NOT_FOUND_MESSAGE"
    exit 1
fi

source "${CONF_FILE}"

( docker volume rm "${VOLUME_NAME}" && echo "Volume deleted")
}

main