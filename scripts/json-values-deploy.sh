#!/usr/bin/env bash
#set -o xtrace
set -o errexit
set -o pipefail

main(){
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CONF_FILE_NOT_FOUND_MESSAGE=".defaults configuration not found in ${DIR}."

CONF_FILE="${DIR}"/.defaults

if [ ! -f "${CONF_FILE}" ]; then
    echo "$CONF_FILE_NOT_FOUND_MESSAGE"
    exit 1
fi

source "${CONF_FILE}"
VERSION="${DEFAULT_VERSION}"

while [ "$1" != "" ]; do
    case $1 in
        --username )   shift
                       username=${1}
                       ;;
        --password )   shift
                       password=${1}
                       ;;
        --gpg_passphrase )   shift
                             gpg_passphrase=${1}
                             ;;
    esac
    shift
done

if [[ -z ${username} ]]
    then
      echo "nexus username required"
      exit 1
fi

if [[ -z ${password} ]]
    then
      echo "nexus password required"
      exit 1
fi

if [[ -z ${gpg_passphrase} ]]
    then
      echo "gpg passphrase required"
      exit 1
fi

"${DIR}"/json-values-create-volume.sh
COMMAND="\
GPG_PASSPHRASE=${gpg_passphrase};\
sed -e s/uservar/${username}/ -e s/passwordvar/${password}/ /tmp/json-values/settings-template.xml > /tmp/json-values/settings.xml;\
mvn -X clean package deploy -Dgpg.keyname=javaio --settings /tmp/json-values/settings.xml  -DskipTests=true -B -U -Prelease;\
rm -rf /tmp/json-values/settings.xml;"

docker run -v json-values-maven-repo:/root/.m2 --rm -it json-values:"${VERSION}" /bin/bash -c "${COMMAND}"

}

main "$@"