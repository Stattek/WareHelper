create table Category(CategoryId int not null auto_increment, Name varchar(255), primary key (CategoryId));
create table Preference(PreferenceId int not null auto_increment, SellWithinNumDays int, LowInventoryThreshold int, PromotionPercentOff double(20,2), primary key (PreferenceId));
create table EconomyInfo(EconomyInfoId int not null auto_increment, Price double(20, 2), NumItems int, primary key (EconomyInfoId));
create table DateInfo(DateInfoId int not null auto_increment, Created Date, LastModified Date, primary key (DateInfoId));
create table Item(ItemId int not null auto_increment, Sku varchar(255), Name varchar(255), CategoryId int, EconomyInfoId int, DateInfoId int, PreferenceId int, primary key (ItemId), foreign key (CategoryId) references Category(CategoryId), foreign key (EconomyInfoId) references EconomyInfo(EconomyInfoId), foreign key (DateInfoId) references DateInfo(DateInfoId), foreign key (PreferenceId) references Preference(PreferenceId));
