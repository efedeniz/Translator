package com.example.efede.translator;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranslateFragment extends Fragment {
    private FirebaseAuth auth;                            /*Android and Firebase component setup.*/
    private TextInputLayout sourceField,resultField;
    private Button translateBtn;
    private ImageButton swapBtn;
    private static String condition = "en-tr";
    public TranslateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_translate, container, false);

       auth = FirebaseAuth.getInstance();

       sourceField = view.findViewById(R.id.sorce_field);
       resultField = view.findViewById(R.id.target_field);
       swapBtn = view.findViewById(R.id.swap_btn);
       translateBtn = view.findViewById(R.id.translate_btn);

       swapBtn.setOnClickListener(new View.OnClickListener() {  // This button change translate condition
           @Override
           public void onClick(View v) {
               if(condition.equals("en-tr")){

                   condition = "tr-en";

                   Snackbar.make(swapBtn.getRootView(),"TR-EN",Snackbar.LENGTH_LONG).show();
               }else {

                   condition = "en-tr";
                   Snackbar.make(swapBtn.getRootView(),"EN-TR",Snackbar.LENGTH_LONG).show();
               }
           }
       });

       translateBtn.setOnClickListener(new View.OnClickListener() { // Get word from user and translate to word.
           @Override
           public void onClick(View v) {

               EditText targetEditText = sourceField.getEditText();
               String sourceWord = String.valueOf(targetEditText.getText()).trim();

               if(!sourceWord.isEmpty()){
                    addNewWord(sourceWord,translateBtn.getContext());
               }else {
                   Snackbar.make(translateBtn.getRootView(),R.string.word_first,Snackbar.LENGTH_LONG).show();
               }

           }
       });

       sourceField.getEditText().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sourceField.getEditText().setText("");
               resultField.getEditText().setText("");
           }
       });


       return  view;
    }

    private void addNewWord(String sourceWord, Context context){

        String result = Translate.Translate(sourceWord,condition,context);

        resultField.getEditText().setText(result);

        if(auth.getCurrentUser() != null){ // if user login add word into database table.
            if(FireBaseConnector.FireBaseAddWord(sourceWord,result,auth.getCurrentUser().getUid())){
                Toast.makeText(context,getString(R.string.s_word_list),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context,getString(R.string.wrong),Toast.LENGTH_LONG).show();
            }
        }

    }

}
