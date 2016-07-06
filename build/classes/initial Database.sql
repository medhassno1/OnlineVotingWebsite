--create first table which stores email and idkey
create table personal_data(email varchar(50) primary key, idkey varchar(10));


--create an admin account to manage the default poll and receive vote outcome via email
insert into personal_data values ('admin@mail.com', 'admin_password');


--create second table which stores poll details, with auto generated increasing poll ID
create table poll_index(pollID INTEGER NOT NULL 
                PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1), owner varchar(50), title varchar(50), question varchar(100), option1 varchar (50), option2 varchar (50), option3 varchar (50), option4 varchar (50), deadline date);

                
--create the first default poll which is Democratic Voting poll                
insert into poll_index (owner, title, question, option1, option2, option3, option4, deadline) values ('admin@mail.com', 'Democratic voting', 'Which candidate do you vote for?', 'Candidate 1', 'Candidate 2', 'Candidate 3', 'Null', '2016-04-01');


--create third table which stores each voter's individual vote for each poll
create table vote_results(pollID int NOT NULL, email varchar(50), choice varchar (50));
