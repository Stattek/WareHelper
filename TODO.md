# TODO

List of TODO info.

## Use Cases

- [ ] Implement Retrieve Inventory

## Database

### How to Create Tables for Warehelper

Category table creation:

```sql
create table Category(CategoryId int not null auto_increment, Name varchar(255), primary key (CategoryId));
```

Item table creation:

```sql
create table Item(ItemId int not null auto_increment, Sku varchar(255), Name varchar(255), Description varchar(1024), CategoryId int, Price double(20, 2), NumItems int, Created Date, LastModified Date, SellWithinNumDays int, LowInventoryThreshold int, PromotionPercentOff double(20,2), primary key (ItemId), foreign key (CategoryId) references Category(CategoryId));
```

#### Test Data

Creating some test data

```sql
insert into Category (Name) values ('firstCategory');
insert into Category (Name) values ('secondCategory');
insert into Category (Name) values ('cool stuff');
insert into Item (Sku, Name, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) values ('234sku', 'shirt', '', 1, 20.45, 20, "2017-6-16", "2018-1-23", 1, 2, 0.3);
insert into Item (Sku, Name, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) values ('morshu1', 'lamp oil', "", 3, 50.0, 20, "1996-5-10", "2018-1-23", 30, 5, 0.0);
insert into Item (Sku, Name, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) values ('morshu2', 'rope', "", 3, 50.0, 20, "1996-5-10", "2018-1-23", 30, 5, 0.0);
insert into Item (Sku, Name, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) values ('morshu3', 'bombs', "You want it? It's yours, my friend.", 3, 50.0, 20, "1996-5-10", "2018-1-23", 30, 5, 0.0);
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
