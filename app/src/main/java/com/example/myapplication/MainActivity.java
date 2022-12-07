package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.DataModel.Note;
import com.example.myapplication.SQLDatabase.NotesDataSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    /**
     * User Interface Elements Declarations
     */
    Toolbar appToolbar ;

    //String [] testNotesTitleList = {"Note 1", "Note 2", "Note 3" , "Note 4"} ;
    //String [] testNotesDateList = {"date 1", "date 2", "date 3","date 4" } ;

    List<Note> allNotes= new ArrayList<Note>();

    /**
     * Data Source
     */
    NotesDataSource dbSource;
    /**
     * Array adapter
     */
    CustomListAllAdapter customListAllAdapter;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lvAllNotesList = findViewById(R.id.listAllNotes);
        /**
         * Setup the Database source
         */
        dbSource= new NotesDataSource(this);
        dbSource.open();
        /**
         * Setup the user interfaces elements
         */
        setupToolbar();
        /**
         * setup the List All Notes Adapter.
         */
        allNotes= dbSource.getAllNotes();
        customListAllAdapter = new CustomListAllAdapter(this,allNotes);
        lvAllNotesList.setAdapter(customListAllAdapter);

    }

    /**
     * Setup the Toolbar.
     */
    private void setupToolbar() {

        appToolbar=(Toolbar)findViewById(R.id.appToolbar);
        appToolbar.setTitle(R.string.theAppName);
        appToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(appToolbar);


    }

    /**
     * Event handler..
     * @param view
     */
    public void onClickFloatingButton(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fragment_add_note);
        Button btnCancel= (Button) dialog.findViewById(R.id.btnCancel);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        dialog.setTitle("Add New Note");
        final EditText etTitle = (EditText)dialog.findViewById(R.id.etNoteTitle) ;
        final EditText etText = (EditText)dialog.findViewById(R.id.etNoteText) ;



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = etTitle.getText().toString();
                String nText = etText.getText().toString();
                if(!nText.equals("") && !nTitle.equals("")){
                    // add to database
                    Note note =dbSource.addNewNote(nTitle,nText);
                    // add to the method
                    Toast.makeText(getApplicationContext(),note.getTitle()+" Added !",Toast.LENGTH_LONG).show();
                    customListAllAdapter.add(note);
                    customListAllAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else{
                    Toast.makeText(dialog.getContext(),"fill the input plz!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}