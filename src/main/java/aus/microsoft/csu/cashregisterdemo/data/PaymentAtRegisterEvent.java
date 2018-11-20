package aus.microsoft.csu.cashregisterdemo.data;

import java.util.ArrayList;

public class PaymentAtRegisterEvent {

   private String customerId;

   private ArrayList<Article> shoppingList = new ArrayList<>();

   private double shoppingTotal;

   public String getCustomerId() {
      return customerId;
   }

   public void setCustomerId(String customerId) {
      this.customerId = customerId;
   }

   public ArrayList<Article> getShoppingList() {
      return shoppingList;
   }

   public void setShoppingList(ArrayList<Article> shoppingList) {
      this.shoppingList = shoppingList;
   }

   public double getShoppingTotal() {
      return shoppingTotal;
   }

   public void setShoppingTotal(double shoppingTotal) {
      this.shoppingTotal = shoppingTotal;
   }
}
