echo Building Javapillar!
del src\*.class /q
del bin\*.* /q
cd src
javac GUI.java
cd..
move src\*.class bin\
copy src\*.png bin\
copy src\*.wav bin\
cd bin
java GUI
cd..

