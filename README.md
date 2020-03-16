Quizzitch

This Alexa skill is a template for a quiz game skill called Quizzitch. Alexa will ask one or two users to translate german words or sentences into english words.

Necessities

- Tomcat (download Apache Tomcat at https://tomcat.apache.org/download-90.cgi and save it to a chosen directory)
- ngrok (go to https://ngrok.com/ and follow the instructions to build a secure introspectable tunnel to a localhost)
- Eclipse (download and install the IDE Eclipse or Anaconda at https://www.eclipse.org/downloads/ or https://docs.anaconda.com/anaconda/install/)
- SQLite or DBrowser for SQLite  (download at https://sqlitebrowser.org/dl/)
- SQLite JDBC Driver


Skill Setup

1. Download the file 'Praxisprojekt-master'.
2. Save file 'Praxisprojekt-master' to a chosen directory.
2. Open Eclipse and 'Import' the existing Maven Project 'Praxisprojekt-master'
3. Update Project: Right mouse click on the project in the left toolbar > Maven > Update Project > Ok
4. Maven Install: Right mouse click on the project in the left toolbar > Run As > Maven install
5. Open Folder 'target' in file 'Praxisprojekt-master' and copy war-file 'myskill.war' to the folder 'webapps' in your Tomcat file.
