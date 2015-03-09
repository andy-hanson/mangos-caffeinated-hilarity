./build-src.sh
if [[ $? == 0 ]]
then
	cd src
	jar cvfm ../html/mango.jar MANIFEST.MF *
	cd ..
fi
./clean.sh
