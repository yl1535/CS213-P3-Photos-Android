package com.example.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.Manifest;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import java.io.*;
import java.util.ArrayList;

public class Photos extends AppCompatActivity implements ConfirmDialogFragment.ConfirmDialogListener{

    private ListView apv1_list;
    private ImageButton apb1;
    private ImageButton apb2;
    private ImageButton apb3;
    private ImageButton apb4;
    private ImageButton apb5;
    private ImageButton apb6;
    private ImageButton apb7;
    private ImageButton dpb1;
    private ImageButton dpb2;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ConstraintLayout messageWindowBackground;
    private TextView mwt1;
    private EditText mwe1;
    private GridView gridView;
    private ConstraintLayout slideShowBackground;
    private ImageView slideimage;
    private ConstraintLayout switchWindowBackground;
    private ConstraintLayout displayBackground;
    private ImageView displayImage;
    private TextView swt1;
    private Switch switch1;
    private ImageAdapter imageAdapter;
    private ListView tag_list;
    private ConstraintLayout albumListBackground;
    private ListView albListView;
    private View listViewSelectedView;
    private enum PageStatus{
        mainPage,
        photoPage,
        displayPage,
        searchPage,
        searchDisplay;
    }
    private enum MainStatus{
        normal,
        deleteMode,
        secondMode,
    }
    private enum SwitchStatus{
        slideShow,
        ctrlXC,
        tagType,
        firstSearch,
        secondSearch,
        tagTypeSearch;
    }
    private boolean messageWindowOn = false;
    private ArrayList<Album> albums = new ArrayList<Album>();
    private ArrayList<String> albumDetails = new ArrayList<String>();
    private int selectedAlbumIndex = -1;
    private ArrayList<String> photoPaths = new ArrayList<String>();
    private PageStatus pageStatus;
    private MainStatus mainStatus;
    private SwitchStatus switchStatus;
    private int MainEditIndex = -1;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> tagadapter;
    private boolean SlideshowForward = false;
    private int slideShowIndex = -1;
    private int selectedPhotoIndex = -1;
    private ArrayList<String> tagStrings = new ArrayList<String>();
    private Tags newTag = new Tags(Tags.TagName.person, "OldCarrot2000");
    private boolean isCopyMode = false;
    private int listViewSelectedIndex = -1;
    private Tags searchTag1 = new Tags(Tags.TagName.person, "OldCarrot2000");
    private Tags searchTag2 = new Tags(Tags.TagName.person, "OldCarrot2000");
    private boolean ifDoubleSearch = false;
    private boolean ifAND = false;
    private boolean searchMode = false;
    private boolean inSecondTag = false;
    private Album searchResults = new Album("Search Results");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_ui);
        albums = initializeAlbumData(this);
        apv1_list = findViewById(R.id.apv1_List);
        apb1 = findViewById(R.id.Apb1);
        apb2 = findViewById(R.id.Apb2);
        apb3 = findViewById(R.id.Apb3);
        apb4 = findViewById(R.id.Apb4);
        apb5 = findViewById(R.id.Apb5);
        apb6 = findViewById(R.id.Apb6);
        apb7 = findViewById(R.id.Apb7);
        dpb1 = findViewById(R.id.Dpb1);
        dpb2 = findViewById(R.id.Dpb2);
        toolbar = findViewById(R.id.Toolbar);
        toolbar_title = findViewById(R.id.Toolbar_title);
        messageWindowBackground = findViewById(R.id.MessageWindowBackground);
        mwt1 = findViewById(R.id.Mwt1);
        mwe1 = findViewById(R.id.Mwe1);
        gridView = findViewById(R.id.apv1_Grid);
        slideShowBackground = findViewById(R.id.SlideShowBackground);
        slideimage = findViewById(R.id.slideImage);
        switchWindowBackground = findViewById(R.id.SwitchWindowBackground);
        displayBackground = findViewById(R.id.DisplayBackground);
        displayImage = findViewById(R.id.DisplayImage);
        swt1 = findViewById(R.id.Swt1);
        switch1 = findViewById(R.id.Switch1);
        tag_list = findViewById(R.id.DisplayTagList);
        albumListBackground = findViewById(R.id.AlbumListBackground);
        albListView = findViewById(R.id.AlbListView);

        setUpAlbumListPage();
    }

    public void onImageButtonClick(View view){
        if(!messageWindowOn) {
            if (view == apb1) {   //delete button
                if (pageStatus == PageStatus.mainPage) {  //delete album in main page
                    if (mainStatus != MainStatus.deleteMode) {
                        mainStatus = MainStatus.deleteMode;
                        toolbar.setBackgroundColor(Color.parseColor("#FFFF0000"));
                        toolbar_title.setText(getString(R.string.delete_mode));
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#B287B8F3")));
                    } else {
                        mainStatus = MainStatus.normal;
                        toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        toolbar_title.setText(getString(R.string.aps1_name));
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    }
                } else if (pageStatus == PageStatus.photoPage) {    //delete photo in photo page
                    if (mainStatus != MainStatus.deleteMode) {
                        mainStatus = MainStatus.deleteMode;
                        toolbar.setBackgroundColor(Color.parseColor("#FFFF0000"));
                        toolbar_title.setText(getString(R.string.delete_photo));
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#B287B8F3")));
                    } else {
                        mainStatus = MainStatus.normal;
                        toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        toolbar_title.setText(albums.get(selectedAlbumIndex).getName());
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    }
                } else if (pageStatus == PageStatus.displayPage) {  //delete tag in display page
                    if (mainStatus != MainStatus.deleteMode) {
                        mainStatus = MainStatus.deleteMode;
                        toolbar.setBackgroundColor(Color.parseColor("#FFFF0000"));
                        toolbar_title.setText(getString(R.string.display_delete));
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#B287B8F3")));
                    } else {
                        mainStatus = MainStatus.normal;
                        toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        toolbar_title.setText(new File(albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex).getPath()).getName());
                        ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    }
                }
            } else if (view == apb2){  //rename button, only appears in main page
                if (mainStatus != MainStatus.secondMode){
                    mainStatus = MainStatus.secondMode;
                    toolbar.setBackgroundColor(Color.parseColor("#FF0000FF"));
                    toolbar_title.setText(getString(R.string.rename_mode));
                    toolbar_title.setTextColor(Color.parseColor("#FFFFFFFF"));
                    ViewCompat.setBackgroundTintList(apb2, ColorStateList.valueOf(Color.parseColor("#B287B8F3")));
                } else{
                    mainStatus = MainStatus.normal;
                    toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    toolbar_title.setText(getString(R.string.aps1_name));
                    toolbar_title.setTextColor(Color.parseColor("#FF000000"));
                    ViewCompat.setBackgroundTintList(apb2, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                }
            } else if (view == apb3) {  //addnew button
                if (pageStatus == PageStatus.mainPage){  //add new album in main page
                    messageWindowOn = true;
                    toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    toolbar_title.setText(getString(R.string.aps1_name));
                    toolbar_title.setTextColor(Color.parseColor("#FF000000"));
                    ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    ViewCompat.setBackgroundTintList(apb2, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    messageWindowBackground.setVisibility(View.VISIBLE);
                    mwt1.setText(getString(R.string.Mw_input_album));
                    mwe1.setText(null);
                } else if (pageStatus == PageStatus.photoPage){    //add new photo in photo page
                    PermissionChecker();
                } else if (pageStatus == PageStatus.displayPage) {  //add new tag in display page
                    newTag = new Tags(Tags.TagName.person,"OldCarrot2000");
                    switchStatus = SwitchStatus.tagType;
                    messageWindowOn = true;
                    toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    toolbar_title.setText(new File(albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex).getPath()).getName());
                    toolbar_title.setTextColor(Color.parseColor("#FF000000"));
                    ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    ViewCompat.setBackgroundTintList(apb2, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                    switchWindowBackground.setVisibility(View.VISIBLE);
                    swt1.setText(getString(R.string.Sw_input_tagtype));
                    switch1.setChecked(false);
                    switch1.setText(getString(R.string.Sw_tagtype_false));
                    switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) switch1.setText(getString(R.string.Sw_tagtype_true));
                            else switch1.setText(getString(R.string.Sw_tagtype_false));
                        }
                    });
                }
            } else if (view == apb4) {  //CtrlXC button
                if (mainStatus != MainStatus.secondMode) {
                    mainStatus = MainStatus.secondMode;
                    toolbar.setBackgroundColor(Color.parseColor("#FF0000FF"));
                    toolbar_title.setText(getString(R.string.CtrlXC_photo));
                    toolbar_title.setTextColor(Color.parseColor("#FFFFFFFF"));
                    ViewCompat.setBackgroundTintList(apb4, ColorStateList.valueOf(Color.parseColor("#B287B8F3")));
                } else {
                    mainStatus = MainStatus.normal;
                    toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    toolbar_title.setText(albums.get(selectedAlbumIndex).getName());
                    toolbar_title.setTextColor(Color.parseColor("#FF000000"));
                    ViewCompat.setBackgroundTintList(apb4, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                }
            } else if (view == apb5) {  //Return button
                if (pageStatus == PageStatus.photoPage) {
                    pageStatus = PageStatus.mainPage;
                    apb2.setVisibility(View.VISIBLE);
                    apb4.setVisibility(View.INVISIBLE);
                    apb5.setVisibility(View.INVISIBLE);
                    apb6.setVisibility(View.VISIBLE);
                    apb7.setVisibility(View.INVISIBLE);
                    apv1_list.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.INVISIBLE);
                    selectedAlbumIndex = -1;
                    toolbar_title.setText(getString(R.string.aps1_name));
                    adapter.notifyDataSetChanged();
                } else if (pageStatus == PageStatus.displayPage) {
                    pageStatus = PageStatus.photoPage;
                    apb4.setVisibility(View.VISIBLE);
                    apb7.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    displayBackground.setVisibility(View.INVISIBLE);
                    selectedPhotoIndex = -1;
                    toolbar_title.setText(albums.get(selectedAlbumIndex).getName());
                    imageAdapter.notifyDataSetChanged();
                } else if(pageStatus == PageStatus.searchPage) {   //search page
                    pageStatus = PageStatus.mainPage;
                    apb1.setVisibility(View.VISIBLE);
                    apb2.setVisibility(View.VISIBLE);
                    apb3.setVisibility(View.VISIBLE);
                    apb5.setVisibility(View.INVISIBLE);
                    apb6.setVisibility(View.VISIBLE);
                    apv1_list.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.INVISIBLE);
                    toolbar_title.setText(getString(R.string.aps1_name));
                } else if(pageStatus == PageStatus.searchDisplay){  //search page to display
                    pageStatus = PageStatus.searchPage;
                    gridView.setVisibility(View.VISIBLE);
                    displayBackground.setVisibility(View.INVISIBLE);
                    toolbar_title.setText(getString(R.string.search_results));
                    selectedPhotoIndex = -1;
                }
                mainStatus = MainStatus.normal;
                toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                ViewCompat.setBackgroundTintList(apb2, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                ViewCompat.setBackgroundTintList(apb4, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
            } else if (view == apb6) {  //Search button
                searchTag1 = new Tags(Tags.TagName.person, "OldCarrot2000");
                searchTag2 = new Tags(Tags.TagName.person, "OldCarrot2000");
                searchMode = true;
                messageWindowOn = true;
                switchStatus = SwitchStatus.firstSearch;
                mainStatus = MainStatus.normal;
                toolbar_title.setText(getString(R.string.aps1_name));
                toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                ViewCompat.setBackgroundTintList(apb4, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                switchWindowBackground.setVisibility(View.VISIBLE);
                switch1.setChecked(false);
                switch1.setText(getString(R.string.Sw_search_false));
                swt1.setText(getString(R.string.Sw_searchtype));
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) switch1.setText(getString(R.string.Sw_search_true));
                        else switch1.setText(getString(R.string.Sw_search_false));
                    }
                });
            } else if (view == apb7) {  //Slideshow button
                messageWindowOn = true;
                switchStatus = SwitchStatus.slideShow;
                mainStatus = MainStatus.normal;
                toolbar_title.setText(albums.get(selectedAlbumIndex).getName());
                toolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ViewCompat.setBackgroundTintList(apb1, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                ViewCompat.setBackgroundTintList(apb4, ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
                switchWindowBackground.setVisibility(View.VISIBLE);
                switch1.setChecked(false);
                switch1.setText(getString(R.string.Sw_slide_false));
                swt1.setText(getString(R.string.Sw_input_slide));
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) switch1.setText(getString(R.string.Sw_slide_true));
                        else switch1.setText(getString(R.string.Sw_slide_false));
                    }
                });
            }
        }
    }

    public void MessageWindowOneConfirm(View view){ //MainEditIndex: -1 -> Add Album, 0+ -> Rename Album
        closeKeyboard();
        messageWindowBackground.setVisibility(View.INVISIBLE);
        if(switchStatus == SwitchStatus.tagTypeSearch && searchMode){
            if(ifDoubleSearch && inSecondTag) searchTag2.setTagValue(mwe1.getText().toString());
            else searchTag1.setTagValue(mwe1.getText().toString());
            if(ifDoubleSearch && !inSecondTag){
                inSecondTag = true;
                switchStatus = SwitchStatus.secondSearch;
                switchWindowBackground.setVisibility(View.VISIBLE);
                switch1.setChecked(false);
                switch1.setText(getString(R.string.Sw_combine_false));
                swt1.setText(getString(R.string.Sw_input_combination_type));
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) switch1.setText(getString(R.string.Sw_combine_true));
                        else switch1.setText(getString(R.string.Sw_combine_false));
                    }
                });
            }
            else{
                inSecondTag = false;
                messageWindowOn = false;
                searchMode = false;
                startSearchProcess();
            }
        }
        else if(MainEditIndex == -1 && pageStatus == PageStatus.mainPage) {
            Album newAlbum = new Album(mwe1.getText().toString());
            if(addSome(albums, newAlbum)) {
                albumDetails.add(newAlbum.toStringFull());
                adapter.notifyDataSetChanged();
            }
            else{
                printErrorMessage("An Album with same name already existed");
            }
            messageWindowOn = false;
        }
        else if(pageStatus == PageStatus.mainPage){
            Album newAlbum = new Album(mwe1.getText().toString());
            if(addSome(albums, newAlbum)){
                int temp = albums.size();
                albums.remove(temp-1);
                Album a = albums.get(MainEditIndex);
                a.setName(newAlbum.getName());
                albums.set(MainEditIndex,a);
                albumDetails.set(MainEditIndex,a.toStringFull());
            }
            else{
                printErrorMessage("An Album with same name already existed");
            }
            MainEditIndex = -1;
            messageWindowOn = false;
        }
        else if(pageStatus == PageStatus.displayPage){
            newTag.setTagValue(mwe1.getText().toString());
            Album a = albums.get(selectedAlbumIndex);
            ArrayList<EachPhoto> eps = a.getContains();
            EachPhoto ep = eps.get(selectedPhotoIndex);
            ArrayList<Tags> tags = ep.getTags();
            if(addSome(tags, newTag)){
                tagStrings.add(newTag.toStringNTLC());
                ep.setTags(tags);
                eps.set(selectedPhotoIndex,ep);
                a.setContains(eps);
                albums.set(selectedAlbumIndex,a);
                tagadapter.notifyDataSetChanged();
            }
            else printErrorMessage("A Tag with same name and value already existed");
            messageWindowOn = false;
            updateTagtoCopys();
        }
        writeData(this,albums);
    }

    public void SwitchWindowConfirm(View view){
        if(switchStatus == SwitchStatus.slideShow){  //Use when Slideshow
            if(switch1.isChecked()){
                SlideshowForward = true;
                slideShowIndex = 0;
            }
            else{
                SlideshowForward = false;
                slideShowIndex = albums.get(selectedAlbumIndex).getContains().size()-1;
            }
            switchWindowBackground.setVisibility(View.INVISIBLE);
            slideShowBackground.setVisibility(View.VISIBLE);
            slideShowBackground.setOnClickListener((view2) -> HandleSlideShowTransition(false));
            slideimage.setOnClickListener((view3) -> HandleSlideShowTransition(true));
            slideimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            slideimage.setImageBitmap(bitmapFromString(albums.get(selectedAlbumIndex).getContains().get(slideShowIndex).getPath()));
        }
        else if(switchStatus == SwitchStatus.ctrlXC){ //Use when CtrlXC
            isCopyMode = !switch1.isChecked();
            switchWindowBackground.setVisibility(View.INVISIBLE);
            albumListBackground.setVisibility(View.VISIBLE);
            ArrayAdapter<String> newadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumDetails);;
            albListView.setAdapter(newadapter);
            albListView.setOnItemClickListener((list, view2, pos, id) -> clickEventListViewWindow(view2, pos));
        }
        else if(switchStatus == SwitchStatus.tagType){  //Use when choose tag
            if(switch1.isChecked()) newTag.setTagName(Tags.TagName.location);
            else newTag.setTagName(Tags.TagName.person);
            switchWindowBackground.setVisibility(View.INVISIBLE);
            messageWindowBackground.setVisibility(View.VISIBLE);
            mwt1.setText(getString(R.string.Mw_add_tag));
            mwe1.setText(null);
        }
        else if(switchStatus == SwitchStatus.firstSearch || switchStatus == SwitchStatus.secondSearch){  //Use when Search Mode First Switch: Single or Double
            if(switchStatus == SwitchStatus.firstSearch) ifDoubleSearch = switch1.isChecked();
            else ifAND = !switch1.isChecked();
            switchStatus = SwitchStatus.tagTypeSearch;
            switch1.setChecked(false);
            switch1.setText(getString(R.string.Sw_tagtype_false));
            swt1.setText(getString(R.string.Sw_input_tagtype_search));
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) switch1.setText(getString(R.string.Sw_tagtype_true));
                    else switch1.setText(getString(R.string.Sw_tagtype_false));
                }
            });
        }
        else if(switchStatus == SwitchStatus.tagTypeSearch){    //Use when entering tagtype/name for search
            if(inSecondTag) {
                if (switch1.isChecked()) searchTag2.setTagName(Tags.TagName.location);
                else searchTag2.setTagName(Tags.TagName.person);
            }
            else {
                if (switch1.isChecked()) searchTag1.setTagName(Tags.TagName.location);
                else searchTag1.setTagName(Tags.TagName.person);
            }
            switchWindowBackground.setVisibility(View.INVISIBLE);
            messageWindowBackground.setVisibility(View.VISIBLE);
            mwt1.setText(getString(R.string.Sw_input_tagvalue_search));
            mwe1.setText(null);
        }
    }

    private void startSearchProcess(){
        boolean Tag1fits = false;
        boolean Tag2fits = false;
        ArrayList<EachPhoto> searchResultsPhoto = new ArrayList<EachPhoto>();
        for(int i=0;i<albums.size();i++){
            Album calbum = albums.get(i);
            ArrayList<EachPhoto> eps = calbum.getContains();
            for(int j=0;j<eps.size();j++){
                Tag1fits = false;
                Tag2fits = false;
                EachPhoto ep = eps.get(j);
                ArrayList<Tags> tags = ep.getTags();
                for(int k=0;k<tags.size();k++){
                    Tags t = tags.get(k);
                    if(ifDoubleSearch){
                        if(ifAND){
                            if(t.ifFit(searchTag1)) Tag1fits = true;
                            if(t.ifFit(searchTag2)) Tag2fits = true;
                            if(Tag1fits && Tag2fits) addSome(searchResultsPhoto, ep);
                        }
                        else{
                            if(t.ifFit(searchTag1) || t.ifFit(searchTag2)) addSome(searchResultsPhoto, ep);
                        }
                    }
                    else{
                        if(t.ifFit(searchTag1)) addSome(searchResultsPhoto, ep);
                    }
                }
            }
        }
        searchResults.setContains(searchResultsPhoto);
        setUpSearchResultPage();
        ifDoubleSearch = false;
    }

    private void setUpSearchResultPage(){
        pageStatus = PageStatus.searchPage;
        apb1.setVisibility(View.INVISIBLE);
        apb2.setVisibility(View.INVISIBLE);
        apb3.setVisibility(View.INVISIBLE);
        apb5.setVisibility(View.VISIBLE);
        apb6.setVisibility(View.INVISIBLE);
        apv1_list.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        toolbar_title.setText(searchResults.getName());
        ArrayList<String> searchPaths = new ArrayList<String>();
        for(EachPhoto ep: searchResults.getContains()) searchPaths.add(ep.getPath());
        gridView.setNumColumns(3);
        int gridWidth = gridView.getWidth();
        int imageSize = gridWidth/3;
        ImageAdapter searchimageAdapter = new ImageAdapter(this, searchPaths);
        searchimageAdapter.setImageSize(imageSize);
        gridView.setAdapter(searchimageAdapter);
        gridView.setOnItemClickListener((list, view, pos, id) -> clickEventSearchPhotoPage(pos));
    }

    private void clickEventSearchPhotoPage(int pos){
        pageStatus = PageStatus.searchDisplay;
        gridView.setVisibility(View.INVISIBLE);
        displayBackground.setVisibility(View.VISIBLE);
        selectedPhotoIndex = pos;
        setUpSearchDisplayPage();
    }

    private void setUpSearchDisplayPage(){
        tagStrings = new ArrayList<String>();
        ArrayList<Tags> tags = searchResults.getContains().get(selectedPhotoIndex).getTags();
        for(Tags t: tags) tagStrings.add(t.toStringNTLC());
        displayImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        displayImage.setImageBitmap(bitmapFromString(searchResults.getContains().get(selectedPhotoIndex).getPath()));
        toolbar_title.setText(new File(searchResults.getContains().get(selectedPhotoIndex).getPath()).getName());
        ArrayAdapter<String> temptagadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagStrings);
        tag_list.setAdapter(temptagadapter);
    }

    private void updateTagtoCopys(){
        EachPhoto ep = albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex);
        for(int i=0;i<albums.size();i++){
            for(int j=0;j<albums.get(i).getContains().size();j++){
                EachPhoto ep2 = albums.get(i).getContains().get(j);
                if(ep2.getPath().equals(ep.getPath())){
                    ep2.setTags(ep.getTags());
                }
            }
        }
    }

    private void clickEventListViewWindow(View view, int pos){
        if(listViewSelectedIndex == pos){
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            listViewSelectedView = null;
            listViewSelectedIndex = -1;
        }
        else{
            if(listViewSelectedIndex != -1) listViewSelectedView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            view.setBackgroundColor(Color.parseColor("#3B000000"));
            listViewSelectedView = view;
            listViewSelectedIndex = pos;
        }
    }

    public void ListViewWindowConfirm(View view){
        if(listViewSelectedIndex != -1){
            albumListBackground.setVisibility(View.INVISIBLE);
            Album a = albums.get(listViewSelectedIndex);
            ArrayList<EachPhoto> ep = a.getContains();
            if(addSome(ep, albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex))){
                a.setContains(ep);
                albums.set(listViewSelectedIndex, a);
                albumDetails.set(listViewSelectedIndex,a.toStringFull());
                if(!isCopyMode){
                    Album b = albums.get(selectedAlbumIndex);
                    ArrayList<EachPhoto> ep2 = b.getContains();
                    ep2.remove(selectedPhotoIndex);
                    b.setContains(ep2);
                    albums.set(selectedAlbumIndex, b);
                    photoPaths.remove(selectedPhotoIndex);
                    albumDetails.set(selectedAlbumIndex,b.toStringFull());
                    imageAdapter.notifyDataSetChanged();
                }
                printErrorMessage("Operation has been successfully proceed.");
            }
            else printErrorMessage("Can't do operation, a copy of the selected photo already existed in target album.");
            messageWindowOn = false;
            listViewSelectedView = null;
            listViewSelectedIndex = -1;
            selectedPhotoIndex = -1;
            writeData(this, albums);
        }
    }

    private void HandleSlideShowTransition(boolean isSlideShowImageField){
        if(isSlideShowImageField){
            if(SlideshowForward){
                if(slideShowIndex == albums.get(selectedAlbumIndex).getContains().size()-1){
                    mainStatus = MainStatus.normal;
                    slideShowBackground.setVisibility(View.INVISIBLE);
                    messageWindowOn = false;
                }
                else{
                    slideShowIndex++;
                    slideimage.setImageBitmap(bitmapFromString(albums.get(selectedAlbumIndex).getContains().get(slideShowIndex).getPath()));
                }
            }
            else{
                if(slideShowIndex == 0){
                    mainStatus = MainStatus.normal;
                    slideShowBackground.setVisibility(View.INVISIBLE);
                    messageWindowOn = false;
                }
                else{
                    slideShowIndex--;
                    slideimage.setImageBitmap(bitmapFromString(albums.get(selectedAlbumIndex).getContains().get(slideShowIndex).getPath()));
                }
            }
        }
        else{
            mainStatus = MainStatus.normal;
            slideShowBackground.setVisibility(View.INVISIBLE);
            messageWindowOn = false;
        }
    }

    private ArrayList<Album> initializeAlbumData(Context context){ //Used to read data from storage and initialize Album list
        ArrayList<Album> readableAlbums = new ArrayList<Album>();//"AlbumLists.tt2"
        try{
            FileInputStream fis = context.openFileInput("AlbumLists.tt2");
            ObjectInputStream ois = new ObjectInputStream(fis);
            readableAlbums = (ArrayList<Album>)ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception e1){
            if(context.getFileStreamPath("AlbumLists.tt2").exists()){
                File file = context.getFileStreamPath("AlbumLists.tt2");
                if(!file.delete()) printErrorMessage("Unexpected Error: Fail to delete corrupted save file");
            }
            try(FileOutputStream fos = context.openFileOutput("AlbumLists.tt2",Context.MODE_PRIVATE)){
                readableAlbums.add(new Album("stock"));
                writeData(this,albums);
            } catch(Exception e2){
                printErrorMessage("Failed to regenerate save file for app when facing save file corruption or loss");
            }
        }
        return readableAlbums;
    }

    private void writeData(Context context, ArrayList<Album> StoredAlbums){
        try{
            FileOutputStream fos = context.openFileOutput("AlbumLists.tt2",Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(StoredAlbums);
            oos.close();
            fos.close();
        } catch (Exception e){
            printErrorMessage("Unexpected error occurred when writing data to app's save file");
        }
    }

    private void setUpAlbumListPage(){
        albumDetails = new ArrayList<String>();
        for(Album a: albums) albumDetails.add(a.toStringFull());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumDetails);
        apv1_list.setAdapter(adapter);
        apv1_list.setOnItemClickListener((list, view, pos, id) -> clickEventMainPage(pos));
        pageStatus = PageStatus.mainPage;
        mainStatus = MainStatus.normal;
    }

    private void clickEventMainPage(int pos){
        if(mainStatus == MainStatus.deleteMode){
            messageWindowOn = true;
            ConfirmDialogFragment newFragment = new ConfirmDialogFragment();
            newFragment.type = 0;
            newFragment.data = pos;
            newFragment.contains = albums.get(pos).getName();
            newFragment.show(getSupportFragmentManager(), "confirmDialog");
        }
        else if(mainStatus == MainStatus.secondMode){
            messageWindowOn = true;
            messageWindowBackground.setVisibility(View.VISIBLE);
            mwt1.setText(getString(R.string.Mw_rename_album));
            mwe1.setText(albums.get(pos).getName());
            MainEditIndex = pos;
        }
        else if(mainStatus == MainStatus.normal){
            pageStatus = PageStatus.photoPage;
            apb2.setVisibility(View.INVISIBLE);
            apb4.setVisibility(View.VISIBLE);
            apb5.setVisibility(View.VISIBLE);
            apb6.setVisibility(View.INVISIBLE);
            apb7.setVisibility(View.VISIBLE);
            apv1_list.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
            selectedAlbumIndex = pos;
            setUpPhotoPage();
        }
    }

    private void setUpPhotoPage(){
        toolbar_title.setText(albums.get(selectedAlbumIndex).getName());
        Album a = albums.get(selectedAlbumIndex);
        photoPaths = new ArrayList<String>();
        for(EachPhoto ep: a.getContains()) photoPaths.add(ep.getPath());
        gridView.setNumColumns(3);
        int gridWidth = gridView.getWidth();
        int imageSize = gridWidth/3;
        imageAdapter = new ImageAdapter(this, photoPaths);
        imageAdapter.setImageSize(imageSize);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener((list, view, pos, id) -> clickEventPhotoPage(pos));
    }

    private void clickEventPhotoPage(int pos){
        if(mainStatus == MainStatus.deleteMode){    //delete photo mode
            ConfirmDialogFragment newFragment = new ConfirmDialogFragment();
            newFragment.type = 1;
            newFragment.data = pos;
            newFragment.show(getSupportFragmentManager(), "confirmDialog");
            messageWindowOn = true;
        }
        else if(mainStatus == MainStatus.secondMode){   //copy/move photo mode
            switchStatus = SwitchStatus.ctrlXC;
            selectedPhotoIndex = pos;
            switchWindowBackground.setVisibility(View.VISIBLE);
            switch1.setChecked(false);
            switch1.setText(getString(R.string.Sw_CtrlXC_false));
            swt1.setText(getString(R.string.Sw_input_CtrlXC));
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) switch1.setText(getString(R.string.Sw_CtrlXC_true));
                    else switch1.setText(getString(R.string.Sw_CtrlXC_false));
                }
            });
            messageWindowOn = true;
        }
        else if(mainStatus == MainStatus.normal){   //display mode
            pageStatus = PageStatus.displayPage;
            apb4.setVisibility(View.INVISIBLE);
            apb7.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            displayBackground.setVisibility(View.VISIBLE);
            selectedPhotoIndex = pos;
            UpdateDisplayDetails();
        }
    }

    public void DisplayPhotoChange(View view) {
        if(view == dpb1){
            if(selectedPhotoIndex != 0){
                selectedPhotoIndex--;
                if(pageStatus == PageStatus.displayPage) UpdateDisplayDetails();
                else if(pageStatus == PageStatus.searchDisplay) setUpSearchDisplayPage();
            }
        }
        else if(view == dpb2){
            if(pageStatus == PageStatus.displayPage){
                if(selectedPhotoIndex != albums.get(selectedAlbumIndex).getContains().size()-1){
                    selectedPhotoIndex++;
                    UpdateDisplayDetails();
                }
            }
            else if(pageStatus == PageStatus.searchDisplay){
                if(selectedPhotoIndex != searchResults.getContains().size()-1){
                    selectedPhotoIndex++;
                    setUpSearchDisplayPage();
                }
            }
        }
    }

    public void UpdateDisplayDetails(){
        tagStrings = new ArrayList<String>();
        ArrayList<Tags> tags = albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex).getTags();
        for(Tags t: tags) tagStrings.add(t.toStringNTLC());
        displayImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        displayImage.setImageBitmap(bitmapFromString(albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex).getPath()));
        toolbar_title.setText(new File(albums.get(selectedAlbumIndex).getContains().get(selectedPhotoIndex).getPath()).getName());
        tagadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagStrings);
        tag_list.setAdapter(tagadapter);
        tag_list.setOnItemClickListener((list, view, pos, id) -> clickEventTag(pos));
    }

    private void clickEventTag(int pos){
        if(mainStatus == MainStatus.deleteMode){
            ConfirmDialogFragment newFragment = new ConfirmDialogFragment();
            newFragment.type = 2;
            newFragment.data = pos;
            newFragment.contains = albums.get(pos).getName();
            newFragment.show(getSupportFragmentManager(), "confirmDialog");
            messageWindowOn = true;
        }
    }

    public void onDialogPositiveClick(ConfirmDialogFragment dialog){
        if(dialog.type == 0){
            albums.remove(dialog.data);
            albumDetails.remove(dialog.data);
            adapter.notifyDataSetChanged();
        }
        else if(dialog.type == 1){
            Album a = albums.get(selectedAlbumIndex);
            ArrayList<EachPhoto> eps = a.getContains();
            eps.remove(dialog.data);
            a.setContains(eps);
            albums.set(selectedAlbumIndex,a);
            photoPaths.remove(dialog.data);
            albumDetails.set(selectedAlbumIndex,a.toStringFull());
            imageAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
        else if(dialog.type == 2){
            Album a = albums.get(selectedAlbumIndex);
            ArrayList<EachPhoto> eps = a.getContains();
            EachPhoto ep = eps.get(selectedPhotoIndex);
            ArrayList<Tags> tags = ep.getTags();
            tags.remove(dialog.data);
            tagStrings.remove(dialog.data);
            ep.setTags(tags);
            eps.set(selectedPhotoIndex, ep);
            a.setContains(eps);
            albums.set(selectedAlbumIndex, a);
            writeData(this, albums);
            updateTagtoCopys();
            tagadapter.notifyDataSetChanged();
        }
        messageWindowOn = false;
        writeData(this, albums);
    }

    public void onDialogNegativeClick(ConfirmDialogFragment dialog) { messageWindowOn = false; }

    public void onDialogCancel(ConfirmDialogFragment dialog){ messageWindowOn = false; }

    private void openImageSelector(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int code, int result, Intent data){
        super.onActivityResult(code, result, data);
        if(code == 1 && result == RESULT_OK) {
            if(data != null){
                Uri selectedUri = data.getData();
                String selectedPath = getPhotoPath(selectedUri);
                if(selectedPath != null){
                    EachPhoto ep = new EachPhoto(selectedPath);
                    if(addSome(photoPaths, selectedPath)){
                        Album a = albums.get(selectedAlbumIndex);
                        ArrayList<EachPhoto> eps = a.getContains();
                        eps.add(ep);
                        a.setContains(eps);
                        albums.set(selectedAlbumIndex, a);
                        writeData(this,albums);
                        imageAdapter.notifyDataSetChanged();
                    }
                    else printErrorMessage("The selected photo is already in this album.");
                }
                else printErrorMessage("Unexpected Error: Failed to read selected Photo's path");
            }
        }
    }

    private void PermissionChecker(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
        }
        else openImageSelector();
    }

    public void onRequestPermissionsResult(int request, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(request, permissions, grantResults);
        if(request == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openImageSelector();
            }
            else{
                printErrorMessage("UnexpectedError: Granted Defined for reading storage");
            }
        }
    }

    private String getPhotoPath(Uri uri){
        Cursor cursor = null;
        int column_index;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri,proj,null,null,null);
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally{
            if(cursor != null) cursor.close();
        }
    }

    private Bitmap bitmapFromString(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    private <T> boolean addSome(ArrayList<T> TargetList, T some){
        for(int i=0;i<TargetList.size();i++){
            if(TargetList.get(i).toString().equals(some.toString())) return false;
        }
        TargetList.add(some);
        return true;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void printErrorMessage(String msg){
        Bundle bundle = new Bundle();
        bundle.putString(MessageDialogFragment.MESSAGE_KEY, msg);
        DialogFragment newFragment = new MessageDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "bad_fields");
    }

}