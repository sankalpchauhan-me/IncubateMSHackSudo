package com.sudo.campusambassdor.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.sudo.campusambassdor.R;
import com.sudo.campusambassdor.adapters.PostsAdapter;
import com.sudo.campusambassdor.enums.ProfileStatus;
import com.sudo.campusambassdor.managers.DatabaseHelper;
import com.sudo.campusambassdor.managers.ProfileManager;
import com.sudo.campusambassdor.model.Post;

public class MainActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private PostsAdapter postsAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private ProfileManager profileManager;
    String name;
    String email;
    Uri photoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                 name = profile.getDisplayName();
                 email = profile.getEmail();
                 photoUrl = Uri.parse(String.valueOf(user.getPhotoUrl()));

            }
        }

        else {
            name = "Please Log In";
            email = "Let's Go";
            photoUrl = Uri.parse("http://chittagongit.com//images/no-profile-picture-icon/no-profile-picture-icon-15.jpg");
        }
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.mipmap.ic_launcher)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
        SecondaryDrawerItem item2;
        if(user!=null){
             item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Tasks");
        }
        else{
             item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Login to View Tasks");
        }
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Recent Hackathons");
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("About");



        Drawer result = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.e(TAG, "onItemClick: " + drawerItem.getType());
                        switch ((int) drawerItem.getIdentifier()){
                            case 2:{
                                if(user!=null) {
                                    Intent intent = new Intent(MainActivity.this, TaskAssignActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                break;}
                            case 1:
                                Toast.makeText(MainActivity.this, "Already on Home", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
//                                Intent intent1 = new Intent(MainActivity.this, WebActivity.class);
//                                intent1.putExtra("url", "frontbench.xyz/hackathons");
//                                startActivity(intent1);
                                String url = "http://www.frontbench.xyz/hackathons";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                            case 4:
                                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent1);

                        }
                        return false;
                    }
                })
                .build();

        profileManager = ProfileManager.getInstance(this);
        initContentView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        postsAdapter.updateSelectedPost();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == CreatePostActivity.CREATE_NEW_POST_REQUEST
                || requestCode == ProfileActivity.OPEN_PROFILE_REQUEST)) {
            postsAdapter.loadFirstPage();
            if (postsAdapter.getItemCount() > 0) {
                recyclerView.scrollToPosition(0);
            }
        }
    }

    private void initContentView() {
        if (recyclerView == null) {
            floatingActionButton = (FloatingActionButton) findViewById(R.id.addNewPostFab);

            if (floatingActionButton != null) {
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hasInternetConnection()) {
                            addPostClickAction();
                        } else {
                            showFloatButtonRelatedSnackBar(R.string.internet_connection_failed);
                        }
                    }
                });
            }

            SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            postsAdapter = new PostsAdapter(this, swipeContainer);
            postsAdapter.setOnItemClickListener(new PostsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Post post) {
                    Intent intent = new Intent(MainActivity.this, PostDetailsActivity.class);
                    intent.putExtra(PostDetailsActivity.POST_EXTRA_KEY, post);
                    startActivity(intent);
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(postsAdapter);
            postsAdapter.loadFirstPage();
        }
    }

    public void showFloatButtonRelatedSnackBar(int messageId) {
        showSnackBar(floatingActionButton, messageId);
    }

    private void addPostClickAction() {
        ProfileStatus profileStatus = profileManager.checkProfile();

        if (profileStatus.equals(ProfileStatus.PROFILE_CREATED)) {
            openCreatePostActivity();
        } else {
            doAuthorization(profileStatus);
        }
    }

    private void openCreatePostActivity() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivityForResult(intent, CreatePostActivity.CREATE_NEW_POST_REQUEST);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivityForResult(intent, ProfileActivity.OPEN_PROFILE_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                ProfileStatus profileStatus = profileManager.checkProfile();

                if (profileStatus.equals(ProfileStatus.PROFILE_CREATED)) {
                    openProfileActivity();
                } else {
                    doAuthorization(profileStatus);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
