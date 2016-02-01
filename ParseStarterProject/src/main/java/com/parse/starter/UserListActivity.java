package com.parse.starter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fragment.OneFragment;
import fragment.TwoFragment;
import service.UpadateUserLocation;

public class UserListActivity extends AppCompatActivity {

    ParseUser parseUser;
    int referalCode;
    SharedPreferences sharedPreferences;
    OneFragment oneFragment;
    TwoFragment twoFragment;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        setupViewPager(viewPager);
        parseUser = ParseUser.getCurrentUser();

        sharedPreferences = getSharedPreferences("ReferalCode", MODE_PRIVATE);
        referalCode = sharedPreferences.getInt("referalCode", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("referalCode", referalCode).commit();


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    if (oneFragment != null) {
//                        oneFragment.getMyUsers();
//                    }
//                } else {
//                    if (twoFragment != null) {
//                        twoFragment.getUserRequests();
//                    }
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        Intent intent = new Intent(UserListActivity.this, UpadateUserLocation.class);
        startService(intent);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(oneFragment, "Users");
        adapter.addFragment(twoFragment, "User Requests");
        viewPager.setAdapter(adapter);
    }

    public void updateUserStatus(String user) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", user);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
//                    if (parseUser.get("rejectlist") != null) {
                    parseUser.addUnique("myusers", object.getObjectId());
                    parseUser.saveInBackground();
//                        rejectList.addAll((Collection<? extends String>) parseUser.get("rejectlist"));
//                        parseUser.removeAll("rejectlist", rejectList);
//                        rejectList.remove(object.getObjectId());
//                        parseUser.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                parseUser.addAllUnique("rejectlist", rejectList);
//                                parseUser.saveInBackground();
//
//                            }
//                        });

//                    }
//                else {
//                        parseUser.addUnique("myusers", object.getObjectId());
//                        parseUser.saveInBackground();
//
//                    }
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                parseUser.logOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("referalCode", -1).commit();
                Intent intent = new Intent(UserListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_refer:
                boolean admin = parseUser.getBoolean("isAdmin");
                if (admin == true) {
                    int ref = sharedPreferences.getInt("referalCode", -1);
                    final ParseUser parseUser = ParseUser.getCurrentUser();
                    if (ref == -1) {
                        if (parseUser.getBoolean("isAdmin") == true) {
                            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("AdminOTP");
                            userQuery.whereEqualTo("createdBy", parseUser);
                            userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        referalCode = object.getInt("otp");
                                        if (referalCode != -1) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("referalCode", referalCode).commit();
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please register with the otp " + referalCode);
                                            sendIntent.setType("text/plain");
                                            startActivity(sendIntent);
                                        }
                                    } else {
                                        ParseObject parseObject = new ParseObject("AdminOTP");
                                        parseObject.put("createdBy", parseUser);
                                        Random random = new Random();
                                        referalCode = random.nextInt(4552);
                                        parseObject.put("otp", referalCode);
                                        parseObject.saveInBackground();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("referalCode", referalCode).commit();
                                        Intent sendIntent = new Intent();
                                        sendIntent.setAction(Intent.ACTION_SEND);
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Please register with the otp " + referalCode);
                                        sendIntent.setType("text/plain");
                                        startActivity(sendIntent);
                                    }

                                }
                            });
                        }
                    } else {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Please register with this otp " + ref);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }

                    break;
                }

        }
        return super.onOptionsItemSelected(item);

    }

    public void removeUser(String user) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", user);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    parseUser.addUnique("rejectlist", object.getObjectId());
                    parseUser.saveInBackground();
                }

            }
        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
