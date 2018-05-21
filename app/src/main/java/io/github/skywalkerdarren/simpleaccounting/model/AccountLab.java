package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.AccountTable.Cols;

import static io.github.skywalkerdarren.simpleaccounting.model.DbSchema.AccountTable.TABLE_NAME;
import static io.github.skywalkerdarren.simpleaccounting.util.FormatUtil.idToBitmap;

/**
 * @author darren
 * @date 2018/3/25
 */

public class AccountLab {

    private static AccountLab sAccountLab;
    private final SQLiteDatabase mDatabase;
    private Context mContext;

    public AccountLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public static AccountLab getInstance(Context context) {
        if (sAccountLab == null) {
            return sAccountLab = new AccountLab(context);
        } else {
            return sAccountLab;
        }
    }

    private static ContentValues getContentValues(Account account) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, account.getId().toString());
        values.put(Cols.NAME, account.getName());
        values.put(Cols.BALANCE, account.getBalance().toString());
        values.put(Cols.BALANCE_HINT, account.getBalanceHint());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        account.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
        values.put(Cols.IMAGE, os.toByteArray());
        values.put(Cols.COLOR_ID, account.getColorId());
        return values;
    }

    static void initAccountDb(SQLiteDatabase sqLiteDatabase, Context context) {
        context = context.getApplicationContext();
        List<Account> accounts = new ArrayList<>(3);
        accounts.add(new Account().setName("现金").setBalanceHint("现金金额")
                .setBitmap(idToBitmap(context, R.drawable.account_cash))
                .setColorId(R.color.amber500)
                .setBalance(BigDecimal.ZERO));
        accounts.add(new Account().setName("支付宝").setBalanceHint("在线支付余额")
                .setBitmap(idToBitmap(context, R.drawable.account_alipay))
                .setColorId(R.color.lightblue500)
                .setBalance(BigDecimal.ZERO));
        accounts.add(new Account().setName("微信").setBalanceHint("在线支付余额")
                .setBitmap(idToBitmap(context, R.drawable.account_wechat))
                .setColorId(R.color.lightgreen500)
                .setBalance(BigDecimal.ZERO));
        for (Account account : accounts) {
            sqLiteDatabase.insert(TABLE_NAME, null,
                    getContentValues(account));
        }
    }

    private AccountCursorWrapper queryAccounts(String where, String[] args) {
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(TABLE_NAME,
                null,
                where,
                args,
                null,
                null,
                null);
        return new AccountCursorWrapper(cursor);
    }

    public Account getAccount(UUID uuid) {
        try (AccountCursorWrapper cursor = queryAccounts(Cols.UUID + " == ?",
                new String[]{uuid.toString()})) {
            cursor.moveToFirst();
            // 转换colorId到color
            Account account = cursor.getAccount();
            account.setColor(mContext.getResources().getColor(account.getColorId()));
            return account;
        }
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>(3);
        try (AccountCursorWrapper cursor = queryAccounts(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // 转换colorId到color
                Account account = cursor.getAccount();
                account.setColor(mContext.getResources().getColor(account.getColorId()));
                accounts.add(account);
                cursor.moveToNext();
            }
        }
        return accounts;
    }

    public void updateAccount(Account account) {
        ContentValues values = getContentValues(account);
        mDatabase.update(TABLE_NAME, values, Cols.UUID + " = ?",
                new String[]{account.getId().toString()});
    }

    /**
     * 删除账户及其账单
     *
     * @param uuid 账户id
     */
    public void delAccount(UUID uuid) {
        mDatabase.delete(TABLE_NAME,
                Cols.UUID + " = ?",
                new String[]{uuid.toString()});
        mDatabase.delete(DbSchema.BillTable.TABLE_NAME,
                DbSchema.BillTable.Cols.ACCOUNT_ID + " = ?",
                new String[]{uuid.toString()});
    }

    /**
     * 根据找到位置账户
     *
     * @param pos 位置
     * @return 账户
     */
    private Account getAccount(int pos) {
        pos++;
        try (AccountCursorWrapper cursor = queryAccounts(TABLE_NAME + "_id = ?", new String[]{pos + ""})) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getAccount();
            }
            return null;
        }
    }

    /**
     * 改变账户位置
     *
     * @param oldPos 老位置
     * @param newPos 新位置
     */
    public void changePosition(int oldPos, int newPos) {
        Account old = getAccount(oldPos);
        Account fresh = getAccount(newPos);
        // 送到位置0上
        setAccountId(old, -1);
        setAccountId(fresh, oldPos);
        setAccountId(old, newPos);
    }

    private void setAccountId(Account old, int t) {
        String s = t + 1 + "";
        mDatabase.update(TABLE_NAME, getContentValues(old), TABLE_NAME + "_id = ?", new String[]{s});
    }
}
