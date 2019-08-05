package com.example.instagram;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.instagram.Activities.AccountProfile;
import com.example.instagram.Models.searchRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link searchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText searchBar;

    // Testing Var
    private ArrayList<String> usernames;
    private OnFragmentInteractionListener mListener;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * The method below is to initialize an arraylist to add to the recyclerview. This is made for testing purposes. I wanted to make sure that the recyclerview works first
     * before adding usernames from firebase.
     */


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search,container,false);
        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        searchBar = view.findViewById(R.id.search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            /**
             * Restructure firebase user_account_details to be able to query. Replace userID with username.
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                //final String text = searchBar.getText().toString().toLowerCase();
                // FireBase Methods
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = mFirebaseDatabase.getReference().child("user_account_details");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String text = searchBar.getText().toString().toLowerCase();
                        ArrayList<String> usernameList = new ArrayList<>();
                        ArrayList<String> userID = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            System.out.println(ds.child("userName").getValue());
                            System.out.println(text + "This is my text");
                            if(text.equals(ds.child("userName").getValue().toString())){
                                System.out.println(true);
                                usernameList.add(text);
                                userID.add(ds.getKey().toString());
                                RecyclerView recyclerView = view.findViewById(R.id.recycler);
                                searchRecyclerView adapter = new searchRecyclerView(view.getContext(), usernameList, userID);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            }else{
                                System.out.println(false);
                            }
                        }


                        // Our SearchRecycler Initializer


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        try{
            mListener = (OnFragmentInteractionListener) getActivity();
        }catch (ClassCastException e){
            //Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
            System.out.println("onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
