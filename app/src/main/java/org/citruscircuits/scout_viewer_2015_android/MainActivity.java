package org.citruscircuits.scout_viewer_2015_android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import io.realm.Realm;
import io.realm.RealmResults;

import org.citruscircuits.realm.*;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    public static ArrayList<Team> teamList = new ArrayList<>();
    public static ArrayList<Team> firstPickList = new ArrayList<>();
    public static ArrayList<Team> secondPickList = new ArrayList<>();
    public static ArrayList<Team> thirdPickList = new ArrayList<>();
    public static ArrayList<Team> thirdPickListLandfill = new ArrayList<>();
    public static ArrayList<Team> mechanismList = new ArrayList<>();
    public static ArrayList<Team> predictedSeedList = new ArrayList<>();

    public static Realm realm;

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static ArrayList<Match> matchList = new ArrayList<>();
    public static ArrayList<String> chartList = new ArrayList<>();
    public static ArrayList<String> teamDataList = new ArrayList<>();
    public static ArrayList<ArrayList> tdl = new ArrayList<ArrayList>();

    static final int REQUEST_LINK_TO_DBX = 1337;  // This value is up to you

    DbxAccountManager mDbxAcctMgr;
    DbxFileSystem dbxFs;

    ViewPager mViewPager;
    private String appKey = "PLACE_APP_KEY_HERE";
    private String appSecret = "PLACE_APP_SECRET_HERE";


    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);

        MainActivity.context = getApplicationContext();

        Log.e(Constants.LOG_TAG, "Running Dbx link code!  Has linked account: " + mDbxAcctMgr.hasLinkedAccount());

        if (!mDbxAcctMgr.hasLinkedAccount()) {
            mDbxAcctMgr.startLink(this, REQUEST_LINK_TO_DBX);
        } else {
            onLinkToDbx();
        }

        chartList.clear();
        chartList.add("Totes moved into auto zone");
        chartList.add("Recons moved into auto zone");
        chartList.add("Totes stacked");
        chartList.add("Recon levels");
        chartList.add("Noodles contributed");
        chartList.add("Recons stacked");
        chartList.add("Recons picked up");
        chartList.add("Totes picked up from ground");
        chartList.add("Litter Dropped");
        chartList.add("Stacks Damaged");
        chartList.add("Max tote height");
        chartList.add("Litter thrown to other side");

        tdl.clear();

        tdl.add(new ArrayList() {{
            add("First Pick Ability");
            add("getFirstPickAbility");
            add("float");
            add("Robot Ability");
        }});
        tdl.add(new ArrayList() {{add("Second Pick Ability");add("getSecondPickAbility");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Third Pick Ability");add("getThirdPickAbility");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Third Pick Ability Landfill");add("getThirdPickAbilityLandfill");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Score");add("getAverageScore");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Stacked Tote Set Percentage");add("getIsStackedToteSetPercentage");add("float");add("Auto");}});
        tdl.add(new ArrayList() {{add("Most Commmn Recon Acquisition Type");add("getMostCommonReconAcquisitionType");add("string");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Step Recons Acquired In Auto");add("getAvgStepReconsAcquiredInAuto");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Step Recon Success Rate");add("getStepReconSuccessRateInAuto");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Stacking Ability");add("getStackingAbility");add("float");add("Stacking");}});
        tdl.add(new ArrayList() {{add("Avg. Max Height Stacks");add("getAvgNumMaxHeightStacks");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Capped Five/Six Stacks");add("getAvgNumCappedSixStacks");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Stack Placing");add("getAvgStackPlacing");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Avg. Num Totes Stacked");add("getAvgNumTotesStacked");add("float");add("Totes");}});
        tdl.add(new ArrayList() {{add("Avg. Max Tote Height");add("getAvgMaxFieldToteHeight");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg Number of Totes From Landfill");add("getAvgNumTotesPickedUpFromGround");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Recon Ability");add("getReconAbility");add("float");add("Recons");}});
        tdl.add(new ArrayList() {{add("Avg. Num Recons Levels");add("getAvgNumReconLevels");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Teleop Recons From Step");add("getAvgNumTeleopReconsFromStep");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Recons Picked Up");add("getAvgNumReconsPickedUp");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Horizontal Recons Picked Up");add("getAvgNumHorizontalReconsPickedUp");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Vertical Recons Picked Up");add("getAvgNumVerticalReconsPickedUp");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Recons Stacked");add("getAvgNumReconsStacked");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Max Recon Height");add("getAvgMaxReconHeight");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Recon Reliability");add("getReconReliability");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Noodle Reliability");add("getNoodleReliability");add("float");add("Noodles");}});
        tdl.add(new ArrayList() {{add("Avg. Num Noodles Contributed");add("getAvgNumNoodlesContributed");add("float");add("FALSE");}});
        tdl.add(new ArrayList() {{add("Avg. Num Litter Dropped");add("getAvgNumLitterDropped");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Avg. Num Totes From HP");add("getAvgNumTotesFromHP");add("float");add("Human Player");}});
        tdl.add(new ArrayList() {{add("Avg. HP Loading");add("getAvgHumanPlayerLoading");add("float");add("FALSE");}});

        tdl.add(new ArrayList() {{add("Avg. Driver Ability");add("getDriverAbility");add("float");add("Super Scout");}});

        tdl.add(new ArrayList() {{add("Pit Organization");add("getPitOrganization");add("string");add("Pit Scout");add("hax");}});
        tdl.add(new ArrayList() {{add("Programming Language");add("getProgrammingLanguage");add("string");add("FALSE");add("hax");}});
        tdl.add(new ArrayList() {{add("Can Cheesecake?");add("isCanMountMechanism");add("boolean");add("FALSE");add("hax");}});
        tdl.add(new ArrayList() {{add("Willing to Cheesecake?");add("isWillingToMount");add("boolean");add("FALSE");add("hax");}});
        tdl.add(new ArrayList() {{add("Easecakeability");add("getEaseOfMounting");add("float");add("FALSE");add("hax");}});
        tdl.add(new ArrayList() {{add("Pit Notes");add("getPitNotes");add("string");add("FALSE");add("hax");}});

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public void onLinkToDbx() {
        Log.e(Constants.LOG_TAG, "Running onLinkToDbx.");
        try {
            dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        } catch (DbxException.Unauthorized e) {
            e.printStackTrace();
        }

        Log.e(Constants.LOG_TAG, Boolean.toString(Globals.gotDB));

        if (!Globals.gotDB) {
            getDatabase();
            Globals.gotDB = true;
        }


        realm = Realm.getInstance(this, Constants.REALM_FILE);
        arraySetup(false);

    }

    public void getDatabase() {
        DbxFile dbfile = null;
        try {
            dbfile = dbxFs.open(new DbxPath("/Database File/" + Constants.REALM_FILE));
            FileInputStream stream = dbfile.getReadStream();

            File file = new File(Constants.REALM_FILE);
            file.delete();

            FileOutputStream out = openFileOutput(Constants.REALM_FILE, Context.MODE_PRIVATE);


            int b = 0;

            while ((b = stream.read()) != -1) {
                out.write(b);
            }

            out.close();
            stream.close();
            Log.e(Constants.LOG_TAG, "Got database file \"" + Constants.REALM_FILE + "\"");

        } catch (DbxException e) {
            e.printStackTrace();
            Log.e(Constants.LOG_TAG, "Hit DbxException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Constants.LOG_TAG, "Hit IOException: " + e.getMessage());
        }
        if (dbfile != null) {
            dbfile.close();
        } else {
            Log.e(Constants.LOG_TAG, "OH GOD EVERYTHING IS FUCKED UP!");
        }
    }

    public void arraySetup(Boolean update) {
        matchSetUp(update);
        teamSetUp(update);
        firstTeamPickSetUp(update);
        secondTeamPickSetUp(update);
        predictedSeedSetUp(update);
        mechanismSetUp(update);
        thirdLandfillTeamPickSetUp(update);
        thirdTeamPickSetUp(update);
    }

    public void teamSetUp(Boolean update){
        if (teamList.size() == 0) {
            RealmResults<Team> result = realm.allObjects(Team.class);

            for (Team team : result) {
                teamList.add(team);
            }

            Collections.sort(teamList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getNumber() < rhs.getNumber()) {
                        return -1;
                    } else if (lhs.getNumber() > rhs.getNumber()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            Log.e(Constants.LOG_TAG, "teamList updated");
        } else {
            if (update) {
                teamList = new ArrayList<>();
                teamSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating teamList");
            }
        }
    }

    public void predictedSeedSetUp(Boolean update){
        if (predictedSeedList.size() == 0) {
            RealmResults<Team> result = realm.allObjects(Team.class);

            for (Team team : result) {
                predictedSeedList.add(team);
            }
            Collections.sort(predictedSeedList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getCalculatedData().getPredictedSeed() < rhs.getCalculatedData().getPredictedSeed()) {
                        return -1;
                    } else if (lhs.getCalculatedData().getPredictedSeed() > rhs.getCalculatedData().getPredictedSeed()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                predictedSeedList = new ArrayList<>();
                predictedSeedSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating predictedSeedList");
            }
        }
    }

    public void firstTeamPickSetUp(Boolean update){
        if (firstPickList.size() == 0) {

            for (Team team : teamList) {
                firstPickList.add(team);
            }

            Collections.sort(firstPickList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getCalculatedData().getFirstPickAbility() < rhs.getCalculatedData().getFirstPickAbility()) {
                        return 1;
                    } else if (lhs.getCalculatedData().getFirstPickAbility() > rhs.getCalculatedData().getFirstPickAbility()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                firstPickList = new ArrayList<>();
                firstTeamPickSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating firstPickList");
            }
        }
    }

    public void secondTeamPickSetUp(Boolean update) {
        if (secondPickList.size() == 0) {

            for (Team team : teamList) {
                secondPickList.add(team);
            }

            Collections.sort(secondPickList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getCalculatedData().getSecondPickAbility() < rhs.getCalculatedData().getSecondPickAbility()) {
                        return 1;
                    } else if (lhs.getCalculatedData().getSecondPickAbility() > rhs.getCalculatedData().getSecondPickAbility()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                secondPickList = new ArrayList<>();
                secondTeamPickSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating secondPickList");
            }
        }
    }

    public void thirdTeamPickSetUp(Boolean update) {
        if (thirdPickList.size() == 0) {

            for (Team team : teamList) {
                thirdPickList.add(team);
            }

            Collections.sort(thirdPickList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getCalculatedData().getThirdPickAbility() < rhs.getCalculatedData().getThirdPickAbility()) {
                        return 1;
                    } else if (lhs.getCalculatedData().getThirdPickAbility() > rhs.getCalculatedData().getThirdPickAbility()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                thirdPickList = new ArrayList<>();
                thirdTeamPickSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating thirdPickList");
            }
        }
    }

    public void thirdLandfillTeamPickSetUp(Boolean update) {
        if (thirdPickListLandfill.size() == 0) {

            for (Team team : teamList) {
                thirdPickListLandfill.add(team);
            }

            Collections.sort(thirdPickListLandfill, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getCalculatedData().getThirdPickAbilityLandfill() < rhs.getCalculatedData().getThirdPickAbilityLandfill()) {
                        return 1;
                    } else if (lhs.getCalculatedData().getThirdPickAbilityLandfill() > rhs.getCalculatedData().getThirdPickAbilityLandfill()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                thirdPickListLandfill = new ArrayList<>();
                thirdLandfillTeamPickSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating thindLandfillPickList");
            }
        }
    }

    public void mechanismSetUp(Boolean update) {
        if (mechanismList.size() == 0) {

            for (Team team : teamList) {
                if (team.getUploadedData().isCanMountMechanism() || team.getUploadedData().isWillingToMount()) {
                    mechanismList.add(team);
                }
            }

            Collections.sort(mechanismList, new Comparator<Team>() {
                @Override
                public int compare(Team lhs, Team rhs) {
                    if (lhs.getUploadedData().getEaseOfMounting() < rhs.getUploadedData().getEaseOfMounting()) {
                        return 1;
                    } else if (lhs.getUploadedData().getEaseOfMounting() > rhs.getUploadedData().getEaseOfMounting()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        } else {
            if (update) {
                mechanismList = new ArrayList<>();
                secondTeamPickSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating mechanismList");
            }
        }
    }

    public void matchSetUp(Boolean update){
        if (matchList.size() == 0) {
            RealmResults<Match> matchResult = realm.allObjects(Match.class);
            for (Match match : matchResult) {
                matchList.add(match);
            }
        } else {
            if (update) {
                matchList = new ArrayList<>();
                matchSetUp(false);
            } else {
                Log.e(Constants.LOG_TAG, "Not updating matchList");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_database) {
            Log.e(Constants.LOG_TAG, "I CAN HAZ CLICKS!");



            getDatabase();
            realm.close();
            realm = Realm.getInstance(this, Constants.REALM_FILE);
            realm.refresh();
            arraySetup(true);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mSectionsPagerAdapter.getItem(0).onCreate(new Bundle());
            //Log.e(Constants.LOG_TAG, teamList.toArray()[0].toString());


            // This is Black Magic.  Do not question it.
            // Cthulu Fataggen
            getDatabase();
            realm.close();
            realm = Realm.getInstance(this, Constants.REALM_FILE);
            realm.refresh();
            arraySetup(true);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mSectionsPagerAdapter.getItem(0).onCreate(new Bundle());
            //Log.e(Constants.LOG_TAG, teamList.toArray()[0].toString());
        }
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TeamListFragment.newInstance(position + 1);
                case 1:
                    return FirstPickListFragment.newInstance(position + 1);
                case 2:
                    return SecondPickListFragment.newInstance(position + 1);
                case 3:
                    return ThirdPickListFragment.newInstance(position + 1);
                case 4:
                    return ThirdLandfillPickListFragment.newInstance(position + 1);
                case 5:
                    return MechanismListFragment.newInstance(position + 1);
                default:
                    return TeamListFragment.newInstance(position + 1); // This will never happen
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Team List".toUpperCase(l);
                case 1:
                    return "1st Pick List".toUpperCase(l);
                case 2:
                    return "2nd Pick List".toUpperCase(l);
                case 3:
                    return "3nd Pick List".toUpperCase(l);
                case 4:
                    return "3nd Pick List Landfill".toUpperCase(l);
                case 5:
                    return "Mechanism List".toUpperCase(l);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public static class TeamListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static TeamListFragment newInstance(int sectionNumber) {
            TeamListFragment fragment = new TeamListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;

        }

        public TeamListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teams, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.listView);
            listView.setAdapter(new TeamListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    public static class FirstPickListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static FirstPickListFragment newInstance(int sectionNumber) {
            FirstPickListFragment fragment = new FirstPickListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public FirstPickListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first_pick_list, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.firstPickList);
            listView.setAdapter(new FirstPickListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    public static class SecondPickListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static SecondPickListFragment newInstance(int sectionNumber) {
            SecondPickListFragment fragment = new SecondPickListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SecondPickListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second_pick_list, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.secondPickList);
            listView.setAdapter(new SecondPickListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    public static class ThirdPickListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static ThirdPickListFragment newInstance(int sectionNumber) {
            ThirdPickListFragment fragment = new ThirdPickListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ThirdPickListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second_pick_list, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.secondPickList);
            listView.setAdapter(new ThirdPickListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    public static class ThirdLandfillPickListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static ThirdLandfillPickListFragment newInstance(int sectionNumber) {
            ThirdLandfillPickListFragment fragment = new ThirdLandfillPickListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ThirdLandfillPickListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second_pick_list, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.secondPickList);
            listView.setAdapter(new ThirdLandfillPickListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    public static class MechanismListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static MechanismListFragment newInstance(int sectionNumber) {
            MechanismListFragment fragment = new MechanismListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MechanismListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second_pick_list, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.secondPickList);
            listView.setAdapter(new MechanismListAdapter(this.getActivity().getApplicationContext()));

            return rootView;
        }
    }

    @Override
    protected void onPause() {
        super.onStart();
        Log.e(Constants.LOG_TAG, "NOT ON MAIN");
        Globals.onMain = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Constants.LOG_TAG, "ON MAIN");
        Globals.onMain = true;
        if (Globals.needDatabaseUpdate) {
            getDatabase();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                onLinkToDbx();
            } else {
                Log.e(Constants.LOG_TAG, "Dropbox linking failure!  The app will not work until you fix this.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
