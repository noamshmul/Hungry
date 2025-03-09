-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `recipe_categories` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL UNIQUE,
  `description` TEXT
);

CREATE TABLE IF NOT EXISTS `recipes` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `preparation_time` INT,
  `cooking_time` INT,
  `servings` INT,
  `difficulty` ENUM('Easy', 'Medium', 'Hard') DEFAULT 'Medium',
  `category_id` INT,
  `author_id` INT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`category_id`) REFERENCES `recipe_categories`(`id`),
  FOREIGN KEY (`author_id`) REFERENCES `users`(`id`)
);

CREATE TABLE IF NOT EXISTS `ingredients` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `recipe_id` INT,
  `name` VARCHAR(100) NOT NULL,
  `quantity` VARCHAR(50),
  `unit` VARCHAR(20),
  FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `instructions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `recipe_id` INT,
  `step_number` INT NOT NULL,
  `description` TEXT NOT NULL,
  FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE
);

-- Insert data into tables

-- Insert categories
INSERT INTO `recipe_categories` (`name`, `description`) VALUES
('Breakfast', 'Morning meals to start your day'),
('Lunch', 'Midday meals to keep you going'),
('Dinner', 'Evening meals to end your day'),
('Dessert', 'Sweet treats for any time'),
('Vegetarian', 'Meals without meat'),
('Vegan', 'Meals without any animal products');

-- Insert users
INSERT INTO `users` (`username`, `email`, `password_hash`) VALUES
('john_doe', 'john@example.com', 'hashed_password_1'),
('jane_smith', 'jane@example.com', 'hashed_password_2'),
('chef_mike', 'mike@example.com', 'hashed_password_3');

-- Insert recipes
INSERT INTO `recipes` (`title`, `description`, `preparation_time`, `cooking_time`, `servings`, `difficulty`, `category_id`, `author_id`) VALUES
('Classic Pancakes', 'Fluffy pancakes perfect for a weekend breakfast', 15, 10, 4, 'Easy', 1, 1),
('Vegetable Stir Fry', 'Quick and healthy vegetable stir fry with soy sauce', 10, 15, 2, 'Easy', 5, 2),
('Spaghetti Carbonara', 'Traditional Italian pasta dish with eggs and cheese', 15, 15, 3, 'Medium', 3, 3),
('Chocolate Chip Cookies', 'Soft and chewy chocolate chip cookies', 20, 10, 24, 'Easy', 4, 1),
('Vegan Buddha Bowl', 'Nutritious bowl with grains, vegetables, and dressing', 20, 10, 1, 'Medium', 6, 2);

-- Insert ingredients for Classic Pancakes
INSERT INTO `ingredients` (`recipe_id`, `name`, `quantity`, `unit`) VALUES
(1, 'All-purpose flour', '1 1/2', 'cups'),
(1, 'Baking powder', '3 1/2', 'teaspoons'),
(1, 'Salt', '1', 'teaspoon'),
(1, 'White sugar', '1', 'tablespoon'),
(1, 'Milk', '1 1/4', 'cups'),
(1, 'Egg', '1', ''),
(1, 'Butter', '3', 'tablespoons');

-- Insert instructions for Classic Pancakes
INSERT INTO `instructions` (`recipe_id`, `step_number`, `description`) VALUES
(1, 1, 'In a large bowl, sift together the flour, baking powder, salt and sugar.'),
(1, 2, 'Make a well in the center and pour in the milk, egg and melted butter; mix until smooth.'),
(1, 3, 'Heat a lightly oiled griddle or frying pan over medium-high heat.'),
(1, 4, 'Pour or scoop the batter onto the griddle, using approximately 1/4 cup for each pancake.'),
(1, 5, 'Cook until bubbles form and the edges are dry, then flip and cook until browned on the other side.');

-- Insert ingredients for Vegetable Stir Fry
INSERT INTO `ingredients` (`recipe_id`, `name`, `quantity`, `unit`) VALUES
(2, 'Broccoli', '1', 'head'),
(2, 'Carrot', '2', ''),
(2, 'Bell pepper', '1', ''),
(2, 'Soy sauce', '2', 'tablespoons'),
(2, 'Garlic', '2', 'cloves'),
(2, 'Ginger', '1', 'teaspoon'),
(2, 'Vegetable oil', '1', 'tablespoon');

-- Insert more data as needed