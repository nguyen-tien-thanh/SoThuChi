use CRUDAndroidDB

create Table Trip (
    TripId varchar(255) PRIMARY KEY,
    TripName varchar(255) NOT NULL,
	Destination varchar(255) NOT NULL,
	TripDate date NOT NULL,
	RiskAssessment bit NOT NULL,
    Description varchar(255),
);

create Table Activity (
    Id varchar(255) PRIMARY KEY,
	Category nvarchar(255) NOT NULL,
	Amount int NOT NULL,
	IssueDate date NOT NULL,
    Note varchar(255),
	TripId varchar(255) FOREIGN KEY REFERENCES Trip(TripId)
	ON DELETE CASCADE
);


