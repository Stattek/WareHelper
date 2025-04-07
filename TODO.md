# TODO

List of TODO info.

## Use Cases

- [ ] Implement Retrieve Inventory

## Database

### How to Create Tables for Warehelper

Item table creation:

```sql
create table Item(ItemId int not null auto_increment, Sku varchar(255), Name varchar(255), CategoryId int, EconomyInfoId int, DateInfoId int, PreferenceId int, primary key (ItemId), foreign key (CategoryId) references Category(CategoryId), foreign key (EconomyInfoId) references EconomyInfo(EconomyInfoId), foreign key (DateInfoId) references DateInfo(DateInfoId), foreign key (PreferenceId) references Preference(PreferenceId));
```

DateInfo table creation:

```sql
create table DateInfo(DateInfoId int not null auto_increment, Created Date, LastModified Date, primary key (DateInfoId));
```

EconomyInfo table creation:

```sql
create table EconomyInfo(EconomyInfoId int not null auto_increment, Price double(20, 2), NumItems int, primary key (EconomyInfoId));
```

Preference table creation:

```sql
create table Preference(PreferenceId int not null auto_increment, SellWithinNumDays int, LowInventoryThreshold int, PromotionPercentOff double(20,2), primary key (PreferenceId));
```

Category table creation:

```sql
create table Category(CategoryId int not null auto_increment, Name varchar(255), primary key (CategoryId));
```

#### Test Data

Creating some test data

```sql
insert into Category (Name) values ('firstCategory');
insert into Category (Name) values ('secondCategory');
insert into Preference (SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) values (1, 2, 0.3);
insert into EconomyInfo (Price, NumItems) values (20.45, 20);
insert into DateInfo (Created, LastModified) values ("2017-6-16", "2018-1-23");
insert into Item (Sku, Name, CategoryId, EconomyInfoId, DateInfoId, PreferenceId) values ('234sku', 'shirt', 1, 1, 1, 1);
```

### Read, write, update, delete

- [ ] Implement read
  - [ ] Item
  - [ ] Bundle
  - [ ] Category
- [ ] Implement update
  - [ ] Item
  - [ ] Bundle
  - [ ] Category
- [ ] Implement create
  - [ ] Item
  - [ ] Bundle
  - [ ] Category
- [ ] Implement delete
  - [ ] Item
  - [ ] Bundle
  - [ ] Category
