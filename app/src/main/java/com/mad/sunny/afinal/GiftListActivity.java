package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GiftListActivity extends AppCompatActivity {

    static final String FIREBASE_PERSON_KEY = "person_key";

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String personKey = getIntent().getStringExtra(MainActivity.FIREBASE_PERSON_KEY);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(Config.FIREBASE_URL);

        Firebase refPerson = ref.child("Persons").child(personKey);
        refPerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                person = snapshot.getValue(Person.class);

                setTitle(person.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(GiftListActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_gift_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_gift) {
            if (person.getSpent() < person.getBudget()) {
                Intent intent = new Intent(this, AddGiftActivity.class);
                intent.putExtra(FIREBASE_PERSON_KEY, Person.generateKey(person.getName()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Remaining budget is $0!", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
