# TODO

List of TODO info.

## Use Cases

- [ ] Implement Retrieve Inventory

## Database

### How to Create Tables for Warehelper

Item table creation:

```sql
create table Item(ItemId int primary key, Sku varchar(255), Name varchar(255), CategoryId int, EconomyInfoId int, DateInfoId int, PreferenceId int, foreign key (CategoryId) references Category(CategoryId), foreign key (EconomyInfoId) references EconomyInfo(EconomyInfoId), foreign key (DateInfoId) references DateInfo(DateInfoId), foreign key (PreferenceId) references Preference(PreferenceId));
```

DateInfo table creation:

```sql
create table DateInfo(DateInfoId int primary key, Created Date, LastModified Date);
```

EconomyInfo table creation:

```sql
create table EconomyInfo(EconomyInfoId int primary key, Price double(20, 2), NumItems int);
```

Preference table creation:

```sql
create table Preference(PreferenceId int primary key, SellWithinNumDays int, LowInventoryThreshold int, PromotionPercentOff double(20,2));
```

Category table creation:

```sql
create table Category(CategoryId int primary key, Name varchar(255));
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
