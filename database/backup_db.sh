#!/bin/sh
# Ce script me permet de recuperer le dernier backup MySQL
# cree par PlayApps et de l'importer sur ma base MySQL en local
# Je ne peux pas laisser un dump sur Git car il y a nos emails
# dans le fichier. Les Passwords sont cryptes par contre.
# Si vous voulez tester en local, je vous envois un dump compressé

scp play@92.243.22.179:backup/current/mysql-dump.gz .
gunzip mysql-dump.gz
/usr/local/mysql/bin/mysql -u demouser -pdemo123 leszindeps < mysql-dump

