package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class AddPersonActivity extends AppCompatActivity {

    private EditText editTextPersonName;
    private EditText editTextBudget;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Person");

        editTextPersonName = (EditText) findViewById(R.id.editTextPersonName);
        editTextBudget = (EditText) findViewById(R.id.editTextBudget);

        Firebase.setAndroidContext(this);
        ref = new Firebase(Config.FIREBASE_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_person) {
            final String personName = editTextPersonName.getText().toString();
            String budget = editTextBudget.getText().toString();
            int budgetAmount = 0;

            if (personName.equals("")) {
                Toast.makeText(this, "Please enter person's name", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (budget.equals("")) {
                Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show();
                return true;
            }

            budgetAmount = Integer.parseInt(budget);

            if (budgetAmount < 0) {
                Toast.makeText(this, "Please enter a positive budget amount", Toast.LENGTH_SHORT).show();
                return true;
            }

            final Person person = new Person();
            person.setName(personName);
            person.setBudget(budgetAmount);
            person.setSpent(0);
            person.setGiftsBought(0);
            person.setGifts(new ArrayList<Gift>());

            // Check for person existing in Firebase
            Firebase refPerson = ref.child("Persons").child(Person.generateKey(personName));
            refPerson.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Person existingPerson = dataSnapshot.getValue(Person.class);

                    if (existingPerson != null) {
                        // Person already exists in Firebase
                        Toast.makeText(AddPersonActivity.this, "This person already exists", Toast.LENGTH_LONG).show();
                    } else {
                        // Add new person in Firebase
                        ref.child("Persons").child(Person.generateKey(personName)).setValue(person);
                        Toast.makeText(AddPersonActivity.this, "Person added!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(AddPersonActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
