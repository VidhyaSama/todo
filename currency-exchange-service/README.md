Docker
--------------

Images
--------

- Currency Exchange Service
     - n9064/msa-currency-exchange-service:0.0.1-SNAPSHOT

- Mysql
     - mysql:8


 Image Creation
---------------
Run as Maven build in 
Goals mention - spring-boot:build-image -DskipTests


Commands
----------


1. docker pull mysql:8
2. docker-compose up

When I ran docker-compose up mysql container got created but application is failing to start .

Please find the issue in logs.txt file.





