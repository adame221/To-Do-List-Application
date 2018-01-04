package com.ElyAdam.AELYProject2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class MainToDoList extends AppCompatActivity implements Serializable, AppInfo {

    private ListView mItemList;
    private TextView mItemsText;
    ObjectArrayAdapter mAdapter;
    private ArrayList<ToDoList> mItems;
    private ArrayList<ToDoList> mNewItems;
    private Toolbar mItemToolbar;
    private ActionBar mItemActionBar;
    private int mItemSize;
    private int mClickedPosition;

    /**
     * Save all appropriate fragment state.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(getResources().getString(R.string.edit_items_text), mItemsText.getText()
                .toString());
        savedInstanceState.putSerializable(getResources().getString(R.string.items_array), mItems);
        savedInstanceState.putSerializable(getResources().getString(R.string.new_items_array), mNewItems);
        savedInstanceState.putInt(getResources().getString(R.string.new_items), mItemSize);
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
                .edit_items_text)));

        mItems = (ArrayList<ToDoList>) savedInstanceState.getSerializable(getResources().getString(R.string.items_array));
        mNewItems = (ArrayList<ToDoList>) savedInstanceState.getSerializable(getResources().getString(R.string.new_items_array));
        mItemSize = mNewItems.size();

        for (ToDoList currentContact : mItems) {
        }

        mAdapter = new ObjectArrayAdapter(MainToDoList.this, R.layout.detail_line_to_do_list, mItems);
        //display the list
        mItemList.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_do_list);

        mItemList = (ListView) findViewById(R.id.itemList);
        mItemsText = (TextView) findViewById(R.id.itemsText);

        mItemList.setAdapter(mAdapter);

        mItemToolbar = (Toolbar) findViewById(R.id.itemToolbar);
        //pass the activity's toolbar
        setSupportActionBar(mItemToolbar);

        mItemActionBar = getSupportActionBar();

        mItems = new ArrayList<>();

        mItemsText.setText(getResources().getString(R.string.no_lists));

        mItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                ToDoList item = (ToDoList) mItemList.getAdapter().getItem(position);

                mClickedPosition = position;

                Bundle bundle = new Bundle();
                Intent itemIntent = new Intent(MainToDoList.this, ToDoListItems.class);
                if(mNewItems != null) {
                    bundle.putSerializable(NEW_ITEMS, mNewItems);
                }
                bundle.putSerializable(LIST_NAME_TITLE, item);
                itemIntent.putExtras(bundle);
                startActivityForResult(itemIntent, 1);
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

            if(requestCode == 1) {
                mNewItems = (ArrayList<ToDoList>) data.getSerializableExtra(ITEM_SIZE);
                mItemSize = mNewItems.size();
            }

            if(requestCode == 0) {
                mItems.add((ToDoList) data.getSerializableExtra(LIST_NAME));
            }

            for (ToDoList currentItem : mItems) {
            }
        }

        Log.d("mItems", mItems.toString());
        mAdapter = new ObjectArrayAdapter(MainToDoList.this, R.layout.detail_line_to_do_list, mItems);
        //display the list
        mItemList.setAdapter(mAdapter);
    }

    public class ObjectArrayAdapter extends ArrayAdapter<ToDoList> {

        //declare ArrayList of item
        private ArrayList<ToDoList> items;

        /**
         *  Override the constructor for ArrayAdapter
         *  The only variable we care about now ArrayList<PlatformVersion> objects
         *  it is the list of the objects we want to display
         *
         * @param context
         * @param resource
         * @param items
         */
        public ObjectArrayAdapter(Context context, int resource, ArrayList<ToDoList> items) {
            super(context, resource, items);
            this.items = items;
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
                view = inflater.inflate(R.layout.detail_line_to_do_list, null);
            }

            /*

              Recall that the variable position is sent in as an argument to this method
              The variable simply refers to the position of the current object on the list.
              The ArrayAdapter iterate through the list we sent it

              versionObject refers to the current PlatformVersion Object

             */
            ToDoList itemObject = items.get(position);

            if (itemObject != null) {
                // obtain a reference to the widgets in the defined layout "wire up the widgets from detail_line"
                TextView mItem = (TextView) view.findViewById(R.id.item);
                TextView mNumberOfItems = (TextView) view.findViewById(R.id.numberOfItems);


                if (mItem != null) {
                    mItem.setText(itemObject.getListName());
                }
                if (mNumberOfItems != null) {
                    if(mItemSize > 0 && mClickedPosition == position) {
                        mNumberOfItems.setText(mItemSize + " items");
                    }else {
                        mNumberOfItems.setText("0 items");
                    }
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
        //handle presses on the app/action bar
        switch (item.getItemId()){
            case R.id.action_add_item:
                Intent intent = new Intent(MainToDoList.this, AddToDoList.class);
                startActivityForResult(intent, 0);   //expect to get data back from called activity
                return true;
            case R.id.action_about:
                Toast.makeText(getApplicationContext(), "Project 2 - ToDoList Version 1", Toast
                        .LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
