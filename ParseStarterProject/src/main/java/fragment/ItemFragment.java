package fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;

import service.GetUserLocationService;


public class ItemFragment extends android.app.Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private List<String> userList = new ArrayList<String>();
    private List<ParseUser> users;
    private int referalcode;


    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referalcode = getArguments().getInt("referalCode");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("adminReferalCode",referalcode);
        query.orderByAscending("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    users = objects;
                    for (int i = 0; i < objects.size(); i++) {

                        userList.add(objects.get(i).getUsername());

                    }

                    mAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, userList);

                    // Set the adapter

                    if (userList.size() == 0)
                        setEmptyText("No record found!");
                    else
                        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
                } else {
                    // Something went wrong.
                }
            }
        });


        // TODO: Change Adapter to display your content


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }


    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String user = parent.getItemAtPosition(position).toString();

        if (users.size() > 0) {
//            Map map = new Map();
//
//            Bundle bundle = new Bundle();
//            PGeoPoint geoPoint = users.get(position).getParseGeoPoint("location");
//            if(geoPoint!=null) {
//                bundle.putDouble("latt", geoPoint.getLatitude());
//                bundle.putDouble("longg", geoPoint.getLongitude());
//                bundle.putString("user", user);
//                map.setArguments(bundle);
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.framelaout, map).addToBackStack(null).commit();

//            }
            Intent intent = new Intent(getActivity(), GetUserLocationService.class);
            intent.putExtra("user", user);
            getActivity().startService(intent);

        }


    }
}
