package com.example.cmput301f22t13.datalayer;

import androidx.annotation.Nullable;

import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShoppingListDL extends FireBaseDL{
    static private ShoppingListDL shoppingListDL;
    private static FireBaseDL fb;

    public static ResultListener listener;

    public static ArrayList<CountedIngredient> shoppingListStorage = new ArrayList<>();

    public static ShoppingListDL getInstance() {
        if (shoppingListDL == null) {
            shoppingListDL = new ShoppingListDL();
            fb = FireBaseDL.getFirebaseDL();
            populateShoppingListOnStartup();
        }
        return shoppingListDL;
    }

    private static void populateShoppingListOnStartup() {
        CollectionReference getShoppingList = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Shopping List");

        getShoppingList.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                shoppingListStorage.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String ingredientName = doc.getString("Ingredient");
                    int count = ((Long) doc.getData().get("Count")).intValue();
                    IngredientItem i = new IngredientItem();
                    i.setName(ingredientName);
                    shoppingListStorage.add(new CountedIngredient(i, count));
                }
                listener.onSuccess();
            }
        });
    }
}
