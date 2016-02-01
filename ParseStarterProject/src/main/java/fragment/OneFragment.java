package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.adapter.UsersAdapter;
import com.parse.starter.dummy.MapActivity;

import java.util.ArrayList;
import java.util.List;

import modelclass.PGeoPoint;


public class OneFragment extends Fragment {
    private ListView mListView;
    private ListAdapter mAdapter;
    private List<String> userList = new ArrayList<String>();
    private ArrayList<String> objectList = new ArrayList<>();
    private List<ParseUser> users;
    private int referalcode;
    private ArrayList<String> myusers = new ArrayList<>();
    private ParseUser parseUser;
    private UsersAdapter usersAdapter;
    private String user;
    private ArrayList<PGeoPoint> geoPointArrayList = new ArrayList<>();


    public OneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseUser = ParseUser.getCurrentUser();
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (item.getTitle().equals("map")) {
//            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
//            parseQuery.whereContainedIn("objectId", objectList);
//            parseQuery.findInBackground(new FindCallback<ParseUser>() {
//                @Override
//                public void done(List<ParseUser> objects, ParseException e) {
//                    for (int i = 0; i < objects.size(); i++) {
//                        PGeoPoint parseGeoPoint = new PGeoPoint();
//                        ParseGeoPoint geoPoint = objects.get(i).getParseGeoPoint("location");
//                        if(geoPoint!=null) {
//                            parseGeoPoint.setLat(geoPoint.getLatitude());
//                            parseGeoPoint.setLongg(geoPoint.getLongitude());
//                            geoPointArrayList.add(parseGeoPoint);
//                        }
//                    }
//                    Intent intent = new Intent(getActivity(), MapActivity.class);
//                    intent.putParcelableArrayListExtra("com.parse.starter.modelclass.PGeoPoint",  geoPointArrayList);
//                    intent.putStringArrayListExtra("users", (ArrayList<String>) userList);
//                    startActivity(intent);
//
//                }
//            });
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ReferalCode", Context.MODE_PRIVATE);
        referalcode = sharedPreferences.getInt("referalCode", -1);
        mListView = (ListView) view.findViewById(R.id.listview);
        myusers = (ArrayList<String>) parseUser.get("myusers");
        if (myusers != null) {
            getMyUsers();
        }

        // TODO: Change Adapter to display your content


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                parent.getItemAtPosition(position).toString();
//                String user = parent.getItemAtPosition(position).toString()
                user = userList.get(position).toString();


                if (user != null) {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereEqualTo("username", user);
                    parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            if (e == null) {
                                ParseGeoPoint geoPoint = object.getParseGeoPoint("location");

                                if (geoPoint != null) {
                                    Intent intent = new Intent(getActivity(), MapActivity.class);
                                    intent.putExtra("user", user);
                                    intent.putExtra("latt", geoPoint.getLatitude());
                                    intent.putExtra("longg", geoPoint.getLongitude());
                                    startActivity(intent);
                                }


                            }

                        }
                    });
//                    Map map = new Map();


                }


            }


        });

        return view;
    }

    public void getMyUsers() {
        userList.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereContainedIn("objectId", myusers);
        query.whereEqualTo("otp", referalcode);

        query.orderByAscending("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (int i = 0; i < objects.size(); i++) {

                        userList.add(objects.get(i).getUsername());
                        objectList.add(objects.get(i).getObjectId());

                    }
                    usersAdapter = new UsersAdapter(getActivity(), userList);
                    mListView.setAdapter(usersAdapter);

                } else {
                    // Something went wrong.
                }
            }
        });
    }




    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String user = parent.getItemAtPosition(position).toString();

        if (users.size() > 0) {
            Map map = new Map();

            PGeoPoint geoPoint = users.get(position).getParseGeoPoint("location");
            if (geoPoint != null) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("latt", geoPoint.getLatitude());
                intent.putExtra("longg", geoPoint.getLongitude());
                intent.putExtra("user", user);
                startActivity(intent);

            }
            Intent intent = new Intent(getActivity(), GetUserLocationService.class);
            intent.putExtra("user", user);
            getActivity().startService(intent);

        }


    }*/
}
