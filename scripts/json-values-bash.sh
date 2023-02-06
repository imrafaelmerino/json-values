#!/usr/bin/env bash
#set -o xtrace
set -o errexit
set -o pipefail
echo "Executing script:$0 $@"

main(){
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CONF_FILE_NOT_FOUND_MESSAGE=".defaults configuration not found in ${DIR}."
CONF_FILE="${DIR}"/.defaults

if [ ! -f "${CONF_FILE}" ]; then
    echo "$CONF_FILE_NOT_FOUND_MESSAGE"
fi

source "${CONF_FILE}"
VERSION="${DEFAULT_VERSION}"

while [ "$1" != "" ]; do
    case $1 in
        --version )   shift
                      VERSION=${1}
                      ;;
    esac
    shift
done

if [[ -z ${VERSION} ]]
    then
      echo "${VERSION_EMPTY_MESSAGE}"
      exit 1
fi

docker run -v json-values-maven-repo:/root/.m2 --rm -it json-values:"${VERSION}" bash
}

main "$@"