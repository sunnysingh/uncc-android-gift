package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddGiftActivity extends AppCompatActivity {

    Person person;
    ListView listView;
    GiftAdapter adapter;
    ArrayList<Gift> gifts = new ArrayList<Gift>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Gift");

        String personKey = getIntent().getStringExtra(GiftListActivity.FIREBASE_PERSON_KEY);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(Config.FIREBASE_URL);

        listView = (ListView) findViewById(R.id.listViewGifts);

        Firebase refPerson = ref.child("Persons").child(personKey);
        refPerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                person = snapshot.getValue(Person.class);

                Firebase refGifts = ref.child("Gifts");
                refGifts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<Gift> newGifts = new ArrayList<Gift>();
                        int index = 0;
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Gift gift = postSnapshot.getValue(Gift.class);
                            int moneyLeft = person.getBudget() - person.getSpent();
                            if (moneyLeft >= gift.getPrice()) {
                                newGifts.add(index, gift);
                                index++;
                            }
                        }
                        updateListView(newGifts);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(AddGiftActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AddGiftActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gifts != null && position < gifts.size()) {
                    Gift selectedGift = gifts.get(position);

                    ArrayList<Gift> personsGifts = new ArrayList<Gift>();
                    personsGifts.add(selectedGift);

                    person.setGiftsBought(person.getGiftsBought() + 1);
                    person.setSpent(person.getSpent() + (int) selectedGift.getPrice());
                    person.setGifts(personsGifts);

                    Firebase refPerson = ref.child("Persons").child(Person.generateKey(person.getName()));
                    refPerson.setValue(person);

                    Toast.makeText(AddGiftActivity.this, "Gift bought!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }

    public void updateListView(ArrayList<Gift> newGifts) {
        if (adapter != null) adapter.clear();
        gifts = newGifts;
        adapter = new GiftAdapter(AddGiftActivity.this, R.layout.row_gift_layout, gifts);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
