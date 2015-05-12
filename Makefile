.SUFFIXES:
.SUFFIXES: .java .class

.java.class:
	javac $*.java

all: DecryptXYZ3W.class

clean:
	$(RM) *.class *~


