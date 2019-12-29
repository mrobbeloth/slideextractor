# A makefile consists of rules with the following format:
# target: dependencies ...
#     commands

slideextractor: SlideExtractor.java
	javac SlideExtractor.java
	jar cfe SlideExtractor.jar SlideExtractor SlideExtractor.class
clean:
	rm -f *.class *.jar
