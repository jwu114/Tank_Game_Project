SRC_DIR = bn 
OUT_DIR = bin

JC = javac
JCR = java
JCFLAGS = -d $(OUT_DIR)

CLASSES = \
		MenuUI.java \
		GameFrame.java \
		GameCore.java \
		MainClass.java

default: build run clean
		
build: 
	$(JC) $(JCFLAGS) $(CLASSES)

run:
	$(JCR) -cp ./bin MainClass


clean:
	$(RM) -r ./bin