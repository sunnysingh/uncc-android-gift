package com.mad.sunny.afinal;

/**
 * Name: Sunny Singh
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String FIREBASE_PERSON_KEY = "person_key";

    ArrayList<Person> persons = new ArrayList<Person>();
    PersonAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(Config.FIREBASE_URL);

        listView = (ListView) findViewById(R.id.listView);

        setTitle("Christmas List");

        if (!isConnected()) {
            Toast.makeText(this, "Error: Not connected to Internet", Toast.LENGTH_LONG).show();
            return;
        }

        Firebase refPersons = ref.child("Persons");
        refPersons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Person> newPersons = new ArrayList<Person>();
                int index = 0;

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    newPersons.add(index, person);
                    index++;
                }

                updateListView(newPersons);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (persons != null && position < persons.size()) {
                    Person selectedPerson = persons.get(position);

                    Intent intent = new Intent(MainActivity.this, GiftListActivity.class);
                    intent.putExtra(FIREBASE_PERSON_KEY, Person.generateKey(selectedPerson.getName()));
                    startActivity(intent);
                }
            }
        });
    }

    public void updateListView(ArrayList<Person> newPersons) {
        if (adapter != null) adapter.clear();
        persons = newPersons;
        adapter = new PersonAdapter(MainActivity.this, R.layout.row_item_layout, persons);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_person) {
            Log.d("TEST", "Open add activity");
            Intent intent = new Intent(this, AddPersonActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
