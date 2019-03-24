package com.example.efede.translator;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
    // This fragment show words list

public class WordListFragment extends Fragment { /*Android and Firebase component setup.*/
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root;
    private RecyclerView recyclerView;

    public WordListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_word_list, container, false);

       auth = FirebaseAuth.getInstance();
       firebaseDatabase = FirebaseDatabase.getInstance();


       recyclerView = view.findViewById(R.id.word_list_recycler);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

       return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(auth.getCurrentUser() != null){
            root = firebaseDatabase.getReference("words").child(auth.getCurrentUser().getUid());


            FirebaseRecyclerAdapter<Word,WordView> recyclerAdapter = new FirebaseRecyclerAdapter<Word, WordView>(Word.class,R.layout.word_view,WordView.class,root) {
                @Override
                protected void populateViewHolder(WordView viewHolder, Word model, int position) {

                    final String key = getRef(position).getKey();

                    viewHolder.setView(model.getSource(),model.getTarget());

                    View view = viewHolder.getView();

                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            DelteWord(v.getContext(),auth.getCurrentUser().getUid(),key,v);
                            return false;
                        }
                    });

                }
            };

            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    private void DelteWord(Context context, final String userId, final String key, final View view){



        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.warning);
        builder.setTitle(getString(R.string.delete_word));
        builder.setMessage(getString(R.string.question));
        builder.setNegativeButton("Reject",null);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(FireBaseConnector.FireBaseDeleteWord(userId,key)){
                    Snackbar.make(view.getRootView(),R.string.deleted,Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(view.getRootView(),R.string.wrong,Snackbar.LENGTH_LONG).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static class WordView extends RecyclerView.ViewHolder{
        private View view;
        private TextInputLayout targetFiled,sourceFiled;


        public WordView(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
            this.targetFiled = view.findViewById(R.id.word_list_target_field);
            this.sourceFiled = view.findViewById(R.id.word_list_source_field);


        }

        public View getView(){
            return view;
        }

        public void setView(String source,String target){
            this.sourceFiled.getEditText().setText(source);
            this.targetFiled.getEditText().setText(target);
        }
    }
}
