package fragment;

/**
 * Created by root on 4/1/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.adapter.UserRequestAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TwoFragment extends Fragment {
    private ListView mListView;
    private List<String> userList = new ArrayList<String>();
    private List<ParseUser> users;
    private ArrayList<String> rejectid = new ArrayList<>();
    private int referalcode;
    private UserRequestAdapter requestAdapter;
    private ParseObject parseUser;
    private ArrayList<String> myUsers = new ArrayList<>();
    private ArrayList<String> rejectedUsers = new ArrayList<>();
    private ArrayList<String> userobjectList = new ArrayList<>();


    public TwoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseUser = ParseUser.getCurrentUser();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ReferalCode", Context.MODE_PRIVATE);
        referalcode = sharedPreferences.getInt("referalCode", -1);
        if (referalcode == -1) {
            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("AdminOTP");
            userQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser());
            userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        referalcode = object.getInt("otp");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("referalCode", referalcode).commit();
//                        getObjectId();
                        getUserRequests();


                    }
                }
            });
        } else {
//            getObjectId();
            getUserRequests();

        }


        return view;
    }

    public void getUserRequests() {
        userList.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("otp", referalcode);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (int i = 0; i < objects.size(); i++) {

                        userList.add(objects.get(i).getObjectId());


                    }
                    getUserList((ArrayList) userList);

//                    parseUser.addAllUnique("rejectlist", userList);
//                    parseUser.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            getUserList((ArrayList) parseUser.get("rejectlist"));
//
//                        }
//                    });

//                    rejectid.clear();

                }
            }
        });

    }

    private void getUserList(ArrayList rejectlist) {
        if (parseUser.get("myusers") != null)
            myUsers.addAll((Collection<? extends String>) parseUser.get("myusers"));
        if (parseUser.get("rejectlist") != null)
            rejectedUsers.addAll((Collection<? extends String>) parseUser.get("rejectlist"));

        if (rejectlist.size() > 0 || myUsers.size() > 0) {
            for (int i = 0; i < myUsers.size(); i++) {
                String rejectId = myUsers.get(i).toString();
                for (int j = 0; j < rejectlist.size(); j++) {
                    if (rejectlist.get(j).equals(rejectId)) {
                        rejectlist.remove(j);
                        break;
                    }

                }

            }
        }
        if (rejectlist.size() > 0 && rejectedUsers.size() > 0) {
            for (int i = 0; i < rejectedUsers.size(); i++) {
                String rejectId = rejectedUsers.get(i).toString();
                for (int j = 0; j < rejectlist.size(); j++) {
                    if (rejectlist.get(j).equals(rejectId)) {
//                        filteredList.add(myUsers.get(i).toString());
                        rejectlist.remove(j);
                        break;
                    }

                }

            }
        }

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereContainedIn("objectId", rejectlist);
//        userParseQuery.whereNotContainedIn("myusers", rejectlist);
//        userParseQuery.whereNotContainedIn("rejectlist",rejectlist);
//        userParseQuery.whereEqualTo("otp",referalcode);
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        userobjectList.add(objects.get(i).getUsername());
                    }
                    requestAdapter = new UserRequestAdapter(getActivity(), userobjectList);
                    mListView.setAdapter(requestAdapter);
                }


            }
        });


    }


}


