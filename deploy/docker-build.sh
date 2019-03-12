#!/usr/bin/env bash

DIR=$(cd `dirname $0`;pwd)
FILE=docker-build.sh
INDEX=1
VERSION=v0.0.$INDEX
CURRENT_VERSION=sharingan:$VERSION

echo " ====> start rebuild sharingan image..."

docker build -f Dockerfile -t $CURRENT_VERSION .
echo " ====> build $CURRENT_VERSION success."

docker tag $CURRENT_VERSION sharingan:lasted
echo " ====> retag lasted link to $CURRENT_VERSION"

OLD_INDEX="INDEX="$INDEX
NEXT_INDEX="INDEX="$(($INDEX+1))

cd $DIR
sed -i '' "s/$OLD_INDEX/$NEXT_INDEX/g" $FILE
