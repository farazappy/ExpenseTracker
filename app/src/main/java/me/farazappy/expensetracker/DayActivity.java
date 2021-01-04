package me.farazappy.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.farazappy.expensetracker.helpers.DatabaseHelper;
import me.farazappy.expensetracker.helpers.ItemAdapter;
import me.farazappy.expensetracker.models.Day;
import me.farazappy.expensetracker.models.Item;

public class DayActivity extends AppCompatActivity implements ItemAdapter.OnClickListener {

    DatabaseHelper databaseHelper;
    ItemAdapter itemAdapter;

    List<Item> items = new ArrayList<Item>();

    LinearLayout mainLayout;
    TextView noTrackers, totalSpent;
    RecyclerView recyclerView;
    Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        day = (Day) i.getSerializableExtra("day");

        mainLayout = findViewById(R.id.mainView);
        noTrackers = findViewById(R.id.noTrackers);
        totalSpent = findViewById(R.id.totalSpent);
        recyclerView = findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(this);
        itemAdapter = new ItemAdapter(this, items, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);

        TextView helloText = findViewById(R.id.displayName);
        helloText.setText(day.getName());

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noTrackers.setText("You don't have any items. \n Click the + button to create a new tracker!");

        setupView();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(DayActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DayActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText itemName = promptsView.findViewById(R.id.itemName);
                final EditText itemCost = promptsView.findViewById(R.id.itemCost);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (itemName.getText().toString().isEmpty()) {
                                    itemName.setError("Item name is required!");
                                    return;
                                }

                                if(itemCost.getText().toString().isEmpty()) {
                                    itemCost.setError("Item cost is required!");
                                    return;
                                }

                                createItem(itemName.getText().toString(), itemCost.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });
    }

    private void createItem(String itemName, String itemCost) {
        databaseHelper.createItem(new Item(itemName, Double.parseDouble(itemCost), day.getId()));
        setupView();
    }

    private void updateItem(String itemName, String itemCost, int itemId) {
        databaseHelper.updateItem(new Item(itemId, itemName, Double.parseDouble(itemCost)));
        setupView();
    }

    private void deleteItem(int itemId) {
        databaseHelper.deleteItem(itemId);
        setupView();
    }

    private void setupView() {
        items.clear();
        items.addAll(databaseHelper.getAllItemsForDay(day.getId()));
        itemAdapter.notifyDataSetChanged();

        if(items.isEmpty()) {
            mainLayout.setVisibility(View.GONE);
            noTrackers.setVisibility(View.VISIBLE);
        } else {

            double totalCost = 0;

            for (Item item : items) {
                totalCost += item.getCost();
            }

            totalSpent.setText("You've spent a total of ₹"+totalCost);
            noTrackers.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view, final Item item) {
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.updateItem:

                        LayoutInflater li = LayoutInflater.from(DayActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DayActivity.this);

                        alertDialogBuilder.setView(promptsView);

                        final EditText itemName = promptsView.findViewById(R.id.itemName);
                        itemName.setText(item.getName());
                        final EditText itemCost = promptsView.findViewById(R.id.itemCost);
                        itemCost.setText(String.valueOf(item.getCost()));

                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (itemName.getText().toString().isEmpty()) {
                                            itemName.setError("Item name is required!");
                                            return;
                                        }

                                        if(itemCost.getText().toString().isEmpty()) {
                                            itemCost.setError("Item cost is required!");
                                            return;
                                        }

                                        updateItem(itemName.getText().toString(), itemCost.getText().toString(), item.getId());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.show();

                        return true;
                    case R.id.deleteItem:

                        AlertDialog.Builder builder = new AlertDialog.Builder(DayActivity.this);
                        builder
                                .setTitle("Are you sure?")
                                .setMessage("Delete " + item.getName() + "?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(item.getId());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog aD = builder.create();

                        aD.show();

                        return true;
                    default:
                }
                return false;
            }
        });

        popup.show();
    }
}
