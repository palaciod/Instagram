package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instagram.Activities.HomeFeedActivity;
import com.example.instagram.Activities.postActivity;
import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.Models.Post;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link post_description.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link post_description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class post_description extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "post_description_fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText caption;

    private OnFragmentInteractionListener mListener;
    private TextView backButton;
    private TextView postButton;
    private FragmentManager fragmentManager;


    public post_description() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment post_description.
     */
    // TODO: Rename and change types and number of parameters
    public static post_description newInstance(String param1, String param2) {
        post_description fragment = new post_description();
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
        final View view = inflater.inflate(R.layout.fragment_post_description,container,false);
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        postButton = view.findViewById(R.id.postButton);
        caption = view.findViewById(R.id.caption);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String captionString = caption.getText().toString();
                mListener.getPost().setCaption(captionString);
                mListener.getFirebaseMethods().addPostToFireBase(mListener.getPost(), mListener.getUri());
                System.out.println("This is what the caption says: " + mListener.getPost().getCaption());
                Intent intent = new Intent(getActivity(), HomeFeedActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

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
        void sendUri(Post post);

        Uri getUri();

        Post getPost();
        FirebaseMethods getFirebaseMethods();
    }
}
