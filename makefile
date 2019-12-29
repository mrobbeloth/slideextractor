# A makefile consists of rules with the following format:
# target: dependencies ...
#     commands

slideextractor: SlideExtractor.java
	javac SlideExtractor.java
clean:
	rm -f *.class
