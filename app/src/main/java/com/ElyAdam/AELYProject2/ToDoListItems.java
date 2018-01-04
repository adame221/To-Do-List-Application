package com.ElyAdam.AELYProject2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Owner on 11/4/2015.
 */
public class ToDoListItems extends AppCompatActivity implements Serializable, AppInfo{

    private ListView mToDoList;
    private TextView mItemsText;
    ObjectArrayAdapter mToDoListAdapter;
    private Toolbar mToDoListToolbar;
    private ActionBar mToDoListActionBar;
    private ArrayList<ToDoList> mToDoListItems;
    private ArrayList<ToDoList> mToDoListItem;
    private ToDoList mNewTitle;
    private String mSubtitle;
    private int mItemSize;

    /**
     * Save all appropriate fragment state.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(getResources().getString(R.string.items_text), mItemsText.getText()
                .toString());
        savedInstanceState.putSerializable(getResources().getString(R.string.items_array), mToDoListItems);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p/>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mItemsText.setText(savedInstanceState.getString(getResources().getString(R.string
                .items_text)));

        mToDoListItems = (ArrayList<ToDoList>) savedInstanceState.getSerializable(getResources().getString(R.string.items_array));
        for (ToDoList currentToDoItem : mToDoListItems) {
        }

        mToDoListAdapter = new ObjectArrayAdapter(ToDoListItems.this, R.layout.detail_line_to_do_list_items, mToDoListItems);
        //display the list
        mToDoList.setAdapter(mToDoListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_items);

        mNewTitle = (ToDoList) getIntent().getSerializableExtra(LIST_NAME_TITLE);
        mToDoListItem = (ArrayList<ToDoList>) getIntent().getSerializableExtra(NEW_ITEMS);

        mToDoList = (ListView) findViewById(R.id.toDoList);
        mItemsText = (TextView) findViewById(R.id.itemsText);

        mToDoList.setAdapter(mToDoListAdapter);
        mToDoListToolbar = (Toolbar) findViewById(R.id.itemToolbar);
        //pass the activity's toolbar

        setSupportActionBar(mToDoListToolbar);

        mToDoListActionBar = getSupportActionBar();

        if(mNewTitle != null) {
            mSubtitle = mNewTitle.getListName();

            mToDoListActionBar.setSubtitle(mSubtitle);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                mToDoListActionBar.setSubtitle(mSubtitle);
            }
        }

        mToDoListActionBar.setDisplayHomeAsUpEnabled(true);


        if(mToDoListItem == null) {
            mItemsText.setText(getResources().getString(R.string.no_lists));
            mToDoListItems = new ArrayList<>();
        }

        if(mToDoListItem != null) {
            mItemsText.setText(null);
            mToDoListItems = mToDoListItem;
            mToDoListAdapter = new ObjectArrayAdapter(ToDoListItems.this, R.layout
                    .detail_line_to_do_list_items, mToDoListItems);
            //display the list
            mToDoList.setAdapter(mToDoListAdapter);
        }

        mToDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mToDoListItems.remove(position);
                mToDoListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            mItemsText.setText(null);

            if (data == null) {
                return;
            }

            mToDoListItems.add((ToDoList) data.getSerializableExtra(EXTRA_ITEM));

            for (ToDoList currentToDoItem : mToDoListItems) {
            }

            mItemSize = mToDoListItems.size();
        }
        mToDoListAdapter = new ObjectArrayAdapter(ToDoListItems.this, R.layout
                .detail_line_to_do_list_items, mToDoListItems);
        //display the list
        mToDoList.setAdapter(mToDoListAdapter);
    }

    public class ObjectArrayAdapter extends ArrayAdapter<ToDoList> {

        //declare ArrayList of item
        private ArrayList<ToDoList> toDoListItems;

        /**
         *  Override the constructor for ArrayAdapter
         *  The only variable we care about now ArrayList<PlatformVersion> objects
         *  it is the list of the objects we want to display
         *
         * @param context
         * @param resource
         * @param toDoListItems
         */
        public ObjectArrayAdapter(Context context, int resource, ArrayList<ToDoList> toDoListItems) {
            super(context, resource, toDoListItems);
            this.toDoListItems = toDoListItems;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // assign the view we are converting to a local variable
            View view = convertView;

            /*
              Check to see if view null.  If so, we have to inflate the view
              "inflate" basically mean to render or show the view
             */

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.detail_line_to_do_list_items, null);
            }

            /*

              Recall that the variable position is sent in as an argument to this method
              The variable simply refers to the position of the current object on the list.
              The ArrayAdapter iterate through the list we sent it

              versionObject refers to the current PlatformVersion Object

             */
            ToDoList itemObject = toDoListItems.get(position);

            if (itemObject != null) {
                // obtain a reference to the widgets in the defined layout "wire up the widgets from detail_line"
                TextView mItem = (TextView) view.findViewById(R.id.item);

                if (mItem != null) {
                    mItem.setText(itemObject.getItem());
                }
            }

            // the view must be returned to our Activity
            return view;
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p/>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p/>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p/>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p/>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        if (menu != null) {
            menu.findItem(R.id.action_about).setVisible(false);
        }
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent data = new Intent();
                data.putExtra(ITEM_SIZE, mToDoListItems);
                setResult(RESULT_OK, data);

                /*ntent itemIntent = new Intent(ToDoListItems.this, MainToDoList.class);
                itemIntent.putExtra(TO_DO_LIST_ITEMS, mToDoListItems);
                startActivityForResult(itemIntent, 1);*/
                onBackPressed();
                return true;
            case R.id.action_add_item:
                Intent intent = new Intent(ToDoListItems.this, AddItem.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
