# Server

In order to make this work, you must first download and install Oracle Database XE (latest edition, but may work with all of them). Then you should adapt the inserUser and updateLocalDb methods in the Receiver class, specifically the connection statement (you either create a username and password identically with mine or write your username and password there).


Before you do anything else you should test the database connection by hardcoding a SQL statement.
