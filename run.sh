./build.sh
if [[ $? == 0 ]]
then
	appletviewer src/Main.java
	rm src/*.class
fi
