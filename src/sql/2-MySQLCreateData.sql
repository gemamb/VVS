-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-------------------------------------------------------------------------------

INSERT into UserProfile
    VALUES (1,'admin','UDn64bBABog2A','1','1','1');
INSERT into UserProfile
    VALUES (2,'user1','FV3ubyBQMUqLk','user1','user1','user1@udc.es');
INSERT into UserProfile
    VALUES (3,'user2','OR6MDQsxRQppQ','user2','user2','user2@udc.es');

INSERT into Category (name) 
    VALUES ('Baloncesto');
INSERT into Category (name) 
    VALUES ('Fútbol');

INSERT INTO Event
    VALUES  (1, 'R.Madrid - FC Barcelona', '2016-4-2', 2);  
INSERT INTO Event
    VALUES  (2, 'España - República Checa', '2016-6-13', 2);
INSERT INTO Event
    VALUES  (3, 'R.Madrid - Getafe','2016-6-13', 2);
INSERT INTO Event
    VALUES  (4, 'Villareal - Deportivo', '2016-8-7', 2);
INSERT INTO Event
    VALUES  (5, 'Getafe - Sporting', '2016-8-7', 2);
INSERT INTO Event
    VALUES  (6, 'Eibar - Betis', '2016-8-7', 2);
INSERT INTO Event
    VALUES  (7, 'Barcelona - Espanyol', '2016-8-8', 2);
INSERT INTO Event
    VALUES  (8, 'Sevilla - Granada', '2016-8-8', 2);
INSERT INTO Event
    VALUES  (9, 'Las palmas - Athelic', '2016-8-8', 2);
INSERT INTO Event
    VALUES  (10, 'Celta - Malaga', '2016-8-8', 2);    
INSERT INTO Event
    VALUES  (11, 'Levante - Atlético', '2016-8-8', 2);
INSERT INTO Event
    VALUES  (12, 'Toronto Raptors - Miami Heat', '2016-7-5', 1);  
INSERT INTO Event
    VALUES  (13, 'LA Lakers - Cavs', '2016-7-5', 1);
INSERT INTO Event
    VALUES  (14, 'R.Madrid - Valencia', '2016-6-13', 2);

------------------------------------------------------
-------- BetTpes en los eventos 1,2 y 14 -------------
------------------------------------------------------

INSERT INTO BetType
    VALUES  (1, '¿Quién ganará el encuentro?', false, 1);

INSERT INTO BetType
    VALUES  (2, '¿Quién ganará el encuentro?', false, 2);
INSERT INTO BetType
    VALUES  (3, '¿Cuantos goles se marcarán en el 1er tiempo?', false, 2);
INSERT INTO BetType
    VALUES  (4, '¿Cuantos goles se marcarán en el 2º tiempo?', false, 2);
INSERT INTO BetType
    VALUES  (5, '¿Será el número de goles par o impar?', false, 2);
INSERT INTO BetType
    VALUES  (6, 'Resultado exacto', false, 2);
INSERT INTO BetType
    VALUES  (7, 'Resultado al descanso', false, 2);
INSERT INTO BetType
    VALUES  (8, '¿Qué equipo ganará ambos tiempos?', false, 2);
INSERT INTO BetType
    VALUES  (9, '¿Cuántos goles marcará el primer equipo?', false, 2);
INSERT INTO BetType
    VALUES  (10, '¿Cuántos goles marcará el segundo equipo?', false, 2);
INSERT INTO BetType
    VALUES  (11, '¿Marcará el primer equipo en ambos tiempos?', false, 2);
INSERT INTO BetType
    VALUES  (12, '¿Marcará el segundo equipo en ambos tiempos?', false, 2);
INSERT INTO BetType
    VALUES  (13, '¿Quién ganará el encuentro?', false, 2);

INSERT INTO BetType
    VALUES  (14, '¿Quién ganará el encuentro?', false, 14);
INSERT INTO BetType
    VALUES  (15, '¿Cuantos goles se marcarán?', false, 14);
INSERT INTO BetType
    VALUES  (16, '¿Quén marcará el primer gol?', false, 14);
INSERT INTO BetType
    VALUES  (17, '¿El resultado del partido será par o impar?', false, 14);
INSERT INTO BetType
    VALUES  (18, '¿Quién ganará el encuentro? MÚLTIPLE', true, 1);

----------------------------------------------------------
-------- BetOptions en los BetTypes 1,2 y 14 -------------
----------------------------------------------------------

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('1', 1.90 , NULL, 1);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('X', 2.20 , NULL, 1);   
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('2', 2.70 , NULL, 1); 


INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('1', 1.90 , NULL, 2);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('X', 2.20 , NULL, 2);   
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('2', 2.70 , NULL, 2); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 0,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 0,5', 4.00 , NULL, 3); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 1,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 1,5', 4.00 , NULL, 3); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 2,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 2,5', 4.00 , NULL, 3);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 3,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 3,5', 4.00 , NULL, 3);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 4,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 4,5', 4.00 , NULL, 3); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 5,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 5,5', 4.00 , NULL, 3);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 6,5', 3.20 , NULL, 3);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 6,5', 4.00 , NULL, 3);

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('1', 1.90 , NULL, 14);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('X', 2.20 , NULL, 14);   
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('2', 2.70 , NULL, 14); 

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 0,5', 3.20 , NULL, 15);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 0,5', 4.00 , NULL, 15); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 1,5', 3.20 , NULL, 15);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 1,5', 4.00 , NULL, 15); 
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Más de 2,5', 3.20 , NULL, 15);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Menos de 2,5', 4.00 , NULL, 15);

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Cristiano Ronaldo', 1.90 , NULL, 16);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Karim Benzema', 2.20 , NULL, 16);   
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Gareth Bale', 2.70 , NULL, 16);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Alvaro Negredo', 3.20 , NULL, 16);  
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Paco Alcácer', 4.00 , NULL, 16);  

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Par', 1.95 , NULL, 17);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('Impar', 2.25 , NULL, 17);   

INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('1', 1.90 , NULL, 18);
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('X', 2.20 , NULL, 18);   
INSERT INTO BetOption (answer,rate,betState,betTypeId)
    VALUES  ('2', 2.70 , NULL, 18);

-----------------------------------------------------------
-------- Bets para los usuarios en las grids --------------
-----------------------------------------------------------

INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);
INSERT INTO Bet (betedMoney,date,usrId,eventId,betOptionId) values (1,CURRENT_TIMESTAMP,2,1,1);



