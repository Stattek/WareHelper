-- Active: 1743917419277@@jdbcmariadblocalhost3306warehelper@3306@warehelper
-- Drop all tables if they exist
    DROP TABLE ItemBundle;
    DROP TABLE Bundle;
    DROP TABLE Item;
    DROP TABLE Category;

-- Create tables
create table Category(
    CategoryId int not null auto_increment, 
    CategoryName varchar(255), 
    primary key (CategoryId), 
    unique (CategoryName)
);

create table Item(
    ItemId int not null auto_increment, 
    Sku varchar(255), 
    ItemName varchar(255), 
    Description varchar(1024), 
    CategoryId int, 
    Price double(20, 2), 
    NumItems int, 
    Created Date, 
    LastModified Date, 
    SellWithinNumDays int, 
    LowInventoryThreshold int, 
    PromotionPercentOff double(20,2), 
    primary key (ItemId), 
    foreign key (CategoryId) references Category(CategoryId)
);

create table Bundle(
    BundleId int not null auto_increment, 
    BundleDiscount double(20,2), 
    primary key (BundleId)
);

create table ItemBundle(
    BundleId int not null, 
    ItemId int not null, 
    primary key (BundleId, ItemId), 
    foreign key (BundleId) references Bundle(BundleId) on delete cascade, 
    foreign key (ItemId) references Item(ItemId) on delete cascade
);

-- Categories
INSERT INTO Category (CategoryName) VALUES ('Electronics');
INSERT INTO Category (CategoryName) VALUES ('Books');
INSERT INTO Category (CategoryName) VALUES ('Clothing');
INSERT INTO Category (CategoryName) VALUES ('Home Decor');
INSERT INTO Category (CategoryName) VALUES ('Tools');
INSERT INTO Category (CategoryName) VALUES ('Toys');
INSERT INTO Category (CategoryName) VALUES ('Food');
INSERT INTO Category (CategoryName) VALUES ('Office Supplies');

-- Items - Electronics
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Electronics1', 'Smartphone', 'High-end smartphone with great camera', 1, 699.99, 25, "2024-12-15", "2025-01-05", 180, 5, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Electronics2', 'Laptop', 'Business laptop with long battery life', 1, 1199.99, 12, "2025-01-10", "2025-01-10", 120, 3, 0.05);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Electronics3', 'Wireless Earbuds', 'Noise cancelling bluetooth earbuds', 1, 129.99, 3, "2024-10-20", "2025-02-01", 90, 10, 0.15);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Electronics4', 'Smart Watch', 'Fitness tracking smartwatch', 1, 249.99, 18, "2025-01-05", "2025-01-05", 150, 5, 0.0);

-- Items - Books (with low inventory)
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Books5', 'Design Patterns', 'Programming design patterns book', 2, 49.99, 2, "2024-08-10", "2024-12-15", 365, 3, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Books6', 'Database Systems', 'Comprehensive database textbook', 2, 79.99, 1, "2024-11-20", "2025-01-10", 180, 5, 0.1);

-- Items - Clothing (some with old dates for unsold inventory report)
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Clothing7', 'Winter Jacket', 'Warm winter jacket with hood', 3, 89.99, 15, "2024-09-01", "2024-09-01", 60, 5, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Clothing8', 'Summer Dress', 'Light cotton summer dress', 3, 39.99, 8, "2024-05-15", "2024-05-15", 120, 3, 0.25);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Clothing9', 'Denim Jeans', 'Classic fit jeans', 3, 59.99, 20, "2025-01-15", "2025-01-15", 365, 5, 0.0);

-- Items - Home Decor (mix of inventory levels)
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('HomeDecor10', 'Table Lamp', 'Modern design table lamp', 4, 45.99, 7, "2024-11-10", "2024-12-20", 90, 5, 0.1);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('HomeDecor11', 'Wall Clock', 'Minimalist wall clock', 4, 29.99, 12, "2024-10-05", "2024-10-05", 180, 3, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('HomeDecor12', 'Throw Pillows', 'Decorative throw pillows set of 2', 4, 24.99, 4, "2024-12-01", "2024-12-01", 120, 5, 0.15);

-- Items - Tools (high inventory)
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Tools13', 'Hammer', 'Standard claw hammer', 5, 19.99, 35, "2024-08-15", "2024-08-15", 730, 10, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Tools14', 'Screwdriver Set', 'Set of 10 screwdrivers', 5, 34.99, 18, "2024-09-20", "2024-09-20", 730, 5, 0.05);

-- Items - Toys
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Toys15', 'Building Blocks', 'Educational building blocks', 6, 29.99, 10, "2024-11-25", "2025-01-02", 90, 5, 0.1);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Toys16', 'Puzzle', '1000 piece landscape puzzle', 6, 19.99, 2, "2024-12-10", "2024-12-10", 180, 3, 0.0);

-- Items - Food (short sell-within days)
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Food17', 'Premium Coffee', 'Gourmet coffee beans', 7, 14.99, 8, "2025-01-01", "2025-01-01", 30, 5, 0.0);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('Food18', 'Chocolate Box', 'Assorted chocolates gift box', 7, 24.99, 15, "2025-01-10", "2025-01-10", 45, 3, 0.0);

-- Items - Office Supplies
INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('OfficeSupplies19', 'Notebook', 'Premium hardcover notebook', 8, 12.99, 25, "2024-10-10", "2024-10-10", 365, 10, 0.50);

INSERT INTO Item (Sku, ItemName, Description, CategoryId, Price, NumItems, Created, LastModified, SellWithinNumDays, LowInventoryThreshold, PromotionPercentOff) 
VALUES ('OfficeSupplies20', 'Pen Set', 'Set of 5 premium pens', 8, 18.99, 4, "2024-11-15", "2024-11-15", 180, 5, 0.65);

-- Bundles
INSERT INTO Bundle (BundleDiscount) VALUES (0.15);  -- Home Office Bundle
INSERT INTO Bundle (BundleDiscount) VALUES (0.2);   -- Electronics Bundle
INSERT INTO Bundle (BundleDiscount) VALUES (0.25);  -- Decorator's Bundle 
INSERT INTO Bundle (BundleDiscount) VALUES (0.1);   -- Book Bundle
INSERT INTO Bundle (BundleDiscount) VALUES (0.3);   -- Gift Bundle

-- Bundle Associations
-- Home Office Bundle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (1, 2);   -- Laptop
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (1, 19);  -- Notebook
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (1, 20);  -- Pen Set

-- Electronics Bundle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (2, 1);   -- Smartphone
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (2, 3);   -- Wireless Earbuds
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (2, 4);   -- Smart Watch

-- Decorator's Bundle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (3, 10);  -- Table Lamp
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (3, 11);  -- Wall Clock
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (3, 12);  -- Throw Pillows

-- Book Bundle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (4, 5);   -- Design Patterns
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (4, 6);   -- Database Systems

-- Gift Bundle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (5, 12);  -- Throw Pillows
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (5, 16);  -- Puzzle
INSERT INTO ItemBundle (BundleId, ItemId) VALUES (5, 18);  -- Chocolate Box