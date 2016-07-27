create table Assay (
	assayID integer primary key,
	creationDate date not null
);

create table Cohort (
	cohortID integer primary key,
	assayID integer not null,
	ageOfPaternalParent integer not null,
	paternalIdentifier char not null,
	creationDate date not null,
	completionDate date,
	constraint ic_cohort foreign key (assayID) references Assay(assayID) on delete cascade
);

create table Fly (
	cohortID integer not null,
	sex varchar(6) not null,
	dateOfEclosion date not null,
	dateOfDeath date,
	constraint ic_sex check (sex in ('Male', 'Female')),
	constraint ic_fly foreign key (cohortID) references Cohort(cohortID) on delete cascade
);