./build-src.sh
if [[ $? == 0 ]]
then
	appletviewer src/Main.java
fi
./clean.sh
