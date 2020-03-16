Quizzitch

This Alexa skill is a template for a quiz game skill called Quizzitch. Alexa will ask one or two users to translate german words or sentences into english words.

Necessities

- Amazon Alexa Account
- Tomcat (download Apache Tomcat at https://tomcat.apache.org/download-90.cgi and save it to a chosen directory)
- ngrok (go to https://ngrok.com/ and follow the instructions to build a secure introspectable tunnel to a localhost)
- Eclipse (download and install the IDE Eclipse or Anaconda at https://www.eclipse.org/downloads/ or https://docs.anaconda.com/anaconda/install/)
- SQLite or DBrowser for SQLite  (download at https://sqlitebrowser.org/dl/)
- SQLite JDBC Driver: is already added as a dependency in the xml-file 'pom.xml' in the 'Praxisprojekt-master'.

First, you need to create a localhost using Apache Tomcat.
On Mac Os X, this can be done as follows in the terminal:
cd apche-tomcat-9.0.12 (may differ in case your Tomcat file is named differently)
cd bin/
chmod +x *.sh
./startup.sh

Check whether or not Tomcat has started properly by writing 'localhost:8080' into your browser. the Apache server is running if you can see an Apache Tomcat interface.



Skill Setup

1. Download the file 'Praxisprojekt-master'.
2. Save file 'Praxisprojekt-master' to a chosen directory.
2. Open Eclipse and 'Import' the existing Maven Project 'Praxisprojekt-master'.
3. Update Project: Right mouse click on the project in the left toolbar (de.unidue.ltl.ourAlexaExample) > Maven > Update Project > Ok.
4. Maven Install: Right mouse click on the project in the left toolbar (de.unidue.ltl.ourAlexaExample) > Run As > Maven install.
5. Open Folder 'target' in file 'Praxisprojekt-master' and copy war-file 'myskill.war' to the folder 'webapps' in your Tomcat file.
6. Open your Amazon Alexa Account
