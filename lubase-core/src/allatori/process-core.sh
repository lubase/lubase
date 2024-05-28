echo "start……"
if [ "$OSTYPE" = "linux-gnu" ]; then
  CURRENT_PATH=$PWD
  JARFILE=$CURRENT_PATH"/src/allatori/allatori.jar"
  CONFIGFILE=$CURRENT_PATH"/src/allatori/config.xml"
  TARGETPATH=$CURRENT_PATH"/target/"
  cp $JARFILE $TARGETPATH
  cp $CONFIGFILE $TARGETPATH
  cd $TARGETPATH
  java -Xms128m -Xmx512m -jar allatori.jar config.xml
  end
fi
echo "end"
