package com.foodapp.config;

import com.foodapp.entity.*;
import com.foodapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@foodapp.com").isEmpty()) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@foodapp.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("1234567890")
                    .role(Role.ADMIN)
                    .build();
            User savedAdmin = userRepository.save(admin);

            Cart adminCart = Cart.builder()
                    .user(savedAdmin)
                    .totalAmount(0.0)
                    .build();
            cartRepository.save(adminCart);
        }

        if (restaurantRepository.count() == 0) {
            List<RestaurantData> restaurantDataList = Arrays.asList(
                new RestaurantData("Taco Fiesta", "Authentic Mexican street tacos with fresh ingredients", "123 Spice St, Food City", "Mexican", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80", Arrays.asList(
                    new MenuItemData("Carnitas Tacos", "Slow-cooked pork tacos with cilantro and onion", 8.99, "Tacos", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Fish Tacos", "Crispy fish tacos with cabbage slaw", 10.99, "Tacos", "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80"),
                    new MenuItemData("Guacamole & Chips", "Fresh avocado dip with tortilla chips", 6.49, "Sides", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80"),
                    new MenuItemData("Churros", "Crispy cinnamon sugar churros", 5.49, "Desserts", "https://images.unsplash.com/photo-1619535860434-ba1d8fa122d6?w=500&q=80"),
                    new MenuItemData("Horchata", "Traditional cinnamon rice drink", 3.99, "Drinks", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80"),
                    new MenuItemData("Quesadilla", "Cheese and chicken quesadilla", 9.99, "Main", "https://images.unsplash.com/photo-1543577199-1cfba6ab0e00?w=500&q=80")
                )),
                new RestaurantData("Burger Barn", "Juicy burgers and loaded fries for fast cravings", "456 Burger Ave, Food City", "American", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80", Arrays.asList(
                    new MenuItemData("Classic Beef Burger", "Beef patty, lettuce, tomato, cheddar", 11.99, "Burgers", "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500&q=80"),
                    new MenuItemData("Bacon Cheeseburger", "Double bacon, cheese, and special sauce", 13.49, "Burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80"),
                    new MenuItemData("Spicy Chicken Burger", "Crispy chicken, hot sauce, pickles", 12.49, "Burgers", "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500&q=80"),
                    new MenuItemData("Loaded Fries", "Golden fries with cheese and bacon", 7.99, "Sides", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500&q=80"),
                    new MenuItemData("Onion Rings", "Crispy beer-battered onion rings", 5.99, "Sides", "https://images.unsplash.com/photo-1543577199-1cfba6ab0e00?w=500&q=80"),
                    new MenuItemData("Vanilla Milkshake", "Creamy vanilla shake with whipped cream", 5.99, "Drinks", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=500&q=80")
                )),
                new RestaurantData("Sushi Spot", "Fresh sushi and Japanese favorites delivered fast", "789 Sushi Ln, Food City", "Japanese", "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500&q=80", Arrays.asList(
                    new MenuItemData("California Roll", "Crab, avocado, cucumber in rice and nori", 12.99, "Sushi", "https://images.unsplash.com/photo-1553621042-f6e147245754?w=500&q=80"),
                    new MenuItemData("Salmon Nigiri", "Fresh salmon slices over seasoned rice", 10.49, "Sushi", "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=500&q=80"),
                    new MenuItemData("Dragon Roll", "Eel, avocado, cucumber and tempura", 15.99, "Sushi", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Spicy Tuna Roll", "Fresh tuna with spicy mayo", 13.99, "Sushi", "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500&q=80"),
                    new MenuItemData("Miso Soup", "Traditional Japanese miso soup", 4.99, "Soups", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Green Tea Ice Cream", "Matcha ice cream", 6.49, "Desserts", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80")
                )),
                new RestaurantData("Urban Tadka", "Street-style Indian meals with bold masala", "321 Curry Rd, Food City", "Indian", "https://images.unsplash.com/photo-1514516870924-7a5b81757c80?w=500&q=80", Arrays.asList(
                    new MenuItemData("Butter Chicken", "Creamy tomato gravy with tender chicken", 14.99, "Main", "https://images.unsplash.com/photo-1601050690767-7f45bc048bb5?w=500&q=80"),
                    new MenuItemData("Paneer Tikka Masala", "Smoky paneer in spiced tomato curry", 13.49, "Main", "https://images.unsplash.com/photo-1598514982536-ac52f13d9bd6?w=500&q=80"),
                    new MenuItemData("Garlic Naan", "Soft naan bread brushed with garlic butter", 4.49, "Sides", "https://images.unsplash.com/photo-1623860516254-4dec0ef33a4a?w=500&q=80"),
                    new MenuItemData("Masala Dosa", "Crispy dosa with spiced potato filling", 9.99, "Breakfast", "https://images.unsplash.com/photo-1602161326982-2f92db57c499?w=500&q=80"),
                    new MenuItemData("Chicken Biryani", "Fragrant rice with chicken and spices", 16.49, "Main", "https://images.unsplash.com/photo-1514516870924-7a5b81757c80?w=500&q=80"),
                    new MenuItemData("Samosa", "Crispy pastry with spiced potatoes", 5.99, "Sides", "https://images.unsplash.com/photo-1543577199-1cfba6ab0e00?w=500&q=80")
                )),
                new RestaurantData("Noodle Nook", "Slurp-worthy noodles and Chinese classics", "555 Wok Way, Food City", "Chinese", "https://images.unsplash.com/photo-1543577199-1cfba6ab0e00?w=500&q=80", Arrays.asList(
                    new MenuItemData("Veg Hakka Noodles", "Stir-fried noodles with crisp veggies", 10.99, "Noodles", "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=500&q=80"),
                    new MenuItemData("Chicken Lo Mein", "Savory chicken noodle stir fry", 12.99, "Noodles", "https://images.unsplash.com/photo-1543577199-1cfba6ab0e00?w=500&q=80"),
                    new MenuItemData("Paneer Chili", "Szechuan-style paneer with peppers", 11.49, "Starters", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Vegetable Spring Rolls", "Crispy rolls filled with veggies", 6.49, "Starters", "https://images.unsplash.com/photo-1604908811864-03e55e38800c?w=500&q=80"),
                    new MenuItemData("Sweet & Sour Chicken", "Crispy chicken in tangy sauce", 13.49, "Main", "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=500&q=80"),
                    new MenuItemData("Iced Lemon Tea", "Refreshing iced tea with lemon", 3.99, "Drinks", "https://images.unsplash.com/photo-1505253218806-44d1cafca1aa?w=500&q=80"),
                    new MenuItemData("Fried Rice", "Wok-fried rice with veggies and egg", 11.99, "Rice", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80")
                )),
                new RestaurantData("Sweet Spoon", "Desserts, shakes, and sweet treats", "888 Dessert Blvd, Food City", "Desserts", "https://images.unsplash.com/photo-1505253218806-44d1cafca1aa?w=500&q=80", Arrays.asList(
                    new MenuItemData("Chocolate Lava Cake", "Warm molten chocolate cake with ice cream", 8.99, "Desserts", "https://images.unsplash.com/photo-1588162492256-4948e0c5cf2d?w=500&q=80"),
                    new MenuItemData("Berry Cheesecake", "Creamy cheesecake with berry compote", 9.49, "Desserts", "https://images.unsplash.com/photo-1542219550-8367f7acb3a5?w=500&q=80"),
                    new MenuItemData("Cold Coffee", "Smooth coffee drink with cream and chocolate", 5.49, "Drinks", "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500&q=80"),
                    new MenuItemData("Brownie Fudge", "Rich fudgy brownie with caramel", 7.49, "Desserts", "https://images.unsplash.com/photo-1542821938-8ded43f46a55?w=500&q=80"),
                    new MenuItemData("Mango Smoothie", "Fresh mango smoothie", 6.49, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Ice Cream Sundae", "Three scoops with toppings", 10.99, "Desserts", "https://images.unsplash.com/photo-1588162492256-4948e0c5cf2d?w=500&q=80")
                )),
                new RestaurantData("Pizza Palace", "Authentic Italian pizza with fresh ingredients", "123 Pizza St, Food City", "Italian", "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80", Arrays.asList(
                    new MenuItemData("Margherita Pizza", "Classic pizza with tomato, mozzarella, and basil", 13.99, "Pizza", "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80"),
                    new MenuItemData("Pepperoni Deluxe", "Pepperoni, jalapeños, extra mozzarella", 16.49, "Pizza", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80"),
                    new MenuItemData("Four Cheese Pizza", "Mozzarella, parmesan, gorgonzola, ricotta", 15.99, "Pizza", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Cheesy Garlic Bread", "Crispy garlic bread topped with cheese", 7.49, "Sides", "https://images.unsplash.com/photo-1619535860434-ba1d8fa122d6?w=500&q=80"),
                    new MenuItemData("Caesar Salad", "Fresh romaine with caesar dressing", 8.99, "Salads", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Tiramisu", "Coffee-flavored Italian dessert", 8.99, "Desserts", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80")
                )),
                new RestaurantData("Green Bowl", "Healthy salads and fresh bowls", "222 Health Ave, Food City", "Healthy", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80", Arrays.asList(
                    new MenuItemData("Caesar Salad", "Romaine lettuce with croutons and parmesan", 10.99, "Salads", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Greek Salad", "Feta, olives, cucumber, tomato", 11.49, "Salads", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Quinoa Bowl", "Quinoa, chickpeas, veggies, tahini", 12.99, "Bowls", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Avocado Toast", "Sourdough with smashed avocado", 9.49, "Breakfast", "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=500&q=80"),
                    new MenuItemData("Green Smoothie", "Spinach, banana, almond milk", 6.49, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Chicken Salad", "Grilled chicken with mixed greens", 13.99, "Salads", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80")
                )),
                new RestaurantData("Seafood Shack", "Fresh seafood and ocean-inspired dishes", "444 Ocean Dr, Food City", "Seafood", "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=500&q=80", Arrays.asList(
                    new MenuItemData("Grilled Salmon", "Fresh salmon with lemon butter", 18.99, "Main", "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=500&q=80"),
                    new MenuItemData("Fish & Chips", "Crispy cod with french fries", 14.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Shrimp Scampi", "Garlic butter shrimp with pasta", 16.99, "Pasta", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Lobster Roll", "Buttery lobster in a bun", 24.99, "Sandwiches", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Clam Chowder", "Creamy New England clam chowder", 8.99, "Soups", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Crab Cakes", "Crispy crab cakes with tartar sauce", 13.99, "Starters", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Grilled Shrimp", "Spiced grilled shrimp skewers", 15.99, "Starters", "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=500&q=80")
                )),
                new RestaurantData("BBQ Pit", "Smoky barbecue and grilled favorites", "666 Smoke St, Food City", "Barbecue", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80", Arrays.asList(
                    new MenuItemData("Smoked Brisket", "Slow-smoked beef brisket", 20.99, "Main", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80"),
                    new MenuItemData("Baby Back Ribs", "Fall-off-the-bone pork ribs", 22.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Pulled Pork Sandwich", "Smoky pulled pork with coleslaw", 14.99, "Sandwiches", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80"),
                    new MenuItemData("BBQ Chicken", "Grilled chicken with BBQ sauce", 16.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Cornbread", "Sweet and moist cornbread", 5.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Coleslaw", "Creamy coleslaw", 4.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Baked Beans", "Smoky baked beans", 4.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80")
                )),
                new RestaurantData("Mediterranean Bites", "Fresh Mediterranean and Greek food", "999 Olive Ln, Food City", "Mediterranean", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80", Arrays.asList(
                    new MenuItemData("Falafel Wrap", "Crispy falafel with hummus and veggies", 10.99, "Wraps", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80"),
                    new MenuItemData("Greek Salad", "Fresh veggies, feta, olives", 11.49, "Salads", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80"),
                    new MenuItemData("Hummus Platter", "Creamy hummus with pita and veggies", 8.99, "Starters", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80"),
                    new MenuItemData("Moussaka", "Layered eggplant and meat casserole", 14.99, "Main", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80"),
                    new MenuItemData("Baklava", "Sweet nutty pastry with honey", 6.99, "Desserts", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Tzatziki", "Cucumber yogurt dip", 5.99, "Sides", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80"),
                    new MenuItemData("Lamb Gyro", "Marinated lamb in pita bread", 13.99, "Wraps", "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500&q=80")
                )),
                new RestaurantData("Thai Spice", "Authentic Thai cuisine with bold flavors", "777 Curry Rd, Food City", "Thai", "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=500&q=80", Arrays.asList(
                    new MenuItemData("Pad Thai", "Stir-fried rice noodles with shrimp", 13.99, "Noodles", "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=500&q=80"),
                    new MenuItemData("Green Curry", "Spicy green curry with chicken", 14.99, "Curries", "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=500&q=80"),
                    new MenuItemData("Tom Yum Soup", "Hot and sour shrimp soup", 9.99, "Soups", "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=500&q=80"),
                    new MenuItemData("Mango Sticky Rice", "Sweet rice with fresh mango", 8.99, "Desserts", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Thai Iced Tea", "Sweet creamy iced tea", 4.99, "Drinks", "https://images.unsplash.com/photo-1505253218806-44d1cafca1aa?w=500&q=80"),
                    new MenuItemData("Red Curry Duck", "Duck in red curry sauce", 18.99, "Curries", "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=500&q=80"),
                    new MenuItemData("Spring Rolls", "Fresh spring rolls with peanut sauce", 7.99, "Starters", "https://images.unsplash.com/photo-1604908811864-03e55e38800c?w=500&q=80")
                )),
                new RestaurantData("Diner Delights", "Classic American diner comfort food", "888 Diner Ave, Food City", "American", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80", Arrays.asList(
                    new MenuItemData("Pancake Stack", "Fluffy buttermilk pancakes", 9.99, "Breakfast", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80"),
                    new MenuItemData("Eggs Benedict", "Poached eggs with hollandaise sauce", 12.99, "Breakfast", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80"),
                    new MenuItemData("Chicken Fried Steak", "Crispy steak with gravy", 15.99, "Main", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80"),
                    new MenuItemData("Meatloaf", "Classic beef meatloaf with mashed potatoes", 14.99, "Main", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80"),
                    new MenuItemData("Milkshake Trio", "Three mini milkshakes", 10.99, "Drinks", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=500&q=80"),
                    new MenuItemData("Apple Pie", "Warm apple pie with ice cream", 8.99, "Desserts", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80"),
                    new MenuItemData("Club Sandwich", "Triple-decker turkey club", 13.99, "Sandwiches", "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=500&q=80")
                )),
                new RestaurantData("Vegan Haven", "Delicious plant-based vegan meals", "111 Green St, Food City", "Vegan", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80", Arrays.asList(
                    new MenuItemData("Vegan Burger", "Plant-based patty with all the fixings", 13.99, "Burgers", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Vegan Tacos", "Crispy vegan tacos with jackfruit", 11.99, "Tacos", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Vegan Buddha Bowl", "Quinoa, veggies, tahini dressing", 12.99, "Bowls", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Vegan Ice Cream", "Dairy-free ice cream", 7.99, "Desserts", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Vegan Mac & Cheese", "Creamy dairy-free mac and cheese", 11.99, "Pasta", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80"),
                    new MenuItemData("Vegan Curry", "Coconut curry with veggies and tofu", 14.99, "Curries", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500&q=80")
                )),
                new RestaurantData("French Crêperie", "Sweet and savory French crêpes", "333 Paris St, Food City", "French", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80", Arrays.asList(
                    new MenuItemData("Nutella Banana Crêpe", "Sweet crêpe with Nutella and banana", 9.99, "Sweet Crêpes", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("Ham & Cheese Crêpe", "Savory crêpe with ham and cheese", 11.99, "Savory Crêpes", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("Strawberry Crêpe", "Fresh strawberries and whipped cream", 10.99, "Sweet Crêpes", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("Spinach & Feta Crêpe", "Savory spinach and feta crêpe", 12.49, "Savory Crêpes", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("French Onion Soup", "Classic French onion soup", 8.99, "Soups", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("Crème Brûlée", "Creamy custard with caramelized sugar", 8.99, "Desserts", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80"),
                    new MenuItemData("Croissant", "Buttery French croissant", 4.99, "Pastries", "https://images.unsplash.com/photo-1558961363-fa8ff392c92b?w=500&q=80")
                )),
                new RestaurantData("Korean Kitchen", "Authentic Korean BBQ and street food", "555 Seoul St, Food City", "Korean", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80", Arrays.asList(
                    new MenuItemData("Bibimbap", "Mixed rice bowl with veggies and meat", 14.99, "Main", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Bulgogi", "Marinated beef BBQ", 16.99, "BBQ", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Kimchi Fried Rice", "Fried rice with kimchi", 11.99, "Rice", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Tteokbokki", "Spicy rice cakes", 10.99, "Street Food", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Japchae", "Glass noodles with veggies", 12.99, "Noodles", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Soju Cocktail", "Korean rice liquor cocktail", 7.99, "Drinks", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80"),
                    new MenuItemData("Haemul Pajeon", "Seafood and scallion pancake", 13.99, "Pancakes", "https://images.unsplash.com/photo-1590301157894-2940a39859e0?w=500&q=80")
                )),
                new RestaurantData("Caribbean Spice", "Tropical Caribbean flavors and jerk", "666 Island Ln, Food City", "Caribbean", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80", Arrays.asList(
                    new MenuItemData("Jerk Chicken", "Spicy jerk chicken with rice", 15.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Curry Goat", "Tender goat in spicy curry", 18.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Ackee & Saltfish", "Traditional Jamaican dish", 16.99, "Main", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
                    new MenuItemData("Plantains", "Fried sweet plantains", 5.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Rice & Peas", "Traditional Caribbean rice", 6.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Tropical Smoothie", "Mango, pineapple, coconut smoothie", 7.99, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Coconut Shrimp", "Crispy coconut shrimp with dip", 12.99, "Starters", "https://images.unsplash.com/photo-1485921325833-c519f76c4927?w=500&q=80")
                )),
                new RestaurantData("Ethiopian Eats", "Authentic Ethiopian injera and wots", "777 Africa St, Food City", "Ethiopian", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80", Arrays.asList(
                    new MenuItemData("Kitfo", "Marinated raw beef dish", 17.99, "Main", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Doro Wat", "Spicy chicken stew with eggs", 15.99, "Main", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Misir Wat", "Spicy red lentil stew", 12.99, "Main", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Gomen", "Collard greens with spices", 10.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Shiro", "Chickpea puree stew", 11.99, "Main", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Tej", "Honey wine (non-alcoholic version)", 6.99, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Vegetarian Combo", "Multiple veggie wots on injera", 16.99, "Combos", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80")
                )),
                new RestaurantData("Argentine Grill", "Argentine asado and empanadas", "888 Buenos Aires St, Food City", "Argentine", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80", Arrays.asList(
                    new MenuItemData("Asado", "Grilled beef ribs and sausages", 24.99, "Main", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80"),
                    new MenuItemData("Empanadas", "Beef and chicken empanadas", 10.99, "Starters", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Choripán", "Grilled chorizo sandwich", 11.99, "Sandwiches", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80"),
                    new MenuItemData("Provoleta", "Grilled provolone cheese", 9.99, "Starters", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Milanesa", "Breaded beef cutlet", 14.99, "Main", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80"),
                    new MenuItemData("Mate", "Traditional yerba mate drink", 5.99, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Dulce de Leche Pancakes", "Pancakes with dulce de leche", 11.99, "Breakfast", "https://images.unsplash.com/photo-1544025162-d76694265947?w=500&q=80")
                )),
                new RestaurantData("Middle Eastern Feast", "Middle Eastern and Persian delights", "999 Baghdad St, Food City", "Middle Eastern", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80", Arrays.asList(
                    new MenuItemData("Shawarma", "Marinated chicken shawarma wrap", 12.99, "Wraps", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Kebab Platter", "Grilled meat kebab platter", 18.99, "Main", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Falafel", "Crispy chickpea falafel balls", 8.99, "Starters", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Baba Ganoush", "Smoky eggplant dip", 7.99, "Dips", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Persian Rice", "Saffron basmati rice", 6.99, "Sides", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Baklava", "Sweet layered pastry", 7.99, "Desserts", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"),
                    new MenuItemData("Mint Lemonade", "Refreshing mint lemon drink", 5.99, "Drinks", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80")
                ))
            );

            for (RestaurantData data : restaurantDataList) {
                Restaurant restaurant = Restaurant.builder()
                        .name(data.name())
                        .description(data.description())
                        .address(data.address())
                        .cuisineType(data.cuisineType())
                        .imageUrl(data.imageUrl())
                        .isActive(true)
                        .build();
                restaurant = restaurantRepository.save(restaurant);

                for (MenuItemData menuData : data.menuItems()) {
                    MenuItem menuItem = MenuItem.builder()
                            .name(menuData.name())
                            .description(menuData.description())
                            .price(menuData.price())
                            .category(menuData.category())
                            .imageUrl(menuData.imageUrl())
                            .isAvailable(true)
                            .restaurant(restaurant)
                            .build();
                    menuItemRepository.save(menuItem);
                }
            }
        }
    }

    private record RestaurantData(String name, String description, String address, String cuisineType, String imageUrl, List<MenuItemData> menuItems) {}
    private record MenuItemData(String name, String description, Double price, String category, String imageUrl) {}
}
