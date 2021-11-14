/** @author AlexChhieng-Henson
 *  11/09/21
 *  Western Governors University
 *  Software 1 - C482
 */

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/** This class is the starting point for the entire inventory program.
 *  <p>FUTURE ENHANCEMENTS - If I were to update this inventory application, I would change a few different things.
 *  First, I would display information for whether a part was in-house or outsourced because as of right now,
 *  this information is treated pretty arbitrarily. Second, I would want to add pictures of the various parts
 *  and products on the add and modify screens so that there is more clarity on exactly what each part or product
 *  looks like.  Finally, I would like to add features to the TableViews that allow us to sort by id numbers,
 *  name, or inventory numbers so that with a large enough list of parts or products, we could organize the
 *  information better.
 */
public class Main extends Application {

    /** This method loads the initial stage and "Main Form", the first scene for the inventory program. */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Main Form");
        stage.setScene(new Scene(root, 950, 320));
        stage.show();
    }

    /** This method is the main method that will instantiate all the initial sample data.
     *  The Javadoc folder is located in the C:\Users\chhie\Desktop\WGU\C482 - Software 1\Javadoc. 
     *  @param args an array of String arguments
     */
    public static void main(String[] args) {
        InHouse brakes = new InHouse(Inventory.incrementPartId(), "Brakes", 14.99, 10, 1, 50, 111);
        InHouse wheel = new InHouse(Inventory.incrementPartId(), "Wheel", 10.99, 16, 1, 50, 112);
        InHouse seat = new InHouse(Inventory.incrementPartId(), "Seat", 14.99, 10, 1, 50, 113);
        Outsourced handleBars = new Outsourced(Inventory.incrementPartId(),"Handle Bars", 10.99, 15, 1, 35, "Huffy");

        Inventory.addPart(brakes);
        Inventory.addPart(wheel);
        Inventory.addPart(seat);
        Inventory.addPart(handleBars);

        Product giantBike = new Product(Inventory.incrementProductId(), "Giant Bike", 299.99, 5, 1, 10);
        Product tricycle = new Product(Inventory.incrementProductId(), "Tricycle", 99.99, 3, 1, 10);
        Product mountainBike = new Product(Inventory.incrementProductId(), "Mountain Bike", 399.99, 4, 1, 10);

        Inventory.addProduct(giantBike);
        giantBike.addAssociatedPart(brakes);
        giantBike.addAssociatedPart(wheel);
        giantBike.addAssociatedPart(seat);

        Inventory.addProduct(tricycle);
        tricycle.addAssociatedPart(brakes);
        tricycle.addAssociatedPart(wheel);
        tricycle.addAssociatedPart(seat);

        Inventory.addProduct(mountainBike);
        mountainBike.addAssociatedPart(brakes);
        mountainBike.addAssociatedPart(wheel);
        mountainBike.addAssociatedPart(seat);
        mountainBike.addAssociatedPart(handleBars);

        launch(args);
    }
}
