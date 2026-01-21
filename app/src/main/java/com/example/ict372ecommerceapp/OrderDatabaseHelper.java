package com.example.ict372ecommerceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OrdersDB";
    private static final int DATABASE_VERSION = 1;

    // Orders table
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTAL_CODE = "postal_code";
    private static final String COLUMN_PHONE = "phone_number";
    private static final String COLUMN_PAYMENT_METHOD = "payment_method";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_TOTAL = "total_amount";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_ITEMS_JSON = "items_json";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_FULL_NAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL, "
                + COLUMN_ADDRESS + " TEXT NOT NULL, "
                + COLUMN_CITY + " TEXT NOT NULL, "
                + COLUMN_POSTAL_CODE + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
                + COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, "
                + COLUMN_ORDER_DATE + " TEXT NOT NULL, "
                + COLUMN_TOTAL + " REAL NOT NULL, "
                + COLUMN_STATUS + " TEXT NOT NULL, "
                + COLUMN_ITEMS_JSON + " TEXT NOT NULL)";
        db.execSQL(createOrdersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Create a new order
    public long createOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, order.userId);
        values.put(COLUMN_FULL_NAME, order.fullName);
        values.put(COLUMN_EMAIL, order.email);
        values.put(COLUMN_ADDRESS, order.address);
        values.put(COLUMN_CITY, order.city);
        values.put(COLUMN_POSTAL_CODE, order.postalCode);
        values.put(COLUMN_PHONE, order.phoneNumber);
        values.put(COLUMN_PAYMENT_METHOD, order.paymentMethod);
        values.put(COLUMN_ORDER_DATE, order.orderDate);
        values.put(COLUMN_TOTAL, order.totalAmount);
        values.put(COLUMN_STATUS, order.status);
        values.put(COLUMN_ITEMS_JSON, order.itemsJson);

        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return result;
    }

    // Get all orders for a user
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS,
                null,
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null,
                COLUMN_ORDER_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Order order = cursorToOrder(cursor);
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    // Get all orders (for admin or if no user system)
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS,
                null, null, null, null, null,
                COLUMN_ORDER_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Order order = cursorToOrder(cursor);
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    // Get order by ID
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;

        Cursor cursor = db.query(TABLE_ORDERS,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(orderId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            order = cursorToOrder(cursor);
        }
        cursor.close();
        db.close();
        return order;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);

        int rows = db.update(TABLE_ORDERS, values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(orderId)});
        db.close();
        return rows > 0;
    }

    // Helper method to convert cursor to Order object
    private Order cursorToOrder(Cursor cursor) {
        Order order = new Order();
        order.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        order.userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        order.fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
        order.email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        order.address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
        order.city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY));
        order.postalCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTAL_CODE));
        order.phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
        order.paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD));
        order.orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
        order.totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL));
        order.status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
        order.itemsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEMS_JSON));
        return order;
    }
}
