### Syntax for mark down files: https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax
#Application designed originally to be a means of cataloguing record collections based upon the record catalogue number (usually on the side of the album cover)
* ###It takes an image passed into it and extracts the text from it using the Google Vision API.
* ###Then it uses regexs to determine which text is the catalogue number
* ###And it looks up the catalog number from Discogs API.
* ###Or it takes a direct string input of the catalogue number
* ###And it looks up the catalog number from Discogs API.
* ###******NOTE: Album Catalog Numbers Are NOT unique. And there is no standard format for them.


## APPLICATION INFO

### Spring Initializer for application setup

### Maven for dependency management

### Hibernate for ORM

### Postgres for DB

## ENVIRONMENT SETUP
### Postgres DB Setup
#### [https://www.postgresql.org/docs/15/tutorial-start.html]
* Use pgAdmin or DBeaver for GUI
#### Setup Connection: 
**In DBeaver navigate to New/Edit connection -> Connection Settings -> Main Tab and enter values below**

**For Connect By Host Enter values:**
* Host: localhost
* Database: Postgres

**For Connect by URL Enter values:**
* URL: jdbc:postgresql://localhost:5432/postgres
* Authentication: Database Native
* Username: See Tech Lead for username
* Password: See Tech Lead for password
* Click Save Password checkbox


* Now click Test Connection to verify connection is working
* Click Okay after successful test

### Docker for containerization
#### https://docs.docker.com/get-started/docker_cheatsheet.pdf
#### Images: Standalone, executable package of software that includes everything needed to run an application:
#### code, runtime, system tools, system libraries and settings.

### Catalog Numbers Format
####******NOTE: Album Catalog Numbers Are NOT unique. And there is no standard format for them.
#### BY COUNTRY
The "R" often stands for "Records" and the preceeding letters are the record label name
The most common form is Alphanumeric.
Start with "ELV002" format since it's the least likely to be a false positive
US: ELV002 CSS 873 BBBR-115 //  RARE *** B0007580-02 
UK:
FRANCE: 884 815-7 | 
Italy: CDL 337