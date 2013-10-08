#!/bin/bash
SSH_USER=ivy
SSH_HOST=178.79.183.218
SSH_PORT=52022
PRODUCT=$1
VERSION=$2
FOLDER=$3
SOURCE_HACK=source
if ([ -z $PRODUCT ] || [ -z $VERSION ] || [ -z $FOLDER ] ) ; then 
  echo "Usage: upload_jar.sh <PRODUCT> <REVISION> <FOLDER>"
  echo "  PRODUCT - The name of the module."
  echo "  VERSION - The revision of the module."
  echo "  FOLDER - The location of the jar file."
  exit
fi

[ -f ${FOLDER}/${PRODUCT}-${VERSION}-source.jar ] && SOURCE_HACK=dont_delete_source

TYPE=jar
IVY_FILE=ivy-${VERSION}.xml
echo "Attempting to create ivy file for ${PRODUCT} ${VERSION}"

cp externals/ivy.template ${FOLDER}/.
pushd ${FOLDER}
sed -e "/${SOURCE_HACK}/ d" ivy.template | sed -e "s:PRODUCT:${PRODUCT}:g" -e "s:VERSION:${VERSION}:g" > ${IVY_FILE}
if [ -d dependencies ] ; then
for f in dependencies/*.jar ; do
  d=`basename $f`
  v=`echo $d | sed -e "s_.*-\([0-9|\.]*\).jar_\1_"`
  p=`echo $d | sed -e "s_\(.*\)-\([0-9|\.]*\).jar_\1_"`
  i=ivy-${p}-${v}.xml
  echo "Found dependency: ${p} version: ${v}"
  echo "        <dependency org=\"thirdparty\" name=\"${p}\" rev=\"${v}\" conf=\"runtime->runtime\"/>" >> ${IVY_FILE}
  sed -e "/${SOURCE_HACK}/ d" ivy.template | sed -e "s:PRODUCT:${p}:g" -e "s:VERSION:${v}:g" > ${i}
  echo "    </dependencies>" >> ${i}
  echo "</ivy-module>" >> ${i}
  ssh -p $SSH_PORT ${SSH_USER}@${SSH_HOST} "mkdir -p /var/www/ivy/thirdparty/${p}/${p}-${v}"
  scp -P $SSH_PORT $f ${SSH_USER}@${SSH_HOST}:/var/www/ivy/thirdparty/${p}/${p}-${v}/${p}-${v}.jar
  scp -P $SSH_PORT ${i} ${SSH_USER}@${SSH_HOST}:/var/www/ivy/thirdparty/${p}/${p}-${v}/ivy-${v}.xml
done
fi
echo "    </dependencies>" >> ${IVY_FILE}
echo "</ivy-module>" >> ${IVY_FILE}
ssh -p $SSH_PORT ${SSH_USER}@${SSH_HOST} "mkdir -p /var/www/ivy/thirdparty/${PRODUCT}/${PRODUCT}-${VERSION}"
scp -P $SSH_PORT ${PRODUCT}-${VERSION}.jar ${SSH_USER}@${SSH_HOST}:/var/www/ivy/thirdparty/${PRODUCT}/${PRODUCT}-${VERSION}/${PRODUCT}-${VERSION}.jar
[ -f ${PRODUCT}-${VERSION}-source.jar ] && scp -P $SSH_PORT ${PRODUCT}-${VERSION}-source.jar ${SSH_USER}@${SSH_HOST}:/var/www/ivy/thirdparty/${PRODUCT}/${PRODUCT}-${VERSION}/${PRODUCT}-src-${VERSION}.jar || echo "No source file found."
scp -P $SSH_PORT ivy-${VERSION}.xml ${SSH_USER}@${SSH_HOST}:/var/www/ivy/thirdparty/${PRODUCT}/${PRODUCT}-${VERSION}/.
popd
